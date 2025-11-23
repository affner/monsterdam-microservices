package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PollOption;
import com.fanflip.admin.domain.PollVote;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.PollOptionDTO;
import com.fanflip.admin.service.dto.PollVoteDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollVote} and its DTO {@link PollVoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollVoteMapper extends EntityMapper<PollVoteDTO, PollVote> {
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "pollOptionOptionDescription")
    @Mapping(target = "votingUser", source = "votingUser", qualifiedByName = "userProfileId")
    PollVoteDTO toDto(PollVote s);

    @Named("pollOptionOptionDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionDescription", source = "optionDescription")
    PollOptionDTO toDtoPollOptionOptionDescription(PollOption pollOption);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
