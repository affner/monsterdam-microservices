package com.monsterdam.interactions.service.mapper;

import com.monsterdam.interactions.domain.PostComment;
import com.monsterdam.interactions.domain.PostFeed;
import com.monsterdam.interactions.domain.UserMention;
import com.monsterdam.interactions.service.dto.PostCommentDTO;
import com.monsterdam.interactions.service.dto.PostFeedDTO;
import com.monsterdam.interactions.service.dto.UserMentionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMention} and its DTO {@link UserMentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMentionMapper extends EntityMapper<UserMentionDTO, UserMention> {
    @Mapping(target = "originPost", source = "originPost", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "originPostComment", source = "originPostComment", qualifiedByName = "postCommentCommentContent")
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
}
