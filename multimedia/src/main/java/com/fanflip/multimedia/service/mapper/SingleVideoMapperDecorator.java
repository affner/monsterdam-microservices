package com.fanflip.multimedia.service.mapper;


import com.fanflip.multimedia.domain.SingleVideo;
import com.fanflip.multimedia.service.dto.SingleVideoDTO;
import com.fanflip.multimedia.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class SingleVideoMapperDecorator implements SingleVideoMapper {

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private SingleVideoMapper delegate;

//    @Override
//    public SingleVideoDTO toDto(SingleVideo singleVideo) {
//        SingleVideoDTO dto = delegate.toDto(singleVideo);
//        if (Objects.nonNull(singleVideo.getContentS3Key())) {
//            dto.setContent(s3Utils.getFile(singleVideo.getContentS3Key()));
//        }
//        if (Objects.nonNull(singleVideo.getThumbnailS3Key())) {
//            dto.setThumbnail(s3Utils.getFile(singleVideo.getThumbnailS3Key()));
//        }
//        return dto;
//    }

    @Override
    public SingleVideo toEntity(SingleVideoDTO singleVideoDTO) {
        SingleVideo entity = delegate.toEntity(singleVideoDTO);
        if (Objects.nonNull(singleVideoDTO.getContent())) {
            entity.setContentS3Key(s3Utils.saveFile(singleVideoDTO.getContent()));
        }
        if (Objects.nonNull(singleVideoDTO.getThumbnail())) {
            entity.setThumbnailS3Key(s3Utils.saveFile(singleVideoDTO.getThumbnail()));
        }
        return entity;
    }
}
