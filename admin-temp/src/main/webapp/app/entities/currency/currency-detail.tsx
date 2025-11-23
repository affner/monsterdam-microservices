import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './currency.reducer';

export const CurrencyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const currencyEntity = useAppSelector(state => state.admin.currency.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="currencyDetailsHeading">
          <Translate contentKey="adminApp.currency.detail.title">Currency</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.currency.name">Name</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.name}</dd>
          <dt>
            <span id="symbol">
              <Translate contentKey="adminApp.currency.symbol">Symbol</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.symbol}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="adminApp.currency.code">Code</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.code}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.currency.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {currencyEntity.createdDate ? <TextFormat value={currencyEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.currency.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {currencyEntity.lastModifiedDate ? (
              <TextFormat value={currencyEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.currency.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.currency.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.currency.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{currencyEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/currency" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/currency/${currencyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CurrencyDetail;
