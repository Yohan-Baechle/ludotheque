package fr.eni.ludotheque.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	            .requestMatchers("/", "/login").permitAll()
	            .requestMatchers("/clients/**", "/jeux/**").hasAnyRole("EMPLOYEE", "ADMIN")
	            .requestMatchers("/utilisateurs/**", "/inventaire/**").hasRole("ADMIN")
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/")
	            .failureUrl("/login?error=true")
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true)
	            .clearAuthentication(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll()
	        );
	    // Ne pas désactiver les utilisateurs anonymes
	    return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        // Utilise PasswordEncoder avec support pour plusieurs formats (BCrypt recommandé)
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM utilisateurs WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT u.username, r.role FROM utilisateurs u INNER JOIN roles r ON u.id = r.utilisateur_id WHERE u.username = ?");
        return manager;
    }

//    // Méthode utilitaire pour générer un mot de passe encodé
//    public static void main(String[] args) {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        String rawPasswordAdmin = "admin123";
//        String rawPasswordEmployee = "employee123";
//
//        System.out.println("Mot de passe encodé pour admin : " + encoder.encode(rawPasswordAdmin));
//        System.out.println("Mot de passe encodé pour employee : " + encoder.encode(rawPasswordEmployee));
//    }
}
