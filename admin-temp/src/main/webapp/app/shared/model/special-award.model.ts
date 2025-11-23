import dayjs from 'dayjs';

export interface ISpecialAward {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  reason?: string | null;
  altSpecialTitle?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  viewerId?: number;
  creatorId?: number;
  specialTitleId?: number;
}

export const defaultValue: Readonly<ISpecialAward> = {
  isDeleted: false,
};
