package net.yassine.springsecurityinmemorydao.config;

// Les utilisateurs en mémoire via InMemoryUserDetailsManager.
// Un PasswordEncoder (BCrypt).
// Les règles de sécurité pour les endpoints.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        // Définir les utilisateurs en mémoire
        UserDetails user1 = User.withUsername("admin")
                .password(passwordEncoder().encode("password")) // password haché
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.withUsername("user")
                .password(passwordEncoder().encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilerChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().permitAll()
                )
        /*http.authorizeHttpRequests(new Consumer<AuthorizeHttpRequestsConfigurer>() {
            @Override
            public void accept(AuthorizeHttpRequestsConfigurer auth) {
                auth.requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().permitAll();
            }
        });*/

                .formLogin(form -> form
                        // Si vous souhaitez utiliser la page par défaut de Spring Security :
                        //Retirez simplement .loginPage("/login") de votre configuration :
                        //.loginPage("/login")
                        .defaultSuccessUrl("/user", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
                return http.build();
                // http ici est une instance de la classe HttpSecurity, qui est utilisée pour définir les configurations de sécurité HTTP. Après avoir spécifié toutes les règles et comportements de sécurité, l'appel à build() crée un objet prêt à être utilisé par Spring Security.
    }


}
