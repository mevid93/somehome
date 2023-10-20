package somehome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Development environment security configuration.
 */
@Profile("default")
@Configuration
@EnableWebSecurity
public class DevelopmentSecurityConfiguration implements WebMvcConfigurer {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private ApplicationContext context;

  /**
   * Set up development environment http configuration.
   *
   * @param http http security
   * @return http security configuration
   * @throws Exception exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf((csrf) -> csrf.disable());

    http.headers((headers) -> headers
        .frameOptions((frameOptions) -> frameOptions
            .sameOrigin()));

    http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
        .requestMatchers("/h2-console", "/h2-console/**").permitAll()
        .requestMatchers("/register").permitAll().anyRequest().authenticated())
        .formLogin(formLoginCustomizer -> formLoginCustomizer
            .loginPage("/login").defaultSuccessUrl("/").permitAll())
        .logout(logoutCustomizer -> logoutCustomizer
            .logoutUrl("/logout").logoutSuccessUrl("/login").permitAll());

    return http.build();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(context.getBean(PasswordEncoder.class));
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    ViewControllerRegistration r = registry.addViewController("/login");
    r.setViewName("login");
    WebMvcConfigurer.super.addViewControllers(registry);
  }

}
