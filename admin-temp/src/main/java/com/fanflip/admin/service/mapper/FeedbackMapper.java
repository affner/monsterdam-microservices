package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Feedback;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.FeedbackDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    FeedbackDTO toDto(Feedback s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
