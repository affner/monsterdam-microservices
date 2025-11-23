import dayjs from 'dayjs';

export interface ISpecialReward {
  id?: number;
  description?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  contentPackageId?: number;
  viewerId?: number;
  offerPromotionId?: number;
}

export const defaultValue: Readonly<ISpecialReward> = {
  isDeleted: false,
};
