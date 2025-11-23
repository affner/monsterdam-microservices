import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { getEntities as getPaymentTransactions } from 'app/entities/payment-transaction/payment-transaction.reducer';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { getEntities as getWalletTransactions } from 'app/entities/wallet-transaction/wallet-transaction.reducer';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { getEntities as getCreatorEarnings } from 'app/entities/creator-earning/creator-earning.reducer';
import { ISubscriptionBundle } from 'app/shared/model/subscription-bundle.model';
import { getEntities as getSubscriptionBundles } from 'app/entities/subscription-bundle/subscription-bundle.reducer';
import { IOfferPromotion } from 'app/shared/model/offer-promotion.model';
import { getEntities as getOfferPromotions } from 'app/entities/offer-promotion/offer-promotion.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { PurchasedSubscriptionStatus } from 'app/shared/model/enumerations/purchased-subscription-status.model';
import { getEntity, updateEntity, createEntity, reset } from './purchased-subscription.reducer';

export const PurchasedSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentTransactions = useAppSelector(state => state.admin.paymentTransaction.entities);
  const walletTransactions = useAppSelector(state => state.admin.walletTransaction.entities);
  const creatorEarnings = useAppSelector(state => state.admin.creatorEarning.entities);
  const subscriptionBundles = useAppSelector(state => state.admin.subscriptionBundle.entities);
  const offerPromotions = useAppSelector(state => state.admin.offerPromotion.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const purchasedSubscriptionEntity = useAppSelector(state => state.admin.purchasedSubscription.entity);
  const loading = useAppSelector(state => state.admin.purchasedSubscription.loading);
  const updating = useAppSelector(state => state.admin.purchasedSubscription.updating);
  const updateSuccess = useAppSelector(state => state.admin.purchasedSubscription.updateSuccess);
  const purchasedSubscriptionStatusValues = Object.keys(PurchasedSubscriptionStatus);

  const handleClose = () => {
    navigate('/purchased-subscription' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPaymentTransactions({}));
    dispatch(getWalletTransactions({}));
    dispatch(getCreatorEarnings({}));
    dispatch(getSubscriptionBundles({}));
    dispatch(getOfferPromotions({}));
    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.viewerId !== undefined && typeof values.viewerId !== 'number') {
      values.viewerId = Number(values.viewerId);
    }
    if (values.creatorId !== undefined && typeof values.creatorId !== 'number') {
      values.creatorId = Number(values.creatorId);
    }

    const entity = {
      ...purchasedSubscriptionEntity,
      ...values,
      payment: paymentTransactions.find(it => it.id.toString() === values.payment.toString()),
      walletTransaction: walletTransactions.find(it => it.id.toString() === values.walletTransaction.toString()),
      creatorEarning: creatorEarnings.find(it => it.id.toString() === values.creatorEarning.toString()),
      subscriptionBundle: subscriptionBundles.find(it => it.id.toString() === values.subscriptionBundle.toString()),
      appliedPromotion: offerPromotions.find(it => it.id.toString() === values.appliedPromotion.toString()),
      viewer: userProfiles.find(it => it.id.toString() === values.viewer.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          subscriptionStatus: 'PURCHASED',
          ...purchasedSubscriptionEntity,
          createdDate: convertDateTimeFromServer(purchasedSubscriptionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(purchasedSubscriptionEntity.lastModifiedDate),
          payment: purchasedSubscriptionEntity?.payment?.id,
          walletTransaction: purchasedSubscriptionEntity?.walletTransaction?.id,
          creatorEarning: purchasedSubscriptionEntity?.creatorEarning?.id,
          subscriptionBundle: purchasedSubscriptionEntity?.subscriptionBundle?.id,
          appliedPromotion: purchasedSubscriptionEntity?.appliedPromotion?.id,
          viewer: purchasedSubscriptionEntity?.viewer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.purchasedSubscription.home.createOrEditLabel" data-cy="PurchasedSubscriptionCreateUpdateHeading">
            <Translate contentKey="adminApp.purchasedSubscription.home.createOrEditLabel">Create or edit a PurchasedSubscription</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="purchased-subscription-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.createdDate')}
                id="purchased-subscription-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.lastModifiedDate')}
                id="purchased-subscription-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.createdBy')}
                id="purchased-subscription-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.lastModifiedBy')}
                id="purchased-subscription-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.isDeleted')}
                id="purchased-subscription-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.endDate')}
                id="purchased-subscription-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.subscriptionStatus')}
                id="purchased-subscription-subscriptionStatus"
                name="subscriptionStatus"
                data-cy="subscriptionStatus"
                type="select"
              >
                {purchasedSubscriptionStatusValues.map(purchasedSubscriptionStatus => (
                  <option value={purchasedSubscriptionStatus} key={purchasedSubscriptionStatus}>
                    {translate('adminApp.PurchasedSubscriptionStatus.' + purchasedSubscriptionStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.viewerId')}
                id="purchased-subscription-viewerId"
                name="viewerId"
                data-cy="viewerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedSubscription.creatorId')}
                id="purchased-subscription-creatorId"
                name="creatorId"
                data-cy="creatorId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="purchased-subscription-payment"
                name="payment"
                data-cy="payment"
                label={translate('adminApp.purchasedSubscription.payment')}
                type="select"
              >
                <option value="" key="0" />
                {paymentTransactions
                  ? paymentTransactions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.amount}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="purchased-subscription-walletTransaction"
                name="walletTransaction"
                data-cy="walletTransaction"
                label={translate('adminApp.purchasedSubscription.walletTransaction')}
                type="select"
              >
                <option value="" key="0" />
                {walletTransactions
                  ? walletTransactions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.amount}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="purchased-subscription-creatorEarning"
                name="creatorEarning"
                data-cy="creatorEarning"
                label={translate('adminApp.purchasedSubscription.creatorEarning')}
                type="select"
                required
              >
                <option value="" key="0" />
                {creatorEarnings
                  ? creatorEarnings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.amount}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="purchased-subscription-subscriptionBundle"
                name="subscriptionBundle"
                data-cy="subscriptionBundle"
                label={translate('adminApp.purchasedSubscription.subscriptionBundle')}
                type="select"
                required
              >
                <option value="" key="0" />
                {subscriptionBundles
                  ? subscriptionBundles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="purchased-subscription-appliedPromotion"
                name="appliedPromotion"
                data-cy="appliedPromotion"
                label={translate('adminApp.purchasedSubscription.appliedPromotion')}
                type="select"
              >
                <option value="" key="0" />
                {offerPromotions
                  ? offerPromotions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.promotionType}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="purchased-subscription-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('adminApp.purchasedSubscription.viewer')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchased-subscription" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PurchasedSubscriptionUpdate;
