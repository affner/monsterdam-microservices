import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscription-bundle.reducer';

export const SubscriptionBundleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscriptionBundleEntity = useAppSelector(state => state.admin.subscriptionBundle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionBundleDetailsHeading">
          <Translate contentKey="adminApp.subscriptionBundle.detail.title">SubscriptionBundle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscriptionBundleEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.subscriptionBundle.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{subscriptionBundleEntity.amount}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="adminApp.subscriptionBundle.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionBundleEntity.duration ? <DurationFormat value={subscriptionBundleEntity.duration} /> : null} (
            {subscriptionBundleEntity.duration})
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.subscriptionBundle.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionBundleEntity.createdDate ? (
              <TextFormat value={subscriptionBundleEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.subscriptionBundle.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionBundleEntity.lastModifiedDate ? (
              <TextFormat value={subscriptionBundleEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.subscriptionBundle.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{subscriptionBundleEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.subscriptionBundle.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{subscriptionBundleEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.subscriptionBundle.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{subscriptionBundleEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.subscriptionBundle.creator">Creator</Translate>
          </dt>
          <dd>{subscriptionBundleEntity.creator ? subscriptionBundleEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscription-bundle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-bundle/${subscriptionBundleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscriptionBundleDetail;
