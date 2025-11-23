import dayjs from 'dayjs';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IChatRoom {
  id?: number;
  lastAction?: string | null;
  lastConnectionDate?: dayjs.Dayjs | null;
  muted?: boolean | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  sentMessages?: IDirectMessage[] | null;
  user?: IUserProfile;
}

export const defaultValue: Readonly<IChatRoom> = {
  muted: false,
  isDeleted: false,
};
