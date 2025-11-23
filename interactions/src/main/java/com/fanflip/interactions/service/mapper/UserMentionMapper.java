package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.PostComment;
import com.fanflip.interactions.domain.PostFeed;
import com.fanflip.interactions.domain.UserMention;
import com.fanflip.interactions.service.dto.PostCommentDTO;
import com.fanflip.interactions.service.dto.PostFeedDTO;
import com.fanflip.interactions.service.dto.UserMentionDTO;
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
