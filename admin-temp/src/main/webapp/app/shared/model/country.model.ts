import dayjs from 'dayjs';

export interface ICountry {
  id?: number;
  name?: string;
  alpha2Code?: string;
  alpha3Code?: string;
  phoneCode?: string | null;
  thumbnailCountryContentType?: string | null;
  thumbnailCountry?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<ICountry> = {
  isDeleted: false,
};
