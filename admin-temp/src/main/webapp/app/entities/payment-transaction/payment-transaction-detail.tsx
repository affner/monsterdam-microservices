import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-transaction.reducer';

export const PaymentTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentTransactionEntity = useAppSelector(state => state.admin.paymentTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentTransactionDetailsHeading">
          <Translate contentKey="adminApp.paymentTransaction.detail.title">PaymentTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentTransactionEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.paymentTransaction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentTransactionEntity.amount}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="adminApp.paymentTransaction.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentTransactionEntity.paymentDate ? (
              <TextFormat value={paymentTransactionEntity.paymentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="paymentStatus">
              <Translate contentKey="adminApp.paymentTransaction.paymentStatus">Payment Status</Translate>
            </span>
          </dt>
          <dd>{paymentTransactionEntity.paymentStatus}</dd>
          <dt>
            <span id="paymentReference">
              <Translate contentKey="adminApp.paymentTransaction.paymentReference">Payment Reference</Translate>
            </span>
          </dt>
          <dd>{paymentTransactionEntity.paymentReference}</dd>
          <dt>
            <span id="cloudTransactionId">
              <Translate contentKey="adminApp.paymentTransaction.cloudTransactionId">Cloud Transaction Id</Translate>
            </span>
          </dt>
          <dd>{paymentTransactionEntity.cloudTransactionId}</dd>
          <dt>
            <Translate contentKey="adminApp.paymentTransaction.paymentMethod">Payment Method</Translate>
          </dt>
          <dd>{paymentTransactionEntity.paymentMethod ? paymentTransactionEntity.paymentMethod.methodName : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.paymentTransaction.paymentProvider">Payment Provider</Translate>
          </dt>
          <dd>{paymentTransactionEntity.paymentProvider ? paymentTransactionEntity.paymentProvider.providerName : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.paymentTransaction.viewer">Viewer</Translate>
          </dt>
          <dd>{paymentTransactionEntity.viewer ? paymentTransactionEntity.viewer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-transaction/${paymentTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentTransactionDetail;
