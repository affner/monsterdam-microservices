import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISingleLiveStream } from 'app/shared/model/single-live-stream.model';
import { getEntity, updateEntity, createEntity, reset } from './single-live-stream.reducer';

export const SingleLiveStreamUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const singleLiveStreamEntity = useAppSelector(state => state.admin.singleLiveStream.entity);
  const loading = useAppSelector(state => state.admin.singleLiveStream.loading);
  const updating = useAppSelector(state => state.admin.singleLiveStream.updating);
  const updateSuccess = useAppSelector(state => state.admin.singleLiveStream.updateSuccess);

  const handleClose = () => {
    navigate('/single-live-stream' + location.search);
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...singleLiveStreamEntity,
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
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...singleLiveStreamEntity,
          startTime: convertDateTimeFromServer(singleLiveStreamEntity.startTime),
          endTime: convertDateTimeFromServer(singleLiveStreamEntity.endTime),
          createdDate: convertDateTimeFromServer(singleLiveStreamEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(singleLiveStreamEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.singleLiveStream.home.createOrEditLabel" data-cy="SingleLiveStreamCreateUpdateHeading">
            <Translate contentKey="adminApp.singleLiveStream.home.createOrEditLabel">Create or edit a SingleLiveStream</Translate>
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
                  id="single-live-stream-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.singleLiveStream.title')}
                id="single-live-stream-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.description')}
                id="single-live-stream-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedBlobField
                label={translate('adminApp.singleLiveStream.thumbnail')}
                id="single-live-stream-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.thumbnailS3Key')}
                id="single-live-stream-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.startTime')}
                id="single-live-stream-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.endTime')}
                id="single-live-stream-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedBlobField
                label={translate('adminApp.singleLiveStream.liveContent')}
                id="single-live-stream-liveContent"
                name="liveContent"
                data-cy="liveContent"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.liveContentS3Key')}
                id="single-live-stream-liveContentS3Key"
                name="liveContentS3Key"
                data-cy="liveContentS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.isRecorded')}
                id="single-live-stream-isRecorded"
                name="isRecorded"
                data-cy="isRecorded"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.likeCount')}
                id="single-live-stream-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.createdDate')}
                id="single-live-stream-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.lastModifiedDate')}
                id="single-live-stream-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.createdBy')}
                id="single-live-stream-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.lastModifiedBy')}
                id="single-live-stream-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singleLiveStream.isDeleted')}
                id="single-live-stream-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/single-live-stream" replace color="info">
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

export default SingleLiveStreamUpdate;
