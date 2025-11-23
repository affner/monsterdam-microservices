import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './asset.reducer';

export const AssetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assetEntity = useAppSelector(state => state.admin.asset.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assetDetailsHeading">
          <Translate contentKey="adminApp.asset.detail.title">Asset</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assetEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.asset.name">Name</Translate>
            </span>
          </dt>
          <dd>{assetEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="adminApp.asset.value">Value</Translate>
            </span>
          </dt>
          <dd>{assetEntity.value}</dd>
          <dt>
            <span id="acquisitionDate">
              <Translate contentKey="adminApp.asset.acquisitionDate">Acquisition Date</Translate>
            </span>
          </dt>
          <dd>
            {assetEntity.acquisitionDate ? (
              <TextFormat value={assetEntity.acquisitionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="adminApp.asset.type">Type</Translate>
            </span>
          </dt>
          <dd>{assetEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/asset" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asset/${assetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssetDetail;
