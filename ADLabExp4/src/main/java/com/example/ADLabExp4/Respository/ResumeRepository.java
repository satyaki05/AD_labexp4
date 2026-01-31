package com.example.ADLabExp4.Respository;

import com.example.ADLabExp4.Entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
