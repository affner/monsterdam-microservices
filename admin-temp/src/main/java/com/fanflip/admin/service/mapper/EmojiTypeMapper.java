package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.EmojiType;
import com.fanflip.admin.service.dto.EmojiTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmojiType} and its DTO {@link EmojiTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmojiTypeMapper extends EntityMapper<EmojiTypeDTO, EmojiType> {}
