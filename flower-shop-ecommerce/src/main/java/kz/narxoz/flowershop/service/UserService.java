package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.user.ChangePasswordRequest;
import kz.narxoz.flowershop.dto.user.UpdateProfileRequest;
import kz.narxoz.flowershop.dto.user.UserResponse;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.UserMapper;
import kz.narxoz.flowershop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User getByEmailOrThrow(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse getProfile(String email) {
        return userMapper.toResponse(getByEmailOrThrow(email));
    }

    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest req) {
        User user = getByEmailOrThrow(email);
        user.setFullName(req.getFullName());
        return userMapper.toResponse(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest req) {
        User user = getByEmailOrThrow(email);
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
    }
}
