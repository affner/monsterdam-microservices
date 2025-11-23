package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SingleDocument;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.SingleDocumentDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
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
