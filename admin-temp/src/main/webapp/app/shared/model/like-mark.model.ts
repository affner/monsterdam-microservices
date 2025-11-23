import dayjs from 'dayjs';

export interface ILikeMark {
  id?: number;
  emojiTypeId?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  multimediaId?: number | null;
  messageId?: number | null;
  postId?: number | null;
  commentId?: number | null;
  likerUserId?: number | null;
}

export const defaultValue: Readonly<ILikeMark> = {
  isDeleted: false,
};
