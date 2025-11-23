import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IVideoStory } from 'app/shared/model/video-story.model';
import { getEntities as getVideoStories } from 'app/entities/video-story/video-story.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { getEntities as getChatRooms } from 'app/entities/chat-room/chat-room.reducer';
import { IAdminAnnouncement } from 'app/shared/model/admin-announcement.model';
import { getEntities as getAdminAnnouncements } from 'app/entities/admin-announcement/admin-announcement.reducer';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { getEntities as getPurchasedTips } from 'app/entities/purchased-tip/purchased-tip.reducer';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntity, updateEntity, createEntity, reset } from './direct-message.reducer';

export const DirectMessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const videoStories = useAppSelector(state => state.admin.videoStory.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const chatRooms = useAppSelector(state => state.admin.chatRoom.entities);
  const adminAnnouncements = useAppSelector(state => state.admin.adminAnnouncement.entities);
  const purchasedTips = useAppSelector(state => state.admin.purchasedTip.entities);
  const directMessageEntity = useAppSelector(state => state.admin.directMessage.entity);
  const loading = useAppSelector(state => state.admin.directMessage.loading);
  const updating = useAppSelector(state => state.admin.directMessage.updating);
  const updateSuccess = useAppSelector(state => state.admin.directMessage.updateSuccess);

  const handleClose = () => {
    navigate('/direct-message');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getContentPackages({}));
    dispatch(getDirectMessages({}));
    dispatch(getVideoStories({}));
    dispatch(getUserProfiles({}));
    dispatch(getChatRooms({}));
    dispatch(getAdminAnnouncements({}));
    dispatch(getPurchasedTips({}));
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
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...directMessageEntity,
      ...values,
      contentPackage: contentPackages.find(it => it.id.toString() === values.contentPackage.toString()),
      responseTo: directMessages.find(it => it.id.toString() === values.responseTo.toString()),
      repliedStory: videoStories.find(it => it.id.toString() === values.repliedStory.toString()),
      user: userProfiles.find(it => it.id.toString() === values.user.toString()),
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
          ...directMessageEntity,
          readDate: convertDateTimeFromServer(directMessageEntity.readDate),
          createdDate: convertDateTimeFromServer(directMessageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(directMessageEntity.lastModifiedDate),
          contentPackage: directMessageEntity?.contentPackage?.id,
          responseTo: directMessageEntity?.responseTo?.id,
          repliedStory: directMessageEntity?.repliedStory?.id,
          user: directMessageEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.directMessage.home.createOrEditLabel" data-cy="DirectMessageCreateUpdateHeading">
            <Translate contentKey="adminApp.directMessage.home.createOrEditLabel">Create or edit a DirectMessage</Translate>
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
                  id="direct-message-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.directMessage.messageContent')}
                id="direct-message-messageContent"
                name="messageContent"
                data-cy="messageContent"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.directMessage.readDate')}
                id="direct-message-readDate"
                name="readDate"
                data-cy="readDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.likeCount')}
                id="direct-message-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.isHidden')}
                id="direct-message-isHidden"
                name="isHidden"
                data-cy="isHidden"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.createdDate')}
                id="direct-message-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.directMessage.lastModifiedDate')}
                id="direct-message-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.createdBy')}
                id="direct-message-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.lastModifiedBy')}
                id="direct-message-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.directMessage.isDeleted')}
                id="direct-message-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="direct-message-contentPackage"
                name="contentPackage"
                data-cy="contentPackage"
                label={translate('adminApp.directMessage.contentPackage')}
                type="select"
              >
                <option value="" key="0" />
                {contentPackages
                  ? contentPackages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="direct-message-responseTo"
                name="responseTo"
                data-cy="responseTo"
                label={translate('adminApp.directMessage.responseTo')}
                type="select"
              >
                <option value="" key="0" />
                {directMessages
                  ? directMessages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.messageContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="direct-message-repliedStory"
                name="repliedStory"
                data-cy="repliedStory"
                label={translate('adminApp.directMessage.repliedStory')}
                type="select"
              >
                <option value="" key="0" />
                {videoStories
                  ? videoStories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="direct-message-user"
                name="user"
                data-cy="user"
                label={translate('adminApp.directMessage.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/direct-message" replace color="info">
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

export default DirectMessageUpdate;
