import dayjs from 'dayjs';

export interface ICurrency {
  id?: number;
  name?: string;
  symbol?: string;
  code?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<ICurrency> = {
  isDeleted: false,
};
