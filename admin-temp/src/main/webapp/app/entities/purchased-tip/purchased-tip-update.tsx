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
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { getEntity, updateEntity, createEntity, reset } from './purchased-tip.reducer';

export const PurchasedTipUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentTransactions = useAppSelector(state => state.admin.paymentTransaction.entities);
  const walletTransactions = useAppSelector(state => state.admin.walletTransaction.entities);
  const creatorEarnings = useAppSelector(state => state.admin.creatorEarning.entities);
  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const purchasedTipEntity = useAppSelector(state => state.admin.purchasedTip.entity);
  const loading = useAppSelector(state => state.admin.purchasedTip.loading);
  const updating = useAppSelector(state => state.admin.purchasedTip.updating);
  const updateSuccess = useAppSelector(state => state.admin.purchasedTip.updateSuccess);

  const handleClose = () => {
    navigate('/purchased-tip' + location.search);
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
    dispatch(getDirectMessages({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...purchasedTipEntity,
      ...values,
      payment: paymentTransactions.find(it => it.id.toString() === values.payment.toString()),
      walletTransaction: walletTransactions.find(it => it.id.toString() === values.walletTransaction.toString()),
      creatorEarning: creatorEarnings.find(it => it.id.toString() === values.creatorEarning.toString()),
      message: directMessages.find(it => it.id.toString() === values.message.toString()),
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
          ...purchasedTipEntity,
          createdDate: convertDateTimeFromServer(purchasedTipEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(purchasedTipEntity.lastModifiedDate),
          payment: purchasedTipEntity?.payment?.id,
          walletTransaction: purchasedTipEntity?.walletTransaction?.id,
          creatorEarning: purchasedTipEntity?.creatorEarning?.id,
          message: purchasedTipEntity?.message?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.purchasedTip.home.createOrEditLabel" data-cy="PurchasedTipCreateUpdateHeading">
            <Translate contentKey="adminApp.purchasedTip.home.createOrEditLabel">Create or edit a PurchasedTip</Translate>
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
                  id="purchased-tip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.purchasedTip.amount')}
                id="purchased-tip-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedTip.createdDate')}
                id="purchased-tip-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.purchasedTip.lastModifiedDate')}
                id="purchased-tip-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.purchasedTip.createdBy')}
                id="purchased-tip-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedTip.lastModifiedBy')}
                id="purchased-tip-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.purchasedTip.isDeleted')}
                id="purchased-tip-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="purchased-tip-payment"
                name="payment"
                data-cy="payment"
                label={translate('adminApp.purchasedTip.payment')}
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
                id="purchased-tip-walletTransaction"
                name="walletTransaction"
                data-cy="walletTransaction"
                label={translate('adminApp.purchasedTip.walletTransaction')}
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
                id="purchased-tip-creatorEarning"
                name="creatorEarning"
                data-cy="creatorEarning"
                label={translate('adminApp.purchasedTip.creatorEarning')}
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
                id="purchased-tip-message"
                name="message"
                data-cy="message"
                label={translate('adminApp.purchasedTip.message')}
                type="select"
                required
              >
                <option value="" key="0" />
                {directMessages
                  ? directMessages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.messageContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchased-tip" replace color="info">
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

export default PurchasedTipUpdate;
