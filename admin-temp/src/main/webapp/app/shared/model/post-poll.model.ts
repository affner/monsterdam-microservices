import dayjs from 'dayjs';
import { IPollOption } from 'app/shared/model/poll-option.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';

export interface IPostPoll {
  id?: number;
  question?: string;
  isMultiChoice?: boolean;
  lastModifiedDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs;
  postPollDuration?: string;
  createdDate?: dayjs.Dayjs;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  options?: IPollOption[] | null;
  post?: IPostFeed;
}

export const defaultValue: Readonly<IPostPoll> = {
  isMultiChoice: false,
  isDeleted: false,
};
