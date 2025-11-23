import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wallet-transaction.reducer';

export const WalletTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const walletTransactionEntity = useAppSelector(state => state.admin.walletTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="walletTransactionDetailsHeading">
          <Translate contentKey="adminApp.walletTransaction.detail.title">WalletTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.walletTransaction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.amount}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.walletTransaction.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {walletTransactionEntity.lastModifiedDate ? (
              <TextFormat value={walletTransactionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="transactionType">
              <Translate contentKey="adminApp.walletTransaction.transactionType">Transaction Type</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.transactionType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.walletTransaction.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {walletTransactionEntity.createdDate ? (
              <TextFormat value={walletTransactionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.walletTransaction.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.walletTransaction.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.walletTransaction.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.walletTransaction.payment">Payment</Translate>
          </dt>
          <dd>{walletTransactionEntity.payment ? walletTransactionEntity.payment.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.walletTransaction.viewer">Viewer</Translate>
          </dt>
          <dd>{walletTransactionEntity.viewer ? walletTransactionEntity.viewer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/wallet-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wallet-transaction/${walletTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WalletTransactionDetail;
