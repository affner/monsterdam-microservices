package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.PersonalSocialLinks;
import com.monsterdam.profile.repository.PersonalSocialLinksRepository;
import com.monsterdam.profile.service.PersonalSocialLinksService;
import com.monsterdam.profile.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.profile.service.mapper.PersonalSocialLinksMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.profile.domain.PersonalSocialLinks}.
 */
@Service
@Transactional
public class PersonalSocialLinksServiceImpl implements PersonalSocialLinksService {

    private final Logger log = LoggerFactory.getLogger(PersonalSocialLinksServiceImpl.class);

    private final PersonalSocialLinksRepository personalSocialLinksRepository;

    private final PersonalSocialLinksMapper personalSocialLinksMapper;

    public PersonalSocialLinksServiceImpl(
        PersonalSocialLinksRepository personalSocialLinksRepository,
        PersonalSocialLinksMapper personalSocialLinksMapper
    ) {
        this.personalSocialLinksRepository = personalSocialLinksRepository;
        this.personalSocialLinksMapper = personalSocialLinksMapper;
    }

    @Override
    public PersonalSocialLinksDTO save(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to save PersonalSocialLinks : {}", personalSocialLinksDTO);
        PersonalSocialLinks personalSocialLinks = personalSocialLinksMapper.toEntity(personalSocialLinksDTO);
        personalSocialLinks = personalSocialLinksRepository.save(personalSocialLinks);
        return personalSocialLinksMapper.toDto(personalSocialLinks);
    }

    @Override
    public PersonalSocialLinksDTO update(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to update PersonalSocialLinks : {}", personalSocialLinksDTO);
        PersonalSocialLinks personalSocialLinks = personalSocialLinksMapper.toEntity(personalSocialLinksDTO);
        personalSocialLinks = personalSocialLinksRepository.save(personalSocialLinks);
        return personalSocialLinksMapper.toDto(personalSocialLinks);
    }

    @Override
    public Optional<PersonalSocialLinksDTO> partialUpdate(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to partially update PersonalSocialLinks : {}", personalSocialLinksDTO);

        return personalSocialLinksRepository
            .findById(personalSocialLinksDTO.getId())
            .map(existingPersonalSocialLinks -> {
                personalSocialLinksMapper.partialUpdate(existingPersonalSocialLinks, personalSocialLinksDTO);

                return existingPersonalSocialLinks;
            })
            .map(personalSocialLinksRepository::save)
            .map(personalSocialLinksMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonalSocialLinksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalSocialLinks");
        return personalSocialLinksRepository.findAll(pageable).map(personalSocialLinksMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonalSocialLinksDTO> findOne(Long id) {
        log.debug("Request to get PersonalSocialLinks : {}", id);
        return personalSocialLinksRepository.findById(id).map(personalSocialLinksMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PersonalSocialLinks : {}", id);
        personalSocialLinksRepository.deleteById(id);
    }
}
