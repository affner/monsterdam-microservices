import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-mention.reducer';

export const UserMentionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userMentionEntity = useAppSelector(state => state.admin.userMention.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userMentionDetailsHeading">
          <Translate contentKey="adminApp.userMention.detail.title">UserMention</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userMentionEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userMention.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userMentionEntity.createdDate ? (
              <TextFormat value={userMentionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userMention.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userMentionEntity.lastModifiedDate ? (
              <TextFormat value={userMentionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userMention.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userMentionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userMention.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userMentionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userMention.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userMentionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.userMention.originPost">Origin Post</Translate>
          </dt>
          <dd>{userMentionEntity.originPost ? userMentionEntity.originPost.postContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userMention.originPostComment">Origin Post Comment</Translate>
          </dt>
          <dd>{userMentionEntity.originPostComment ? userMentionEntity.originPostComment.commentContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userMention.mentionedUser">Mentioned User</Translate>
          </dt>
          <dd>{userMentionEntity.mentionedUser ? userMentionEntity.mentionedUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-mention" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-mention/${userMentionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserMentionDetail;
