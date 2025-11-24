package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.Feedback;
import com.monsterdam.profile.service.dto.FeedbackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {}
