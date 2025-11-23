package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.IdentityDocument;
import com.fanflip.admin.domain.IdentityDocumentReview;
import com.fanflip.admin.service.dto.IdentityDocumentDTO;
import com.fanflip.admin.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdentityDocument} and its DTO {@link IdentityDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdentityDocumentMapper extends EntityMapper<IdentityDocumentDTO, IdentityDocument> {
    @Mapping(target = "review", source = "review", qualifiedByName = "identityDocumentReviewId")
    IdentityDocumentDTO toDto(IdentityDocument s);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);
}
