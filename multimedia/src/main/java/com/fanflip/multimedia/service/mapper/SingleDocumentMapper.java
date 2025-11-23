package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.SingleDocument;
import com.fanflip.multimedia.service.dto.SingleDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleDocument} and its DTO {@link SingleDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleDocumentMapper extends EntityMapper<SingleDocumentDTO, SingleDocument> {}
