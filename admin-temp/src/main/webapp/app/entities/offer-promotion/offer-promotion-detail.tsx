import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './offer-promotion.reducer';

export const OfferPromotionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const offerPromotionEntity = useAppSelector(state => state.admin.offerPromotion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="offerPromotionDetailsHeading">
          <Translate contentKey="adminApp.offerPromotion.detail.title">OfferPromotion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.id}</dd>
          <dt>
            <span id="freeDaysDuration">
              <Translate contentKey="adminApp.offerPromotion.freeDaysDuration">Free Days Duration</Translate>
            </span>
          </dt>
          <dd>
            {offerPromotionEntity.freeDaysDuration ? <DurationFormat value={offerPromotionEntity.freeDaysDuration} /> : null} (
            {offerPromotionEntity.freeDaysDuration})
          </dd>
          <dt>
            <span id="discountPercentage">
              <Translate contentKey="adminApp.offerPromotion.discountPercentage">Discount Percentage</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.discountPercentage}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="adminApp.offerPromotion.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {offerPromotionEntity.startDate ? (
              <TextFormat value={offerPromotionEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="adminApp.offerPromotion.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {offerPromotionEntity.endDate ? (
              <TextFormat value={offerPromotionEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionsLimit">
              <Translate contentKey="adminApp.offerPromotion.subscriptionsLimit">Subscriptions Limit</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.subscriptionsLimit}</dd>
          <dt>
            <span id="linkCode">
              <Translate contentKey="adminApp.offerPromotion.linkCode">Link Code</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.linkCode}</dd>
          <dt>
            <span id="isFinished">
              <Translate contentKey="adminApp.offerPromotion.isFinished">Is Finished</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.isFinished ? 'true' : 'false'}</dd>
          <dt>
            <span id="promotionType">
              <Translate contentKey="adminApp.offerPromotion.promotionType">Promotion Type</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.promotionType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.offerPromotion.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {offerPromotionEntity.createdDate ? (
              <TextFormat value={offerPromotionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.offerPromotion.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {offerPromotionEntity.lastModifiedDate ? (
              <TextFormat value={offerPromotionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.offerPromotion.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.offerPromotion.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.offerPromotion.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{offerPromotionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.offerPromotion.creator">Creator</Translate>
          </dt>
          <dd>{offerPromotionEntity.creator ? offerPromotionEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/offer-promotion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/offer-promotion/${offerPromotionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OfferPromotionDetail;
