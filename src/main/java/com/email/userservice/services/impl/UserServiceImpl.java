package com.email.userservice.services.impl;

import com.email.userservice.domain.Confirmation;
import com.email.userservice.domain.User;
import com.email.userservice.repositories.ConfirmationRepository;
import com.email.userservice.repositories.UserRepository;
import com.email.userservice.services.EmailService;
import com.email.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    @Override
    public User saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists !");
        }
        user.setEnabled(false);
        userRepository.save(user);

        var confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        // TODO SEND EMAIL TO USER WITH TOKEN
//        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        var confirmation = confirmationRepository.findByToken(token);
        var user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        return Boolean.TRUE;
    }
}
