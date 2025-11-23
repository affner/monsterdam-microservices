import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-provider.reducer';

export const PaymentProviderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentProviderEntity = useAppSelector(state => state.admin.paymentProvider.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentProviderDetailsHeading">
          <Translate contentKey="adminApp.paymentProvider.detail.title">PaymentProvider</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.id}</dd>
          <dt>
            <span id="providerName">
              <Translate contentKey="adminApp.paymentProvider.providerName">Provider Name</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.providerName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.paymentProvider.description">Description</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.description}</dd>
          <dt>
            <span id="apiKeyText">
              <Translate contentKey="adminApp.paymentProvider.apiKeyText">Api Key Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.apiKeyText}</dd>
          <dt>
            <span id="apiSecretText">
              <Translate contentKey="adminApp.paymentProvider.apiSecretText">Api Secret Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.apiSecretText}</dd>
          <dt>
            <span id="endpointText">
              <Translate contentKey="adminApp.paymentProvider.endpointText">Endpoint Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.endpointText}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.paymentProvider.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEntity.createdDate ? (
              <TextFormat value={paymentProviderEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.paymentProvider.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEntity.lastModifiedDate ? (
              <TextFormat value={paymentProviderEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.paymentProvider.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.paymentProvider.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.paymentProvider.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/payment-provider" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-provider/${paymentProviderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentProviderDetail;
