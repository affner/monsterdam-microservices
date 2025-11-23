import dayjs from 'dayjs';

export interface IUserUIPreferences {
  id?: number;
  preferences?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
}

export const defaultValue: Readonly<IUserUIPreferences> = {};
