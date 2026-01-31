package com.example.ADLabExp4.Respository;

import com.example.ADLabExp4.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
