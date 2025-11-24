package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.BookMark;
import com.monsterdam.admin.domain.DirectMessage;
import com.monsterdam.admin.domain.PostFeed;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.BookMarkDTO;
import com.monsterdam.admin.service.dto.DirectMessageDTO;
import com.monsterdam.admin.service.dto.PostFeedDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookMark} and its DTO {@link BookMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMarkMapper extends EntityMapper<BookMarkDTO, BookMark> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "message", source = "message", qualifiedByName = "directMessageMessageContent")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    BookMarkDTO toDto(BookMark s);

    @Named("postFeedPostContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postContent", source = "postContent")
    PostFeedDTO toDtoPostFeedPostContent(PostFeed postFeed);

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
