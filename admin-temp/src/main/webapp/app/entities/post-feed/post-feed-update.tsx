import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPostPoll } from 'app/shared/model/post-poll.model';
import { getEntities as getPostPolls } from 'app/entities/post-poll/post-poll.reducer';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { IHashTag } from 'app/shared/model/hash-tag.model';
import { getEntities as getHashTags } from 'app/entities/hash-tag/hash-tag.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { getEntity, updateEntity, createEntity, reset } from './post-feed.reducer';

export const PostFeedUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postPolls = useAppSelector(state => state.admin.postPoll.entities);
  const contentPackages = useAppSelector(state => state.admin.contentPackage.entities);
  const hashTags = useAppSelector(state => state.admin.hashTag.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const postFeedEntity = useAppSelector(state => state.admin.postFeed.entity);
  const loading = useAppSelector(state => state.admin.postFeed.loading);
  const updating = useAppSelector(state => state.admin.postFeed.updating);
  const updateSuccess = useAppSelector(state => state.admin.postFeed.updateSuccess);

  const handleClose = () => {
    navigate('/post-feed');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getPostPolls({}));
    dispatch(getContentPackages({}));
    dispatch(getHashTags({}));
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
      ...postFeedEntity,
      ...values,
      hashTags: mapIdList(values.hashTags),
      poll: postPolls.find(it => it.id.toString() === values.poll.toString()),
      contentPackage: contentPackages.find(it => it.id.toString() === values.contentPackage.toString()),
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
          ...postFeedEntity,
          createdDate: convertDateTimeFromServer(postFeedEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(postFeedEntity.lastModifiedDate),
          poll: postFeedEntity?.poll?.id,
          contentPackage: postFeedEntity?.contentPackage?.id,
          hashTags: postFeedEntity?.hashTags?.map(e => e.id.toString()),
          creator: postFeedEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.postFeed.home.createOrEditLabel" data-cy="PostFeedCreateUpdateHeading">
            <Translate contentKey="adminApp.postFeed.home.createOrEditLabel">Create or edit a PostFeed</Translate>
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
                  id="post-feed-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.postFeed.postContent')}
                id="post-feed-postContent"
                name="postContent"
                data-cy="postContent"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postFeed.isHidden')}
                id="post-feed-isHidden"
                name="isHidden"
                data-cy="isHidden"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.pinnedPost')}
                id="post-feed-pinnedPost"
                name="pinnedPost"
                data-cy="pinnedPost"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.likeCount')}
                id="post-feed-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.createdDate')}
                id="post-feed-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postFeed.lastModifiedDate')}
                id="post-feed-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.createdBy')}
                id="post-feed-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.lastModifiedBy')}
                id="post-feed-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postFeed.isDeleted')}
                id="post-feed-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField id="post-feed-poll" name="poll" data-cy="poll" label={translate('adminApp.postFeed.poll')} type="select">
                <option value="" key="0" />
                {postPolls
                  ? postPolls.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.question}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="post-feed-contentPackage"
                name="contentPackage"
                data-cy="contentPackage"
                label={translate('adminApp.postFeed.contentPackage')}
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
                label={translate('adminApp.postFeed.hashTags')}
                id="post-feed-hashTags"
                data-cy="hashTags"
                type="select"
                multiple
                name="hashTags"
              >
                <option value="" key="0" />
                {hashTags
                  ? hashTags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="post-feed-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.postFeed.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/post-feed" replace color="info">
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

export default PostFeedUpdate;
