import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';
import { TaxType } from 'app/shared/model/enumerations/tax-type.model';

export interface ITaxInfo {
  id?: number;
  ratePercentage?: number | null;
  taxType?: keyof typeof TaxType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  country?: ICountry;
}

export const defaultValue: Readonly<ITaxInfo> = {
  isDeleted: false,
};
