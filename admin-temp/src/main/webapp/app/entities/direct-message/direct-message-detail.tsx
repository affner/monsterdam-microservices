import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './direct-message.reducer';

export const DirectMessageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const directMessageEntity = useAppSelector(state => state.admin.directMessage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="directMessageDetailsHeading">
          <Translate contentKey="adminApp.directMessage.detail.title">DirectMessage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.id}</dd>
          <dt>
            <span id="messageContent">
              <Translate contentKey="adminApp.directMessage.messageContent">Message Content</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.messageContent}</dd>
          <dt>
            <span id="readDate">
              <Translate contentKey="adminApp.directMessage.readDate">Read Date</Translate>
            </span>
          </dt>
          <dd>
            {directMessageEntity.readDate ? <TextFormat value={directMessageEntity.readDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.directMessage.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.likeCount}</dd>
          <dt>
            <span id="isHidden">
              <Translate contentKey="adminApp.directMessage.isHidden">Is Hidden</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.isHidden ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.directMessage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {directMessageEntity.createdDate ? (
              <TextFormat value={directMessageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.directMessage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {directMessageEntity.lastModifiedDate ? (
              <TextFormat value={directMessageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.directMessage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.directMessage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.directMessage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{directMessageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.directMessage.contentPackage">Content Package</Translate>
          </dt>
          <dd>{directMessageEntity.contentPackage ? directMessageEntity.contentPackage.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.directMessage.responseTo">Response To</Translate>
          </dt>
          <dd>{directMessageEntity.responseTo ? directMessageEntity.responseTo.messageContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.directMessage.repliedStory">Replied Story</Translate>
          </dt>
          <dd>{directMessageEntity.repliedStory ? directMessageEntity.repliedStory.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.directMessage.user">User</Translate>
          </dt>
          <dd>{directMessageEntity.user ? directMessageEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/direct-message" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/direct-message/${directMessageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DirectMessageDetail;
