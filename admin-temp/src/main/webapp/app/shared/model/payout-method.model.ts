import dayjs from 'dayjs';

export interface IPayoutMethod {
  id?: number;
  methodName?: string;
  tokenText?: string;
  expirationDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IPayoutMethod> = {
  isDeleted: false,
};
