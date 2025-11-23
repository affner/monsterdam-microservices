package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.domain.SingleVideo;
import com.fanflip.multimedia.service.dto.ContentPackageDTO;
import com.fanflip.multimedia.service.dto.SingleVideoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleVideo} and its DTO {@link SingleVideoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(SingleVideoMapperDecorator.class)
public interface SingleVideoMapper extends EntityMapper<SingleVideoDTO, SingleVideo> {
    @Mapping(target = "belongPackage", source = "belongPackage", qualifiedByName = "contentPackageAmount")
    SingleVideoDTO toDto(SingleVideo s);

    @Named("contentPackageAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    ContentPackageDTO toDtoContentPackageAmount(ContentPackage contentPackage);
}
