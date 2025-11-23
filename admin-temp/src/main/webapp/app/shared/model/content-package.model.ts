import dayjs from 'dayjs';
import { ISingleAudio } from 'app/shared/model/single-audio.model';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { ISingleVideo } from 'app/shared/model/single-video.model';
import { ISinglePhoto } from 'app/shared/model/single-photo.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';

export interface IContentPackage {
  id?: number;
  amount?: number | null;
  videoCount?: number | null;
  imageCount?: number | null;
  isPaidContent?: boolean;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  audio?: ISingleAudio | null;
  selledPackages?: IPurchasedContent[] | null;
  videos?: ISingleVideo[] | null;
  photos?: ISinglePhoto[] | null;
  usersTaggeds?: IUserProfile[] | null;
  message?: IDirectMessage;
  post?: IPostFeed;
}

export const defaultValue: Readonly<IContentPackage> = {
  isPaidContent: false,
  isDeleted: false,
};
