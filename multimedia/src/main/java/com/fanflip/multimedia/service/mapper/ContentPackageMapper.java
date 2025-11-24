package com.monsterdam.multimedia.service.mapper;

import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.domain.SingleAudio;
import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
import com.monsterdam.multimedia.service.dto.SingleAudioDTO;
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
