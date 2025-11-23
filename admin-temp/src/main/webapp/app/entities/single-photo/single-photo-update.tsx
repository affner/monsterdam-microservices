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
import { ISinglePhoto } from 'app/shared/model/single-photo.model';
import { getEntity, updateEntity, createEntity, reset } from './single-photo.reducer';

export const SinglePhotoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const singlePhotoEntity = useAppSelector(state => state.admin.singlePhoto.entity);
  const loading = useAppSelector(state => state.admin.singlePhoto.loading);
  const updating = useAppSelector(state => state.admin.singlePhoto.updating);
  const updateSuccess = useAppSelector(state => state.admin.singlePhoto.updateSuccess);

  const handleClose = () => {
    navigate('/single-photo' + location.search);
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
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...singlePhotoEntity,
      ...values,
      belongPackage: contentPackages.find(it => it.id.toString() === values.belongPackage.toString()),
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
          ...singlePhotoEntity,
          createdDate: convertDateTimeFromServer(singlePhotoEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(singlePhotoEntity.lastModifiedDate),
          belongPackage: singlePhotoEntity?.belongPackage?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.singlePhoto.home.createOrEditLabel" data-cy="SinglePhotoCreateUpdateHeading">
            <Translate contentKey="adminApp.singlePhoto.home.createOrEditLabel">Create or edit a SinglePhoto</Translate>
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
                  id="single-photo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('adminApp.singlePhoto.thumbnail')}
                id="single-photo-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.thumbnailS3Key')}
                id="single-photo-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('adminApp.singlePhoto.content')}
                id="single-photo-content"
                name="content"
                data-cy="content"
                isImage
                accept="image/*"
                validate={{}}
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.contentS3Key')}
                id="single-photo-contentS3Key"
                name="contentS3Key"
                data-cy="contentS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.likeCount')}
                id="single-photo-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.createdDate')}
                id="single-photo-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.lastModifiedDate')}
                id="single-photo-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.createdBy')}
                id="single-photo-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.lastModifiedBy')}
                id="single-photo-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.singlePhoto.isDeleted')}
                id="single-photo-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="single-photo-belongPackage"
                name="belongPackage"
                data-cy="belongPackage"
                label={translate('adminApp.singlePhoto.belongPackage')}
                type="select"
              >
                <option value="" key="0" />
                {contentPackages
                  ? contentPackages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.amount}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/single-photo" replace color="info">
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

export default SinglePhotoUpdate;
