package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PostComment;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.UserMention;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.PostCommentDTO;
import com.fanflip.admin.service.dto.PostFeedDTO;
import com.fanflip.admin.service.dto.UserMentionDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
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
