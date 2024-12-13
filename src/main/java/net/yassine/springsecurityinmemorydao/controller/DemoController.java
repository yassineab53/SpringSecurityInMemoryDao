package net.yassine.springsecurityinmemorydao.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/")
    public String home() {
        return "Bienvenue dans notre presentation sur Spring Security :)) ";
    }
    /*@GetMapping("/user")
    public String user(){
        return "Bienvenue, utilisateur authentifié !";
    }*/
    /*@GetMapping("/admin")
    public String admin() {
        return "Bienvenue, administrateur !";
    }*/
}
