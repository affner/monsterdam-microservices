package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.domain.SpecialReward;
import com.fanflip.multimedia.service.dto.ContentPackageDTO;
import com.fanflip.multimedia.service.dto.SpecialRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialReward} and its DTO {@link SpecialRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialRewardMapper extends EntityMapper<SpecialRewardDTO, SpecialReward> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    SpecialRewardDTO toDto(SpecialReward s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);
}
