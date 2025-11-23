import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISingleAudio } from 'app/shared/model/single-audio.model';
import { getEntities as getSingleAudios } from 'app/entities/single-audio/single-audio.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntity, updateEntity, createEntity, reset } from './content-package.reducer';

export const ContentPackageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const singleAudios = useAppSelector(state => state.admin.singleAudio.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const contentPackageEntity = useAppSelector(state => state.admin.contentPackage.entity);
  const loading = useAppSelector(state => state.admin.contentPackage.loading);
  const updating = useAppSelector(state => state.admin.contentPackage.updating);
  const updateSuccess = useAppSelector(state => state.admin.contentPackage.updateSuccess);

  const handleClose = () => {
    navigate('/content-package' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSingleAudios({}));
    dispatch(getUserProfiles({}));
    dispatch(getDirectMessages({}));
    dispatch(getPostFeeds({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.videoCount !== undefined && typeof values.videoCount !== 'number') {
      values.videoCount = Number(values.videoCount);
    }
    if (values.imageCount !== undefined && typeof values.imageCount !== 'number') {
      values.imageCount = Number(values.imageCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...contentPackageEntity,
      ...values,
      usersTaggeds: mapIdList(values.usersTaggeds),
      audio: singleAudios.find(it => it.id.toString() === values.audio.toString()),
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
          ...contentPackageEntity,
          createdDate: convertDateTimeFromServer(contentPackageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(contentPackageEntity.lastModifiedDate),
          audio: contentPackageEntity?.audio?.id,
          usersTaggeds: contentPackageEntity?.usersTaggeds?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.contentPackage.home.createOrEditLabel" data-cy="ContentPackageCreateUpdateHeading">
            <Translate contentKey="adminApp.contentPackage.home.createOrEditLabel">Create or edit a ContentPackage</Translate>
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
                  id="content-package-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.contentPackage.amount')}
                id="content-package-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.videoCount')}
                id="content-package-videoCount"
                name="videoCount"
                data-cy="videoCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.imageCount')}
                id="content-package-imageCount"
                name="imageCount"
                data-cy="imageCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.isPaidContent')}
                id="content-package-isPaidContent"
                name="isPaidContent"
                data-cy="isPaidContent"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.createdDate')}
                id="content-package-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.lastModifiedDate')}
                id="content-package-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.createdBy')}
                id="content-package-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.lastModifiedBy')}
                id="content-package-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.contentPackage.isDeleted')}
                id="content-package-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="content-package-audio"
                name="audio"
                data-cy="audio"
                label={translate('adminApp.contentPackage.audio')}
                type="select"
              >
                <option value="" key="0" />
                {singleAudios
                  ? singleAudios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.contentPackage.usersTagged')}
                id="content-package-usersTagged"
                data-cy="usersTagged"
                type="select"
                multiple
                name="usersTaggeds"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content-package" replace color="info">
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

export default ContentPackageUpdate;
