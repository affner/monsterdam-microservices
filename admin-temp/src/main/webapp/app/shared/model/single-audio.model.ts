import dayjs from 'dayjs';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IContentPackage } from 'app/shared/model/content-package.model';

export interface ISingleAudio {
  id?: number;
  thumbnailContentType?: string;
  thumbnail?: string;
  thumbnailS3Key?: string;
  contentContentType?: string | null;
  content?: string | null;
  contentS3Key?: string;
  duration?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reports?: IUserReport[] | null;
  contentPackage?: IContentPackage;
}

export const defaultValue: Readonly<ISingleAudio> = {
  isDeleted: false,
};
