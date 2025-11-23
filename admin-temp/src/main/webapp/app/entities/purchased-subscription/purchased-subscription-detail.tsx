import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchased-subscription.reducer';

export const PurchasedSubscriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const purchasedSubscriptionEntity = useAppSelector(state => state.admin.purchasedSubscription.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchasedSubscriptionDetailsHeading">
          <Translate contentKey="adminApp.purchasedSubscription.detail.title">PurchasedSubscription</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.purchasedSubscription.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedSubscriptionEntity.createdDate ? (
              <TextFormat value={purchasedSubscriptionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.purchasedSubscription.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedSubscriptionEntity.lastModifiedDate ? (
              <TextFormat value={purchasedSubscriptionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.purchasedSubscription.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.purchasedSubscription.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.purchasedSubscription.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="adminApp.purchasedSubscription.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedSubscriptionEntity.endDate ? (
              <TextFormat value={purchasedSubscriptionEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionStatus">
              <Translate contentKey="adminApp.purchasedSubscription.subscriptionStatus">Subscription Status</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.subscriptionStatus}</dd>
          <dt>
            <span id="viewerId">
              <Translate contentKey="adminApp.purchasedSubscription.viewerId">Viewer Id</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.viewerId}</dd>
          <dt>
            <span id="creatorId">
              <Translate contentKey="adminApp.purchasedSubscription.creatorId">Creator Id</Translate>
            </span>
          </dt>
          <dd>{purchasedSubscriptionEntity.creatorId}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.payment">Payment</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.payment ? purchasedSubscriptionEntity.payment.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.walletTransaction">Wallet Transaction</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.walletTransaction ? purchasedSubscriptionEntity.walletTransaction.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.creatorEarning">Creator Earning</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.creatorEarning ? purchasedSubscriptionEntity.creatorEarning.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.subscriptionBundle">Subscription Bundle</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.subscriptionBundle ? purchasedSubscriptionEntity.subscriptionBundle.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.appliedPromotion">Applied Promotion</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.appliedPromotion ? purchasedSubscriptionEntity.appliedPromotion.promotionType : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedSubscription.viewer">Viewer</Translate>
          </dt>
          <dd>{purchasedSubscriptionEntity.viewer ? purchasedSubscriptionEntity.viewer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchased-subscription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchased-subscription/${purchasedSubscriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchasedSubscriptionDetail;
