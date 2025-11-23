import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tax-info.reducer';

export const TaxInfoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const taxInfoEntity = useAppSelector(state => state.admin.taxInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taxInfoDetailsHeading">
          <Translate contentKey="adminApp.taxInfo.detail.title">TaxInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.id}</dd>
          <dt>
            <span id="ratePercentage">
              <Translate contentKey="adminApp.taxInfo.ratePercentage">Rate Percentage</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.ratePercentage}</dd>
          <dt>
            <span id="taxType">
              <Translate contentKey="adminApp.taxInfo.taxType">Tax Type</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.taxType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.taxInfo.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {taxInfoEntity.createdDate ? <TextFormat value={taxInfoEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.taxInfo.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {taxInfoEntity.lastModifiedDate ? (
              <TextFormat value={taxInfoEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.taxInfo.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.taxInfo.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.taxInfo.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{taxInfoEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.taxInfo.country">Country</Translate>
          </dt>
          <dd>{taxInfoEntity.country ? taxInfoEntity.country.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/tax-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tax-info/${taxInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaxInfoDetail;
