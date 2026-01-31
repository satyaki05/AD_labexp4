package com.example.ADLabExp4.Controller;

import com.example.ADLabExp4.Entity.User;
import com.example.ADLabExp4.Respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public String signup(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/login.html";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password) {

        User user = userRepo.findByEmail(email);

        if (user != null && encoder.matches(password, user.getPassword())) {
            return "redirect:/dashboard.html";
        }

        return "redirect:/login.html";
    }

}
