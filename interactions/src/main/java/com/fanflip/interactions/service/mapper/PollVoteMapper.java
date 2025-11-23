package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.PollOption;
import com.fanflip.interactions.domain.PollVote;
import com.fanflip.interactions.service.dto.PollOptionDTO;
import com.fanflip.interactions.service.dto.PollVoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollVote} and its DTO {@link PollVoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollVoteMapper extends EntityMapper<PollVoteDTO, PollVote> {
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "pollOptionOptionDescription")
    PollVoteDTO toDto(PollVote s);

    @Named("pollOptionOptionDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionDescription", source = "optionDescription")
    PollOptionDTO toDtoPollOptionOptionDescription(PollOption pollOption);
}
