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
import { IPostComment } from 'app/shared/model/post-comment.model';
import { getEntities as getPostComments } from 'app/entities/post-comment/post-comment.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IUserMention } from 'app/shared/model/user-mention.model';
import { getEntity, updateEntity, createEntity, reset } from './user-mention.reducer';

export const UserMentionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const postComments = useAppSelector(state => state.admin.postComment.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const userMentionEntity = useAppSelector(state => state.admin.userMention.entity);
  const loading = useAppSelector(state => state.admin.userMention.loading);
  const updating = useAppSelector(state => state.admin.userMention.updating);
  const updateSuccess = useAppSelector(state => state.admin.userMention.updateSuccess);

  const handleClose = () => {
    navigate('/user-mention' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostFeeds({}));
    dispatch(getPostComments({}));
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
      ...userMentionEntity,
      ...values,
      originPost: postFeeds.find(it => it.id.toString() === values.originPost.toString()),
      originPostComment: postComments.find(it => it.id.toString() === values.originPostComment.toString()),
      mentionedUser: userProfiles.find(it => it.id.toString() === values.mentionedUser.toString()),
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
          ...userMentionEntity,
          createdDate: convertDateTimeFromServer(userMentionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userMentionEntity.lastModifiedDate),
          originPost: userMentionEntity?.originPost?.id,
          originPostComment: userMentionEntity?.originPostComment?.id,
          mentionedUser: userMentionEntity?.mentionedUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userMention.home.createOrEditLabel" data-cy="UserMentionCreateUpdateHeading">
            <Translate contentKey="adminApp.userMention.home.createOrEditLabel">Create or edit a UserMention</Translate>
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
                  id="user-mention-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.userMention.createdDate')}
                id="user-mention-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userMention.lastModifiedDate')}
                id="user-mention-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userMention.createdBy')}
                id="user-mention-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userMention.lastModifiedBy')}
                id="user-mention-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userMention.isDeleted')}
                id="user-mention-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="user-mention-originPost"
                name="originPost"
                data-cy="originPost"
                label={translate('adminApp.userMention.originPost')}
                type="select"
              >
                <option value="" key="0" />
                {postFeeds
                  ? postFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.postContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-mention-originPostComment"
                name="originPostComment"
                data-cy="originPostComment"
                label={translate('adminApp.userMention.originPostComment')}
                type="select"
              >
                <option value="" key="0" />
                {postComments
                  ? postComments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.commentContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-mention-mentionedUser"
                name="mentionedUser"
                data-cy="mentionedUser"
                label={translate('adminApp.userMention.mentionedUser')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-mention" replace color="info">
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

export default UserMentionUpdate;
