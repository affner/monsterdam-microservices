package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.EmojiType;
import com.monsterdam.admin.service.dto.EmojiTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmojiType} and its DTO {@link EmojiTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmojiTypeMapper extends EntityMapper<EmojiTypeDTO, EmojiType> {}
