import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { getEntities as getAccountingRecords } from 'app/entities/accounting-record/accounting-record.reducer';
import { IFinancialStatement } from 'app/shared/model/financial-statement.model';
import { StatementType } from 'app/shared/model/enumerations/statement-type.model';
import { getEntity, updateEntity, createEntity, reset } from './financial-statement.reducer';

export const FinancialStatementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const accountingRecords = useAppSelector(state => state.admin.accountingRecord.entities);
  const financialStatementEntity = useAppSelector(state => state.admin.financialStatement.entity);
  const loading = useAppSelector(state => state.admin.financialStatement.loading);
  const updating = useAppSelector(state => state.admin.financialStatement.updating);
  const updateSuccess = useAppSelector(state => state.admin.financialStatement.updateSuccess);
  const statementTypeValues = Object.keys(StatementType);

  const handleClose = () => {
    navigate('/financial-statement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAccountingRecords({}));
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

    const entity = {
      ...financialStatementEntity,
      ...values,
      accountingRecords: mapIdList(values.accountingRecords),
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
        }
      : {
          statementType: 'BALANCE_SHEET',
          ...financialStatementEntity,
          createdDate: convertDateTimeFromServer(financialStatementEntity.createdDate),
          accountingRecords: financialStatementEntity?.accountingRecords?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.financialStatement.home.createOrEditLabel" data-cy="FinancialStatementCreateUpdateHeading">
            <Translate contentKey="adminApp.financialStatement.home.createOrEditLabel">Create or edit a FinancialStatement</Translate>
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
                  id="financial-statement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.financialStatement.statementType')}
                id="financial-statement-statementType"
                name="statementType"
                data-cy="statementType"
                type="select"
              >
                {statementTypeValues.map(statementType => (
                  <option value={statementType} key={statementType}>
                    {translate('adminApp.StatementType.' + statementType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.financialStatement.periodStartDate')}
                id="financial-statement-periodStartDate"
                name="periodStartDate"
                data-cy="periodStartDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.financialStatement.periodEndDate')}
                id="financial-statement-periodEndDate"
                name="periodEndDate"
                data-cy="periodEndDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.financialStatement.createdDate')}
                id="financial-statement-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.financialStatement.accountingRecords')}
                id="financial-statement-accountingRecords"
                data-cy="accountingRecords"
                type="select"
                multiple
                name="accountingRecords"
              >
                <option value="" key="0" />
                {accountingRecords
                  ? accountingRecords.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/financial-statement" replace color="info">
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

export default FinancialStatementUpdate;
