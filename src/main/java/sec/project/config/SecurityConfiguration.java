package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at the moment
        http.authorizeRequests()
                .anyRequest().permitAll();
    }
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/form").permitAll()
                .antMatchers("/done").permitAll()
                .antMatchers("/accounts").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().permitAll()
                .defaultSuccessUrl("/secret")
                .and()
                .logout().permitAll();
            //.invalidateHttpSession(true)                                             
            //.addLogoutHandler(logoutHandler)                                         
            //.deleteCookies(cookieNamesToClear);
                //.logoutSuccessUrl("/form");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
