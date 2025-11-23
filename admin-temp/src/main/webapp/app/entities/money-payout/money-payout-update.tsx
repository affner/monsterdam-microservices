import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { getEntities as getCreatorEarnings } from 'app/entities/creator-earning/creator-earning.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IMoneyPayout } from 'app/shared/model/money-payout.model';
import { PayoutStatus } from 'app/shared/model/enumerations/payout-status.model';
import { getEntity, updateEntity, createEntity, reset } from './money-payout.reducer';

export const MoneyPayoutUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const creatorEarnings = useAppSelector(state => state.admin.creatorEarning.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const moneyPayoutEntity = useAppSelector(state => state.admin.moneyPayout.entity);
  const loading = useAppSelector(state => state.admin.moneyPayout.loading);
  const updating = useAppSelector(state => state.admin.moneyPayout.updating);
  const updateSuccess = useAppSelector(state => state.admin.moneyPayout.updateSuccess);
  const payoutStatusValues = Object.keys(PayoutStatus);

  const handleClose = () => {
    navigate('/money-payout' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCreatorEarnings({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...moneyPayoutEntity,
      ...values,
      creatorEarning: creatorEarnings.find(it => it.id.toString() === values.creatorEarning.toString()),
      creator: userProfiles.find(it => it.id.toString() === values.creator.toString()),
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
          withdrawStatus: 'PENDING',
          ...moneyPayoutEntity,
          createdDate: convertDateTimeFromServer(moneyPayoutEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(moneyPayoutEntity.lastModifiedDate),
          creatorEarning: moneyPayoutEntity?.creatorEarning?.id,
          creator: moneyPayoutEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.moneyPayout.home.createOrEditLabel" data-cy="MoneyPayoutCreateUpdateHeading">
            <Translate contentKey="adminApp.moneyPayout.home.createOrEditLabel">Create or edit a MoneyPayout</Translate>
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
                  id="money-payout-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.moneyPayout.amount')}
                id="money-payout-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.createdDate')}
                id="money-payout-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.lastModifiedDate')}
                id="money-payout-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.createdBy')}
                id="money-payout-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.lastModifiedBy')}
                id="money-payout-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.isDeleted')}
                id="money-payout-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.moneyPayout.withdrawStatus')}
                id="money-payout-withdrawStatus"
                name="withdrawStatus"
                data-cy="withdrawStatus"
                type="select"
              >
                {payoutStatusValues.map(payoutStatus => (
                  <option value={payoutStatus} key={payoutStatus}>
                    {translate('adminApp.PayoutStatus.' + payoutStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="money-payout-creatorEarning"
                name="creatorEarning"
                data-cy="creatorEarning"
                label={translate('adminApp.moneyPayout.creatorEarning')}
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
                id="money-payout-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.moneyPayout.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/money-payout" replace color="info">
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

export default MoneyPayoutUpdate;
