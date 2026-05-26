package com.liciot.usermanagementms.configuration.security;

 import com.liciot.usermanagementms.filter.security.JwtRequestFilter;
 import com.liciot.usermanagementms.service.security.JWTUserDetailsService;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 import org.springframework.scheduling.annotation.EnableScheduling;
 import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebSecurityEntrypoint webSecurityEntrypoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    AuthenticationManagerBuilder auth;

    // @Autowired
    //    AuthenticationManagerBuilder auth; daca pun asta in paramatru la configura global da aia ucm cabeanul acucm este in costructie ...
     public void configureGlobal( ) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        this.auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
        //aici poti return altceva al tau
    }
//indiferent de c epit tot imi trece prin Filtru stiu ca zicea de acel proxi care sta intr-un deleggator ca alece ce seucirity chain sa trimita requestu in functie de enpoint dar cred cred ca eu trebuie sa configuraz mi kute chqinuri cred ca acum au uul si toatwe reaquesturile trec prin el asa ca  grijka c efaci
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity.cors().and().csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers("/security/authenticate","/security/authenticate/*", "/security/oauth2/*", "/crud/humanuser/insert", "/crud/user/*","/crud/user/*/*", "/crud/device/*", "/crud/device/*/*" ).permitAll().
                // all other requests need to be authenticated
                        anyRequest().authenticated().and().
                // make sure we use stateless session; session won't be used to
                // store user's state.
                        exceptionHandling().authenticationEntryPoint(webSecurityEntrypoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

