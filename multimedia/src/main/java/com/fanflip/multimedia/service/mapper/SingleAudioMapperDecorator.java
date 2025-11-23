package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.SingleAudio;
import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import com.fanflip.multimedia.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class SingleAudioMapperDecorator implements SingleAudioMapper {

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private SingleAudioMapper delegate;



    @Override
    public SingleAudio toEntity(SingleAudioDTO singleAudioDTO) {
        SingleAudio entity = delegate.toEntity(singleAudioDTO);
        if (Objects.nonNull(singleAudioDTO.getContent())) {
            entity.setContentS3Key(s3Utils.saveFile(singleAudioDTO.getContent()));
        }
        if (Objects.nonNull(singleAudioDTO.getThumbnail())) {
            entity.setThumbnailS3Key(s3Utils.saveFile(singleAudioDTO.getThumbnail()));
        }

        return entity;
    }
}
