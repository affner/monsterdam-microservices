package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PollOption;
import com.fanflip.admin.domain.PostPoll;
import com.fanflip.admin.service.dto.PollOptionDTO;
import com.fanflip.admin.service.dto.PostPollDTO;
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
