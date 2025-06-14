package sk.tuke.kp.kpi.pexeso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kp.kpi.pexeso.entity.User;
import sk.tuke.kp.kpi.pexeso.repository.UserRepository;
import sk.tuke.kp.kpi.pexeso.service.UserService;

@Controller("userController")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private boolean registerMode = false;
    private User loggedUser;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("register_mode", registerMode);
        return "gamestudio";
    }

    @GetMapping("/register-mode")
    public String switchToRegister(Model model) {
        registerMode = true;
        return "redirect:/login";
    }

    @GetMapping("/login-mode")
    public String switchToLogin(Model model) {
        registerMode = false;
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String passwd) {
        User user = userRepository.findByLogin(login);
        if (user != null && user.getPassword().equals(passwd)) {
            loggedUser = user;
            return "redirect:/";
        }
        return "redirect:/login?error=true";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            return "redirect:/login?error=true";
        }

        if (userService.checkLogin(login) == 1) {
            return "redirect:/login?error=true";
        }

        User newUser = new User(login, password);
        userService.addUser(newUser);
        loggedUser = newUser;
        registerMode = false;
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}
