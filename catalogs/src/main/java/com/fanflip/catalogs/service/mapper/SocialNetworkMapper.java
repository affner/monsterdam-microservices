package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.SocialNetwork;
import com.monsterdam.catalogs.service.dto.SocialNetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialNetwork} and its DTO {@link SocialNetworkDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialNetworkMapper extends EntityMapper<SocialNetworkDTO, SocialNetwork> {}
