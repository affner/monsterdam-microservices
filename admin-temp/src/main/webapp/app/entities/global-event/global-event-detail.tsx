import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './global-event.reducer';

export const GlobalEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const globalEventEntity = useAppSelector(state => state.admin.globalEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="globalEventDetailsHeading">
          <Translate contentKey="adminApp.globalEvent.detail.title">GlobalEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.id}</dd>
          <dt>
            <span id="eventName">
              <Translate contentKey="adminApp.globalEvent.eventName">Event Name</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.eventName}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="adminApp.globalEvent.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {globalEventEntity.startDate ? (
              <TextFormat value={globalEventEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="adminApp.globalEvent.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {globalEventEntity.endDate ? <TextFormat value={globalEventEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.globalEvent.description">Description</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.globalEvent.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {globalEventEntity.createdDate ? (
              <TextFormat value={globalEventEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.globalEvent.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {globalEventEntity.lastModifiedDate ? (
              <TextFormat value={globalEventEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.globalEvent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.globalEvent.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.globalEvent.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{globalEventEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/global-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/global-event/${globalEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GlobalEventDetail;
