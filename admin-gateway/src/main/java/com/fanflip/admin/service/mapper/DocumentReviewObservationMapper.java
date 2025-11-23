package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.DocumentReviewObservation;
import com.fanflip.admin.domain.IdentityDocumentReview;
import com.fanflip.admin.service.dto.DocumentReviewObservationDTO;
import com.fanflip.admin.service.dto.IdentityDocumentReviewDTO;
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
