package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SocialNetwork;
import com.fanflip.admin.service.dto.SocialNetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialNetwork} and its DTO {@link SocialNetworkDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialNetworkMapper extends EntityMapper<SocialNetworkDTO, SocialNetwork> {}
