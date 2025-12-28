package kz.narxoz.flowershop.config;

import kz.narxoz.flowershop.entity.Cart;
import kz.narxoz.flowershop.entity.Role;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.repository.CartRepository;
import kz.narxoz.flowershop.repository.RoleRepository;
import kz.narxoz.flowershop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepository,
                           UserRepository userRepository,
                           CartRepository cartRepository,
                           PasswordEncoder encoder) {
        return args -> {
            Role admin = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            Role user = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
            Role manager = roleRepository.findByName("MANAGER").orElseGet(() -> roleRepository.save(new Role("MANAGER")));

            // default admin for demo
            if (!userRepository.existsByEmail("admin@flowershop.kz")) {
                User u = new User();
                u.setEmail("admin@flowershop.kz");
                u.setFullName("Admin Flower Shop");
                u.setPasswordHash(encoder.encode("Admin123!"));
                u.setRole(admin);
                u.setEnabled(true);
                u = userRepository.save(u);

                Cart c = new Cart();
                c.setUser(u);
                cartRepository.save(c);
            }
        };
    }
}
