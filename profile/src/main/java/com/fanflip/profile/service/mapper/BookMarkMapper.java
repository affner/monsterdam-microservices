package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.BookMark;
import com.fanflip.profile.service.dto.BookMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookMark} and its DTO {@link BookMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMarkMapper extends EntityMapper<BookMarkDTO, BookMark> {}
