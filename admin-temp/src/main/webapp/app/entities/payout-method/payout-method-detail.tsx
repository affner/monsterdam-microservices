import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payout-method.reducer';

export const PayoutMethodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const payoutMethodEntity = useAppSelector(state => state.admin.payoutMethod.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="payoutMethodDetailsHeading">
          <Translate contentKey="adminApp.payoutMethod.detail.title">PayoutMethod</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.id}</dd>
          <dt>
            <span id="methodName">
              <Translate contentKey="adminApp.payoutMethod.methodName">Method Name</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.methodName}</dd>
          <dt>
            <span id="tokenText">
              <Translate contentKey="adminApp.payoutMethod.tokenText">Token Text</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.tokenText}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="adminApp.payoutMethod.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {payoutMethodEntity.expirationDate ? (
              <TextFormat value={payoutMethodEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.payoutMethod.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {payoutMethodEntity.createdDate ? (
              <TextFormat value={payoutMethodEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.payoutMethod.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {payoutMethodEntity.lastModifiedDate ? (
              <TextFormat value={payoutMethodEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.payoutMethod.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.payoutMethod.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.payoutMethod.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{payoutMethodEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/payout-method" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payout-method/${payoutMethodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PayoutMethodDetail;
