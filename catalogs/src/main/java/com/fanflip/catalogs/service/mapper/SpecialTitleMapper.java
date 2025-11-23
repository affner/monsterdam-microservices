package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.SpecialTitle;
import com.fanflip.catalogs.service.dto.SpecialTitleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialTitle} and its DTO {@link SpecialTitleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialTitleMapper extends EntityMapper<SpecialTitleDTO, SpecialTitle> {}
