package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SocialNetwork;
import com.monsterdam.admin.service.dto.SocialNetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialNetwork} and its DTO {@link SocialNetworkDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialNetworkMapper extends EntityMapper<SocialNetworkDTO, SocialNetwork> {}
