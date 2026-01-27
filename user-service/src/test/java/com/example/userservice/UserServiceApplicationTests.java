package com.example.userservice;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceApplicationTests {
    private UserRepository repo;
    private UserService service;

    @BeforeEach
    void setup() {
        repo = mock(UserRepository.class);
        service = new UserService(repo);
    }

    @Test
    void testCreateUser() {
        UserDto dto = UserDto.builder()
                .name("Marko Markovic")
                .email("marko@example.com")
                .address("Bulevar 1, Beograd")
                .build();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(repo.save(captor.capture())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L); // simulate DB generated ID
            return u;
        });

        UserDto created = service.create(dto);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Marko Markovic", created.getName());
        assertEquals("marko@example.com", created.getEmail());
        assertEquals("Bulevar 1, Beograd", created.getAddress());

        User saved = captor.getValue();
        assertEquals("Marko Markovic", saved.getName());
    }

    @Test
    void testGetUser() {
        User user = User.builder().id(1L).name("Ana Anic").email("ana@example.com").address("Ulica 2, Novi Sad").build();
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto dto = service.get(1L);

        assertNotNull(dto);
        assertEquals("Ana Anic", dto.getName());
        assertEquals("ana@example.com", dto.getEmail());
    }

    @Test
    void testGetAllUsers() {
        User u1 = User.builder().id(1L).name("Ana Anic").email("ana@example.com").address("Novi Sad").build();
        User u2 = User.builder().id(2L).name("Marko Markovic").email("marko@example.com").address("Beograd").build();
        when(repo.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UserDto> users = service.getAll();

        assertEquals(2, users.size());
        assertEquals("Ana Anic", users.get(0).getName());
        assertEquals("Marko Markovic", users.get(1).getName());
    }

    @Test
    void testUpdateUser() {
        User existing = User.builder().id(1L).name("Ana Anic").email("ana@example.com").address("Novi Sad").build();
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updateDto = UserDto.builder().name("Ana A.").email("ana.a@example.com").address("Beograd").build();
        UserDto updated = service.update(1L, updateDto);

        assertEquals("Ana A.", updated.getName());
        assertEquals("ana.a@example.com", updated.getEmail());
        assertEquals("Beograd", updated.getAddress());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(repo).deleteById(1L);
        service.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}