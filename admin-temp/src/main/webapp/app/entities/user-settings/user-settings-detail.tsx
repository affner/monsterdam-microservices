import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-settings.reducer';

export const UserSettingsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userSettingsEntity = useAppSelector(state => state.admin.userSettings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userSettingsDetailsHeading">
          <Translate contentKey="adminApp.userSettings.detail.title">UserSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.id}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userSettings.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userSettingsEntity.lastModifiedDate ? (
              <TextFormat value={userSettingsEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="darkMode">
              <Translate contentKey="adminApp.userSettings.darkMode">Dark Mode</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.darkMode ? 'true' : 'false'}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="adminApp.userSettings.language">Language</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.language}</dd>
          <dt>
            <span id="contentFilter">
              <Translate contentKey="adminApp.userSettings.contentFilter">Content Filter</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.contentFilter ? 'true' : 'false'}</dd>
          <dt>
            <span id="messageBlurIntensity">
              <Translate contentKey="adminApp.userSettings.messageBlurIntensity">Message Blur Intensity</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.messageBlurIntensity}</dd>
          <dt>
            <span id="activityStatusVisibility">
              <Translate contentKey="adminApp.userSettings.activityStatusVisibility">Activity Status Visibility</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.activityStatusVisibility ? 'true' : 'false'}</dd>
          <dt>
            <span id="twoFactorAuthentication">
              <Translate contentKey="adminApp.userSettings.twoFactorAuthentication">Two Factor Authentication</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.twoFactorAuthentication ? 'true' : 'false'}</dd>
          <dt>
            <span id="sessionsActiveCount">
              <Translate contentKey="adminApp.userSettings.sessionsActiveCount">Sessions Active Count</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.sessionsActiveCount}</dd>
          <dt>
            <span id="emailNotifications">
              <Translate contentKey="adminApp.userSettings.emailNotifications">Email Notifications</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.emailNotifications ? 'true' : 'false'}</dd>
          <dt>
            <span id="importantSubscriptionNotifications">
              <Translate contentKey="adminApp.userSettings.importantSubscriptionNotifications">
                Important Subscription Notifications
              </Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.importantSubscriptionNotifications ? 'true' : 'false'}</dd>
          <dt>
            <span id="newMessages">
              <Translate contentKey="adminApp.userSettings.newMessages">New Messages</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.newMessages ? 'true' : 'false'}</dd>
          <dt>
            <span id="postReplies">
              <Translate contentKey="adminApp.userSettings.postReplies">Post Replies</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.postReplies ? 'true' : 'false'}</dd>
          <dt>
            <span id="postLikes">
              <Translate contentKey="adminApp.userSettings.postLikes">Post Likes</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.postLikes ? 'true' : 'false'}</dd>
          <dt>
            <span id="newFollowers">
              <Translate contentKey="adminApp.userSettings.newFollowers">New Followers</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.newFollowers ? 'true' : 'false'}</dd>
          <dt>
            <span id="smsNewStream">
              <Translate contentKey="adminApp.userSettings.smsNewStream">Sms New Stream</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.smsNewStream ? 'true' : 'false'}</dd>
          <dt>
            <span id="toastNewComment">
              <Translate contentKey="adminApp.userSettings.toastNewComment">Toast New Comment</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.toastNewComment ? 'true' : 'false'}</dd>
          <dt>
            <span id="toastNewLikes">
              <Translate contentKey="adminApp.userSettings.toastNewLikes">Toast New Likes</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.toastNewLikes ? 'true' : 'false'}</dd>
          <dt>
            <span id="toastNewStream">
              <Translate contentKey="adminApp.userSettings.toastNewStream">Toast New Stream</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.toastNewStream ? 'true' : 'false'}</dd>
          <dt>
            <span id="siteNewComment">
              <Translate contentKey="adminApp.userSettings.siteNewComment">Site New Comment</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.siteNewComment ? 'true' : 'false'}</dd>
          <dt>
            <span id="siteNewLikes">
              <Translate contentKey="adminApp.userSettings.siteNewLikes">Site New Likes</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.siteNewLikes ? 'true' : 'false'}</dd>
          <dt>
            <span id="siteDiscountsFromFollowedUsers">
              <Translate contentKey="adminApp.userSettings.siteDiscountsFromFollowedUsers">Site Discounts From Followed Users</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.siteDiscountsFromFollowedUsers ? 'true' : 'false'}</dd>
          <dt>
            <span id="siteNewStream">
              <Translate contentKey="adminApp.userSettings.siteNewStream">Site New Stream</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.siteNewStream ? 'true' : 'false'}</dd>
          <dt>
            <span id="siteUpcomingStreamReminders">
              <Translate contentKey="adminApp.userSettings.siteUpcomingStreamReminders">Site Upcoming Stream Reminders</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.siteUpcomingStreamReminders ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userSettings.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userSettingsEntity.createdDate ? (
              <TextFormat value={userSettingsEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userSettings.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userSettings.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userSettings.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userSettingsEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/user-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-settings/${userSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserSettingsDetail;
