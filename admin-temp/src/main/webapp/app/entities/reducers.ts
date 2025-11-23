import adminUserProfile from 'app/entities/admin-user-profile/admin-user-profile.reducer';
import identityDocumentReview from 'app/entities/identity-document-review/identity-document-review.reducer';
import documentReviewObservation from 'app/entities/document-review-observation/document-review-observation.reducer';
import identityDocument from 'app/entities/identity-document/identity-document.reducer';
import assistanceTicket from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import moderationAction from 'app/entities/moderation-action/moderation-action.reducer';
import adminAnnouncement from 'app/entities/admin-announcement/admin-announcement.reducer';
import userReport from 'app/entities/user-report/user-report.reducer';
import accountingRecord from 'app/entities/accounting-record/accounting-record.reducer';
import financialStatement from 'app/entities/financial-statement/financial-statement.reducer';
import taxDeclaration from 'app/entities/tax-declaration/tax-declaration.reducer';
import budget from 'app/entities/budget/budget.reducer';
import asset from 'app/entities/asset/asset.reducer';
import liability from 'app/entities/liability/liability.reducer';
import moneyPayout from 'app/entities/money-payout/money-payout.reducer';
import creatorEarning from 'app/entities/creator-earning/creator-earning.reducer';
import subscriptionBundle from 'app/entities/subscription-bundle/subscription-bundle.reducer';
import paymentTransaction from 'app/entities/payment-transaction/payment-transaction.reducer';
import offerPromotion from 'app/entities/offer-promotion/offer-promotion.reducer';
import purchasedContent from 'app/entities/purchased-content/purchased-content.reducer';
import purchasedSubscription from 'app/entities/purchased-subscription/purchased-subscription.reducer';
import walletTransaction from 'app/entities/wallet-transaction/wallet-transaction.reducer';
import purchasedTip from 'app/entities/purchased-tip/purchased-tip.reducer';
import specialAward from 'app/entities/special-award/special-award.reducer';
import specialReward from 'app/entities/special-reward/special-reward.reducer';
import userLite from 'app/entities/user-lite/user-lite.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import userSettings from 'app/entities/user-settings/user-settings.reducer';
import userAssociation from 'app/entities/user-association/user-association.reducer';
import userEvent from 'app/entities/user-event/user-event.reducer';
import bookMark from 'app/entities/book-mark/book-mark.reducer';
import feedback from 'app/entities/feedback/feedback.reducer';
import personalSocialLinks from 'app/entities/personal-social-links/personal-social-links.reducer';
import notification from 'app/entities/notification/notification.reducer';
import postFeed from 'app/entities/post-feed/post-feed.reducer';
import postComment from 'app/entities/post-comment/post-comment.reducer';
import postPoll from 'app/entities/post-poll/post-poll.reducer';
import chatRoom from 'app/entities/chat-room/chat-room.reducer';
import directMessage from 'app/entities/direct-message/direct-message.reducer';
import userMention from 'app/entities/user-mention/user-mention.reducer';
import likeMark from 'app/entities/like-mark/like-mark.reducer';
import hashTag from 'app/entities/hash-tag/hash-tag.reducer';
import pollVote from 'app/entities/poll-vote/poll-vote.reducer';
import pollOption from 'app/entities/poll-option/poll-option.reducer';
import singleAudio from 'app/entities/single-audio/single-audio.reducer';
import singleVideo from 'app/entities/single-video/single-video.reducer';
import singlePhoto from 'app/entities/single-photo/single-photo.reducer';
import contentPackage from 'app/entities/content-package/content-package.reducer';
import singleDocument from 'app/entities/single-document/single-document.reducer';
import videoStory from 'app/entities/video-story/video-story.reducer';
import singleLiveStream from 'app/entities/single-live-stream/single-live-stream.reducer';
import country from 'app/entities/country/country.reducer';
import state from 'app/entities/state/state.reducer';
import socialNetwork from 'app/entities/social-network/social-network.reducer';
import emojiType from 'app/entities/emoji-type/emoji-type.reducer';
import payoutMethod from 'app/entities/payout-method/payout-method.reducer';
import paymentMethod from 'app/entities/payment-method/payment-method.reducer';
import paymentProvider from 'app/entities/payment-provider/payment-provider.reducer';
import specialTitle from 'app/entities/special-title/special-title.reducer';
import taxInfo from 'app/entities/tax-info/tax-info.reducer';
import currency from 'app/entities/currency/currency.reducer';
import globalEvent from 'app/entities/global-event/global-event.reducer';
import helpCategory from 'app/entities/help-category/help-category.reducer';
import helpSubcategory from 'app/entities/help-subcategory/help-subcategory.reducer';
import helpQuestion from 'app/entities/help-question/help-question.reducer';
import helpRelatedArticle from 'app/entities/help-related-article/help-related-article.reducer';
import adminSystemConfigs from 'app/entities/admin-system-configs/admin-system-configs.reducer';
import adminEmailConfigs from 'app/entities/admin-email-configs/admin-email-configs.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  adminUserProfile,
  identityDocumentReview,
  documentReviewObservation,
  identityDocument,
  assistanceTicket,
  moderationAction,
  adminAnnouncement,
  userReport,
  accountingRecord,
  financialStatement,
  taxDeclaration,
  budget,
  asset,
  liability,
  moneyPayout,
  creatorEarning,
  subscriptionBundle,
  paymentTransaction,
  offerPromotion,
  purchasedContent,
  purchasedSubscription,
  walletTransaction,
  purchasedTip,
  specialAward,
  specialReward,
  userLite,
  userProfile,
  userSettings,
  userAssociation,
  userEvent,
  bookMark,
  feedback,
  personalSocialLinks,
  notification,
  postFeed,
  postComment,
  postPoll,
  chatRoom,
  directMessage,
  userMention,
  likeMark,
  hashTag,
  pollVote,
  pollOption,
  singleAudio,
  singleVideo,
  singlePhoto,
  contentPackage,
  singleDocument,
  videoStory,
  singleLiveStream,
  country,
  state,
  socialNetwork,
  emojiType,
  payoutMethod,
  paymentMethod,
  paymentProvider,
  specialTitle,
  taxInfo,
  currency,
  globalEvent,
  helpCategory,
  helpSubcategory,
  helpQuestion,
  helpRelatedArticle,
  adminSystemConfigs,
  adminEmailConfigs,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
