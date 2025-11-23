import dayjs from 'dayjs';

export interface IGlobalEvent {
  id?: number;
  eventName?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IGlobalEvent> = {
  isDeleted: false,
};
