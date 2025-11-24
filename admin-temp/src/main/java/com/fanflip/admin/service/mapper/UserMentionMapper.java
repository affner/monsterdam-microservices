package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PostComment;
import com.monsterdam.admin.domain.PostFeed;
import com.monsterdam.admin.domain.UserMention;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.PostCommentDTO;
import com.monsterdam.admin.service.dto.PostFeedDTO;
import com.monsterdam.admin.service.dto.UserMentionDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMention} and its DTO {@link UserMentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMentionMapper extends EntityMapper<UserMentionDTO, UserMention> {
    @Mapping(target = "originPost", source = "originPost", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "originPostComment", source = "originPostComment", qualifiedByName = "postCommentCommentContent")
    @Mapping(target = "mentionedUser", source = "mentionedUser", qualifiedByName = "userProfileId")
    UserMentionDTO toDto(UserMention s);

    @Named("postFeedPostContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postContent", source = "postContent")
    PostFeedDTO toDtoPostFeedPostContent(PostFeed postFeed);

    @Named("postCommentCommentContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentContent", source = "commentContent")
    PostCommentDTO toDtoPostCommentCommentContent(PostComment postComment);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
