import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { getEntity, updateEntity, createEntity, reset } from './chat-room.reducer';

export const ChatRoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const chatRoomEntity = useAppSelector(state => state.admin.chatRoom.entity);
  const loading = useAppSelector(state => state.admin.chatRoom.loading);
  const updating = useAppSelector(state => state.admin.chatRoom.updating);
  const updateSuccess = useAppSelector(state => state.admin.chatRoom.updateSuccess);

  const handleClose = () => {
    navigate('/chat-room' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDirectMessages({}));
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
    values.lastConnectionDate = convertDateTimeToServer(values.lastConnectionDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...chatRoomEntity,
      ...values,
      sentMessages: mapIdList(values.sentMessages),
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
          lastConnectionDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...chatRoomEntity,
          lastConnectionDate: convertDateTimeFromServer(chatRoomEntity.lastConnectionDate),
          createdDate: convertDateTimeFromServer(chatRoomEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(chatRoomEntity.lastModifiedDate),
          sentMessages: chatRoomEntity?.sentMessages?.map(e => e.id.toString()),
          user: chatRoomEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.chatRoom.home.createOrEditLabel" data-cy="ChatRoomCreateUpdateHeading">
            <Translate contentKey="adminApp.chatRoom.home.createOrEditLabel">Create or edit a ChatRoom</Translate>
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
                  id="chat-room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.chatRoom.lastAction')}
                id="chat-room-lastAction"
                name="lastAction"
                data-cy="lastAction"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.lastConnectionDate')}
                id="chat-room-lastConnectionDate"
                name="lastConnectionDate"
                data-cy="lastConnectionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.muted')}
                id="chat-room-muted"
                name="muted"
                data-cy="muted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.createdDate')}
                id="chat-room-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.lastModifiedDate')}
                id="chat-room-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.createdBy')}
                id="chat-room-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.lastModifiedBy')}
                id="chat-room-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.isDeleted')}
                id="chat-room-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.chatRoom.sentMessages')}
                id="chat-room-sentMessages"
                data-cy="sentMessages"
                type="select"
                multiple
                name="sentMessages"
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
                id="chat-room-user"
                name="user"
                data-cy="user"
                label={translate('adminApp.chatRoom.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chat-room" replace color="info">
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

export default ChatRoomUpdate;
