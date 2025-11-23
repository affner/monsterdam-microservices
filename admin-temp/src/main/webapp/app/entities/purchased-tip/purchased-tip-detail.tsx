import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchased-tip.reducer';

export const PurchasedTipDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const purchasedTipEntity = useAppSelector(state => state.admin.purchasedTip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchasedTipDetailsHeading">
          <Translate contentKey="adminApp.purchasedTip.detail.title">PurchasedTip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchasedTipEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.purchasedTip.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{purchasedTipEntity.amount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.purchasedTip.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedTipEntity.createdDate ? (
              <TextFormat value={purchasedTipEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.purchasedTip.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedTipEntity.lastModifiedDate ? (
              <TextFormat value={purchasedTipEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.purchasedTip.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{purchasedTipEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.purchasedTip.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{purchasedTipEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.purchasedTip.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{purchasedTipEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedTip.payment">Payment</Translate>
          </dt>
          <dd>{purchasedTipEntity.payment ? purchasedTipEntity.payment.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedTip.walletTransaction">Wallet Transaction</Translate>
          </dt>
          <dd>{purchasedTipEntity.walletTransaction ? purchasedTipEntity.walletTransaction.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedTip.creatorEarning">Creator Earning</Translate>
          </dt>
          <dd>{purchasedTipEntity.creatorEarning ? purchasedTipEntity.creatorEarning.amount : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.purchasedTip.message">Message</Translate>
          </dt>
          <dd>{purchasedTipEntity.message ? purchasedTipEntity.message.messageContent : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchased-tip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchased-tip/${purchasedTipEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchasedTipDetail;
