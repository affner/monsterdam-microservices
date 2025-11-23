import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISingleDocument {
  id?: number;
  title?: string;
  description?: string | null;
  documentFileContentType?: string;
  documentFile?: string;
  documentFileS3Key?: string;
  documentType?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  user?: IUserProfile;
}

export const defaultValue: Readonly<ISingleDocument> = {
  isDeleted: false,
};
