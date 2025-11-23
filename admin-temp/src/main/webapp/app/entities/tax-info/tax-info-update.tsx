import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { ITaxInfo } from 'app/shared/model/tax-info.model';
import { TaxType } from 'app/shared/model/enumerations/tax-type.model';
import { getEntity, updateEntity, createEntity, reset } from './tax-info.reducer';

export const TaxInfoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const countries = useAppSelector(state => state.admin.country.entities);
  const taxInfoEntity = useAppSelector(state => state.admin.taxInfo.entity);
  const loading = useAppSelector(state => state.admin.taxInfo.loading);
  const updating = useAppSelector(state => state.admin.taxInfo.updating);
  const updateSuccess = useAppSelector(state => state.admin.taxInfo.updateSuccess);
  const taxTypeValues = Object.keys(TaxType);

  const handleClose = () => {
    navigate('/tax-info' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCountries({}));
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
    if (values.ratePercentage !== undefined && typeof values.ratePercentage !== 'number') {
      values.ratePercentage = Number(values.ratePercentage);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...taxInfoEntity,
      ...values,
      country: countries.find(it => it.id.toString() === values.country.toString()),
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
          taxType: 'VAT',
          ...taxInfoEntity,
          createdDate: convertDateTimeFromServer(taxInfoEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(taxInfoEntity.lastModifiedDate),
          country: taxInfoEntity?.country?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.taxInfo.home.createOrEditLabel" data-cy="TaxInfoCreateUpdateHeading">
            <Translate contentKey="adminApp.taxInfo.home.createOrEditLabel">Create or edit a TaxInfo</Translate>
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
                  id="tax-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.taxInfo.ratePercentage')}
                id="tax-info-ratePercentage"
                name="ratePercentage"
                data-cy="ratePercentage"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxInfo.taxType')}
                id="tax-info-taxType"
                name="taxType"
                data-cy="taxType"
                type="select"
              >
                {taxTypeValues.map(taxType => (
                  <option value={taxType} key={taxType}>
                    {translate('adminApp.TaxType.' + taxType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.taxInfo.createdDate')}
                id="tax-info-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.taxInfo.lastModifiedDate')}
                id="tax-info-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.taxInfo.createdBy')}
                id="tax-info-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxInfo.lastModifiedBy')}
                id="tax-info-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.taxInfo.isDeleted')}
                id="tax-info-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="tax-info-country"
                name="country"
                data-cy="country"
                label={translate('adminApp.taxInfo.country')}
                type="select"
                required
              >
                <option value="" key="0" />
                {countries
                  ? countries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tax-info" replace color="info">
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

export default TaxInfoUpdate;
