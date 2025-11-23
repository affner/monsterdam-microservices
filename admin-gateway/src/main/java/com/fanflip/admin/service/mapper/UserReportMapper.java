package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.UserReport;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.service.dto.UserReportDTO;
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
