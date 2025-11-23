import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPostComments } from 'app/entities/post-comment/post-comment.reducer';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { getEntity, updateEntity, createEntity, reset } from './post-comment.reducer';

export const PostCommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postComments = useAppSelector(state => state.admin.postComment.entities);
  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const postCommentEntity = useAppSelector(state => state.admin.postComment.entity);
  const loading = useAppSelector(state => state.admin.postComment.loading);
  const updating = useAppSelector(state => state.admin.postComment.updating);
  const updateSuccess = useAppSelector(state => state.admin.postComment.updateSuccess);

  const handleClose = () => {
    navigate('/post-comment');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getPostComments({}));
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
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...postCommentEntity,
      ...values,
      responseTo: postComments.find(it => it.id.toString() === values.responseTo.toString()),
      post: postFeeds.find(it => it.id.toString() === values.post.toString()),
      commenter: userProfiles.find(it => it.id.toString() === values.commenter.toString()),
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
          ...postCommentEntity,
          createdDate: convertDateTimeFromServer(postCommentEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(postCommentEntity.lastModifiedDate),
          post: postCommentEntity?.post?.id,
          responseTo: postCommentEntity?.responseTo?.id,
          commenter: postCommentEntity?.commenter?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.postComment.home.createOrEditLabel" data-cy="PostCommentCreateUpdateHeading">
            <Translate contentKey="adminApp.postComment.home.createOrEditLabel">Create or edit a PostComment</Translate>
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
                  id="post-comment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.postComment.commentContent')}
                id="post-comment-commentContent"
                name="commentContent"
                data-cy="commentContent"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postComment.likeCount')}
                id="post-comment-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postComment.createdDate')}
                id="post-comment-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postComment.lastModifiedDate')}
                id="post-comment-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.postComment.createdBy')}
                id="post-comment-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postComment.lastModifiedBy')}
                id="post-comment-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postComment.isDeleted')}
                id="post-comment-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="post-comment-post"
                name="post"
                data-cy="post"
                label={translate('adminApp.postComment.post')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="post-comment-responseTo"
                name="responseTo"
                data-cy="responseTo"
                label={translate('adminApp.postComment.responseTo')}
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
                id="post-comment-commenter"
                name="commenter"
                data-cy="commenter"
                label={translate('adminApp.postComment.commenter')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/post-comment" replace color="info">
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

export default PostCommentUpdate;
