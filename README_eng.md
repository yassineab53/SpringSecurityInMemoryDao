# Spring Security with In-Memory Authentication and Custom Login Page

This project demonstrates how to configure Spring Security using:
- **InMemoryUserDetailsManager** for user authentication.
- **DaoAuthenticationProvider**.
- A custom login page.

## Project Setup

### Prerequisites
- Java 17+
- Maven
- Spring Boot 3+
- Thymeleaf (for the custom login page)

---

## Step-by-Step Guide

### 1. Add Dependencies

Include the following dependencies in your `pom.xml`:

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

### 2. Configure Security

Create a `SecurityConfig` class:

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

### 3. Add a Login Controller

Create a `LoginController` to serve the login page:

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

### 4. Create Thymeleaf Views

#### `login.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login Page</h1>
    <form th:action="@{/login}" method="post">
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>
</body>
</html>
```

#### `user-home.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User Home</title>
</head>
<body>
    <h1>Welcome User</h1>
    <a th:href="@{/logout}">Logout</a>
</body>
</html>
```

#### `admin-home.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Admin Home</title>
</head>
<body>
    <h1>Welcome Admin</h1>
    <a th:href="@{/logout}">Logout</a>
</body>
</html>
```

---

### 5. Application Properties

Add the following to `application.properties`:

```properties
server.port=8086
```

---

### 6. Run the Application

Start the application and access the following URLs:
- Login: [http://localhost:8080/login](http://localhost:8080/login)
- User Home: [http://localhost:8080/user](http://localhost:8080/user)
- Admin Home: [http://localhost:8080/admin](http://localhost:8080/admin)

---

## Test Credentials

| Username | Password   | Role  |
|----------|------------|-------|
| admin    | admin123   | ADMIN |
| user     | user123    | USER  |

---

## Summary
This project showcases how to:
- Configure Spring Security for in-memory authentication.
- Create a custom login page.
- Protect endpoints based on roles.

You can extend this setup by integrating a database for user authentication or adding more security features like CSRF protection and HTTPS enforcement.
