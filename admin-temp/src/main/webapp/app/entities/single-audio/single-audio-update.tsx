import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { ISingleAudio } from 'app/shared/model/single-audio.model';
import { getEntity, updateEntity, createEntity, reset } from './single-audio.reducer';

export const SingleAudioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const singleAudioEntity = useAppSelector(state => state.admin.singleAudio.entity);
  const loading = useAppSelector(state => state.admin.singleAudio.loading);
  const updating = useAppSelector(state => state.admin.singleAudio.updating);
  const updateSuccess = useAppSelector(state => state.admin.singleAudio.updateSuccess);

  const handleClose = () => {
    navigate('/single-audio' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...singleAudioEntity,
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
          ...singleAudioEntity,
          createdDate: convertDateTimeFromServer(singleAudioEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(singleAudioEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.singleAudio.home.createOrEditLabel" data-cy="SingleAudioCreateUpdateHeading">
            <Translate contentKey="adminApp.singleAudio.home.createOrEditLabel">Create or edit a SingleAudio</Translate>
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
                  id="single-audio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('adminApp.singleAudio.thumbnail')}
                id="single-audio-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.thumbnailS3Key')}
                id="single-audio-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('adminApp.singleAudio.content')}
                id="single-audio-content"
                name="content"
                data-cy="content"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.contentS3Key')}
                id="single-audio-contentS3Key"
                name="contentS3Key"
                data-cy="contentS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.duration')}
                id="single-audio-duration"
                name="duration"
                data-cy="duration"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.createdDate')}
                id="single-audio-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.lastModifiedDate')}
                id="single-audio-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.createdBy')}
                id="single-audio-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.lastModifiedBy')}
                id="single-audio-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleAudio.isDeleted')}
                id="single-audio-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/single-audio" replace color="info">
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

export default SingleAudioUpdate;
