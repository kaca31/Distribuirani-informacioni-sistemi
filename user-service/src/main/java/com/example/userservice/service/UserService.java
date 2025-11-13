package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserDto create(UserDto dto) {
        User u = User.builder().name(dto.getName()).email(dto.getEmail()).address(dto.getAddress()).build();
        return toDto(repo.save(u));
    }

    public UserDto get(Long id) {
        return repo.findById(id).map(this::toDto).orElse(null);
    }

    public List<UserDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto update(Long id, UserDto dto) {
        User u = repo.findById(id).orElseThrow();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setAddress(dto.getAddress());
        return toDto(repo.save(u));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private UserDto toDto(User u) {
        return UserDto.builder().id(u.getId()).name(u.getName()).email(u.getEmail()).address(u.getAddress()).build();
    }
}
