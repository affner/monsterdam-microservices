import dayjs from 'dayjs';
import { ConfigurationValueType } from 'app/shared/model/enumerations/configuration-value-type.model';
import { ConfigurationCategory } from 'app/shared/model/enumerations/configuration-category.model';

export interface IAdminSystemConfigs {
  id?: number;
  configKey?: string;
  configValue?: string;
  description?: string | null;
  configValueType?: keyof typeof ConfigurationValueType | null;
  configCategory?: keyof typeof ConfigurationCategory | null;
  configFileContentType?: string | null;
  configFile?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IAdminSystemConfigs> = {
  isActive: false,
};
