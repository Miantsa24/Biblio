package com.example.biblio.service;

import com.example.biblio.model.Admin;
import com.example.biblio.repository.AdminRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Initialisation de l'admin par défaut après le démarrage de l'application
    @PostConstruct
    public void initDefaultAdmin() {
        Optional<Admin> adminOpt = adminRepository.findByEmail("admin@biblio.com");
        if (adminOpt.isEmpty()) {
            String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
            Admin admin = new Admin("Admin Bibliothèque", "admin@biblio.com", hashedPassword);
            adminRepository.save(admin);
        }
    }

    public boolean authenticate(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return BCrypt.checkpw(password, admin.getMotDePasse());
        }
        return false;
    }
}