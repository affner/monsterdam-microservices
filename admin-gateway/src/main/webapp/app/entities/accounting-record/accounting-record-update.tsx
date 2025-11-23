import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBudget } from 'app/shared/model/budget.model';
import { getEntities as getBudgets } from 'app/entities/budget/budget.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { getEntities as getAssets } from 'app/entities/asset/asset.reducer';
import { ILiability } from 'app/shared/model/liability.model';
import { getEntities as getLiabilities } from 'app/entities/liability/liability.reducer';
import { ITaxDeclaration } from 'app/shared/model/tax-declaration.model';
import { getEntities as getTaxDeclarations } from 'app/entities/tax-declaration/tax-declaration.reducer';
import { IFinancialStatement } from 'app/shared/model/financial-statement.model';
import { getEntities as getFinancialStatements } from 'app/entities/financial-statement/financial-statement.reducer';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { AccountingType } from 'app/shared/model/enumerations/accounting-type.model';
import { getEntity, updateEntity, createEntity, reset } from './accounting-record.reducer';

export const AccountingRecordUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const budgets = useAppSelector(state => state.admin.budget.entities);
  const assets = useAppSelector(state => state.admin.asset.entities);
  const liabilities = useAppSelector(state => state.admin.liability.entities);
  const taxDeclarations = useAppSelector(state => state.admin.taxDeclaration.entities);
  const financialStatements = useAppSelector(state => state.admin.financialStatement.entities);
  const accountingRecordEntity = useAppSelector(state => state.admin.accountingRecord.entity);
  const loading = useAppSelector(state => state.admin.accountingRecord.loading);
  const updating = useAppSelector(state => state.admin.accountingRecord.updating);
  const updateSuccess = useAppSelector(state => state.admin.accountingRecord.updateSuccess);
  const accountingTypeValues = Object.keys(AccountingType);

  const handleClose = () => {
    navigate('/accounting-record');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBudgets({}));
    dispatch(getAssets({}));
    dispatch(getLiabilities({}));
    dispatch(getTaxDeclarations({}));
    dispatch(getFinancialStatements({}));
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
    values.date = convertDateTimeToServer(values.date);
    if (values.debit !== undefined && typeof values.debit !== 'number') {
      values.debit = Number(values.debit);
    }
    if (values.credit !== undefined && typeof values.credit !== 'number') {
      values.credit = Number(values.credit);
    }
    if (values.balance !== undefined && typeof values.balance !== 'number') {
      values.balance = Number(values.balance);
    }
    if (values.paymentId !== undefined && typeof values.paymentId !== 'number') {
      values.paymentId = Number(values.paymentId);
    }

    const entity = {
      ...accountingRecordEntity,
      ...values,
      budget: budgets.find(it => it.id.toString() === values.budget.toString()),
      asset: assets.find(it => it.id.toString() === values.asset.toString()),
      liability: liabilities.find(it => it.id.toString() === values.liability.toString()),
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
          date: displayDefaultDateTime(),
        }
      : {
          accountType: 'ASSET',
          ...accountingRecordEntity,
          date: convertDateTimeFromServer(accountingRecordEntity.date),
          budget: accountingRecordEntity?.budget?.id,
          asset: accountingRecordEntity?.asset?.id,
          liability: accountingRecordEntity?.liability?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.accountingRecord.home.createOrEditLabel" data-cy="AccountingRecordCreateUpdateHeading">
            <Translate contentKey="adminApp.accountingRecord.home.createOrEditLabel">Create or edit a AccountingRecord</Translate>
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
                  id="accounting-record-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.accountingRecord.date')}
                id="accounting-record-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.accountingRecord.description')}
                id="accounting-record-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.accountingRecord.debit')}
                id="accounting-record-debit"
                name="debit"
                data-cy="debit"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.accountingRecord.credit')}
                id="accounting-record-credit"
                name="credit"
                data-cy="credit"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.accountingRecord.balance')}
                id="accounting-record-balance"
                name="balance"
                data-cy="balance"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.accountingRecord.accountType')}
                id="accounting-record-accountType"
                name="accountType"
                data-cy="accountType"
                type="select"
              >
                {accountingTypeValues.map(accountingType => (
                  <option value={accountingType} key={accountingType}>
                    {translate('adminApp.AccountingType.' + accountingType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.accountingRecord.paymentId')}
                id="accounting-record-paymentId"
                name="paymentId"
                data-cy="paymentId"
                type="text"
              />
              <ValidatedField
                id="accounting-record-budget"
                name="budget"
                data-cy="budget"
                label={translate('adminApp.accountingRecord.budget')}
                type="select"
              >
                <option value="" key="0" />
                {budgets
                  ? budgets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="accounting-record-asset"
                name="asset"
                data-cy="asset"
                label={translate('adminApp.accountingRecord.asset')}
                type="select"
              >
                <option value="" key="0" />
                {assets
                  ? assets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="accounting-record-liability"
                name="liability"
                data-cy="liability"
                label={translate('adminApp.accountingRecord.liability')}
                type="select"
              >
                <option value="" key="0" />
                {liabilities
                  ? liabilities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/accounting-record" replace color="info">
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

export default AccountingRecordUpdate;
