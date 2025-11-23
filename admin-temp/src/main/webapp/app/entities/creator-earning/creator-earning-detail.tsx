import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './creator-earning.reducer';

export const CreatorEarningDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const creatorEarningEntity = useAppSelector(state => state.admin.creatorEarning.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="creatorEarningDetailsHeading">
          <Translate contentKey="adminApp.creatorEarning.detail.title">CreatorEarning</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{creatorEarningEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.creatorEarning.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{creatorEarningEntity.amount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.creatorEarning.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {creatorEarningEntity.createdDate ? (
              <TextFormat value={creatorEarningEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.creatorEarning.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {creatorEarningEntity.lastModifiedDate ? (
              <TextFormat value={creatorEarningEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.creatorEarning.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{creatorEarningEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.creatorEarning.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{creatorEarningEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.creatorEarning.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{creatorEarningEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.creatorEarning.creator">Creator</Translate>
          </dt>
          <dd>{creatorEarningEntity.creator ? creatorEarningEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/creator-earning" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/creator-earning/${creatorEarningEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CreatorEarningDetail;
