package com.example.ADLabExp4.Controller;

import com.example.ADLabExp4.Entity.Resume;
import com.example.ADLabExp4.Entity.User;
import com.example.ADLabExp4.Respository.ResumeRepository;
import com.example.ADLabExp4.Respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import java.io.File;
import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ResumeRepository resumeRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ================= SIGNUP =================
    @PostMapping("/signup")
    public String signup(User user) {

        if (userRepo.findByEmail(user.getEmail()) != null) {
            return "redirect:/signup.html";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/dashboard.html";
    }

    // ================= LOGIN =================
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

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            return "redirect:/forgot-password.html";
        }

        return "redirect:/reset.html?email=" + email;
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String password) {

        User user = userRepo.findByEmail(email);

        if (user != null) {
            user.setPassword(encoder.encode(password));
            userRepo.save(user);
        }

        return "redirect:/login.html";
    }

    @PostMapping("/upload-resume")
    public String uploadResume(@RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return "redirect:/dashboard.html";
            }

            // ✅ Use user home directory (safe & writable)
            String baseDir = System.getProperty("user.home");
            String uploadDir = baseDir + File.separator + "portfolio_uploads" + File.separator + "resume";

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setFilePath(filePath);
            resumeRepo.save(resume);

            System.out.println("Resume saved at: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard.html";
    }



}

