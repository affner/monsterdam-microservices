package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.HelpRelatedArticle;
import com.fanflip.catalogs.repository.HelpRelatedArticleRepository;
import com.fanflip.catalogs.service.HelpRelatedArticleService;
import com.fanflip.catalogs.service.dto.HelpRelatedArticleDTO;
import com.fanflip.catalogs.service.mapper.HelpRelatedArticleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.HelpRelatedArticle}.
 */
@Service
@Transactional
public class HelpRelatedArticleServiceImpl implements HelpRelatedArticleService {

    private final Logger log = LoggerFactory.getLogger(HelpRelatedArticleServiceImpl.class);

    private final HelpRelatedArticleRepository helpRelatedArticleRepository;

    private final HelpRelatedArticleMapper helpRelatedArticleMapper;

    public HelpRelatedArticleServiceImpl(
        HelpRelatedArticleRepository helpRelatedArticleRepository,
        HelpRelatedArticleMapper helpRelatedArticleMapper
    ) {
        this.helpRelatedArticleRepository = helpRelatedArticleRepository;
        this.helpRelatedArticleMapper = helpRelatedArticleMapper;
    }

    @Override
    public HelpRelatedArticleDTO save(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to save HelpRelatedArticle : {}", helpRelatedArticleDTO);
        HelpRelatedArticle helpRelatedArticle = helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO);
        helpRelatedArticle = helpRelatedArticleRepository.save(helpRelatedArticle);
        return helpRelatedArticleMapper.toDto(helpRelatedArticle);
    }

    @Override
    public HelpRelatedArticleDTO update(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to update HelpRelatedArticle : {}", helpRelatedArticleDTO);
        HelpRelatedArticle helpRelatedArticle = helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO);
        helpRelatedArticle = helpRelatedArticleRepository.save(helpRelatedArticle);
        return helpRelatedArticleMapper.toDto(helpRelatedArticle);
    }

    @Override
    public Optional<HelpRelatedArticleDTO> partialUpdate(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to partially update HelpRelatedArticle : {}", helpRelatedArticleDTO);

        return helpRelatedArticleRepository
            .findById(helpRelatedArticleDTO.getId())
            .map(existingHelpRelatedArticle -> {
                helpRelatedArticleMapper.partialUpdate(existingHelpRelatedArticle, helpRelatedArticleDTO);

                return existingHelpRelatedArticle;
            })
            .map(helpRelatedArticleRepository::save)
            .map(helpRelatedArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpRelatedArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpRelatedArticles");
        return helpRelatedArticleRepository.findAll(pageable).map(helpRelatedArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpRelatedArticleDTO> findOne(Long id) {
        log.debug("Request to get HelpRelatedArticle : {}", id);
        return helpRelatedArticleRepository.findById(id).map(helpRelatedArticleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HelpRelatedArticle : {}", id);
        helpRelatedArticleRepository.deleteById(id);
    }
}
