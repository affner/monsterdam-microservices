import dayjs from 'dayjs';

export interface IEmojiType {
  id?: number;
  thumbnailContentType?: string;
  thumbnail?: string;
  description?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IEmojiType> = {
  isDeleted: false,
};
