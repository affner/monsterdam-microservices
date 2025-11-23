import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-event.reducer';

export const UserEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userEventEntity = useAppSelector(state => state.admin.userEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userEventDetailsHeading">
          <Translate contentKey="adminApp.userEvent.detail.title">UserEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.userEvent.title">Title</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.userEvent.description">Description</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.description}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="adminApp.userEvent.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {userEventEntity.startDate ? <TextFormat value={userEventEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="adminApp.userEvent.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {userEventEntity.endDate ? <TextFormat value={userEventEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="creatorEventStatus">
              <Translate contentKey="adminApp.userEvent.creatorEventStatus">Creator Event Status</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.creatorEventStatus}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userEvent.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userEventEntity.createdDate ? <TextFormat value={userEventEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userEvent.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userEventEntity.lastModifiedDate ? (
              <TextFormat value={userEventEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userEvent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userEvent.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userEvent.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userEventEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.userEvent.creator">Creator</Translate>
          </dt>
          <dd>{userEventEntity.creator ? userEventEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-event/${userEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserEventDetail;
