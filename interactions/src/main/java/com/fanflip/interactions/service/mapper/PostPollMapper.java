package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.PostPoll;
import com.fanflip.interactions.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostPoll} and its DTO {@link PostPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostPollMapper extends EntityMapper<PostPollDTO, PostPoll> {}
