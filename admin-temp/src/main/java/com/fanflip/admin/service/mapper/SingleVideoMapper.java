package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.ContentPackage;
import com.fanflip.admin.domain.SingleVideo;
import com.fanflip.admin.service.dto.ContentPackageDTO;
import com.fanflip.admin.service.dto.SingleVideoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleVideo} and its DTO {@link SingleVideoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleVideoMapper extends EntityMapper<SingleVideoDTO, SingleVideo> {
    @Mapping(target = "belongPackage", source = "belongPackage", qualifiedByName = "contentPackageAmount")
    SingleVideoDTO toDto(SingleVideo s);

    @Named("contentPackageAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    ContentPackageDTO toDtoContentPackageAmount(ContentPackage contentPackage);
}
