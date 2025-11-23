import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchased-content.reducer';

export const PurchasedContentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const purchasedContentEntity = useAppSelector(state => state.admin.purchasedContent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchasedContentDetailsHeading">
          <Translate contentKey="adminApp.purchasedContent.detail.title">PurchasedContent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.id}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="adminApp.purchasedContent.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.rating}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.purchasedContent.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedContentEntity.createdDate ? (
              <TextFormat value={purchasedContentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.purchasedContent.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedContentEntity.lastModifiedDate ? (
              <TextFormat value={purchasedContentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.purchasedContent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.purchasedContent.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.purchasedContent.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedContent.payment">Payment</Translate>
          </dt>
          <dd>{purchasedContentEntity.payment ? purchasedContentEntity.payment.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedContent.walletTransaction">Wallet Transaction</Translate>
          </dt>
          <dd>{purchasedContentEntity.walletTransaction ? purchasedContentEntity.walletTransaction.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedContent.creatorEarning">Creator Earning</Translate>
          </dt>
          <dd>{purchasedContentEntity.creatorEarning ? purchasedContentEntity.creatorEarning.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedContent.viewer">Viewer</Translate>
          </dt>
          <dd>{purchasedContentEntity.viewer ? purchasedContentEntity.viewer.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedContent.purchasedContentPackage">Purchased Content Package</Translate>
          </dt>
          <dd>{purchasedContentEntity.purchasedContentPackage ? purchasedContentEntity.purchasedContentPackage.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchased-content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchased-content/${purchasedContentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchasedContentDetail;
