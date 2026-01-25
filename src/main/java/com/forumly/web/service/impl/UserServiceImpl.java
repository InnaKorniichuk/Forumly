package com.forumly.web.service.impl;

import com.forumly.web.dto.user.UserMapper;
import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserUpdateDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.User;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public  UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
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

        // TODO ENCODE PASSWORD
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    @Override
    public User readById(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("User with id " + id + " not found"));
    }

    @Override
    public User update(Long id, UserUpdateDto dto) {
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
        User user=readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ApplicationException.notFound("User with such email doesn't exist"));
    }

    @Override
    public User getByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository.findByNickname(username)
                .orElseThrow(() -> ApplicationException.notFound("User not found with nickname: " + username));
    }
}
