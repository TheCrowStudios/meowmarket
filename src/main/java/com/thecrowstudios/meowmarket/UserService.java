package com.thecrowstudios.meowmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setSession(httpSession.getId());

        Address address = new Address();
        address.setHouseNumber(userRegistrationDTO.getHouseNumber());
        address.setStreetName(userRegistrationDTO.getStreetName());
        address.setCity(userRegistrationDTO.getCity());
        address.setPostCode(userRegistrationDTO.getPostCode());

        user.setAddress(address);

        return userRepository.save(user);
    }
}
