import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './moderation-action.reducer';

export const ModerationActionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const moderationActionEntity = useAppSelector(state => state.admin.moderationAction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="moderationActionDetailsHeading">
          <Translate contentKey="adminApp.moderationAction.detail.title">ModerationAction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{moderationActionEntity.id}</dd>
          <dt>
            <span id="actionType">
              <Translate contentKey="adminApp.moderationAction.actionType">Action Type</Translate>
            </span>
          </dt>
          <dd>{moderationActionEntity.actionType}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="adminApp.moderationAction.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{moderationActionEntity.reason}</dd>
          <dt>
            <span id="actionDate">
              <Translate contentKey="adminApp.moderationAction.actionDate">Action Date</Translate>
            </span>
          </dt>
          <dd>
            {moderationActionEntity.actionDate ? (
              <TextFormat value={moderationActionEntity.actionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="durationDays">
              <Translate contentKey="adminApp.moderationAction.durationDays">Duration Days</Translate>
            </span>
          </dt>
          <dd>
            {moderationActionEntity.durationDays ? <DurationFormat value={moderationActionEntity.durationDays} /> : null} (
            {moderationActionEntity.durationDays})
          </dd>
        </dl>
        <Button tag={Link} to="/moderation-action" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/moderation-action/${moderationActionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ModerationActionDetail;
