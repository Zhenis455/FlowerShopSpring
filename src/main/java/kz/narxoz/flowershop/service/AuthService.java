package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.auth.AuthRequest;
import kz.narxoz.flowershop.dto.auth.AuthResponse;
import kz.narxoz.flowershop.dto.auth.RegisterRequest;
import kz.narxoz.flowershop.entity.Cart;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.repository.CartRepository;
import kz.narxoz.flowershop.repository.RoleRepository;
import kz.narxoz.flowershop.repository.UserRepository;
import kz.narxoz.flowershop.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       CartRepository cartRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        var role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

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

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getName());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().getName());
    }

    public AuthResponse login(AuthRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase(), req.getPassword())
        );

        var user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User is blocked");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getName());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().getName());
    }
}
