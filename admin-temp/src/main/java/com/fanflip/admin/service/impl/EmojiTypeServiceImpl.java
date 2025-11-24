package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.EmojiTypeRepository;
import com.monsterdam.admin.repository.search.EmojiTypeSearchRepository;
import com.monsterdam.admin.service.EmojiTypeService;
import com.monsterdam.admin.service.dto.EmojiTypeDTO;
import com.monsterdam.admin.service.mapper.EmojiTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.EmojiType}.
 */
@Service
@Transactional
public class EmojiTypeServiceImpl implements EmojiTypeService {

    private final Logger log = LoggerFactory.getLogger(EmojiTypeServiceImpl.class);

    private final EmojiTypeRepository emojiTypeRepository;

    private final EmojiTypeMapper emojiTypeMapper;

    private final EmojiTypeSearchRepository emojiTypeSearchRepository;

    public EmojiTypeServiceImpl(
        EmojiTypeRepository emojiTypeRepository,
        EmojiTypeMapper emojiTypeMapper,
        EmojiTypeSearchRepository emojiTypeSearchRepository
    ) {
        this.emojiTypeRepository = emojiTypeRepository;
        this.emojiTypeMapper = emojiTypeMapper;
        this.emojiTypeSearchRepository = emojiTypeSearchRepository;
    }

    @Override
    public Mono<EmojiTypeDTO> save(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to save EmojiType : {}", emojiTypeDTO);
        return emojiTypeRepository
            .save(emojiTypeMapper.toEntity(emojiTypeDTO))
            .flatMap(emojiTypeSearchRepository::save)
            .map(emojiTypeMapper::toDto);
    }

    @Override
    public Mono<EmojiTypeDTO> update(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to update EmojiType : {}", emojiTypeDTO);
        return emojiTypeRepository
            .save(emojiTypeMapper.toEntity(emojiTypeDTO))
            .flatMap(emojiTypeSearchRepository::save)
            .map(emojiTypeMapper::toDto);
    }

    @Override
    public Mono<EmojiTypeDTO> partialUpdate(EmojiTypeDTO emojiTypeDTO) {
        log.debug("Request to partially update EmojiType : {}", emojiTypeDTO);

        return emojiTypeRepository
            .findById(emojiTypeDTO.getId())
            .map(existingEmojiType -> {
                emojiTypeMapper.partialUpdate(existingEmojiType, emojiTypeDTO);

                return existingEmojiType;
            })
            .flatMap(emojiTypeRepository::save)
            .flatMap(savedEmojiType -> {
                emojiTypeSearchRepository.save(savedEmojiType);
                return Mono.just(savedEmojiType);
            })
            .map(emojiTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EmojiTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmojiTypes");
        return emojiTypeRepository.findAllBy(pageable).map(emojiTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return emojiTypeRepository.count();
    }

    public Mono<Long> searchCount() {
        return emojiTypeSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EmojiTypeDTO> findOne(Long id) {
        log.debug("Request to get EmojiType : {}", id);
        return emojiTypeRepository.findById(id).map(emojiTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EmojiType : {}", id);
        return emojiTypeRepository.deleteById(id).then(emojiTypeSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EmojiTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmojiTypes for query {}", query);
        return emojiTypeSearchRepository.search(query, pageable).map(emojiTypeMapper::toDto);
    }
}
