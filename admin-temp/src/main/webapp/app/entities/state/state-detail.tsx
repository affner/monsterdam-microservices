import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './state.reducer';

export const StateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stateEntity = useAppSelector(state => state.admin.state.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stateDetailsHeading">
          <Translate contentKey="adminApp.state.detail.title">State</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stateEntity.id}</dd>
          <dt>
            <span id="stateName">
              <Translate contentKey="adminApp.state.stateName">State Name</Translate>
            </span>
          </dt>
          <dd>{stateEntity.stateName}</dd>
          <dt>
            <span id="isoCode">
              <Translate contentKey="adminApp.state.isoCode">Iso Code</Translate>
            </span>
          </dt>
          <dd>{stateEntity.isoCode}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.state.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{stateEntity.createdDate ? <TextFormat value={stateEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.state.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {stateEntity.lastModifiedDate ? <TextFormat value={stateEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.state.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{stateEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.state.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{stateEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.state.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{stateEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.state.country">Country</Translate>
          </dt>
          <dd>{stateEntity.country ? stateEntity.country.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/state" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/state/${stateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StateDetail;
