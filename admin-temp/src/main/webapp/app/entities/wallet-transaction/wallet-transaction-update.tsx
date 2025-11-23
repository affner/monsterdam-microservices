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
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { getEntities as getPurchasedContents } from 'app/entities/purchased-content/purchased-content.reducer';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { getEntities as getPurchasedSubscriptions } from 'app/entities/purchased-subscription/purchased-subscription.reducer';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { getEntities as getPurchasedTips } from 'app/entities/purchased-tip/purchased-tip.reducer';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { WalletTransactionType } from 'app/shared/model/enumerations/wallet-transaction-type.model';
import { getEntity, updateEntity, createEntity, reset } from './wallet-transaction.reducer';

export const WalletTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentTransactions = useAppSelector(state => state.admin.paymentTransaction.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const purchasedContents = useAppSelector(state => state.admin.purchasedContent.entities);
  const purchasedSubscriptions = useAppSelector(state => state.admin.purchasedSubscription.entities);
  const purchasedTips = useAppSelector(state => state.admin.purchasedTip.entities);
  const walletTransactionEntity = useAppSelector(state => state.admin.walletTransaction.entity);
  const loading = useAppSelector(state => state.admin.walletTransaction.loading);
  const updating = useAppSelector(state => state.admin.walletTransaction.updating);
  const updateSuccess = useAppSelector(state => state.admin.walletTransaction.updateSuccess);
  const walletTransactionTypeValues = Object.keys(WalletTransactionType);

  const handleClose = () => {
    navigate('/wallet-transaction' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPaymentTransactions({}));
    dispatch(getUserProfiles({}));
    dispatch(getPurchasedContents({}));
    dispatch(getPurchasedSubscriptions({}));
    dispatch(getPurchasedTips({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);

    const entity = {
      ...walletTransactionEntity,
      ...values,
      payment: paymentTransactions.find(it => it.id.toString() === values.payment.toString()),
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
          lastModifiedDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
        }
      : {
          transactionType: 'TOP_UP',
          ...walletTransactionEntity,
          lastModifiedDate: convertDateTimeFromServer(walletTransactionEntity.lastModifiedDate),
          createdDate: convertDateTimeFromServer(walletTransactionEntity.createdDate),
          payment: walletTransactionEntity?.payment?.id,
          viewer: walletTransactionEntity?.viewer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.walletTransaction.home.createOrEditLabel" data-cy="WalletTransactionCreateUpdateHeading">
            <Translate contentKey="adminApp.walletTransaction.home.createOrEditLabel">Create or edit a WalletTransaction</Translate>
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
                  id="wallet-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.walletTransaction.amount')}
                id="wallet-transaction-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.walletTransaction.lastModifiedDate')}
                id="wallet-transaction-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.walletTransaction.transactionType')}
                id="wallet-transaction-transactionType"
                name="transactionType"
                data-cy="transactionType"
                type="select"
              >
                {walletTransactionTypeValues.map(walletTransactionType => (
                  <option value={walletTransactionType} key={walletTransactionType}>
                    {translate('adminApp.WalletTransactionType.' + walletTransactionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.walletTransaction.createdDate')}
                id="wallet-transaction-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.walletTransaction.createdBy')}
                id="wallet-transaction-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.walletTransaction.lastModifiedBy')}
                id="wallet-transaction-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.walletTransaction.isDeleted')}
                id="wallet-transaction-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="wallet-transaction-payment"
                name="payment"
                data-cy="payment"
                label={translate('adminApp.walletTransaction.payment')}
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
                id="wallet-transaction-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('adminApp.walletTransaction.viewer')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/wallet-transaction" replace color="info">
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

export default WalletTransactionUpdate;
