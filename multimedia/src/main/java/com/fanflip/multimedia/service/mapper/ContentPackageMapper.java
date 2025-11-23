package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.domain.SingleAudio;
import com.fanflip.multimedia.service.dto.ContentPackageDTO;
import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentPackage} and its DTO {@link ContentPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentPackageMapper extends EntityMapper<ContentPackageDTO, ContentPackage> {
    @Mapping(target = "audio", source = "audio", qualifiedByName = "singleAudioId")
    ContentPackageDTO toDto(ContentPackage s);

    @Named("singleAudioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SingleAudioDTO toDtoSingleAudioId(SingleAudio singleAudio);
}
