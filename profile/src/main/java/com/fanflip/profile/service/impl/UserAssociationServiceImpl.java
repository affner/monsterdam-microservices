package com.fanflip.profile.service.impl;

import com.fanflip.profile.domain.UserAssociation;
import com.fanflip.profile.repository.UserAssociationRepository;
import com.fanflip.profile.service.UserAssociationService;
import com.fanflip.profile.service.dto.UserAssociationDTO;
import com.fanflip.profile.service.mapper.UserAssociationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.profile.domain.UserAssociation}.
 */
@Service
@Transactional
public class UserAssociationServiceImpl implements UserAssociationService {

    private final Logger log = LoggerFactory.getLogger(UserAssociationServiceImpl.class);

    private final UserAssociationRepository userAssociationRepository;

    private final UserAssociationMapper userAssociationMapper;

    public UserAssociationServiceImpl(UserAssociationRepository userAssociationRepository, UserAssociationMapper userAssociationMapper) {
        this.userAssociationRepository = userAssociationRepository;
        this.userAssociationMapper = userAssociationMapper;
    }

    @Override
    public UserAssociationDTO save(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to save UserAssociation : {}", userAssociationDTO);
        UserAssociation userAssociation = userAssociationMapper.toEntity(userAssociationDTO);
        userAssociation = userAssociationRepository.save(userAssociation);
        return userAssociationMapper.toDto(userAssociation);
    }

    @Override
    public UserAssociationDTO update(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to update UserAssociation : {}", userAssociationDTO);
        UserAssociation userAssociation = userAssociationMapper.toEntity(userAssociationDTO);
        userAssociation = userAssociationRepository.save(userAssociation);
        return userAssociationMapper.toDto(userAssociation);
    }

    @Override
    public Optional<UserAssociationDTO> partialUpdate(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to partially update UserAssociation : {}", userAssociationDTO);

        return userAssociationRepository
            .findById(userAssociationDTO.getId())
            .map(existingUserAssociation -> {
                userAssociationMapper.partialUpdate(existingUserAssociation, userAssociationDTO);

                return existingUserAssociation;
            })
            .map(userAssociationRepository::save)
            .map(userAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAssociationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAssociations");
        return userAssociationRepository.findAll(pageable).map(userAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAssociationDTO> findOne(Long id) {
        log.debug("Request to get UserAssociation : {}", id);
        return userAssociationRepository.findById(id).map(userAssociationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAssociation : {}", id);
        userAssociationRepository.deleteById(id);
    }
}
