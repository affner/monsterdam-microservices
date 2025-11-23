import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { UserGender } from 'app/shared/model/enumerations/user-gender.model';
import { ContentPreference } from 'app/shared/model/enumerations/content-preference.model';
import { getEntity, updateEntity, createEntity, reset } from './user-lite.reducer';

export const UserLiteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const userLiteEntity = useAppSelector(state => state.admin.userLite.entity);
  const loading = useAppSelector(state => state.admin.userLite.loading);
  const updating = useAppSelector(state => state.admin.userLite.updating);
  const updateSuccess = useAppSelector(state => state.admin.userLite.updateSuccess);
  const userGenderValues = Object.keys(UserGender);
  const contentPreferenceValues = Object.keys(ContentPreference);

  const handleClose = () => {
    navigate('/user-lite' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...userLiteEntity,
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
          gender: 'MALE',
          contentPreference: 'ALL',
          ...userLiteEntity,
          createdDate: convertDateTimeFromServer(userLiteEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userLiteEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userLite.home.createOrEditLabel" data-cy="UserLiteCreateUpdateHeading">
            <Translate contentKey="adminApp.userLite.home.createOrEditLabel">Create or edit a UserLite</Translate>
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
                  id="user-lite-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('adminApp.userLite.thumbnail')}
                id="user-lite-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('adminApp.userLite.thumbnailS3Key')}
                id="user-lite-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userLite.birthDate')}
                id="user-lite-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userLite.gender')}
                id="user-lite-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {userGenderValues.map(userGender => (
                  <option value={userGender} key={userGender}>
                    {translate('adminApp.UserGender.' + userGender)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userLite.createdDate')}
                id="user-lite-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userLite.lastModifiedDate')}
                id="user-lite-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userLite.createdBy')}
                id="user-lite-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userLite.lastModifiedBy')}
                id="user-lite-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userLite.isDeleted')}
                id="user-lite-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userLite.nickName')}
                id="user-lite-nickName"
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
                label={translate('adminApp.userLite.fullName')}
                id="user-lite-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[a-z0-9_-]+$/, message: translate('entity.validation.pattern', { pattern: '^[a-z0-9_-]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userLite.contentPreference')}
                id="user-lite-contentPreference"
                name="contentPreference"
                data-cy="contentPreference"
                type="select"
              >
                {contentPreferenceValues.map(contentPreference => (
                  <option value={contentPreference} key={contentPreference}>
                    {translate('adminApp.ContentPreference.' + contentPreference)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-lite" replace color="info">
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

export default UserLiteUpdate;
