package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.UserAssociation;
import com.monsterdam.profile.service.dto.UserAssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssociation} and its DTO {@link UserAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssociationMapper extends EntityMapper<UserAssociationDTO, UserAssociation> {}
