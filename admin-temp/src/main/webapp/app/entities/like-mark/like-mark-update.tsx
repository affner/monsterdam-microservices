import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILikeMark } from 'app/shared/model/like-mark.model';
import { getEntity, updateEntity, createEntity, reset } from './like-mark.reducer';

export const LikeMarkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const likeMarkEntity = useAppSelector(state => state.admin.likeMark.entity);
  const loading = useAppSelector(state => state.admin.likeMark.loading);
  const updating = useAppSelector(state => state.admin.likeMark.updating);
  const updateSuccess = useAppSelector(state => state.admin.likeMark.updateSuccess);

  const handleClose = () => {
    navigate('/like-mark' + location.search);
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
    if (values.emojiTypeId !== undefined && typeof values.emojiTypeId !== 'number') {
      values.emojiTypeId = Number(values.emojiTypeId);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.multimediaId !== undefined && typeof values.multimediaId !== 'number') {
      values.multimediaId = Number(values.multimediaId);
    }
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }
    if (values.commentId !== undefined && typeof values.commentId !== 'number') {
      values.commentId = Number(values.commentId);
    }
    if (values.likerUserId !== undefined && typeof values.likerUserId !== 'number') {
      values.likerUserId = Number(values.likerUserId);
    }

    const entity = {
      ...likeMarkEntity,
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
          ...likeMarkEntity,
          createdDate: convertDateTimeFromServer(likeMarkEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(likeMarkEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.likeMark.home.createOrEditLabel" data-cy="LikeMarkCreateUpdateHeading">
            <Translate contentKey="adminApp.likeMark.home.createOrEditLabel">Create or edit a LikeMark</Translate>
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
                  id="like-mark-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.likeMark.emojiTypeId')}
                id="like-mark-emojiTypeId"
                name="emojiTypeId"
                data-cy="emojiTypeId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.likeMark.createdDate')}
                id="like-mark-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.likeMark.lastModifiedDate')}
                id="like-mark-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.createdBy')}
                id="like-mark-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.lastModifiedBy')}
                id="like-mark-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.isDeleted')}
                id="like-mark-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.multimediaId')}
                id="like-mark-multimediaId"
                name="multimediaId"
                data-cy="multimediaId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.messageId')}
                id="like-mark-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.postId')}
                id="like-mark-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.commentId')}
                id="like-mark-commentId"
                name="commentId"
                data-cy="commentId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.likeMark.likerUserId')}
                id="like-mark-likerUserId"
                name="likerUserId"
                data-cy="likerUserId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/like-mark" replace color="info">
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

export default LikeMarkUpdate;
