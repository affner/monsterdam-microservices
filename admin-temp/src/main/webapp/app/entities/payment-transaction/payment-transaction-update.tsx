import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { IPaymentProvider } from 'app/shared/model/payment-provider.model';
import { getEntities as getPaymentProviders } from 'app/entities/payment-provider/payment-provider.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { getEntities as getAccountingRecords } from 'app/entities/accounting-record/accounting-record.reducer';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { getEntities as getPurchasedContents } from 'app/entities/purchased-content/purchased-content.reducer';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { getEntities as getPurchasedSubscriptions } from 'app/entities/purchased-subscription/purchased-subscription.reducer';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { getEntities as getWalletTransactions } from 'app/entities/wallet-transaction/wallet-transaction.reducer';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { getEntities as getPurchasedTips } from 'app/entities/purchased-tip/purchased-tip.reducer';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';
import { getEntity, updateEntity, createEntity, reset } from './payment-transaction.reducer';

export const PaymentTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentMethods = useAppSelector(state => state.admin.paymentMethod.entities);
  const paymentProviders = useAppSelector(state => state.admin.paymentProvider.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const accountingRecords = useAppSelector(state => state.admin.accountingRecord.entities);
  const purchasedContents = useAppSelector(state => state.admin.purchasedContent.entities);
  const purchasedSubscriptions = useAppSelector(state => state.admin.purchasedSubscription.entities);
  const walletTransactions = useAppSelector(state => state.admin.walletTransaction.entities);
  const purchasedTips = useAppSelector(state => state.admin.purchasedTip.entities);
  const paymentTransactionEntity = useAppSelector(state => state.admin.paymentTransaction.entity);
  const loading = useAppSelector(state => state.admin.paymentTransaction.loading);
  const updating = useAppSelector(state => state.admin.paymentTransaction.updating);
  const updateSuccess = useAppSelector(state => state.admin.paymentTransaction.updateSuccess);
  const genericStatusValues = Object.keys(GenericStatus);

  const handleClose = () => {
    navigate('/payment-transaction' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPaymentMethods({}));
    dispatch(getPaymentProviders({}));
    dispatch(getUserProfiles({}));
    dispatch(getAccountingRecords({}));
    dispatch(getPurchasedContents({}));
    dispatch(getPurchasedSubscriptions({}));
    dispatch(getWalletTransactions({}));
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
    values.paymentDate = convertDateTimeToServer(values.paymentDate);

    const entity = {
      ...paymentTransactionEntity,
      ...values,
      paymentMethod: paymentMethods.find(it => it.id.toString() === values.paymentMethod.toString()),
      paymentProvider: paymentProviders.find(it => it.id.toString() === values.paymentProvider.toString()),
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
          paymentDate: displayDefaultDateTime(),
        }
      : {
          paymentStatus: 'PENDING',
          ...paymentTransactionEntity,
          paymentDate: convertDateTimeFromServer(paymentTransactionEntity.paymentDate),
          paymentMethod: paymentTransactionEntity?.paymentMethod?.id,
          paymentProvider: paymentTransactionEntity?.paymentProvider?.id,
          viewer: paymentTransactionEntity?.viewer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.paymentTransaction.home.createOrEditLabel" data-cy="PaymentTransactionCreateUpdateHeading">
            <Translate contentKey="adminApp.paymentTransaction.home.createOrEditLabel">Create or edit a PaymentTransaction</Translate>
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
                  id="payment-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.paymentTransaction.amount')}
                id="payment-transaction-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.paymentTransaction.paymentDate')}
                id="payment-transaction-paymentDate"
                name="paymentDate"
                data-cy="paymentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.paymentTransaction.paymentStatus')}
                id="payment-transaction-paymentStatus"
                name="paymentStatus"
                data-cy="paymentStatus"
                type="select"
              >
                {genericStatusValues.map(genericStatus => (
                  <option value={genericStatus} key={genericStatus}>
                    {translate('adminApp.GenericStatus.' + genericStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.paymentTransaction.paymentReference')}
                id="payment-transaction-paymentReference"
                name="paymentReference"
                data-cy="paymentReference"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.paymentTransaction.cloudTransactionId')}
                id="payment-transaction-cloudTransactionId"
                name="cloudTransactionId"
                data-cy="cloudTransactionId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                id="payment-transaction-paymentMethod"
                name="paymentMethod"
                data-cy="paymentMethod"
                label={translate('adminApp.paymentTransaction.paymentMethod')}
                type="select"
              >
                <option value="" key="0" />
                {paymentMethods
                  ? paymentMethods.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.methodName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="payment-transaction-paymentProvider"
                name="paymentProvider"
                data-cy="paymentProvider"
                label={translate('adminApp.paymentTransaction.paymentProvider')}
                type="select"
              >
                <option value="" key="0" />
                {paymentProviders
                  ? paymentProviders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.providerName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="payment-transaction-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('adminApp.paymentTransaction.viewer')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-transaction" replace color="info">
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

export default PaymentTransactionUpdate;
