import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IUserSettings } from 'app/shared/model/user-settings.model';
import { UserLanguage } from 'app/shared/model/enumerations/user-language.model';
import { getEntity, updateEntity, createEntity, reset } from './user-settings.reducer';

export const UserSettingsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const userSettingsEntity = useAppSelector(state => state.admin.userSettings.entity);
  const loading = useAppSelector(state => state.admin.userSettings.loading);
  const updating = useAppSelector(state => state.admin.userSettings.updating);
  const updateSuccess = useAppSelector(state => state.admin.userSettings.updateSuccess);
  const userLanguageValues = Object.keys(UserLanguage);

  const handleClose = () => {
    navigate('/user-settings' + location.search);
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
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.messageBlurIntensity !== undefined && typeof values.messageBlurIntensity !== 'number') {
      values.messageBlurIntensity = Number(values.messageBlurIntensity);
    }
    if (values.sessionsActiveCount !== undefined && typeof values.sessionsActiveCount !== 'number') {
      values.sessionsActiveCount = Number(values.sessionsActiveCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);

    const entity = {
      ...userSettingsEntity,
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
          lastModifiedDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
        }
      : {
          language: 'ES',
          ...userSettingsEntity,
          lastModifiedDate: convertDateTimeFromServer(userSettingsEntity.lastModifiedDate),
          createdDate: convertDateTimeFromServer(userSettingsEntity.createdDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userSettings.home.createOrEditLabel" data-cy="UserSettingsCreateUpdateHeading">
            <Translate contentKey="adminApp.userSettings.home.createOrEditLabel">Create or edit a UserSettings</Translate>
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
                  id="user-settings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.userSettings.lastModifiedDate')}
                id="user-settings-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.darkMode')}
                id="user-settings-darkMode"
                name="darkMode"
                data-cy="darkMode"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.language')}
                id="user-settings-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {userLanguageValues.map(userLanguage => (
                  <option value={userLanguage} key={userLanguage}>
                    {translate('adminApp.UserLanguage.' + userLanguage)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userSettings.contentFilter')}
                id="user-settings-contentFilter"
                name="contentFilter"
                data-cy="contentFilter"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.messageBlurIntensity')}
                id="user-settings-messageBlurIntensity"
                name="messageBlurIntensity"
                data-cy="messageBlurIntensity"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.activityStatusVisibility')}
                id="user-settings-activityStatusVisibility"
                name="activityStatusVisibility"
                data-cy="activityStatusVisibility"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.twoFactorAuthentication')}
                id="user-settings-twoFactorAuthentication"
                name="twoFactorAuthentication"
                data-cy="twoFactorAuthentication"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.sessionsActiveCount')}
                id="user-settings-sessionsActiveCount"
                name="sessionsActiveCount"
                data-cy="sessionsActiveCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.emailNotifications')}
                id="user-settings-emailNotifications"
                name="emailNotifications"
                data-cy="emailNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.importantSubscriptionNotifications')}
                id="user-settings-importantSubscriptionNotifications"
                name="importantSubscriptionNotifications"
                data-cy="importantSubscriptionNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.newMessages')}
                id="user-settings-newMessages"
                name="newMessages"
                data-cy="newMessages"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.postReplies')}
                id="user-settings-postReplies"
                name="postReplies"
                data-cy="postReplies"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.postLikes')}
                id="user-settings-postLikes"
                name="postLikes"
                data-cy="postLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.newFollowers')}
                id="user-settings-newFollowers"
                name="newFollowers"
                data-cy="newFollowers"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.smsNewStream')}
                id="user-settings-smsNewStream"
                name="smsNewStream"
                data-cy="smsNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.toastNewComment')}
                id="user-settings-toastNewComment"
                name="toastNewComment"
                data-cy="toastNewComment"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.toastNewLikes')}
                id="user-settings-toastNewLikes"
                name="toastNewLikes"
                data-cy="toastNewLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.toastNewStream')}
                id="user-settings-toastNewStream"
                name="toastNewStream"
                data-cy="toastNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.siteNewComment')}
                id="user-settings-siteNewComment"
                name="siteNewComment"
                data-cy="siteNewComment"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.siteNewLikes')}
                id="user-settings-siteNewLikes"
                name="siteNewLikes"
                data-cy="siteNewLikes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.siteDiscountsFromFollowedUsers')}
                id="user-settings-siteDiscountsFromFollowedUsers"
                name="siteDiscountsFromFollowedUsers"
                data-cy="siteDiscountsFromFollowedUsers"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.siteNewStream')}
                id="user-settings-siteNewStream"
                name="siteNewStream"
                data-cy="siteNewStream"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.siteUpcomingStreamReminders')}
                id="user-settings-siteUpcomingStreamReminders"
                name="siteUpcomingStreamReminders"
                data-cy="siteUpcomingStreamReminders"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.createdDate')}
                id="user-settings-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userSettings.createdBy')}
                id="user-settings-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.lastModifiedBy')}
                id="user-settings-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userSettings.isDeleted')}
                id="user-settings-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-settings" replace color="info">
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

export default UserSettingsUpdate;
