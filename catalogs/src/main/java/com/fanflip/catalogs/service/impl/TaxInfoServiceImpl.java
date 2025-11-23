package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.TaxInfo;
import com.fanflip.catalogs.repository.TaxInfoRepository;
import com.fanflip.catalogs.service.TaxInfoService;
import com.fanflip.catalogs.service.dto.TaxInfoDTO;
import com.fanflip.catalogs.service.mapper.TaxInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.TaxInfo}.
 */
@Service
@Transactional
public class TaxInfoServiceImpl implements TaxInfoService {

    private final Logger log = LoggerFactory.getLogger(TaxInfoServiceImpl.class);

    private final TaxInfoRepository taxInfoRepository;

    private final TaxInfoMapper taxInfoMapper;

    public TaxInfoServiceImpl(TaxInfoRepository taxInfoRepository, TaxInfoMapper taxInfoMapper) {
        this.taxInfoRepository = taxInfoRepository;
        this.taxInfoMapper = taxInfoMapper;
    }

    @Override
    public TaxInfoDTO save(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to save TaxInfo : {}", taxInfoDTO);
        TaxInfo taxInfo = taxInfoMapper.toEntity(taxInfoDTO);
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDto(taxInfo);
    }

    @Override
    public TaxInfoDTO update(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to update TaxInfo : {}", taxInfoDTO);
        TaxInfo taxInfo = taxInfoMapper.toEntity(taxInfoDTO);
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDto(taxInfo);
    }

    @Override
    public Optional<TaxInfoDTO> partialUpdate(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to partially update TaxInfo : {}", taxInfoDTO);

        return taxInfoRepository
            .findById(taxInfoDTO.getId())
            .map(existingTaxInfo -> {
                taxInfoMapper.partialUpdate(existingTaxInfo, taxInfoDTO);

                return existingTaxInfo;
            })
            .map(taxInfoRepository::save)
            .map(taxInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxInfos");
        return taxInfoRepository.findAll(pageable).map(taxInfoMapper::toDto);
    }

    public Page<TaxInfoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taxInfoRepository.findAllWithEagerRelationships(pageable).map(taxInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxInfoDTO> findOne(Long id) {
        log.debug("Request to get TaxInfo : {}", id);
        return taxInfoRepository.findOneWithEagerRelationships(id).map(taxInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxInfo : {}", id);
        taxInfoRepository.deleteById(id);
    }
}
