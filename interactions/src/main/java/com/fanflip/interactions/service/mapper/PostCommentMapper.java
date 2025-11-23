package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.PostComment;
import com.fanflip.interactions.domain.PostFeed;
import com.fanflip.interactions.service.dto.PostCommentDTO;
import com.fanflip.interactions.service.dto.PostFeedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostComment} and its DTO {@link PostCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostCommentMapper extends EntityMapper<PostCommentDTO, PostComment> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "postCommentCommentContent")
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
}
