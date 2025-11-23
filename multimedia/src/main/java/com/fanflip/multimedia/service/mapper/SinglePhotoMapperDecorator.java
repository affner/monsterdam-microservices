package com.fanflip.multimedia.service.mapper;


import com.fanflip.multimedia.domain.SinglePhoto;
import com.fanflip.multimedia.service.dto.SinglePhotoDTO;
import com.fanflip.multimedia.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class SinglePhotoMapperDecorator implements SinglePhotoMapper {

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private SinglePhotoMapper delegate;

//    @Override
//    public SinglePhotoDTO toDto(SinglePhoto singlePhoto) {
//        SinglePhotoDTO dto = delegate.toDto(singlePhoto);
//        if (Objects.nonNull(singlePhoto.getContentS3Key())) {
//            dto.setContent(s3Utils.getFile(singlePhoto.getContentS3Key()));
//        }
//        if (Objects.nonNull(singlePhoto.getThumbnailS3Key())) {
//            dto.setThumbnail(s3Utils.getFile(singlePhoto.getThumbnailS3Key()));
//        }
//        return dto;
//    }

    @Override
    public SinglePhoto toEntity(SinglePhotoDTO singlePhotoDTO) {
        SinglePhoto entity = delegate.toEntity(singlePhotoDTO);
        if (Objects.nonNull(singlePhotoDTO.getContent())) {
            entity.setContentS3Key(s3Utils.saveFile(singlePhotoDTO.getContent()));
        }
        if (Objects.nonNull(singlePhotoDTO.getThumbnail())) {
            entity.setThumbnailS3Key(s3Utils.saveFile(singlePhotoDTO.getThumbnail()));
        }
        return entity;
    }
}
