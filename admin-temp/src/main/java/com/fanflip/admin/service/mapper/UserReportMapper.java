package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.DirectMessage;
import com.fanflip.admin.domain.PostComment;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.SingleAudio;
import com.fanflip.admin.domain.SingleLiveStream;
import com.fanflip.admin.domain.SinglePhoto;
import com.fanflip.admin.domain.SingleVideo;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserReport;
import com.fanflip.admin.domain.VideoStory;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.service.dto.DirectMessageDTO;
import com.fanflip.admin.service.dto.PostCommentDTO;
import com.fanflip.admin.service.dto.PostFeedDTO;
import com.fanflip.admin.service.dto.SingleAudioDTO;
import com.fanflip.admin.service.dto.SingleLiveStreamDTO;
import com.fanflip.admin.service.dto.SinglePhotoDTO;
import com.fanflip.admin.service.dto.SingleVideoDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import com.fanflip.admin.service.dto.UserReportDTO;
import com.fanflip.admin.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserReport} and its DTO {@link UserReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserReportMapper extends EntityMapper<UserReportDTO, UserReport> {
    @Mapping(target = "ticket", source = "ticket", qualifiedByName = "assistanceTicketId")
    @Mapping(target = "reporter", source = "reporter", qualifiedByName = "userProfileId")
    @Mapping(target = "reported", source = "reported", qualifiedByName = "userProfileId")
    @Mapping(target = "story", source = "story", qualifiedByName = "videoStoryDuration")
    @Mapping(target = "video", source = "video", qualifiedByName = "singleVideoDuration")
    @Mapping(target = "photo", source = "photo", qualifiedByName = "singlePhotoThumbnail")
    @Mapping(target = "audio", source = "audio", qualifiedByName = "singleAudioDuration")
    @Mapping(target = "liveStream", source = "liveStream", qualifiedByName = "singleLiveStreamThumbnail")
    @Mapping(target = "message", source = "message", qualifiedByName = "directMessageMessageContent")
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedPostContent")
    @Mapping(target = "postComment", source = "postComment", qualifiedByName = "postCommentCommentContent")
    UserReportDTO toDto(UserReport s);

    @Named("assistanceTicketId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssistanceTicketDTO toDtoAssistanceTicketId(AssistanceTicket assistanceTicket);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("videoStoryDuration")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "duration", source = "duration")
    VideoStoryDTO toDtoVideoStoryDuration(VideoStory videoStory);

    @Named("singleVideoDuration")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "duration", source = "duration")
    SingleVideoDTO toDtoSingleVideoDuration(SingleVideo singleVideo);

    @Named("singlePhotoThumbnail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "thumbnail", source = "thumbnail")
    SinglePhotoDTO toDtoSinglePhotoThumbnail(SinglePhoto singlePhoto);

    @Named("singleAudioDuration")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "duration", source = "duration")
    SingleAudioDTO toDtoSingleAudioDuration(SingleAudio singleAudio);

    @Named("singleLiveStreamThumbnail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "thumbnail", source = "thumbnail")
    SingleLiveStreamDTO toDtoSingleLiveStreamThumbnail(SingleLiveStream singleLiveStream);

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);

    @Named("postFeedPostContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postContent", source = "postContent")
    PostFeedDTO toDtoPostFeedPostContent(PostFeed postFeed);

    @Named("postCommentCommentContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentContent", source = "commentContent")
    PostCommentDTO toDtoPostCommentCommentContent(PostComment postComment);
}
