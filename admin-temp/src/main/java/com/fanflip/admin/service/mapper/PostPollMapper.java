package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PostPoll;
import com.monsterdam.admin.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostPoll} and its DTO {@link PostPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostPollMapper extends EntityMapper<PostPollDTO, PostPoll> {}
