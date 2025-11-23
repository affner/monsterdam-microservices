import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPostFeed } from 'app/shared/model/post-feed.model';
import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IHashTag } from 'app/shared/model/hash-tag.model';
import { HashtagType } from 'app/shared/model/enumerations/hashtag-type.model';
import { getEntity, updateEntity, createEntity, reset } from './hash-tag.reducer';

export const HashTagUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const hashTagEntity = useAppSelector(state => state.admin.hashTag.entity);
  const loading = useAppSelector(state => state.admin.hashTag.loading);
  const updating = useAppSelector(state => state.admin.hashTag.updating);
  const updateSuccess = useAppSelector(state => state.admin.hashTag.updateSuccess);
  const hashtagTypeValues = Object.keys(HashtagType);

  const handleClose = () => {
    navigate('/hash-tag' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostFeeds({}));
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
      ...hashTagEntity,
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
          hashtagType: 'USER',
          ...hashTagEntity,
          createdDate: convertDateTimeFromServer(hashTagEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hashTagEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.hashTag.home.createOrEditLabel" data-cy="HashTagCreateUpdateHeading">
            <Translate contentKey="adminApp.hashTag.home.createOrEditLabel">Create or edit a HashTag</Translate>
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
                  id="hash-tag-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.hashTag.tagName')}
                id="hash-tag-tagName"
                name="tagName"
                data-cy="tagName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.hashTag.hashtagType')}
                id="hash-tag-hashtagType"
                name="hashtagType"
                data-cy="hashtagType"
                type="select"
              >
                {hashtagTypeValues.map(hashtagType => (
                  <option value={hashtagType} key={hashtagType}>
                    {translate('adminApp.HashtagType.' + hashtagType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.hashTag.createdDate')}
                id="hash-tag-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.hashTag.lastModifiedDate')}
                id="hash-tag-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.hashTag.createdBy')}
                id="hash-tag-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.hashTag.lastModifiedBy')}
                id="hash-tag-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.hashTag.isDeleted')}
                id="hash-tag-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hash-tag" replace color="info">
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

export default HashTagUpdate;
