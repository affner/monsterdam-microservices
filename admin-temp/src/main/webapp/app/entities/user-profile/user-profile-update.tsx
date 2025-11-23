import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserLite } from 'app/shared/model/user-lite.model';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { IUserSettings } from 'app/shared/model/user-settings.model';
import { getEntities as getUserSettings } from 'app/entities/user-settings/user-settings.reducer';
import { IUserEvent } from 'app/shared/model/user-event.model';
import { getEntities as getUserEvents } from 'app/entities/user-event/user-event.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IState } from 'app/shared/model/state.model';
import { getEntities as getStates } from 'app/entities/state/state.reducer';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IHashTag } from 'app/shared/model/hash-tag.model';
import { getEntities as getHashTags } from 'app/entities/hash-tag/hash-tag.reducer';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.admin.userLite.entities);
  const userSettings = useAppSelector(state => state.admin.userSettings.entities);
  const userEvents = useAppSelector(state => state.admin.userEvent.entities);
  const countries = useAppSelector(state => state.admin.country.entities);
  const states = useAppSelector(state => state.admin.state.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const hashTags = useAppSelector(state => state.admin.hashTag.entities);
  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const userProfileEntity = useAppSelector(state => state.admin.userProfile.entity);
  const loading = useAppSelector(state => state.admin.userProfile.loading);
  const updating = useAppSelector(state => state.admin.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.admin.userProfile.updateSuccess);

  const handleClose = () => {
    navigate('/user-profile' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
    dispatch(getUserSettings({}));
    dispatch(getUserEvents({}));
    dispatch(getCountries({}));
    dispatch(getStates({}));
    dispatch(getUserProfiles({}));
    dispatch(getHashTags({}));
    dispatch(getContentPackages({}));
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
      ...userProfileEntity,
      ...values,
      joinedEvents: mapIdList(values.joinedEvents),
      blockedUbications: mapIdList(values.blockedUbications),
      followeds: mapIdList(values.followeds),
      blockedLists: mapIdList(values.blockedLists),
      loyaLists: mapIdList(values.loyaLists),
      subscribeds: mapIdList(values.subscribeds),
      hashTags: mapIdList(values.hashTags),
      userLite: userLites.find(it => it.id.toString() === values.userLite.toString()),
      settings: userSettings.find(it => it.id.toString() === values.settings.toString()),
      countryOfBirth: countries.find(it => it.id.toString() === values.countryOfBirth.toString()),
      stateOfResidence: states.find(it => it.id.toString() === values.stateOfResidence.toString()),
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
          ...userProfileEntity,
          lastLoginDate: convertDateTimeFromServer(userProfileEntity.lastLoginDate),
          createdDate: convertDateTimeFromServer(userProfileEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userProfileEntity.lastModifiedDate),
          userLite: userProfileEntity?.userLite?.id,
          settings: userProfileEntity?.settings?.id,
          countryOfBirth: userProfileEntity?.countryOfBirth?.id,
          stateOfResidence: userProfileEntity?.stateOfResidence?.id,
          followeds: userProfileEntity?.followeds?.map(e => e.id.toString()),
          blockedLists: userProfileEntity?.blockedLists?.map(e => e.id.toString()),
          loyaLists: userProfileEntity?.loyaLists?.map(e => e.id.toString()),
          subscribeds: userProfileEntity?.subscribeds?.map(e => e.id.toString()),
          joinedEvents: userProfileEntity?.joinedEvents?.map(e => e.id.toString()),
          blockedUbications: userProfileEntity?.blockedUbications?.map(e => e.id.toString()),
          hashTags: userProfileEntity?.hashTags?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="adminApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.userProfile.emailContact')}
                id="user-profile-emailContact"
                name="emailContact"
                data-cy="emailContact"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[a-z0-9_-]+$/, message: translate('entity.validation.pattern', { pattern: '^[a-z0-9_-]+$' }) },
                }}
              />
              <ValidatedBlobField
                label={translate('adminApp.userProfile.profilePhoto')}
                id="user-profile-profilePhoto"
                name="profilePhoto"
                data-cy="profilePhoto"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label={translate('adminApp.userProfile.coverPhoto')}
                id="user-profile-coverPhoto"
                name="coverPhoto"
                data-cy="coverPhoto"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.profilePhotoS3Key')}
                id="user-profile-profilePhotoS3Key"
                name="profilePhotoS3Key"
                data-cy="profilePhotoS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.coverPhotoS3Key')}
                id="user-profile-coverPhotoS3Key"
                name="coverPhotoS3Key"
                data-cy="coverPhotoS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.mainContentUrl')}
                id="user-profile-mainContentUrl"
                name="mainContentUrl"
                data-cy="mainContentUrl"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.mobilePhone')}
                id="user-profile-mobilePhone"
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
                label={translate('adminApp.userProfile.websiteUrl')}
                id="user-profile-websiteUrl"
                name="websiteUrl"
                data-cy="websiteUrl"
                type="text"
                validate={{
                  pattern: {
                    value: /^[^@]+@[^@]+\.[^@]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@]+@[^@]+\\.[^@]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userProfile.amazonWishlistUrl')}
                id="user-profile-amazonWishlistUrl"
                name="amazonWishlistUrl"
                data-cy="amazonWishlistUrl"
                type="text"
                validate={{
                  pattern: {
                    value: /^[^@]+@[^@]+\.[^@]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@]+@[^@]+\\.[^@]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userProfile.lastLoginDate')}
                id="user-profile-lastLoginDate"
                name="lastLoginDate"
                data-cy="lastLoginDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userProfile.biography')}
                id="user-profile-biography"
                name="biography"
                data-cy="biography"
                type="textarea"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.isFree')}
                id="user-profile-isFree"
                name="isFree"
                data-cy="isFree"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.createdDate')}
                id="user-profile-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userProfile.lastModifiedDate')}
                id="user-profile-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.createdBy')}
                id="user-profile-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.lastModifiedBy')}
                id="user-profile-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userProfile.isDeleted')}
                id="user-profile-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="user-profile-userLite"
                name="userLite"
                data-cy="userLite"
                label={translate('adminApp.userProfile.userLite')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="user-profile-settings"
                name="settings"
                data-cy="settings"
                label={translate('adminApp.userProfile.settings')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userSettings
                  ? userSettings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="user-profile-countryOfBirth"
                name="countryOfBirth"
                data-cy="countryOfBirth"
                label={translate('adminApp.userProfile.countryOfBirth')}
                type="select"
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
              <ValidatedField
                id="user-profile-stateOfResidence"
                name="stateOfResidence"
                data-cy="stateOfResidence"
                label={translate('adminApp.userProfile.stateOfResidence')}
                type="select"
              >
                <option value="" key="0" />
                {states
                  ? states.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.stateName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.followed')}
                id="user-profile-followed"
                data-cy="followed"
                type="select"
                multiple
                name="followeds"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.blockedList')}
                id="user-profile-blockedList"
                data-cy="blockedList"
                type="select"
                multiple
                name="blockedLists"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.loyaLists')}
                id="user-profile-loyaLists"
                data-cy="loyaLists"
                type="select"
                multiple
                name="loyaLists"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.subscribed')}
                id="user-profile-subscribed"
                data-cy="subscribed"
                type="select"
                multiple
                name="subscribeds"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.joinedEvents')}
                id="user-profile-joinedEvents"
                data-cy="joinedEvents"
                type="select"
                multiple
                name="joinedEvents"
              >
                <option value="" key="0" />
                {userEvents
                  ? userEvents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.startDate}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.blockedUbications')}
                id="user-profile-blockedUbications"
                data-cy="blockedUbications"
                type="select"
                multiple
                name="blockedUbications"
              >
                <option value="" key="0" />
                {states
                  ? states.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userProfile.hashTags')}
                id="user-profile-hashTags"
                data-cy="hashTags"
                type="select"
                multiple
                name="hashTags"
              >
                <option value="" key="0" />
                {hashTags
                  ? hashTags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
