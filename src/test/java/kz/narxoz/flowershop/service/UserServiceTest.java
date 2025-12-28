package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.user.ChangePasswordRequest;
import kz.narxoz.flowershop.dto.user.UpdateProfileRequest;
import kz.narxoz.flowershop.entity.Role;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.mapper.UserMapper;
import kz.narxoz.flowershop.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void updateProfile_updatesName() {
        UserRepository repo = mock(UserRepository.class);
        var encoder = new BCryptPasswordEncoder();
        UserMapper mapper = Mappers.getMapper(UserMapper.class);

        UserService service = new UserService(repo, encoder, mapper);

        Role role = new Role("USER"); role.setId(2L);

        User u = new User();
        u.setId(1L);
        u.setEmail("u@demo.kz");
        u.setFullName("Old Name");
        u.setPasswordHash(encoder.encode("User123!"));
        u.setRole(role);
        u.setEnabled(true);

        when(repo.findByEmail("u@demo.kz")).thenReturn(Optional.of(u));

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setFullName("New Name");

        var resp = service.updateProfile("u@demo.kz", req);
        assertEquals("New Name", resp.fullName());
    }

    @Test
    void changePassword_wrongOldPassword_throws() {
        UserRepository repo = mock(UserRepository.class);
        var encoder = new BCryptPasswordEncoder();
        UserMapper mapper = Mappers.getMapper(UserMapper.class);

        UserService service = new UserService(repo, encoder, mapper);

        Role role = new Role("USER"); role.setId(2L);
        User u = new User();
        u.setEmail("u@demo.kz");
        u.setFullName("User");
        u.setPasswordHash(encoder.encode("CorrectOld"));
        u.setRole(role);
        u.setEnabled(true);

        when(repo.findByEmail("u@demo.kz")).thenReturn(Optional.of(u));

        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setOldPassword("WrongOld");
        req.setNewPassword("NewSecret123");

        assertThrows(BadRequestException.class, () -> service.changePassword("u@demo.kz", req));
    }
}
