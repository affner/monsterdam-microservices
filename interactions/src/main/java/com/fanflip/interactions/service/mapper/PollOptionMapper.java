package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.PollOption;
import com.fanflip.interactions.domain.PostPoll;
import com.fanflip.interactions.service.dto.PollOptionDTO;
import com.fanflip.interactions.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollOption} and its DTO {@link PollOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollOptionMapper extends EntityMapper<PollOptionDTO, PollOption> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "postPollQuestion")
    PollOptionDTO toDto(PollOption s);

    @Named("postPollQuestion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "question", source = "question")
    PostPollDTO toDtoPostPollQuestion(PostPoll postPoll);
}
