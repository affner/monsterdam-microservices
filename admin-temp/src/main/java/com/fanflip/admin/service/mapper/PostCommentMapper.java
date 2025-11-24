package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PostComment;
import com.monsterdam.admin.domain.PostFeed;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.PostCommentDTO;
import com.monsterdam.admin.service.dto.PostFeedDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostComment} and its DTO {@link PostCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostCommentMapper extends EntityMapper<PostCommentDTO, PostComment> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "postCommentCommentContent")
    @Mapping(target = "commenter", source = "commenter", qualifiedByName = "userProfileId")
    PostCommentDTO toDto(PostComment s);

    @Named("postCommentCommentContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentContent", source = "commentContent")
    PostCommentDTO toDtoPostCommentCommentContent(PostComment postComment);

    @Named("postFeedPostContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postContent", source = "postContent")
    PostFeedDTO toDtoPostFeedPostContent(PostFeed postFeed);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
