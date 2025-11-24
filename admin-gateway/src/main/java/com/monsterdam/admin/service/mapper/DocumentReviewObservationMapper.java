package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.DocumentReviewObservation;
import com.monsterdam.admin.domain.IdentityDocumentReview;
import com.monsterdam.admin.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.admin.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentReviewObservation} and its DTO {@link DocumentReviewObservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentReviewObservationMapper extends EntityMapper<DocumentReviewObservationDTO, DocumentReviewObservation> {
    @Mapping(target = "review", source = "review", qualifiedByName = "identityDocumentReviewId")
    DocumentReviewObservationDTO toDto(DocumentReviewObservation s);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);
}
