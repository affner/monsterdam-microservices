package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Asset;
import com.monsterdam.admin.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {}
