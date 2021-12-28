package org.but.feec.esport.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.esport.api.AdminAuthView;
import org.but.feec.esport.data.AdminRepository;
import org.but.feec.esport.exceptions.ResourceNotFoundException;

public class AdminAuthService {

    private AdminRepository adminRepository;

    public AdminAuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    private AdminAuthView findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }


        AdminAuthView adminAuthView = findAdminByEmail(username);


        if (adminAuthView == null) {
            throw new ResourceNotFoundException("Provided username is not found.");
        }



        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), adminAuthView.getPassword());
        return result.verified;

    }
}

