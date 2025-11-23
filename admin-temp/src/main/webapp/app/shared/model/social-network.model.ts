import dayjs from 'dayjs';

export interface ISocialNetwork {
  id?: number;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  name?: string;
  completeName?: string;
  mainLink?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<ISocialNetwork> = {
  isDeleted: false,
};
