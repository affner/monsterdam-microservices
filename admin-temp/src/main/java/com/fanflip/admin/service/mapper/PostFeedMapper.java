package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.ContentPackage;
import com.fanflip.admin.domain.HashTag;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.PostPoll;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.ContentPackageDTO;
import com.fanflip.admin.service.dto.HashTagDTO;
import com.fanflip.admin.service.dto.PostFeedDTO;
import com.fanflip.admin.service.dto.PostPollDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFeed} and its DTO {@link PostFeedDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostFeedMapper extends EntityMapper<PostFeedDTO, PostFeed> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "postPollQuestion")
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "hashTags", source = "hashTags", qualifiedByName = "hashTagIdSet")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    PostFeedDTO toDto(PostFeed s);

    @Mapping(target = "removeHashTags", ignore = true)
    PostFeed toEntity(PostFeedDTO postFeedDTO);

    @Named("postPollQuestion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "question", source = "question")
    PostPollDTO toDtoPostPollQuestion(PostPoll postPoll);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("hashTagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HashTagDTO toDtoHashTagId(HashTag hashTag);

    @Named("hashTagIdSet")
    default Set<HashTagDTO> toDtoHashTagIdSet(Set<HashTag> hashTag) {
        return hashTag.stream().map(this::toDtoHashTagId).collect(Collectors.toSet());
    }

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
