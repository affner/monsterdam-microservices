package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.domain.IdentityDocumentReview;
import com.monsterdam.admin.service.dto.AssistanceTicketDTO;
import com.monsterdam.admin.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdentityDocumentReview} and its DTO {@link IdentityDocumentReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdentityDocumentReviewMapper extends EntityMapper<IdentityDocumentReviewDTO, IdentityDocumentReview> {
    @Mapping(target = "ticket", source = "ticket", qualifiedByName = "assistanceTicketId")
    IdentityDocumentReviewDTO toDto(IdentityDocumentReview s);

    @Named("assistanceTicketId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssistanceTicketDTO toDtoAssistanceTicketId(AssistanceTicket assistanceTicket);
}
