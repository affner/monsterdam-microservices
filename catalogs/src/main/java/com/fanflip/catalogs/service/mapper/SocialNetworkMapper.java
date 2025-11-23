package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.SocialNetwork;
import com.fanflip.catalogs.service.dto.SocialNetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialNetwork} and its DTO {@link SocialNetworkDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialNetworkMapper extends EntityMapper<SocialNetworkDTO, SocialNetwork> {}
