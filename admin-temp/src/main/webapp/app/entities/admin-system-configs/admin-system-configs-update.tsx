import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAdminSystemConfigs } from 'app/shared/model/admin-system-configs.model';
import { ConfigurationValueType } from 'app/shared/model/enumerations/configuration-value-type.model';
import { ConfigurationCategory } from 'app/shared/model/enumerations/configuration-category.model';
import { getEntity, updateEntity, createEntity, reset } from './admin-system-configs.reducer';

export const AdminSystemConfigsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const adminSystemConfigsEntity = useAppSelector(state => state.admin.adminSystemConfigs.entity);
  const loading = useAppSelector(state => state.admin.adminSystemConfigs.loading);
  const updating = useAppSelector(state => state.admin.adminSystemConfigs.updating);
  const updateSuccess = useAppSelector(state => state.admin.adminSystemConfigs.updateSuccess);
  const configurationValueTypeValues = Object.keys(ConfigurationValueType);
  const configurationCategoryValues = Object.keys(ConfigurationCategory);

  const handleClose = () => {
    navigate('/admin-system-configs' + location.search);
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...adminSystemConfigsEntity,
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
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          configValueType: 'STRING',
          configCategory: 'GENERAL',
          ...adminSystemConfigsEntity,
          createdDate: convertDateTimeFromServer(adminSystemConfigsEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(adminSystemConfigsEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.adminSystemConfigs.home.createOrEditLabel" data-cy="AdminSystemConfigsCreateUpdateHeading">
            <Translate contentKey="adminApp.adminSystemConfigs.home.createOrEditLabel">Create or edit a AdminSystemConfigs</Translate>
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
                  id="admin-system-configs-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.configKey')}
                id="admin-system-configs-configKey"
                name="configKey"
                data-cy="configKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.configValue')}
                id="admin-system-configs-configValue"
                name="configValue"
                data-cy="configValue"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.description')}
                id="admin-system-configs-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.configValueType')}
                id="admin-system-configs-configValueType"
                name="configValueType"
                data-cy="configValueType"
                type="select"
              >
                {configurationValueTypeValues.map(configurationValueType => (
                  <option value={configurationValueType} key={configurationValueType}>
                    {translate('adminApp.ConfigurationValueType.' + configurationValueType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.configCategory')}
                id="admin-system-configs-configCategory"
                name="configCategory"
                data-cy="configCategory"
                type="select"
              >
                {configurationCategoryValues.map(configurationCategory => (
                  <option value={configurationCategory} key={configurationCategory}>
                    {translate('adminApp.ConfigurationCategory.' + configurationCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('adminApp.adminSystemConfigs.configFile')}
                id="admin-system-configs-configFile"
                name="configFile"
                data-cy="configFile"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.createdDate')}
                id="admin-system-configs-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.lastModifiedDate')}
                id="admin-system-configs-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.createdBy')}
                id="admin-system-configs-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.lastModifiedBy')}
                id="admin-system-configs-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.adminSystemConfigs.isActive')}
                id="admin-system-configs-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-system-configs" replace color="info">
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

export default AdminSystemConfigsUpdate;
