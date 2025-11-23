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
import { IVideoStory } from 'app/shared/model/video-story.model';
import { getEntity, updateEntity, createEntity, reset } from './video-story.reducer';

export const VideoStoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const videoStoryEntity = useAppSelector(state => state.admin.videoStory.entity);
  const loading = useAppSelector(state => state.admin.videoStory.loading);
  const updating = useAppSelector(state => state.admin.videoStory.updating);
  const updateSuccess = useAppSelector(state => state.admin.videoStory.updateSuccess);

  const handleClose = () => {
    navigate('/video-story' + location.search);
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
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...videoStoryEntity,
      ...values,
      creator: userProfiles.find(it => it.id.toString() === values.creator.toString()),
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
          ...videoStoryEntity,
          createdDate: convertDateTimeFromServer(videoStoryEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(videoStoryEntity.lastModifiedDate),
          creator: videoStoryEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.videoStory.home.createOrEditLabel" data-cy="VideoStoryCreateUpdateHeading">
            <Translate contentKey="adminApp.videoStory.home.createOrEditLabel">Create or edit a VideoStory</Translate>
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
                  id="video-story-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('adminApp.videoStory.thumbnail')}
                id="video-story-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.videoStory.thumbnailS3Key')}
                id="video-story-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('adminApp.videoStory.content')}
                id="video-story-content"
                name="content"
                data-cy="content"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('adminApp.videoStory.contentS3Key')}
                id="video-story-contentS3Key"
                name="contentS3Key"
                data-cy="contentS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.videoStory.duration')}
                id="video-story-duration"
                name="duration"
                data-cy="duration"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.videoStory.likeCount')}
                id="video-story-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.videoStory.createdDate')}
                id="video-story-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.videoStory.lastModifiedDate')}
                id="video-story-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.videoStory.createdBy')}
                id="video-story-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.videoStory.lastModifiedBy')}
                id="video-story-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.videoStory.isDeleted')}
                id="video-story-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="video-story-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.videoStory.creator')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/video-story" replace color="info">
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

export default VideoStoryUpdate;
