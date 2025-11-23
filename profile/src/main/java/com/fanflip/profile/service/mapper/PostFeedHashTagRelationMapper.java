package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.HashTag;
import com.fanflip.profile.domain.PostFeedHashTagRelation;
import com.fanflip.profile.service.dto.HashTagDTO;
import com.fanflip.profile.service.dto.PostFeedHashTagRelationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFeedHashTagRelation} and its DTO {@link PostFeedHashTagRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostFeedHashTagRelationMapper extends EntityMapper<PostFeedHashTagRelationDTO, PostFeedHashTagRelation> {
    @Mapping(target = "hashtag", source = "hashtag", qualifiedByName = "hashTagId")
    PostFeedHashTagRelationDTO toDto(PostFeedHashTagRelation s);

    @Named("hashTagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HashTagDTO toDtoHashTagId(HashTag hashTag);
}
