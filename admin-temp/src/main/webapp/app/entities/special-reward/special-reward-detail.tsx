import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './special-reward.reducer';

export const SpecialRewardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialRewardEntity = useAppSelector(state => state.admin.specialReward.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialRewardDetailsHeading">
          <Translate contentKey="adminApp.specialReward.detail.title">SpecialReward</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.specialReward.description">Description</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.specialReward.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {specialRewardEntity.createdDate ? (
              <TextFormat value={specialRewardEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.specialReward.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {specialRewardEntity.lastModifiedDate ? (
              <TextFormat value={specialRewardEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.specialReward.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.specialReward.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.specialReward.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="contentPackageId">
              <Translate contentKey="adminApp.specialReward.contentPackageId">Content Package Id</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.contentPackageId}</dd>
          <dt>
            <span id="viewerId">
              <Translate contentKey="adminApp.specialReward.viewerId">Viewer Id</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.viewerId}</dd>
          <dt>
            <span id="offerPromotionId">
              <Translate contentKey="adminApp.specialReward.offerPromotionId">Offer Promotion Id</Translate>
            </span>
          </dt>
          <dd>{specialRewardEntity.offerPromotionId}</dd>
        </dl>
        <Button tag={Link} to="/special-reward" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/special-reward/${specialRewardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialRewardDetail;
