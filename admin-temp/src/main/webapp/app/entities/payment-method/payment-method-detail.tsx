import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-method.reducer';

export const PaymentMethodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentMethodEntity = useAppSelector(state => state.admin.paymentMethod.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentMethodDetailsHeading">
          <Translate contentKey="adminApp.paymentMethod.detail.title">PaymentMethod</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.id}</dd>
          <dt>
            <span id="methodName">
              <Translate contentKey="adminApp.paymentMethod.methodName">Method Name</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.methodName}</dd>
          <dt>
            <span id="tokenText">
              <Translate contentKey="adminApp.paymentMethod.tokenText">Token Text</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.tokenText}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="adminApp.paymentMethod.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentMethodEntity.expirationDate ? (
              <TextFormat value={paymentMethodEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.paymentMethod.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentMethodEntity.createdDate ? (
              <TextFormat value={paymentMethodEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.paymentMethod.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentMethodEntity.lastModifiedDate ? (
              <TextFormat value={paymentMethodEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.paymentMethod.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.paymentMethod.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.paymentMethod.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/payment-method" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-method/${paymentMethodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentMethodDetail;
