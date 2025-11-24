package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SingleDocument;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.SingleDocumentDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleDocument} and its DTO {@link SingleDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleDocumentMapper extends EntityMapper<SingleDocumentDTO, SingleDocument> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    SingleDocumentDTO toDto(SingleDocument s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
