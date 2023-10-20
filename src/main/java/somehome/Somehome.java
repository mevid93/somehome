package somehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class for starting the application.
 */
@SpringBootApplication
public class Somehome extends SpringBootServletInitializer {

  /**
   * Override package structure configuration. To keep controllers, entities,
   * repository-interfaces and services in different packages, we have to
   * override default configuration. Otherwise Spring would not be able to
   * pick classes from right packages.
   *
   * @param application spring application builder
   * @return configured spring application builder
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Somehome.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(Somehome.class);
  }

}
