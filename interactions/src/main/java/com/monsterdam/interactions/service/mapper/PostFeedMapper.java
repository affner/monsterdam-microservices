package com.monsterdam.interactions.service.mapper;

import com.monsterdam.interactions.domain.PostFeed;
import com.monsterdam.interactions.domain.PostPoll;
import com.monsterdam.interactions.service.dto.PostFeedDTO;
import com.monsterdam.interactions.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFeed} and its DTO {@link PostFeedDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostFeedMapper extends EntityMapper<PostFeedDTO, PostFeed> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "postPollQuestion")
    PostFeedDTO toDto(PostFeed s);

    @Named("postPollQuestion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "question", source = "question")
    PostPollDTO toDtoPostPollQuestion(PostPoll postPoll);
}
