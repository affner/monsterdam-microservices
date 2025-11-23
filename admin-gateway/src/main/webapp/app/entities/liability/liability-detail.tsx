import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './liability.reducer';

export const LiabilityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const liabilityEntity = useAppSelector(state => state.admin.liability.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="liabilityDetailsHeading">
          <Translate contentKey="adminApp.liability.detail.title">Liability</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{liabilityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.liability.name">Name</Translate>
            </span>
          </dt>
          <dd>{liabilityEntity.name}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.liability.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{liabilityEntity.amount}</dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="adminApp.liability.dueDate">Due Date</Translate>
            </span>
          </dt>
          <dd>
            {liabilityEntity.dueDate ? <TextFormat value={liabilityEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="adminApp.liability.type">Type</Translate>
            </span>
          </dt>
          <dd>{liabilityEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/liability" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/liability/${liabilityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LiabilityDetail;
