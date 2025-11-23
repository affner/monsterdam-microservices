import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification.reducer';

export const NotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificationEntity = useAppSelector(state => state.admin.notification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationDetailsHeading">
          <Translate contentKey="adminApp.notification.detail.title">Notification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.id}</dd>
          <dt>
            <span id="readDate">
              <Translate contentKey="adminApp.notification.readDate">Read Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.readDate ? <TextFormat value={notificationEntity.readDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.notification.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.createdDate ? (
              <TextFormat value={notificationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.notification.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.lastModifiedDate ? (
              <TextFormat value={notificationEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.notification.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.notification.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.notification.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="postCommentId">
              <Translate contentKey="adminApp.notification.postCommentId">Post Comment Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.postCommentId}</dd>
          <dt>
            <span id="postFeedId">
              <Translate contentKey="adminApp.notification.postFeedId">Post Feed Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.postFeedId}</dd>
          <dt>
            <span id="directMessageId">
              <Translate contentKey="adminApp.notification.directMessageId">Direct Message Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.directMessageId}</dd>
          <dt>
            <span id="userMentionId">
              <Translate contentKey="adminApp.notification.userMentionId">User Mention Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.userMentionId}</dd>
          <dt>
            <span id="likeMarkId">
              <Translate contentKey="adminApp.notification.likeMarkId">Like Mark Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.likeMarkId}</dd>
          <dt>
            <Translate contentKey="adminApp.notification.commentedUser">Commented User</Translate>
          </dt>
          <dd>{notificationEntity.commentedUser ? notificationEntity.commentedUser.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.notification.messagedUser">Messaged User</Translate>
          </dt>
          <dd>{notificationEntity.messagedUser ? notificationEntity.messagedUser.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.notification.mentionerUserInPost">Mentioner User In Post</Translate>
          </dt>
          <dd>{notificationEntity.mentionerUserInPost ? notificationEntity.mentionerUserInPost.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.notification.mentionerUserInComment">Mentioner User In Comment</Translate>
          </dt>
          <dd>{notificationEntity.mentionerUserInComment ? notificationEntity.mentionerUserInComment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationDetail;
