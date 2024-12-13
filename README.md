# Spring Security avec Authentification en Mémoire et Page de Connexion Personnalisée

Ce projet démontre comment configurer Spring Security en utilisant :
- **InMemoryUserDetailsManager** pour l'authentification des utilisateurs.
- **DaoAuthenticationProvider**.
- Une page de connexion personnalisée.

## Configuration du Projet

### Prérequis
- Java 17+
- Maven
- Spring Boot 3+
- Thymeleaf (pour la page de connexion personnalisée)

---

## Guide Étape par Étape

### 1. Ajouter les Dépendances

Incluez les dépendances suivantes dans votre `pom.xml` :

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

---

### 2. Configurer la Sécurité

Créez une classe `SecurityConfig` :

```java
package com.example.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/user", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
```

---

### 3. Ajouter un Contrôleur de Connexion

Créez un `LoginController` pour gérer la page de connexion :

```java
package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String userHome() {
        return "user-home";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin-home";
    }
}
```

---

### 4. Créer les Vues Thymeleaf

#### `login.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
    <h1>Page de Connexion</h1>
    <form th:action="@{/login}" method="post">
        <div>
            <label for="username">Nom d'utilisateur :</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="password">Mot de passe :</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Connexion</button>
    </form>
</body>
</html>
```

#### `user-home.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Accueil Utilisateur</title>
</head>
<body>
    <h1>Bienvenue Utilisateur</h1>
    <a th:href="@{/logout}">Déconnexion</a>
</body>
</html>
```

#### `admin-home.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Accueil Admin</title>
</head>
<body>
    <h1>Bienvenue Admin</h1>
    <a th:href="@{/logout}">Déconnexion</a>
</body>
</html>
```

---

### 5. Propriétés de l'Application

Ajoutez ce qui suit dans `application.properties` :

```properties
server.port=8086
```

---

### 6. Lancer l'Application

Démarrez l'application et accédez aux URLs suivantes :
- Connexion : [http://localhost:8060/login](http://localhost:8086/login)
- Accueil Utilisateur : [http://localhost:8086/user](http://localhost:8086/user)
- Accueil Admin : [http://localhost:8086/admin](http://localhost:8086/admin)

---

## Identifiants de Test

| Nom d'utilisateur | Mot de passe | Rôle  |
|-------------------|--------------|-------|
| admin            | admin123    | ADMIN |
| user             | user123     | USER  |

---

## Résumé
Ce projet montre comment :
- Configurer Spring Security pour une authentification en mémoire.
- Créer une page de connexion personnalisée.
- Protéger les points d'accès en fonction des rôles.

Vous pouvez étendre cette configuration en intégrant une base de données pour l'authentification des utilisateurs ou en ajoutant d'autres fonctionnalités de sécurité comme la protection CSRF et l'utilisation de HTTPS.

---

## Contributeurs
- **Abbou Yassine**
- **Darghal Mohamed**
