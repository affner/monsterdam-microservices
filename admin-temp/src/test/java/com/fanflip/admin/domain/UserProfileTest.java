package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.admin.domain.BookMarkTestSamples.*;
import static com.monsterdam.admin.domain.ChatRoomTestSamples.*;
import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.CountryTestSamples.*;
import static com.monsterdam.admin.domain.CreatorEarningTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.FeedbackTestSamples.*;
import static com.monsterdam.admin.domain.HashTagTestSamples.*;
import static com.monsterdam.admin.domain.MoneyPayoutTestSamples.*;
import static com.monsterdam.admin.domain.NotificationTestSamples.*;
import static com.monsterdam.admin.domain.OfferPromotionTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static com.monsterdam.admin.domain.PersonalSocialLinksTestSamples.*;
import static com.monsterdam.admin.domain.PollVoteTestSamples.*;
import static com.monsterdam.admin.domain.PostCommentTestSamples.*;
import static com.monsterdam.admin.domain.PostFeedTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedSubscriptionTestSamples.*;
import static com.monsterdam.admin.domain.SingleDocumentTestSamples.*;
import static com.monsterdam.admin.domain.StateTestSamples.*;
import static com.monsterdam.admin.domain.SubscriptionBundleTestSamples.*;
import static com.monsterdam.admin.domain.UserAssociationTestSamples.*;
import static com.monsterdam.admin.domain.UserEventTestSamples.*;
import static com.monsterdam.admin.domain.UserLiteTestSamples.*;
import static com.monsterdam.admin.domain.UserMentionTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static com.monsterdam.admin.domain.UserSettingsTestSamples.*;
import static com.monsterdam.admin.domain.VideoStoryTestSamples.*;
import static com.monsterdam.admin.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void userLiteTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        userProfile.setUserLite(userLiteBack);
        assertThat(userProfile.getUserLite()).isEqualTo(userLiteBack);

        userProfile.userLite(null);
        assertThat(userProfile.getUserLite()).isNull();
    }

    @Test
    void settingsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserSettings userSettingsBack = getUserSettingsRandomSampleGenerator();

        userProfile.setSettings(userSettingsBack);
        assertThat(userProfile.getSettings()).isEqualTo(userSettingsBack);

        userProfile.settings(null);
        assertThat(userProfile.getSettings()).isNull();
    }

    @Test
    void makedReportsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        userProfile.addMakedReports(userReportBack);
        assertThat(userProfile.getMakedReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getReporter()).isEqualTo(userProfile);

        userProfile.removeMakedReports(userReportBack);
        assertThat(userProfile.getMakedReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getReporter()).isNull();

        userProfile.makedReports(new HashSet<>(Set.of(userReportBack)));
        assertThat(userProfile.getMakedReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getReporter()).isEqualTo(userProfile);

        userProfile.setMakedReports(new HashSet<>());
        assertThat(userProfile.getMakedReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getReporter()).isNull();
    }

    @Test
    void receivedReportsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        userProfile.addReceivedReports(userReportBack);
        assertThat(userProfile.getReceivedReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getReported()).isEqualTo(userProfile);

        userProfile.removeReceivedReports(userReportBack);
        assertThat(userProfile.getReceivedReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getReported()).isNull();

        userProfile.receivedReports(new HashSet<>(Set.of(userReportBack)));
        assertThat(userProfile.getReceivedReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getReported()).isEqualTo(userProfile);

        userProfile.setReceivedReports(new HashSet<>());
        assertThat(userProfile.getReceivedReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getReported()).isNull();
    }

    @Test
    void assistanceTicketsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        userProfile.addAssistanceTickets(assistanceTicketBack);
        assertThat(userProfile.getAssistanceTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isEqualTo(userProfile);

        userProfile.removeAssistanceTickets(assistanceTicketBack);
        assertThat(userProfile.getAssistanceTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isNull();

        userProfile.assistanceTickets(new HashSet<>(Set.of(assistanceTicketBack)));
        assertThat(userProfile.getAssistanceTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isEqualTo(userProfile);

        userProfile.setAssistanceTickets(new HashSet<>());
        assertThat(userProfile.getAssistanceTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isNull();
    }

    @Test
    void withdrawsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        MoneyPayout moneyPayoutBack = getMoneyPayoutRandomSampleGenerator();

        userProfile.addWithdraws(moneyPayoutBack);
        assertThat(userProfile.getWithdraws()).containsOnly(moneyPayoutBack);
        assertThat(moneyPayoutBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeWithdraws(moneyPayoutBack);
        assertThat(userProfile.getWithdraws()).doesNotContain(moneyPayoutBack);
        assertThat(moneyPayoutBack.getCreator()).isNull();

        userProfile.withdraws(new HashSet<>(Set.of(moneyPayoutBack)));
        assertThat(userProfile.getWithdraws()).containsOnly(moneyPayoutBack);
        assertThat(moneyPayoutBack.getCreator()).isEqualTo(userProfile);

        userProfile.setWithdraws(new HashSet<>());
        assertThat(userProfile.getWithdraws()).doesNotContain(moneyPayoutBack);
        assertThat(moneyPayoutBack.getCreator()).isNull();
    }

    @Test
    void subscriptionBundlesTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        SubscriptionBundle subscriptionBundleBack = getSubscriptionBundleRandomSampleGenerator();

        userProfile.addSubscriptionBundles(subscriptionBundleBack);
        assertThat(userProfile.getSubscriptionBundles()).containsOnly(subscriptionBundleBack);
        assertThat(subscriptionBundleBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeSubscriptionBundles(subscriptionBundleBack);
        assertThat(userProfile.getSubscriptionBundles()).doesNotContain(subscriptionBundleBack);
        assertThat(subscriptionBundleBack.getCreator()).isNull();

        userProfile.subscriptionBundles(new HashSet<>(Set.of(subscriptionBundleBack)));
        assertThat(userProfile.getSubscriptionBundles()).containsOnly(subscriptionBundleBack);
        assertThat(subscriptionBundleBack.getCreator()).isEqualTo(userProfile);

        userProfile.setSubscriptionBundles(new HashSet<>());
        assertThat(userProfile.getSubscriptionBundles()).doesNotContain(subscriptionBundleBack);
        assertThat(subscriptionBundleBack.getCreator()).isNull();
    }

    @Test
    void earningsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        CreatorEarning creatorEarningBack = getCreatorEarningRandomSampleGenerator();

        userProfile.addEarnings(creatorEarningBack);
        assertThat(userProfile.getEarnings()).containsOnly(creatorEarningBack);
        assertThat(creatorEarningBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeEarnings(creatorEarningBack);
        assertThat(userProfile.getEarnings()).doesNotContain(creatorEarningBack);
        assertThat(creatorEarningBack.getCreator()).isNull();

        userProfile.earnings(new HashSet<>(Set.of(creatorEarningBack)));
        assertThat(userProfile.getEarnings()).containsOnly(creatorEarningBack);
        assertThat(creatorEarningBack.getCreator()).isEqualTo(userProfile);

        userProfile.setEarnings(new HashSet<>());
        assertThat(userProfile.getEarnings()).doesNotContain(creatorEarningBack);
        assertThat(creatorEarningBack.getCreator()).isNull();
    }

    @Test
    void paymentsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        userProfile.addPayments(paymentTransactionBack);
        assertThat(userProfile.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getViewer()).isEqualTo(userProfile);

        userProfile.removePayments(paymentTransactionBack);
        assertThat(userProfile.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getViewer()).isNull();

        userProfile.payments(new HashSet<>(Set.of(paymentTransactionBack)));
        assertThat(userProfile.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getViewer()).isEqualTo(userProfile);

        userProfile.setPayments(new HashSet<>());
        assertThat(userProfile.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getViewer()).isNull();
    }

    @Test
    void planOffersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        OfferPromotion offerPromotionBack = getOfferPromotionRandomSampleGenerator();

        userProfile.addPlanOffers(offerPromotionBack);
        assertThat(userProfile.getPlanOffers()).containsOnly(offerPromotionBack);
        assertThat(offerPromotionBack.getCreator()).isEqualTo(userProfile);

        userProfile.removePlanOffers(offerPromotionBack);
        assertThat(userProfile.getPlanOffers()).doesNotContain(offerPromotionBack);
        assertThat(offerPromotionBack.getCreator()).isNull();

        userProfile.planOffers(new HashSet<>(Set.of(offerPromotionBack)));
        assertThat(userProfile.getPlanOffers()).containsOnly(offerPromotionBack);
        assertThat(offerPromotionBack.getCreator()).isEqualTo(userProfile);

        userProfile.setPlanOffers(new HashSet<>());
        assertThat(userProfile.getPlanOffers()).doesNotContain(offerPromotionBack);
        assertThat(offerPromotionBack.getCreator()).isNull();
    }

    @Test
    void purchasedContentTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        userProfile.addPurchasedContent(purchasedContentBack);
        assertThat(userProfile.getPurchasedContents()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getViewer()).isEqualTo(userProfile);

        userProfile.removePurchasedContent(purchasedContentBack);
        assertThat(userProfile.getPurchasedContents()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getViewer()).isNull();

        userProfile.purchasedContents(new HashSet<>(Set.of(purchasedContentBack)));
        assertThat(userProfile.getPurchasedContents()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getViewer()).isEqualTo(userProfile);

        userProfile.setPurchasedContents(new HashSet<>());
        assertThat(userProfile.getPurchasedContents()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getViewer()).isNull();
    }

    @Test
    void purchasedSubscriptionsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        userProfile.addPurchasedSubscriptions(purchasedSubscriptionBack);
        assertThat(userProfile.getPurchasedSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isEqualTo(userProfile);

        userProfile.removePurchasedSubscriptions(purchasedSubscriptionBack);
        assertThat(userProfile.getPurchasedSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isNull();

        userProfile.purchasedSubscriptions(new HashSet<>(Set.of(purchasedSubscriptionBack)));
        assertThat(userProfile.getPurchasedSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isEqualTo(userProfile);

        userProfile.setPurchasedSubscriptions(new HashSet<>());
        assertThat(userProfile.getPurchasedSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isNull();
    }

    @Test
    void walletTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        userProfile.addWallet(walletTransactionBack);
        assertThat(userProfile.getWallets()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getViewer()).isEqualTo(userProfile);

        userProfile.removeWallet(walletTransactionBack);
        assertThat(userProfile.getWallets()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getViewer()).isNull();

        userProfile.wallets(new HashSet<>(Set.of(walletTransactionBack)));
        assertThat(userProfile.getWallets()).containsOnly(walletTransactionBack);
        assertThat(walletTransactionBack.getViewer()).isEqualTo(userProfile);

        userProfile.setWallets(new HashSet<>());
        assertThat(userProfile.getWallets()).doesNotContain(walletTransactionBack);
        assertThat(walletTransactionBack.getViewer()).isNull();
    }

    @Test
    void socialNetworksTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PersonalSocialLinks personalSocialLinksBack = getPersonalSocialLinksRandomSampleGenerator();

        userProfile.addSocialNetworks(personalSocialLinksBack);
        assertThat(userProfile.getSocialNetworks()).containsOnly(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isEqualTo(userProfile);

        userProfile.removeSocialNetworks(personalSocialLinksBack);
        assertThat(userProfile.getSocialNetworks()).doesNotContain(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isNull();

        userProfile.socialNetworks(new HashSet<>(Set.of(personalSocialLinksBack)));
        assertThat(userProfile.getSocialNetworks()).containsOnly(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isEqualTo(userProfile);

        userProfile.setSocialNetworks(new HashSet<>());
        assertThat(userProfile.getSocialNetworks()).doesNotContain(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isNull();
    }

    @Test
    void commentNotificationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userProfile.addCommentNotifications(notificationBack);
        assertThat(userProfile.getCommentNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getCommentedUser()).isEqualTo(userProfile);

        userProfile.removeCommentNotifications(notificationBack);
        assertThat(userProfile.getCommentNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getCommentedUser()).isNull();

        userProfile.commentNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(userProfile.getCommentNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getCommentedUser()).isEqualTo(userProfile);

        userProfile.setCommentNotifications(new HashSet<>());
        assertThat(userProfile.getCommentNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getCommentedUser()).isNull();
    }

    @Test
    void messageNotificationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userProfile.addMessageNotifications(notificationBack);
        assertThat(userProfile.getMessageNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMessagedUser()).isEqualTo(userProfile);

        userProfile.removeMessageNotifications(notificationBack);
        assertThat(userProfile.getMessageNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMessagedUser()).isNull();

        userProfile.messageNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(userProfile.getMessageNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMessagedUser()).isEqualTo(userProfile);

        userProfile.setMessageNotifications(new HashSet<>());
        assertThat(userProfile.getMessageNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMessagedUser()).isNull();
    }

    @Test
    void userMentionNotificationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userProfile.addUserMentionNotifications(notificationBack);
        assertThat(userProfile.getUserMentionNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMentionerUserInPost()).isEqualTo(userProfile);

        userProfile.removeUserMentionNotifications(notificationBack);
        assertThat(userProfile.getUserMentionNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMentionerUserInPost()).isNull();

        userProfile.userMentionNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(userProfile.getUserMentionNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMentionerUserInPost()).isEqualTo(userProfile);

        userProfile.setUserMentionNotifications(new HashSet<>());
        assertThat(userProfile.getUserMentionNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMentionerUserInPost()).isNull();
    }

    @Test
    void commentMentionNotificationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userProfile.addCommentMentionNotifications(notificationBack);
        assertThat(userProfile.getCommentMentionNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMentionerUserInComment()).isEqualTo(userProfile);

        userProfile.removeCommentMentionNotifications(notificationBack);
        assertThat(userProfile.getCommentMentionNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMentionerUserInComment()).isNull();

        userProfile.commentMentionNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(userProfile.getCommentMentionNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMentionerUserInComment()).isEqualTo(userProfile);

        userProfile.setCommentMentionNotifications(new HashSet<>());
        assertThat(userProfile.getCommentMentionNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMentionerUserInComment()).isNull();
    }

    @Test
    void ownAccountsAssociationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserAssociation userAssociationBack = getUserAssociationRandomSampleGenerator();

        userProfile.addOwnAccountsAssociations(userAssociationBack);
        assertThat(userProfile.getOwnAccountsAssociations()).containsOnly(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isEqualTo(userProfile);

        userProfile.removeOwnAccountsAssociations(userAssociationBack);
        assertThat(userProfile.getOwnAccountsAssociations()).doesNotContain(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isNull();

        userProfile.ownAccountsAssociations(new HashSet<>(Set.of(userAssociationBack)));
        assertThat(userProfile.getOwnAccountsAssociations()).containsOnly(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isEqualTo(userProfile);

        userProfile.setOwnAccountsAssociations(new HashSet<>());
        assertThat(userProfile.getOwnAccountsAssociations()).doesNotContain(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isNull();
    }

    @Test
    void createdEventsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserEvent userEventBack = getUserEventRandomSampleGenerator();

        userProfile.addCreatedEvents(userEventBack);
        assertThat(userProfile.getCreatedEvents()).containsOnly(userEventBack);
        assertThat(userEventBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeCreatedEvents(userEventBack);
        assertThat(userProfile.getCreatedEvents()).doesNotContain(userEventBack);
        assertThat(userEventBack.getCreator()).isNull();

        userProfile.createdEvents(new HashSet<>(Set.of(userEventBack)));
        assertThat(userProfile.getCreatedEvents()).containsOnly(userEventBack);
        assertThat(userEventBack.getCreator()).isEqualTo(userProfile);

        userProfile.setCreatedEvents(new HashSet<>());
        assertThat(userProfile.getCreatedEvents()).doesNotContain(userEventBack);
        assertThat(userEventBack.getCreator()).isNull();
    }

    @Test
    void bookMarksTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        BookMark bookMarkBack = getBookMarkRandomSampleGenerator();

        userProfile.addBookMarks(bookMarkBack);
        assertThat(userProfile.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getUser()).isEqualTo(userProfile);

        userProfile.removeBookMarks(bookMarkBack);
        assertThat(userProfile.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getUser()).isNull();

        userProfile.bookMarks(new HashSet<>(Set.of(bookMarkBack)));
        assertThat(userProfile.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getUser()).isEqualTo(userProfile);

        userProfile.setBookMarks(new HashSet<>());
        assertThat(userProfile.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getUser()).isNull();
    }

    @Test
    void feedbackTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        userProfile.addFeedback(feedbackBack);
        assertThat(userProfile.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeFeedback(feedbackBack);
        assertThat(userProfile.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getCreator()).isNull();

        userProfile.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(userProfile.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getCreator()).isEqualTo(userProfile);

        userProfile.setFeedbacks(new HashSet<>());
        assertThat(userProfile.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getCreator()).isNull();
    }

    @Test
    void sentMessagesTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        userProfile.addSentMessages(directMessageBack);
        assertThat(userProfile.getSentMessages()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getUser()).isEqualTo(userProfile);

        userProfile.removeSentMessages(directMessageBack);
        assertThat(userProfile.getSentMessages()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getUser()).isNull();

        userProfile.sentMessages(new HashSet<>(Set.of(directMessageBack)));
        assertThat(userProfile.getSentMessages()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getUser()).isEqualTo(userProfile);

        userProfile.setSentMessages(new HashSet<>());
        assertThat(userProfile.getSentMessages()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getUser()).isNull();
    }

    @Test
    void chatsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        userProfile.addChats(chatRoomBack);
        assertThat(userProfile.getChats()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getUser()).isEqualTo(userProfile);

        userProfile.removeChats(chatRoomBack);
        assertThat(userProfile.getChats()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getUser()).isNull();

        userProfile.chats(new HashSet<>(Set.of(chatRoomBack)));
        assertThat(userProfile.getChats()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getUser()).isEqualTo(userProfile);

        userProfile.setChats(new HashSet<>());
        assertThat(userProfile.getChats()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getUser()).isNull();
    }

    @Test
    void mentionsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserMention userMentionBack = getUserMentionRandomSampleGenerator();

        userProfile.addMentions(userMentionBack);
        assertThat(userProfile.getMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getMentionedUser()).isEqualTo(userProfile);

        userProfile.removeMentions(userMentionBack);
        assertThat(userProfile.getMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getMentionedUser()).isNull();

        userProfile.mentions(new HashSet<>(Set.of(userMentionBack)));
        assertThat(userProfile.getMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getMentionedUser()).isEqualTo(userProfile);

        userProfile.setMentions(new HashSet<>());
        assertThat(userProfile.getMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getMentionedUser()).isNull();
    }

    @Test
    void commentsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        userProfile.addComments(postCommentBack);
        assertThat(userProfile.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isEqualTo(userProfile);

        userProfile.removeComments(postCommentBack);
        assertThat(userProfile.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isNull();

        userProfile.comments(new HashSet<>(Set.of(postCommentBack)));
        assertThat(userProfile.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isEqualTo(userProfile);

        userProfile.setComments(new HashSet<>());
        assertThat(userProfile.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isNull();
    }

    @Test
    void feedsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        userProfile.addFeeds(postFeedBack);
        assertThat(userProfile.getFeeds()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeFeeds(postFeedBack);
        assertThat(userProfile.getFeeds()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getCreator()).isNull();

        userProfile.feeds(new HashSet<>(Set.of(postFeedBack)));
        assertThat(userProfile.getFeeds()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getCreator()).isEqualTo(userProfile);

        userProfile.setFeeds(new HashSet<>());
        assertThat(userProfile.getFeeds()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getCreator()).isNull();
    }

    @Test
    void votedPollsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PollVote pollVoteBack = getPollVoteRandomSampleGenerator();

        userProfile.addVotedPolls(pollVoteBack);
        assertThat(userProfile.getVotedPolls()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getVotingUser()).isEqualTo(userProfile);

        userProfile.removeVotedPolls(pollVoteBack);
        assertThat(userProfile.getVotedPolls()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getVotingUser()).isNull();

        userProfile.votedPolls(new HashSet<>(Set.of(pollVoteBack)));
        assertThat(userProfile.getVotedPolls()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getVotingUser()).isEqualTo(userProfile);

        userProfile.setVotedPolls(new HashSet<>());
        assertThat(userProfile.getVotedPolls()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getVotingUser()).isNull();
    }

    @Test
    void videoStoriesTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        VideoStory videoStoryBack = getVideoStoryRandomSampleGenerator();

        userProfile.addVideoStories(videoStoryBack);
        assertThat(userProfile.getVideoStories()).containsOnly(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isEqualTo(userProfile);

        userProfile.removeVideoStories(videoStoryBack);
        assertThat(userProfile.getVideoStories()).doesNotContain(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isNull();

        userProfile.videoStories(new HashSet<>(Set.of(videoStoryBack)));
        assertThat(userProfile.getVideoStories()).containsOnly(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isEqualTo(userProfile);

        userProfile.setVideoStories(new HashSet<>());
        assertThat(userProfile.getVideoStories()).doesNotContain(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        SingleDocument singleDocumentBack = getSingleDocumentRandomSampleGenerator();

        userProfile.addDocuments(singleDocumentBack);
        assertThat(userProfile.getDocuments()).containsOnly(singleDocumentBack);
        assertThat(singleDocumentBack.getUser()).isEqualTo(userProfile);

        userProfile.removeDocuments(singleDocumentBack);
        assertThat(userProfile.getDocuments()).doesNotContain(singleDocumentBack);
        assertThat(singleDocumentBack.getUser()).isNull();

        userProfile.documents(new HashSet<>(Set.of(singleDocumentBack)));
        assertThat(userProfile.getDocuments()).containsOnly(singleDocumentBack);
        assertThat(singleDocumentBack.getUser()).isEqualTo(userProfile);

        userProfile.setDocuments(new HashSet<>());
        assertThat(userProfile.getDocuments()).doesNotContain(singleDocumentBack);
        assertThat(singleDocumentBack.getUser()).isNull();
    }

    @Test
    void countryOfBirthTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        userProfile.setCountryOfBirth(countryBack);
        assertThat(userProfile.getCountryOfBirth()).isEqualTo(countryBack);

        userProfile.countryOfBirth(null);
        assertThat(userProfile.getCountryOfBirth()).isNull();
    }

    @Test
    void stateOfResidenceTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        userProfile.setStateOfResidence(stateBack);
        assertThat(userProfile.getStateOfResidence()).isEqualTo(stateBack);

        userProfile.stateOfResidence(null);
        assertThat(userProfile.getStateOfResidence()).isNull();
    }

    @Test
    void followedTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addFollowed(userProfileBack);
        assertThat(userProfile.getFolloweds()).containsOnly(userProfileBack);

        userProfile.removeFollowed(userProfileBack);
        assertThat(userProfile.getFolloweds()).doesNotContain(userProfileBack);

        userProfile.followeds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getFolloweds()).containsOnly(userProfileBack);

        userProfile.setFolloweds(new HashSet<>());
        assertThat(userProfile.getFolloweds()).doesNotContain(userProfileBack);
    }

    @Test
    void blockedListTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addBlockedList(userProfileBack);
        assertThat(userProfile.getBlockedLists()).containsOnly(userProfileBack);

        userProfile.removeBlockedList(userProfileBack);
        assertThat(userProfile.getBlockedLists()).doesNotContain(userProfileBack);

        userProfile.blockedLists(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getBlockedLists()).containsOnly(userProfileBack);

        userProfile.setBlockedLists(new HashSet<>());
        assertThat(userProfile.getBlockedLists()).doesNotContain(userProfileBack);
    }

    @Test
    void loyaListsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addLoyaLists(userProfileBack);
        assertThat(userProfile.getLoyaLists()).containsOnly(userProfileBack);

        userProfile.removeLoyaLists(userProfileBack);
        assertThat(userProfile.getLoyaLists()).doesNotContain(userProfileBack);

        userProfile.loyaLists(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getLoyaLists()).containsOnly(userProfileBack);

        userProfile.setLoyaLists(new HashSet<>());
        assertThat(userProfile.getLoyaLists()).doesNotContain(userProfileBack);
    }

    @Test
    void subscribedTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addSubscribed(userProfileBack);
        assertThat(userProfile.getSubscribeds()).containsOnly(userProfileBack);

        userProfile.removeSubscribed(userProfileBack);
        assertThat(userProfile.getSubscribeds()).doesNotContain(userProfileBack);

        userProfile.subscribeds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getSubscribeds()).containsOnly(userProfileBack);

        userProfile.setSubscribeds(new HashSet<>());
        assertThat(userProfile.getSubscribeds()).doesNotContain(userProfileBack);
    }

    @Test
    void joinedEventsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserEvent userEventBack = getUserEventRandomSampleGenerator();

        userProfile.addJoinedEvents(userEventBack);
        assertThat(userProfile.getJoinedEvents()).containsOnly(userEventBack);

        userProfile.removeJoinedEvents(userEventBack);
        assertThat(userProfile.getJoinedEvents()).doesNotContain(userEventBack);

        userProfile.joinedEvents(new HashSet<>(Set.of(userEventBack)));
        assertThat(userProfile.getJoinedEvents()).containsOnly(userEventBack);

        userProfile.setJoinedEvents(new HashSet<>());
        assertThat(userProfile.getJoinedEvents()).doesNotContain(userEventBack);
    }

    @Test
    void blockedUbicationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        userProfile.addBlockedUbications(stateBack);
        assertThat(userProfile.getBlockedUbications()).containsOnly(stateBack);

        userProfile.removeBlockedUbications(stateBack);
        assertThat(userProfile.getBlockedUbications()).doesNotContain(stateBack);

        userProfile.blockedUbications(new HashSet<>(Set.of(stateBack)));
        assertThat(userProfile.getBlockedUbications()).containsOnly(stateBack);

        userProfile.setBlockedUbications(new HashSet<>());
        assertThat(userProfile.getBlockedUbications()).doesNotContain(stateBack);
    }

    @Test
    void hashTagsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        HashTag hashTagBack = getHashTagRandomSampleGenerator();

        userProfile.addHashTags(hashTagBack);
        assertThat(userProfile.getHashTags()).containsOnly(hashTagBack);

        userProfile.removeHashTags(hashTagBack);
        assertThat(userProfile.getHashTags()).doesNotContain(hashTagBack);

        userProfile.hashTags(new HashSet<>(Set.of(hashTagBack)));
        assertThat(userProfile.getHashTags()).containsOnly(hashTagBack);

        userProfile.setHashTags(new HashSet<>());
        assertThat(userProfile.getHashTags()).doesNotContain(hashTagBack);
    }

    @Test
    void followersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addFollowers(userProfileBack);
        assertThat(userProfile.getFollowers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).containsOnly(userProfile);

        userProfile.removeFollowers(userProfileBack);
        assertThat(userProfile.getFollowers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).doesNotContain(userProfile);

        userProfile.followers(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getFollowers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).containsOnly(userProfile);

        userProfile.setFollowers(new HashSet<>());
        assertThat(userProfile.getFollowers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).doesNotContain(userProfile);
    }

    @Test
    void blockersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addBlockers(userProfileBack);
        assertThat(userProfile.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).containsOnly(userProfile);

        userProfile.removeBlockers(userProfileBack);
        assertThat(userProfile.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).doesNotContain(userProfile);

        userProfile.blockers(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).containsOnly(userProfile);

        userProfile.setBlockers(new HashSet<>());
        assertThat(userProfile.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).doesNotContain(userProfile);
    }

    @Test
    void awardsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addAwards(userProfileBack);
        assertThat(userProfile.getAwards()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).containsOnly(userProfile);

        userProfile.removeAwards(userProfileBack);
        assertThat(userProfile.getAwards()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).doesNotContain(userProfile);

        userProfile.awards(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getAwards()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).containsOnly(userProfile);

        userProfile.setAwards(new HashSet<>());
        assertThat(userProfile.getAwards()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).doesNotContain(userProfile);
    }

    @Test
    void subscriptionsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addSubscriptions(userProfileBack);
        assertThat(userProfile.getSubscriptions()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).containsOnly(userProfile);

        userProfile.removeSubscriptions(userProfileBack);
        assertThat(userProfile.getSubscriptions()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).doesNotContain(userProfile);

        userProfile.subscriptions(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getSubscriptions()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).containsOnly(userProfile);

        userProfile.setSubscriptions(new HashSet<>());
        assertThat(userProfile.getSubscriptions()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).doesNotContain(userProfile);
    }

    @Test
    void contentPackageTagsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        userProfile.addContentPackageTags(contentPackageBack);
        assertThat(userProfile.getContentPackageTags()).containsOnly(contentPackageBack);
        assertThat(contentPackageBack.getUsersTaggeds()).containsOnly(userProfile);

        userProfile.removeContentPackageTags(contentPackageBack);
        assertThat(userProfile.getContentPackageTags()).doesNotContain(contentPackageBack);
        assertThat(contentPackageBack.getUsersTaggeds()).doesNotContain(userProfile);

        userProfile.contentPackageTags(new HashSet<>(Set.of(contentPackageBack)));
        assertThat(userProfile.getContentPackageTags()).containsOnly(contentPackageBack);
        assertThat(contentPackageBack.getUsersTaggeds()).containsOnly(userProfile);

        userProfile.setContentPackageTags(new HashSet<>());
        assertThat(userProfile.getContentPackageTags()).doesNotContain(contentPackageBack);
        assertThat(contentPackageBack.getUsersTaggeds()).doesNotContain(userProfile);
    }
}
