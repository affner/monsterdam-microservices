import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './accounting-record.reducer';

export const AccountingRecordDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const accountingRecordEntity = useAppSelector(state => state.admin.accountingRecord.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountingRecordDetailsHeading">
          <Translate contentKey="adminApp.accountingRecord.detail.title">AccountingRecord</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="adminApp.accountingRecord.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {accountingRecordEntity.date ? <TextFormat value={accountingRecordEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.accountingRecord.description">Description</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.description}</dd>
          <dt>
            <span id="debit">
              <Translate contentKey="adminApp.accountingRecord.debit">Debit</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.debit}</dd>
          <dt>
            <span id="credit">
              <Translate contentKey="adminApp.accountingRecord.credit">Credit</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.credit}</dd>
          <dt>
            <span id="balance">
              <Translate contentKey="adminApp.accountingRecord.balance">Balance</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.balance}</dd>
          <dt>
            <span id="accountType">
              <Translate contentKey="adminApp.accountingRecord.accountType">Account Type</Translate>
            </span>
          </dt>
          <dd>{accountingRecordEntity.accountType}</dd>
          <dt>
            <Translate contentKey="adminApp.accountingRecord.payment">Payment</Translate>
          </dt>
          <dd>{accountingRecordEntity.payment ? accountingRecordEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.accountingRecord.budget">Budget</Translate>
          </dt>
          <dd>{accountingRecordEntity.budget ? accountingRecordEntity.budget.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.accountingRecord.asset">Asset</Translate>
          </dt>
          <dd>{accountingRecordEntity.asset ? accountingRecordEntity.asset.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.accountingRecord.liability">Liability</Translate>
          </dt>
          <dd>{accountingRecordEntity.liability ? accountingRecordEntity.liability.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/accounting-record" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/accounting-record/${accountingRecordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AccountingRecordDetail;
