import dayjs from 'dayjs';

export interface ISpecialTitle {
  id?: number;
  description?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<ISpecialTitle> = {
  isDeleted: false,
};
