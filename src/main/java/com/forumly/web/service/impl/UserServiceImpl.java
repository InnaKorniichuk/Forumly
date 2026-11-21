package com.forumly.web.service.impl;

import com.forumly.web.dto.user.UserMapper;
import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Role;
import com.forumly.web.model.User;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public User create(UserRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw ApplicationException.conflict("Email already exists");
        }
        if (userRepository.existsByNickname(dto.getNickname())) {
            throw ApplicationException.conflict("Nickname already exists");
        }

        User user = UserMapper.toEntity(dto);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public UserResponseDto readById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("User with id " + id + " not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public User update(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        if (dto.getNickname() != null && !dto.getNickname().equals(user.getNickname())) {
            userRepository.findByNickname(dto.getNickname())
                    .ifPresent(u -> {
                        if (!u.getId().equals(id)) {
                            throw ApplicationException.conflict("Nickname already taken");
                        }
                    });
            user.setNickname(dto.getNickname());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(dto.getEmail())
                    .ifPresent(u -> {
                        if (!u.getId().equals(id)) {
                            throw ApplicationException.conflict("Email already in use");
                        }
                    });
            user.setEmail(dto.getEmail());
        }

        return userRepository.save(user);
    }


    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("User with id "+id+" not found"));
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ApplicationException.notFound("User with such email doesn't exist"));
    }

    @Override
    public User getByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));
    }
}
