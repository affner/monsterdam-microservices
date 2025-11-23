import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './special-award.reducer';

export const SpecialAwardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialAwardEntity = useAppSelector(state => state.admin.specialAward.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialAwardDetailsHeading">
          <Translate contentKey="adminApp.specialAward.detail.title">SpecialAward</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="adminApp.specialAward.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {specialAwardEntity.startDate ? (
              <TextFormat value={specialAwardEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="adminApp.specialAward.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {specialAwardEntity.endDate ? (
              <TextFormat value={specialAwardEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="reason">
              <Translate contentKey="adminApp.specialAward.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.reason}</dd>
          <dt>
            <span id="altSpecialTitle">
              <Translate contentKey="adminApp.specialAward.altSpecialTitle">Alt Special Title</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.altSpecialTitle}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.specialAward.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {specialAwardEntity.createdDate ? (
              <TextFormat value={specialAwardEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.specialAward.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {specialAwardEntity.lastModifiedDate ? (
              <TextFormat value={specialAwardEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.specialAward.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.specialAward.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.specialAward.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="viewerId">
              <Translate contentKey="adminApp.specialAward.viewerId">Viewer Id</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.viewerId}</dd>
          <dt>
            <span id="creatorId">
              <Translate contentKey="adminApp.specialAward.creatorId">Creator Id</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.creatorId}</dd>
          <dt>
            <span id="specialTitleId">
              <Translate contentKey="adminApp.specialAward.specialTitleId">Special Title Id</Translate>
            </span>
          </dt>
          <dd>{specialAwardEntity.specialTitleId}</dd>
        </dl>
        <Button tag={Link} to="/special-award" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/special-award/${specialAwardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialAwardDetail;
