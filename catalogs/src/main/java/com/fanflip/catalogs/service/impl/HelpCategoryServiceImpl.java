package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.HelpCategory;
import com.fanflip.catalogs.repository.HelpCategoryRepository;
import com.fanflip.catalogs.service.HelpCategoryService;
import com.fanflip.catalogs.service.dto.HelpCategoryDTO;
import com.fanflip.catalogs.service.mapper.HelpCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.HelpCategory}.
 */
@Service
@Transactional
public class HelpCategoryServiceImpl implements HelpCategoryService {

    private final Logger log = LoggerFactory.getLogger(HelpCategoryServiceImpl.class);

    private final HelpCategoryRepository helpCategoryRepository;

    private final HelpCategoryMapper helpCategoryMapper;

    public HelpCategoryServiceImpl(HelpCategoryRepository helpCategoryRepository, HelpCategoryMapper helpCategoryMapper) {
        this.helpCategoryRepository = helpCategoryRepository;
        this.helpCategoryMapper = helpCategoryMapper;
    }

    @Override
    public HelpCategoryDTO save(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to save HelpCategory : {}", helpCategoryDTO);
        HelpCategory helpCategory = helpCategoryMapper.toEntity(helpCategoryDTO);
        helpCategory = helpCategoryRepository.save(helpCategory);
        return helpCategoryMapper.toDto(helpCategory);
    }

    @Override
    public HelpCategoryDTO update(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to update HelpCategory : {}", helpCategoryDTO);
        HelpCategory helpCategory = helpCategoryMapper.toEntity(helpCategoryDTO);
        helpCategory = helpCategoryRepository.save(helpCategory);
        return helpCategoryMapper.toDto(helpCategory);
    }

    @Override
    public Optional<HelpCategoryDTO> partialUpdate(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to partially update HelpCategory : {}", helpCategoryDTO);

        return helpCategoryRepository
            .findById(helpCategoryDTO.getId())
            .map(existingHelpCategory -> {
                helpCategoryMapper.partialUpdate(existingHelpCategory, helpCategoryDTO);

                return existingHelpCategory;
            })
            .map(helpCategoryRepository::save)
            .map(helpCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpCategories");
        return helpCategoryRepository.findAll(pageable).map(helpCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpCategoryDTO> findOne(Long id) {
        log.debug("Request to get HelpCategory : {}", id);
        return helpCategoryRepository.findById(id).map(helpCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HelpCategory : {}", id);
        helpCategoryRepository.deleteById(id);
    }
}
