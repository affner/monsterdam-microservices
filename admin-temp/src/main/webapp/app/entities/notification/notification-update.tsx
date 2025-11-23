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
import { INotification } from 'app/shared/model/notification.model';
import { getEntity, updateEntity, createEntity, reset } from './notification.reducer';

export const NotificationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const notificationEntity = useAppSelector(state => state.admin.notification.entity);
  const loading = useAppSelector(state => state.admin.notification.loading);
  const updating = useAppSelector(state => state.admin.notification.updating);
  const updateSuccess = useAppSelector(state => state.admin.notification.updateSuccess);

  const handleClose = () => {
    navigate('/notification' + location.search);
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
    values.readDate = convertDateTimeToServer(values.readDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.postCommentId !== undefined && typeof values.postCommentId !== 'number') {
      values.postCommentId = Number(values.postCommentId);
    }
    if (values.postFeedId !== undefined && typeof values.postFeedId !== 'number') {
      values.postFeedId = Number(values.postFeedId);
    }
    if (values.directMessageId !== undefined && typeof values.directMessageId !== 'number') {
      values.directMessageId = Number(values.directMessageId);
    }
    if (values.userMentionId !== undefined && typeof values.userMentionId !== 'number') {
      values.userMentionId = Number(values.userMentionId);
    }
    if (values.likeMarkId !== undefined && typeof values.likeMarkId !== 'number') {
      values.likeMarkId = Number(values.likeMarkId);
    }

    const entity = {
      ...notificationEntity,
      ...values,
      commentedUser: userProfiles.find(it => it.id.toString() === values.commentedUser.toString()),
      messagedUser: userProfiles.find(it => it.id.toString() === values.messagedUser.toString()),
      mentionerUserInPost: userProfiles.find(it => it.id.toString() === values.mentionerUserInPost.toString()),
      mentionerUserInComment: userProfiles.find(it => it.id.toString() === values.mentionerUserInComment.toString()),
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
          readDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...notificationEntity,
          readDate: convertDateTimeFromServer(notificationEntity.readDate),
          createdDate: convertDateTimeFromServer(notificationEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(notificationEntity.lastModifiedDate),
          commentedUser: notificationEntity?.commentedUser?.id,
          messagedUser: notificationEntity?.messagedUser?.id,
          mentionerUserInPost: notificationEntity?.mentionerUserInPost?.id,
          mentionerUserInComment: notificationEntity?.mentionerUserInComment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.notification.home.createOrEditLabel" data-cy="NotificationCreateUpdateHeading">
            <Translate contentKey="adminApp.notification.home.createOrEditLabel">Create or edit a Notification</Translate>
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
                  id="notification-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.notification.readDate')}
                id="notification-readDate"
                name="readDate"
                data-cy="readDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.notification.createdDate')}
                id="notification-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.notification.lastModifiedDate')}
                id="notification-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.notification.createdBy')}
                id="notification-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.lastModifiedBy')}
                id="notification-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.isDeleted')}
                id="notification-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.notification.postCommentId')}
                id="notification-postCommentId"
                name="postCommentId"
                data-cy="postCommentId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.postFeedId')}
                id="notification-postFeedId"
                name="postFeedId"
                data-cy="postFeedId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.directMessageId')}
                id="notification-directMessageId"
                name="directMessageId"
                data-cy="directMessageId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.userMentionId')}
                id="notification-userMentionId"
                name="userMentionId"
                data-cy="userMentionId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.notification.likeMarkId')}
                id="notification-likeMarkId"
                name="likeMarkId"
                data-cy="likeMarkId"
                type="text"
              />
              <ValidatedField
                id="notification-commentedUser"
                name="commentedUser"
                data-cy="commentedUser"
                label={translate('adminApp.notification.commentedUser')}
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
              <ValidatedField
                id="notification-messagedUser"
                name="messagedUser"
                data-cy="messagedUser"
                label={translate('adminApp.notification.messagedUser')}
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
              <ValidatedField
                id="notification-mentionerUserInPost"
                name="mentionerUserInPost"
                data-cy="mentionerUserInPost"
                label={translate('adminApp.notification.mentionerUserInPost')}
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
              <ValidatedField
                id="notification-mentionerUserInComment"
                name="mentionerUserInComment"
                data-cy="mentionerUserInComment"
                label={translate('adminApp.notification.mentionerUserInComment')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification" replace color="info">
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

export default NotificationUpdate;
