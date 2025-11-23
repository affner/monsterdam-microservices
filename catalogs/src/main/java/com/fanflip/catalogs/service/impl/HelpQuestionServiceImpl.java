package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.HelpQuestion;
import com.fanflip.catalogs.repository.HelpQuestionRepository;
import com.fanflip.catalogs.service.HelpQuestionService;
import com.fanflip.catalogs.service.dto.HelpQuestionDTO;
import com.fanflip.catalogs.service.mapper.HelpQuestionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.HelpQuestion}.
 */
@Service
@Transactional
public class HelpQuestionServiceImpl implements HelpQuestionService {

    private final Logger log = LoggerFactory.getLogger(HelpQuestionServiceImpl.class);

    private final HelpQuestionRepository helpQuestionRepository;

    private final HelpQuestionMapper helpQuestionMapper;

    public HelpQuestionServiceImpl(HelpQuestionRepository helpQuestionRepository, HelpQuestionMapper helpQuestionMapper) {
        this.helpQuestionRepository = helpQuestionRepository;
        this.helpQuestionMapper = helpQuestionMapper;
    }

    @Override
    public HelpQuestionDTO save(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to save HelpQuestion : {}", helpQuestionDTO);
        HelpQuestion helpQuestion = helpQuestionMapper.toEntity(helpQuestionDTO);
        helpQuestion = helpQuestionRepository.save(helpQuestion);
        return helpQuestionMapper.toDto(helpQuestion);
    }

    @Override
    public HelpQuestionDTO update(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to update HelpQuestion : {}", helpQuestionDTO);
        HelpQuestion helpQuestion = helpQuestionMapper.toEntity(helpQuestionDTO);
        helpQuestion = helpQuestionRepository.save(helpQuestion);
        return helpQuestionMapper.toDto(helpQuestion);
    }

    @Override
    public Optional<HelpQuestionDTO> partialUpdate(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to partially update HelpQuestion : {}", helpQuestionDTO);

        return helpQuestionRepository
            .findById(helpQuestionDTO.getId())
            .map(existingHelpQuestion -> {
                helpQuestionMapper.partialUpdate(existingHelpQuestion, helpQuestionDTO);

                return existingHelpQuestion;
            })
            .map(helpQuestionRepository::save)
            .map(helpQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpQuestions");
        return helpQuestionRepository.findAll(pageable).map(helpQuestionMapper::toDto);
    }

    public Page<HelpQuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return helpQuestionRepository.findAllWithEagerRelationships(pageable).map(helpQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpQuestionDTO> findOne(Long id) {
        log.debug("Request to get HelpQuestion : {}", id);
        return helpQuestionRepository.findOneWithEagerRelationships(id).map(helpQuestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HelpQuestion : {}", id);
        helpQuestionRepository.deleteById(id);
    }
}
