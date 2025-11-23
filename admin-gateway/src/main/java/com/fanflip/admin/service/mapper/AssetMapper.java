package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Asset;
import com.fanflip.admin.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {}
