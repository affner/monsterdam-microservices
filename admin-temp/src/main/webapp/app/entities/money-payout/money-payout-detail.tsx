import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './money-payout.reducer';

export const MoneyPayoutDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const moneyPayoutEntity = useAppSelector(state => state.admin.moneyPayout.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="moneyPayoutDetailsHeading">
          <Translate contentKey="adminApp.moneyPayout.detail.title">MoneyPayout</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.moneyPayout.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.amount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.moneyPayout.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyPayoutEntity.createdDate ? (
              <TextFormat value={moneyPayoutEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.moneyPayout.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyPayoutEntity.lastModifiedDate ? (
              <TextFormat value={moneyPayoutEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.moneyPayout.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.moneyPayout.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.moneyPayout.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="withdrawStatus">
              <Translate contentKey="adminApp.moneyPayout.withdrawStatus">Withdraw Status</Translate>
            </span>
          </dt>
          <dd>{moneyPayoutEntity.withdrawStatus}</dd>
          <dt>
            <Translate contentKey="adminApp.moneyPayout.creatorEarning">Creator Earning</Translate>
          </dt>
          <dd>{moneyPayoutEntity.creatorEarning ? moneyPayoutEntity.creatorEarning.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.moneyPayout.creator">Creator</Translate>
          </dt>
          <dd>{moneyPayoutEntity.creator ? moneyPayoutEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/money-payout" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/money-payout/${moneyPayoutEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MoneyPayoutDetail;
