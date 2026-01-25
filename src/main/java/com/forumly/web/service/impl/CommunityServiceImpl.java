package com.forumly.web.service.impl;

import com.forumly.web.dto.community.CommunityMapper;
import com.forumly.web.dto.community.CommunityRequestDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Community;
import com.forumly.web.model.User;
import com.forumly.web.repository.CommunityRepository;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.service.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public CommunityServiceImpl(CommunityRepository communityRepository, UserRepository userRepository){
        this.communityRepository=communityRepository;
        this.userRepository=userRepository;
    }
    @Override
    public Community create(CommunityRequestDto dto, Long ownerID) {
        User owner = userRepository.findById(ownerID)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        if (communityRepository.existsByName(dto.getName()))
            throw ApplicationException.conflict("Community with name '" + dto.getName() + "' already exists");

        Community community = CommunityMapper.toEntity(dto,owner);

       return communityRepository.save(community);
    }

    @Override
    public Community readById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("Community with id " + id + " not found"));
    }

    @Override
    public void delete(Long id) {
        Community community=readById(id);
        communityRepository.delete(community);
    }

    @Override
    public List<Community> getAll() {
        return communityRepository.findAll();
    }

    @Override
    public Community getByName(String name) {
        return communityRepository.findByName(name)
                .orElseThrow(() -> ApplicationException.notFound("Community with name " + name +" not found"));
    }

    @Override
    public List<Community> getByOwnerId(Long userId) {
        List<Community> communities =communityRepository.findByOwnerId(userId);
        return communities != null ? communities : Collections.emptyList() ;
    }
}
