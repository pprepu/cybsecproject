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
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
            .authorizeRequests()
                .anyRequest().permitAll().and()
                .formLogin().permitAll()
                .defaultSuccessUrl("/secret")
                .and()
                .logout().permitAll();
    }
    
    /* 1.1 the method above should be replaced with the method below, so that accessing the /secret -path
    would be open to authenticated users.
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
    }
    */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        
        auth.userDetailsService(userDetailsService);
        /* 1.4, second part to fixing unencrypted passwords
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        */
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
