package com.monsterdam.multimedia.service.mapper;

import com.monsterdam.multimedia.domain.SingleDocument;
import com.monsterdam.multimedia.service.dto.SingleDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleDocument} and its DTO {@link SingleDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleDocumentMapper extends EntityMapper<SingleDocumentDTO, SingleDocument> {}
