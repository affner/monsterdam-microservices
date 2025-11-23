package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.UserAssociation;
import com.fanflip.profile.service.dto.UserAssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssociation} and its DTO {@link UserAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssociationMapper extends EntityMapper<UserAssociationDTO, UserAssociation> {}
