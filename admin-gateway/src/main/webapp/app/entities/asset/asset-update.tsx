import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAsset } from 'app/shared/model/asset.model';
import { AssetType } from 'app/shared/model/enumerations/asset-type.model';
import { getEntity, updateEntity, createEntity, reset } from './asset.reducer';

export const AssetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assetEntity = useAppSelector(state => state.admin.asset.entity);
  const loading = useAppSelector(state => state.admin.asset.loading);
  const updating = useAppSelector(state => state.admin.asset.updating);
  const updateSuccess = useAppSelector(state => state.admin.asset.updateSuccess);
  const assetTypeValues = Object.keys(AssetType);

  const handleClose = () => {
    navigate('/asset');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.value !== undefined && typeof values.value !== 'number') {
      values.value = Number(values.value);
    }

    const entity = {
      ...assetEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'CURRENT',
          ...assetEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.asset.home.createOrEditLabel" data-cy="AssetCreateUpdateHeading">
            <Translate contentKey="adminApp.asset.home.createOrEditLabel">Create or edit a Asset</Translate>
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
                  id="asset-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.asset.name')}
                id="asset-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.asset.value')}
                id="asset-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.asset.acquisitionDate')}
                id="asset-acquisitionDate"
                name="acquisitionDate"
                data-cy="acquisitionDate"
                type="date"
              />
              <ValidatedField label={translate('adminApp.asset.type')} id="asset-type" name="type" data-cy="type" type="select">
                {assetTypeValues.map(assetType => (
                  <option value={assetType} key={assetType}>
                    {translate('adminApp.AssetType.' + assetType)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/asset" replace color="info">
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

export default AssetUpdate;
