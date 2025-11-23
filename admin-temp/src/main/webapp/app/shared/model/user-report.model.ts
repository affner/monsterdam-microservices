import dayjs from 'dayjs';
import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IVideoStory } from 'app/shared/model/video-story.model';
import { ISingleVideo } from 'app/shared/model/single-video.model';
import { ISinglePhoto } from 'app/shared/model/single-photo.model';
import { ISingleAudio } from 'app/shared/model/single-audio.model';
import { ISingleLiveStream } from 'app/shared/model/single-live-stream.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { ReportCategory } from 'app/shared/model/enumerations/report-category.model';

export interface IUserReport {
  id?: number;
  reportDescription?: string | null;
  status?: keyof typeof ReportStatus;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reportCategory?: keyof typeof ReportCategory;
  ticket?: IAssistanceTicket;
  reporter?: IUserProfile;
  reported?: IUserProfile;
  story?: IVideoStory | null;
  video?: ISingleVideo | null;
  photo?: ISinglePhoto | null;
  audio?: ISingleAudio | null;
  liveStream?: ISingleLiveStream | null;
  message?: IDirectMessage | null;
  post?: IPostFeed | null;
  postComment?: IPostComment | null;
}

export const defaultValue: Readonly<IUserReport> = {
  isDeleted: false,
};
