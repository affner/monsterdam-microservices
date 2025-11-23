package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.EmojiType;
import com.fanflip.catalogs.service.dto.EmojiTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmojiType} and its DTO {@link EmojiTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmojiTypeMapper extends EntityMapper<EmojiTypeDTO, EmojiType> {}
