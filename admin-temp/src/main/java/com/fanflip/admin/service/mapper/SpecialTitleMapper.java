package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SpecialTitle;
import com.fanflip.admin.service.dto.SpecialTitleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialTitle} and its DTO {@link SpecialTitleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialTitleMapper extends EntityMapper<SpecialTitleDTO, SpecialTitle> {}
