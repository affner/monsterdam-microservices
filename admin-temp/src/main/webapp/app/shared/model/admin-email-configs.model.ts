import dayjs from 'dayjs';
import { EmailTemplateType } from 'app/shared/model/enumerations/email-template-type.model';

export interface IAdminEmailConfigs {
  id?: number;
  title?: string;
  subject?: string;
  content?: string;
  mailTemplateType?: keyof typeof EmailTemplateType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isActive?: boolean;
}

export const defaultValue: Readonly<IAdminEmailConfigs> = {
  isActive: false,
};
