package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.domain.UserReport;
import com.monsterdam.admin.service.dto.AssistanceTicketDTO;
import com.monsterdam.admin.service.dto.UserReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserReport} and its DTO {@link UserReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserReportMapper extends EntityMapper<UserReportDTO, UserReport> {
    @Mapping(target = "ticket", source = "ticket", qualifiedByName = "assistanceTicketId")
    UserReportDTO toDto(UserReport s);

    @Named("assistanceTicketId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssistanceTicketDTO toDtoAssistanceTicketId(AssistanceTicket assistanceTicket);
}
