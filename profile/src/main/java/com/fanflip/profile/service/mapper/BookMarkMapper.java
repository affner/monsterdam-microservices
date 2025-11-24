package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.BookMark;
import com.monsterdam.profile.service.dto.BookMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookMark} and its DTO {@link BookMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMarkMapper extends EntityMapper<BookMarkDTO, BookMark> {}
