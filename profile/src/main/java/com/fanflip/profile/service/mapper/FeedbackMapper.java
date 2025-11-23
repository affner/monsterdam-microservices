package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.Feedback;
import com.fanflip.profile.service.dto.FeedbackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {}
