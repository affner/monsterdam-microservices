package com.monsterdam.catalogs.service.impl;

import com.monsterdam.catalogs.domain.EmojiType;
import com.monsterdam.catalogs.repository.EmojiTypeRepository;
import com.monsterdam.catalogs.service.EmojiTypeService;
import com.monsterdam.catalogs.service.dto.EmojiTypeDTO;
import com.monsterdam.catalogs.service.mapper.EmojiTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.catalogs.domain.EmojiType}.
 */
@Service
@Transactional
public class EmojiTypeServiceImpl implements EmojiTypeService {

    private final Logger log = LoggerFactory.getLogger(EmojiTypeServiceImpl.class);

    private final EmojiTypeRepository emojiTypeRepository;

    private final EmojiTypeMapper emojiTypeMapper;

    public EmojiTypeServiceImpl(EmojiTypeRepository emojiTypeRepository, EmojiTypeMapper emojiTypeMapper) {
        this.emojiTypeRepository = emojiTypeRepository;
        this.emojiTypeMapper = emojiTypeMapper;
    }

    @Override
    public EmojiTypeDTO save(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to save EmojiType : {}", emojiTypeDTO);
        EmojiType emojiType = emojiTypeMapper.toEntity(emojiTypeDTO);
        emojiType = emojiTypeRepository.save(emojiType);
        return emojiTypeMapper.toDto(emojiType);
    }

    @Override
    public EmojiTypeDTO update(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to update EmojiType : {}", emojiTypeDTO);
        EmojiType emojiType = emojiTypeMapper.toEntity(emojiTypeDTO);
        emojiType = emojiTypeRepository.save(emojiType);
        return emojiTypeMapper.toDto(emojiType);
    }

    @Override
    public Optional<EmojiTypeDTO> partialUpdate(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to partially update EmojiType : {}", emojiTypeDTO);

        return emojiTypeRepository
            .findById(emojiTypeDTO.getId())
            .map(existingEmojiType -> {
                emojiTypeMapper.partialUpdate(existingEmojiType, emojiTypeDTO);

                return existingEmojiType;
            })
            .map(emojiTypeRepository::save)
            .map(emojiTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmojiTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmojiTypes");
        return emojiTypeRepository.findAll(pageable).map(emojiTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmojiTypeDTO> findOne(Long id) {
        log.debug("Request to get EmojiType : {}", id);
        return emojiTypeRepository.findById(id).map(emojiTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmojiType : {}", id);
        emojiTypeRepository.deleteById(id);
    }
}
