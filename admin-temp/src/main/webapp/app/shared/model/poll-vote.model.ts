import dayjs from 'dayjs';
import { IPollOption } from 'app/shared/model/poll-option.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IPollVote {
  id?: number;
  createdDate?: dayjs.Dayjs;
  pollOption?: IPollOption;
  votingUser?: IUserProfile | null;
}

export const defaultValue: Readonly<IPollVote> = {};
