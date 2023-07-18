package com.email.userservice.services;

import com.email.userservice.domain.Confirmation;
import com.email.userservice.domain.User;
import com.email.userservice.repositories.ConfirmationRepository;
import com.email.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
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
