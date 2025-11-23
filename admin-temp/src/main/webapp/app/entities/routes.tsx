import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import AdminUserProfile from './admin-user-profile';
import IdentityDocumentReview from './identity-document-review';
import DocumentReviewObservation from './document-review-observation';
import IdentityDocument from './identity-document';
import AssistanceTicket from './assistance-ticket';
import ModerationAction from './moderation-action';
import AdminAnnouncement from './admin-announcement';
import UserReport from './user-report';
import AccountingRecord from './accounting-record';
import FinancialStatement from './financial-statement';
import TaxDeclaration from './tax-declaration';
import Budget from './budget';
import Asset from './asset';
import Liability from './liability';
import MoneyPayout from './money-payout';
import CreatorEarning from './creator-earning';
import SubscriptionBundle from './subscription-bundle';
import PaymentTransaction from './payment-transaction';
import OfferPromotion from './offer-promotion';
import PurchasedContent from './purchased-content';
import PurchasedSubscription from './purchased-subscription';
import WalletTransaction from './wallet-transaction';
import PurchasedTip from './purchased-tip';
import SpecialAward from './special-award';
import SpecialReward from './special-reward';
import UserLite from './user-lite';
import UserProfile from './user-profile';
import UserSettings from './user-settings';
import UserAssociation from './user-association';
import UserEvent from './user-event';
import BookMark from './book-mark';
import Feedback from './feedback';
import PersonalSocialLinks from './personal-social-links';
import Notification from './notification';
import PostFeed from './post-feed';
import PostComment from './post-comment';
import PostPoll from './post-poll';
import ChatRoom from './chat-room';
import DirectMessage from './direct-message';
import UserMention from './user-mention';
import LikeMark from './like-mark';
import HashTag from './hash-tag';
import PollVote from './poll-vote';
import PollOption from './poll-option';
import SingleAudio from './single-audio';
import SingleVideo from './single-video';
import SinglePhoto from './single-photo';
import ContentPackage from './content-package';
import SingleDocument from './single-document';
import VideoStory from './video-story';
import SingleLiveStream from './single-live-stream';
import Country from './country';
import State from './state';
import SocialNetwork from './social-network';
import EmojiType from './emoji-type';
import PayoutMethod from './payout-method';
import PaymentMethod from './payment-method';
import PaymentProvider from './payment-provider';
import SpecialTitle from './special-title';
import TaxInfo from './tax-info';
import Currency from './currency';
import GlobalEvent from './global-event';
import HelpCategory from './help-category';
import HelpSubcategory from './help-subcategory';
import HelpQuestion from './help-question';
import HelpRelatedArticle from './help-related-article';
import AdminSystemConfigs from './admin-system-configs';
import AdminEmailConfigs from './admin-email-configs';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('admin', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="admin-user-profile/*" element={<AdminUserProfile />} />
        <Route path="identity-document-review/*" element={<IdentityDocumentReview />} />
        <Route path="document-review-observation/*" element={<DocumentReviewObservation />} />
        <Route path="identity-document/*" element={<IdentityDocument />} />
        <Route path="assistance-ticket/*" element={<AssistanceTicket />} />
        <Route path="moderation-action/*" element={<ModerationAction />} />
        <Route path="admin-announcement/*" element={<AdminAnnouncement />} />
        <Route path="user-report/*" element={<UserReport />} />
        <Route path="accounting-record/*" element={<AccountingRecord />} />
        <Route path="financial-statement/*" element={<FinancialStatement />} />
        <Route path="tax-declaration/*" element={<TaxDeclaration />} />
        <Route path="budget/*" element={<Budget />} />
        <Route path="asset/*" element={<Asset />} />
        <Route path="liability/*" element={<Liability />} />
        <Route path="money-payout/*" element={<MoneyPayout />} />
        <Route path="creator-earning/*" element={<CreatorEarning />} />
        <Route path="subscription-bundle/*" element={<SubscriptionBundle />} />
        <Route path="payment-transaction/*" element={<PaymentTransaction />} />
        <Route path="offer-promotion/*" element={<OfferPromotion />} />
        <Route path="purchased-content/*" element={<PurchasedContent />} />
        <Route path="purchased-subscription/*" element={<PurchasedSubscription />} />
        <Route path="wallet-transaction/*" element={<WalletTransaction />} />
        <Route path="purchased-tip/*" element={<PurchasedTip />} />
        <Route path="special-award/*" element={<SpecialAward />} />
        <Route path="special-reward/*" element={<SpecialReward />} />
        <Route path="user-lite/*" element={<UserLite />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="user-settings/*" element={<UserSettings />} />
        <Route path="user-association/*" element={<UserAssociation />} />
        <Route path="user-event/*" element={<UserEvent />} />
        <Route path="book-mark/*" element={<BookMark />} />
        <Route path="feedback/*" element={<Feedback />} />
        <Route path="personal-social-links/*" element={<PersonalSocialLinks />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="post-feed/*" element={<PostFeed />} />
        <Route path="post-comment/*" element={<PostComment />} />
        <Route path="post-poll/*" element={<PostPoll />} />
        <Route path="chat-room/*" element={<ChatRoom />} />
        <Route path="direct-message/*" element={<DirectMessage />} />
        <Route path="user-mention/*" element={<UserMention />} />
        <Route path="like-mark/*" element={<LikeMark />} />
        <Route path="hash-tag/*" element={<HashTag />} />
        <Route path="poll-vote/*" element={<PollVote />} />
        <Route path="poll-option/*" element={<PollOption />} />
        <Route path="single-audio/*" element={<SingleAudio />} />
        <Route path="single-video/*" element={<SingleVideo />} />
        <Route path="single-photo/*" element={<SinglePhoto />} />
        <Route path="content-package/*" element={<ContentPackage />} />
        <Route path="single-document/*" element={<SingleDocument />} />
        <Route path="video-story/*" element={<VideoStory />} />
        <Route path="single-live-stream/*" element={<SingleLiveStream />} />
        <Route path="country/*" element={<Country />} />
        <Route path="state/*" element={<State />} />
        <Route path="social-network/*" element={<SocialNetwork />} />
        <Route path="emoji-type/*" element={<EmojiType />} />
        <Route path="payout-method/*" element={<PayoutMethod />} />
        <Route path="payment-method/*" element={<PaymentMethod />} />
        <Route path="payment-provider/*" element={<PaymentProvider />} />
        <Route path="special-title/*" element={<SpecialTitle />} />
        <Route path="tax-info/*" element={<TaxInfo />} />
        <Route path="currency/*" element={<Currency />} />
        <Route path="global-event/*" element={<GlobalEvent />} />
        <Route path="help-category/*" element={<HelpCategory />} />
        <Route path="help-subcategory/*" element={<HelpSubcategory />} />
        <Route path="help-question/*" element={<HelpQuestion />} />
        <Route path="help-related-article/*" element={<HelpRelatedArticle />} />
        <Route path="admin-system-configs/*" element={<AdminSystemConfigs />} />
        <Route path="admin-email-configs/*" element={<AdminEmailConfigs />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
