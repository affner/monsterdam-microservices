package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.EmojiType;
import com.monsterdam.catalogs.service.dto.EmojiTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmojiType} and its DTO {@link EmojiTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmojiTypeMapper extends EntityMapper<EmojiTypeDTO, EmojiType> {}
