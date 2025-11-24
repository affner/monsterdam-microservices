package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.UserSettings;
import com.monsterdam.profile.repository.UserSettingsRepository;
import com.monsterdam.profile.service.UserSettingsService;
import com.monsterdam.profile.service.dto.UserSettingsDTO;
import com.monsterdam.profile.service.mapper.UserSettingsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.profile.domain.UserSettings}.
 */
@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final Logger log = LoggerFactory.getLogger(UserSettingsServiceImpl.class);

    private final UserSettingsRepository userSettingsRepository;

    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsServiceImpl(UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
    }

    @Override
    public UserSettingsDTO save(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to save UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public UserSettingsDTO update(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to update UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public Optional<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to partially update UserSettings : {}", userSettingsDTO);

        return userSettingsRepository
            .findById(userSettingsDTO.getId())
            .map(existingUserSettings -> {
                userSettingsMapper.partialUpdate(existingUserSettings, userSettingsDTO);

                return existingUserSettings;
            })
            .map(userSettingsRepository::save)
            .map(userSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSettings");
        return userSettingsRepository.findAll(pageable).map(userSettingsMapper::toDto);
    }

    /**
     *  Get all the userSettings where UserProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserSettingsDTO> findAllWhereUserProfileIsNull() {
        log.debug("Request to get all userSettings where UserProfile is null");
        return StreamSupport
            .stream(userSettingsRepository.findAll().spliterator(), false)
            .filter(userSettings -> userSettings.getUserProfile() == null)
            .map(userSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSettingsDTO> findOne(Long id) {
        log.debug("Request to get UserSettings : {}", id);
        return userSettingsRepository.findById(id).map(userSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSettings : {}", id);
        userSettingsRepository.deleteById(id);
    }
}
