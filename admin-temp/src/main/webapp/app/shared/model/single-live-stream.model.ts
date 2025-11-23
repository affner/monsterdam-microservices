import dayjs from 'dayjs';
import { IUserReport } from 'app/shared/model/user-report.model';

export interface ISingleLiveStream {
  id?: number;
  title?: string | null;
  description?: string | null;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  thumbnailS3Key?: string | null;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs | null;
  liveContentContentType?: string | null;
  liveContent?: string | null;
  liveContentS3Key?: string | null;
  isRecorded?: boolean;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reports?: IUserReport[] | null;
}

export const defaultValue: Readonly<ISingleLiveStream> = {
  isRecorded: false,
  isDeleted: false,
};
