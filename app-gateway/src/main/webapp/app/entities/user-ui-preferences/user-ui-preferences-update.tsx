import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserUIPreferences } from 'app/shared/model/user-ui-preferences.model';
import { getEntity, updateEntity, createEntity, reset } from './user-ui-preferences.reducer';

export const UserUIPreferencesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userUIPreferencesEntity = useAppSelector(state => state.fanflip.userUIPreferences.entity);
  const loading = useAppSelector(state => state.fanflip.userUIPreferences.loading);
  const updating = useAppSelector(state => state.fanflip.userUIPreferences.updating);
  const updateSuccess = useAppSelector(state => state.fanflip.userUIPreferences.updateSuccess);

  const handleClose = () => {
    navigate('/user-ui-preferences' + location.search);
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
      ...userUIPreferencesEntity,
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
          ...userUIPreferencesEntity,
          createdDate: convertDateTimeFromServer(userUIPreferencesEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userUIPreferencesEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fanflipApp.userUIPreferences.home.createOrEditLabel" data-cy="UserUIPreferencesCreateUpdateHeading">
            <Translate contentKey="fanflipApp.userUIPreferences.home.createOrEditLabel">Create or edit a UserUIPreferences</Translate>
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
                  id="user-ui-preferences-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('fanflipApp.userUIPreferences.preferences')}
                id="user-ui-preferences-preferences"
                name="preferences"
                data-cy="preferences"
                type="textarea"
              />
              <ValidatedField
                label={translate('fanflipApp.userUIPreferences.createdDate')}
                id="user-ui-preferences-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('fanflipApp.userUIPreferences.lastModifiedDate')}
                id="user-ui-preferences-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('fanflipApp.userUIPreferences.createdBy')}
                id="user-ui-preferences-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('fanflipApp.userUIPreferences.lastModifiedBy')}
                id="user-ui-preferences-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-ui-preferences" replace color="info">
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

export default UserUIPreferencesUpdate;
