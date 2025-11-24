package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Feedback;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.FeedbackDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
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
