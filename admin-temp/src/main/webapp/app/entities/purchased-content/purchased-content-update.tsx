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
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { getEntity, updateEntity, createEntity, reset } from './purchased-content.reducer';

export const PurchasedContentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentTransactions = useAppSelector(state => state.admin.paymentTransaction.entities);
  const walletTransactions = useAppSelector(state => state.admin.walletTransaction.entities);
  const creatorEarnings = useAppSelector(state => state.admin.creatorEarning.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const purchasedContentEntity = useAppSelector(state => state.admin.purchasedContent.entity);
  const loading = useAppSelector(state => state.admin.purchasedContent.loading);
  const updating = useAppSelector(state => state.admin.purchasedContent.updating);
  const updateSuccess = useAppSelector(state => state.admin.purchasedContent.updateSuccess);

  const handleClose = () => {
    navigate('/purchased-content' + location.search);
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
    dispatch(getUserProfiles({}));
    dispatch(getContentPackages({}));
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
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...purchasedContentEntity,
      ...values,
      payment: paymentTransactions.find(it => it.id.toString() === values.payment.toString()),
      walletTransaction: walletTransactions.find(it => it.id.toString() === values.walletTransaction.toString()),
      creatorEarning: creatorEarnings.find(it => it.id.toString() === values.creatorEarning.toString()),
      viewer: userProfiles.find(it => it.id.toString() === values.viewer.toString()),
      purchasedContentPackage: contentPackages.find(it => it.id.toString() === values.purchasedContentPackage.toString()),
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
          ...purchasedContentEntity,
          createdDate: convertDateTimeFromServer(purchasedContentEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(purchasedContentEntity.lastModifiedDate),
          payment: purchasedContentEntity?.payment?.id,
          walletTransaction: purchasedContentEntity?.walletTransaction?.id,
          creatorEarning: purchasedContentEntity?.creatorEarning?.id,
          viewer: purchasedContentEntity?.viewer?.id,
          purchasedContentPackage: purchasedContentEntity?.purchasedContentPackage?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.purchasedContent.home.createOrEditLabel" data-cy="PurchasedContentCreateUpdateHeading">
            <Translate contentKey="adminApp.purchasedContent.home.createOrEditLabel">Create or edit a PurchasedContent</Translate>
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
                  id="purchased-content-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.purchasedContent.rating')}
                id="purchased-content-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedContent.createdDate')}
                id="purchased-content-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedContent.lastModifiedDate')}
                id="purchased-content-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.purchasedContent.createdBy')}
                id="purchased-content-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedContent.lastModifiedBy')}
                id="purchased-content-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedContent.isDeleted')}
                id="purchased-content-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="purchased-content-payment"
                name="payment"
                data-cy="payment"
                label={translate('adminApp.purchasedContent.payment')}
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
                id="purchased-content-walletTransaction"
                name="walletTransaction"
                data-cy="walletTransaction"
                label={translate('adminApp.purchasedContent.walletTransaction')}
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
                id="purchased-content-creatorEarning"
                name="creatorEarning"
                data-cy="creatorEarning"
                label={translate('adminApp.purchasedContent.creatorEarning')}
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
                id="purchased-content-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('adminApp.purchasedContent.viewer')}
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
              <ValidatedField
                id="purchased-content-purchasedContentPackage"
                name="purchasedContentPackage"
                data-cy="purchasedContentPackage"
                label={translate('adminApp.purchasedContent.purchasedContentPackage')}
                type="select"
                required
              >
                <option value="" key="0" />
                {contentPackages
                  ? contentPackages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchased-content" replace color="info">
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

export default PurchasedContentUpdate;
