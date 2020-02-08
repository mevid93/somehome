package projekti.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekti.entity.Account;
import projekti.entity.Comment;
import projekti.entity.MyLike;
import projekti.entity.Picture;
import projekti.repository.CommentRepository;
import projekti.repository.MyLikeRepository;
import projekti.repository.PictureRepository;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private MyLikeRepository myLikeRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Get list of all pictures for gicen account
     *
     * @param account
     * @return list of pictures
     */
    @Transactional
    public List<Picture> getPicturesByAccount(Account account) {
        // if account null or account does not have picturealbum
        if (account == null || account.getPictureAlbum() == null) {
            return new ArrayList<>();
        }
        // find pictures for accounts picturealbum
        List<Picture> pictures = pictureRepository.findByPictureAlbum(account.getPictureAlbum());
        return (pictures == null) ? new ArrayList<>() : pictures;
    }

    /**
     * Add picture for account. Returns null if no erros. If there was error
     * returns error message.
     *
     * @param account
     * @param file
     * @param description
     * @return error message / null
     * @throws java.io.IOException
     */
    @Transactional
    public String addPictureForAccount(Account account, MultipartFile file, String description) throws IOException {
        // if account does not exist
        if (account == null || file == null || file.getContentType() == null) {
            return "Invalid account or file";
        }
        // if invalid file type
        if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg")) {
            return "Picture file type must be png or jpeg.";
        }
        // if description too long
        if (description != null && description.length() > 200) {
            return "Picture description max length is 200 characters";
        }
        // if pictureAlbum empty --> initialize the array
        if (account.getPictureAlbum().getPictures() == null) {
            account.getPictureAlbum().setPictures(new ArrayList<>());
        }
        // check that account picturealbum is not full
        if (account.getPictureAlbum().getPictures().size() == 10) {
            return "Album is full, pleace add space by deleting existing picture.";
        }
        // finally add picture to album
        savePictureForAccount(account, file, description);
        return null;
    }

    /**
     * Save picture for account. No validations here.
     */
    @Transactional
    private void savePictureForAccount(Account account, MultipartFile file, String description) throws IOException {
        // create picture object
        Picture picture = new Picture();
        picture.setName(file.getOriginalFilename());
        picture.setMediaType(file.getContentType());
        picture.setSize(file.getSize());
        picture.setContent(file.getBytes());
        picture.setDescription(description);

        // save
        picture.setPictureAlbum(account.getPictureAlbum());
        account.getPictureAlbum().getPictures().add(picture);
        pictureRepository.save(picture);

    }

    /**
     * Delete accounts picture.
     *
     * @param account
     * @param pictureId
     */
    @Transactional
    public void deleteUsersPictureById(Account account, long pictureId) {
        // check that account is valid
        if (account == null) {
            return;
        }
        // get picture and check that it belongs to account
        Picture picture = pictureRepository.getOne(pictureId);
        if (picture == null || account.getId() != picture.getPictureAlbum().getOwner().getId()) {
            return;
        }
        // delete picture
        pictureRepository.deleteById(pictureId);
    }

    /**
     * Return picture corresponding pictureId given as parameter.
     *
     * @param id
     * @return picture
     */
    @Transactional
    public Picture getPictureById(long id) {
        return pictureRepository.getById(id);
    }

    /**
     * Get profilepicture to given account. If account does not have profile
     * picture, null is returned.
     *
     * @param account
     * @return profilepicture
     */
    @Transactional
    public Picture getAccountProfilePicture(Account account) {
        return pictureRepository.getAccountProfilePicture(account);
    }

    /**
     * Return owner of the picture.
     *
     * @param picture
     * @return owner
     */
    public Account getPictureOwner(Picture picture) {
        if (picture == null) {
            return null;
        }
        Account account = pictureRepository.getPictureOwner(picture);
        return account;
    }

    /**
     * Create picture like for account. Only if there was no previous like for
     * picture.
     *
     * @param account
     * @param picture
     */
    @Transactional
    public void createLike(Account account, Picture picture) {
        // validate input
        if (account == null || picture == null) {
            return;
        }
        // make sure that account has not liked picture before
        MyLike like = myLikeRepository.findByAccountAndPicture(account, picture);
        if (like != null) {
            return;
        }
        // make sure that account is owner of the picture or that account
        // is friend of the picture owner
        Account owner = picture.getPictureAlbum().getOwner();
        if (friendService.areAccountsFriends(account, owner) != 2 && account.getId() != owner.getId()) {
            return;
        }
        // create new like
        like = new MyLike();
        like.setAccount(account);
        like.setPicture(picture);
        myLikeRepository.save(like);
        picture.setLikeCounter(picture.getLikeCounter() + 1);
        pictureRepository.save(picture);
    }

    /**
     * Create picture comment for account.
     *
     * @param account
     * @param picture
     * @param msg
     */
    @Transactional
    public void createComment(Account account, Picture picture, String msg) {
        // validate input
        if (account == null || picture == null || msg == null || msg.isEmpty() || msg.length() > 200) {
            return;
        }
        // make sure that account is owner of the picture or that account
        // is friend of the picture owner
        Account owner = picture.getPictureAlbum().getOwner();
        if (friendService.areAccountsFriends(account, owner) != 2 && account.getId() != owner.getId()) {
            return;
        }
        // create new comment
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setPicture(picture);
        comment.setContent(msg);
        comment.setTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    /**
     * Get 10 latest comments for given picture.
     *
     * @param picture
     * @return commentlist
     */
    @Transactional
    public List<Comment> getLatestComments(Picture picture) {
        if (picture == null) {
            return null;
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("time").descending());
        return commentRepository.findByPicture(picture, pageable);
    }

    /**
     * Returns picture content for given picture.
     *
     * @param id
     * @return content
     */
    @Transactional
    public byte[] getPictureContentByPictureId(long id) {
        return pictureRepository.getContentForPicture(id);
    }
}
