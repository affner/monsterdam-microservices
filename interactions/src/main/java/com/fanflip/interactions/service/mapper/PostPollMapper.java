package com.monsterdam.interactions.service.mapper;

import com.monsterdam.interactions.domain.PostPoll;
import com.monsterdam.interactions.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostPoll} and its DTO {@link PostPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostPollMapper extends EntityMapper<PostPollDTO, PostPoll> {}
