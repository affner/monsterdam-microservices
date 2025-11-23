import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAdminUserProfile } from 'app/shared/model/admin-user-profile.model';
import { AdminGender } from 'app/shared/model/enumerations/admin-gender.model';
import { getEntity, updateEntity, createEntity, reset } from './admin-user-profile.reducer';

export const AdminUserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const adminUserProfileEntity = useAppSelector(state => state.admin.adminUserProfile.entity);
  const loading = useAppSelector(state => state.admin.adminUserProfile.loading);
  const updating = useAppSelector(state => state.admin.adminUserProfile.updating);
  const updateSuccess = useAppSelector(state => state.admin.adminUserProfile.updateSuccess);
  const adminGenderValues = Object.keys(AdminGender);

  const handleClose = () => {
    navigate('/admin-user-profile' + location.search);
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
    values.lastLoginDate = convertDateTimeToServer(values.lastLoginDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...adminUserProfileEntity,
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
          lastLoginDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          gender: 'MALE',
          ...adminUserProfileEntity,
          lastLoginDate: convertDateTimeFromServer(adminUserProfileEntity.lastLoginDate),
          createdDate: convertDateTimeFromServer(adminUserProfileEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(adminUserProfileEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.adminUserProfile.home.createOrEditLabel" data-cy="AdminUserProfileCreateUpdateHeading">
            <Translate contentKey="adminApp.adminUserProfile.home.createOrEditLabel">Create or edit a AdminUserProfile</Translate>
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
                  id="admin-user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.adminUserProfile.fullName')}
                id="admin-user-profile-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[a-z0-9_-]+$/, message: translate('entity.validation.pattern', { pattern: '^[a-z0-9_-]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.emailAddress')}
                id="admin-user-profile-emailAddress"
                name="emailAddress"
                data-cy="emailAddress"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@]+@[^@]+\.[^@]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@]+@[^@]+\\.[^@]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.nickName')}
                id="admin-user-profile-nickName"
                name="nickName"
                data-cy="nickName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[a-z0-9_-]{3,16}$/,
                    message: translate('entity.validation.pattern', { pattern: '^[a-z0-9_-]{3,16}$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.gender')}
                id="admin-user-profile-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {adminGenderValues.map(adminGender => (
                  <option value={adminGender} key={adminGender}>
                    {translate('adminApp.AdminGender.' + adminGender)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.adminUserProfile.mobilePhone')}
                id="admin-user-profile-mobilePhone"
                name="mobilePhone"
                data-cy="mobilePhone"
                type="text"
                validate={{
                  pattern: {
                    value: /^\+?[0-9]{10,15}$/,
                    message: translate('entity.validation.pattern', { pattern: '^\\+?[0-9]{10,15}$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.lastLoginDate')}
                id="admin-user-profile-lastLoginDate"
                name="lastLoginDate"
                data-cy="lastLoginDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.birthDate')}
                id="admin-user-profile-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.createdDate')}
                id="admin-user-profile-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.lastModifiedDate')}
                id="admin-user-profile-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.createdBy')}
                id="admin-user-profile-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.lastModifiedBy')}
                id="admin-user-profile-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.adminUserProfile.isDeleted')}
                id="admin-user-profile-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-user-profile" replace color="info">
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

export default AdminUserProfileUpdate;
