package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.admin.CreateUserRequest;
import kz.narxoz.flowershop.dto.user.UserResponse;
import kz.narxoz.flowershop.entity.Cart;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.UserMapper;
import kz.narxoz.flowershop.repository.CartRepository;
import kz.narxoz.flowershop.repository.RoleRepository;
import kz.narxoz.flowershop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AdminUserService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            CartRepository cartRepository,
                            PasswordEncoder passwordEncoder,
                            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail().toLowerCase())) {
            throw new BadRequestException("Email already exists");
        }
        var role = roleRepository.findByName(req.getRole().toUpperCase())
                .orElseThrow(() -> new BadRequestException("Role not found: " + req.getRole()));

        User user = new User();
        user.setEmail(req.getEmail().toLowerCase());
        user.setFullName(req.getFullName());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setEnabled(true);

        user = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse setBlocked(Long userId, boolean blocked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        user.setEnabled(!blocked);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
