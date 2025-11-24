package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PollOption;
import com.monsterdam.admin.domain.PollVote;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.PollOptionDTO;
import com.monsterdam.admin.service.dto.PollVoteDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
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
