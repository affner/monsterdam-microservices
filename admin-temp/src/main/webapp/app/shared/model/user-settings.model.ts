import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserLanguage } from 'app/shared/model/enumerations/user-language.model';

export interface IUserSettings {
  id?: number;
  lastModifiedDate?: dayjs.Dayjs | null;
  darkMode?: boolean;
  language?: keyof typeof UserLanguage;
  contentFilter?: boolean;
  messageBlurIntensity?: number | null;
  activityStatusVisibility?: boolean;
  twoFactorAuthentication?: boolean;
  sessionsActiveCount?: number | null;
  emailNotifications?: boolean;
  importantSubscriptionNotifications?: boolean;
  newMessages?: boolean;
  postReplies?: boolean;
  postLikes?: boolean;
  newFollowers?: boolean;
  smsNewStream?: boolean;
  toastNewComment?: boolean;
  toastNewLikes?: boolean;
  toastNewStream?: boolean;
  siteNewComment?: boolean;
  siteNewLikes?: boolean;
  siteDiscountsFromFollowedUsers?: boolean;
  siteNewStream?: boolean;
  siteUpcomingStreamReminders?: boolean;
  createdDate?: dayjs.Dayjs;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUserSettings> = {
  darkMode: false,
  contentFilter: false,
  activityStatusVisibility: false,
  twoFactorAuthentication: false,
  emailNotifications: false,
  importantSubscriptionNotifications: false,
  newMessages: false,
  postReplies: false,
  postLikes: false,
  newFollowers: false,
  smsNewStream: false,
  toastNewComment: false,
  toastNewLikes: false,
  toastNewStream: false,
  siteNewComment: false,
  siteNewLikes: false,
  siteDiscountsFromFollowedUsers: false,
  siteNewStream: false,
  siteUpcomingStreamReminders: false,
  isDeleted: false,
};
