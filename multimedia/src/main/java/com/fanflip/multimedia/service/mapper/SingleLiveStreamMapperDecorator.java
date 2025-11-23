package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.SingleLiveStream;
import com.fanflip.multimedia.service.dto.SingleLiveStreamDTO;
import com.fanflip.multimedia.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;



public abstract class SingleLiveStreamMapperDecorator implements SingleLiveStreamMapper {

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private SingleLiveStreamMapper delegate;

//    @Override
//    public SingleLiveStreamDTO toDto(SingleLiveStream singleLiveStream) {
//        SingleLiveStreamDTO dto = delegate.toDto(singleLiveStream);
//        if (Objects.nonNull(singleLiveStream.getContentS3Key())) {
//            dto.setContent(s3Utils.getFile(singleLiveStream.getContentS3Key()));
//        }
//        if (Objects.nonNull(singleLiveStream.getThumbnailS3Key())) {
//            dto.setThumbnail(s3Utils.getFile(singleLiveStream.getThumbnailS3Key()));
//        }
//        return dto;
//    }

    @Override
    public SingleLiveStream toEntity(SingleLiveStreamDTO singleLiveStreamDTO) {
        SingleLiveStream entity = delegate.toEntity(singleLiveStreamDTO);
        if (Objects.nonNull(singleLiveStreamDTO.getContent())) {
            entity.setContentS3Key(s3Utils.saveFile(singleLiveStreamDTO.getContent()));
        }
        if (Objects.nonNull(singleLiveStreamDTO.getThumbnail())) {
            entity.setThumbnailS3Key(s3Utils.saveFile(singleLiveStreamDTO.getThumbnail()));
        }

        return entity;
    }
}
