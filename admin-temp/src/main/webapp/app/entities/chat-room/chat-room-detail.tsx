import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-room.reducer';

export const ChatRoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatRoomEntity = useAppSelector(state => state.admin.chatRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatRoomDetailsHeading">
          <Translate contentKey="adminApp.chatRoom.detail.title">ChatRoom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.id}</dd>
          <dt>
            <span id="lastAction">
              <Translate contentKey="adminApp.chatRoom.lastAction">Last Action</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.lastAction}</dd>
          <dt>
            <span id="lastConnectionDate">
              <Translate contentKey="adminApp.chatRoom.lastConnectionDate">Last Connection Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.lastConnectionDate ? (
              <TextFormat value={chatRoomEntity.lastConnectionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="muted">
              <Translate contentKey="adminApp.chatRoom.muted">Muted</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.muted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.chatRoom.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.createdDate ? <TextFormat value={chatRoomEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.chatRoom.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.lastModifiedDate ? (
              <TextFormat value={chatRoomEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.chatRoom.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.chatRoom.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.chatRoom.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.chatRoom.sentMessages">Sent Messages</Translate>
          </dt>
          <dd>
            {chatRoomEntity.sentMessages
              ? chatRoomEntity.sentMessages.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.messageContent}</a>
                    {chatRoomEntity.sentMessages && i === chatRoomEntity.sentMessages.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.chatRoom.user">User</Translate>
          </dt>
          <dd>{chatRoomEntity.user ? chatRoomEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/chat-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-room/${chatRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatRoomDetail;
