package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PostPoll;
import com.fanflip.admin.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostPoll} and its DTO {@link PostPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostPollMapper extends EntityMapper<PostPollDTO, PostPoll> {}
