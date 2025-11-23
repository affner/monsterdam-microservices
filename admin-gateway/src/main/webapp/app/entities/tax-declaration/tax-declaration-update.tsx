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
import { ITaxDeclaration } from 'app/shared/model/tax-declaration.model';
import { TaxDeclarationType } from 'app/shared/model/enumerations/tax-declaration-type.model';
import { TaxDeclarationStatus } from 'app/shared/model/enumerations/tax-declaration-status.model';
import { getEntity, updateEntity, createEntity, reset } from './tax-declaration.reducer';

export const TaxDeclarationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const accountingRecords = useAppSelector(state => state.admin.accountingRecord.entities);
  const taxDeclarationEntity = useAppSelector(state => state.admin.taxDeclaration.entity);
  const loading = useAppSelector(state => state.admin.taxDeclaration.loading);
  const updating = useAppSelector(state => state.admin.taxDeclaration.updating);
  const updateSuccess = useAppSelector(state => state.admin.taxDeclaration.updateSuccess);
  const taxDeclarationTypeValues = Object.keys(TaxDeclarationType);
  const taxDeclarationStatusValues = Object.keys(TaxDeclarationStatus);

  const handleClose = () => {
    navigate('/tax-declaration');
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
    if (values.year !== undefined && typeof values.year !== 'number') {
      values.year = Number(values.year);
    }
    values.submittedDate = convertDateTimeToServer(values.submittedDate);
    if (values.totalIncome !== undefined && typeof values.totalIncome !== 'number') {
      values.totalIncome = Number(values.totalIncome);
    }
    if (values.totalTaxableIncome !== undefined && typeof values.totalTaxableIncome !== 'number') {
      values.totalTaxableIncome = Number(values.totalTaxableIncome);
    }
    if (values.totalTaxPaid !== undefined && typeof values.totalTaxPaid !== 'number') {
      values.totalTaxPaid = Number(values.totalTaxPaid);
    }

    const entity = {
      ...taxDeclarationEntity,
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
          submittedDate: displayDefaultDateTime(),
        }
      : {
          declarationType: 'INCOME_TAX',
          status: 'DRAFT',
          ...taxDeclarationEntity,
          submittedDate: convertDateTimeFromServer(taxDeclarationEntity.submittedDate),
          accountingRecords: taxDeclarationEntity?.accountingRecords?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.taxDeclaration.home.createOrEditLabel" data-cy="TaxDeclarationCreateUpdateHeading">
            <Translate contentKey="adminApp.taxDeclaration.home.createOrEditLabel">Create or edit a TaxDeclaration</Translate>
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
                  id="tax-declaration-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.taxDeclaration.year')}
                id="tax-declaration-year"
                name="year"
                data-cy="year"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.declarationType')}
                id="tax-declaration-declarationType"
                name="declarationType"
                data-cy="declarationType"
                type="select"
              >
                {taxDeclarationTypeValues.map(taxDeclarationType => (
                  <option value={taxDeclarationType} key={taxDeclarationType}>
                    {translate('adminApp.TaxDeclarationType.' + taxDeclarationType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.taxDeclaration.submittedDate')}
                id="tax-declaration-submittedDate"
                name="submittedDate"
                data-cy="submittedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.status')}
                id="tax-declaration-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {taxDeclarationStatusValues.map(taxDeclarationStatus => (
                  <option value={taxDeclarationStatus} key={taxDeclarationStatus}>
                    {translate('adminApp.TaxDeclarationStatus.' + taxDeclarationStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.taxDeclaration.totalIncome')}
                id="tax-declaration-totalIncome"
                name="totalIncome"
                data-cy="totalIncome"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.totalTaxableIncome')}
                id="tax-declaration-totalTaxableIncome"
                name="totalTaxableIncome"
                data-cy="totalTaxableIncome"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.totalTaxPaid')}
                id="tax-declaration-totalTaxPaid"
                name="totalTaxPaid"
                data-cy="totalTaxPaid"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.supportingDocumentsKey')}
                id="tax-declaration-supportingDocumentsKey"
                name="supportingDocumentsKey"
                data-cy="supportingDocumentsKey"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxDeclaration.accountingRecords')}
                id="tax-declaration-accountingRecords"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tax-declaration" replace color="info">
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

export default TaxDeclarationUpdate;
