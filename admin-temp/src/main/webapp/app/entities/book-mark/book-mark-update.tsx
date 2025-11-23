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
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IBookMark } from 'app/shared/model/book-mark.model';
import { getEntity, updateEntity, createEntity, reset } from './book-mark.reducer';

export const BookMarkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const bookMarkEntity = useAppSelector(state => state.admin.bookMark.entity);
  const loading = useAppSelector(state => state.admin.bookMark.loading);
  const updating = useAppSelector(state => state.admin.bookMark.updating);
  const updateSuccess = useAppSelector(state => state.admin.bookMark.updateSuccess);

  const handleClose = () => {
    navigate('/book-mark' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostFeeds({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...bookMarkEntity,
      ...values,
      post: postFeeds.find(it => it.id.toString() === values.post.toString()),
      message: directMessages.find(it => it.id.toString() === values.message.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...bookMarkEntity,
          createdDate: convertDateTimeFromServer(bookMarkEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(bookMarkEntity.lastModifiedDate),
          post: bookMarkEntity?.post?.id,
          message: bookMarkEntity?.message?.id,
          user: bookMarkEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.bookMark.home.createOrEditLabel" data-cy="BookMarkCreateUpdateHeading">
            <Translate contentKey="adminApp.bookMark.home.createOrEditLabel">Create or edit a BookMark</Translate>
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
                  id="book-mark-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.bookMark.createdDate')}
                id="book-mark-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.bookMark.lastModifiedDate')}
                id="book-mark-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.bookMark.createdBy')}
                id="book-mark-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.bookMark.lastModifiedBy')}
                id="book-mark-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.bookMark.isDeleted')}
                id="book-mark-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="book-mark-post"
                name="post"
                data-cy="post"
                label={translate('adminApp.bookMark.post')}
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
                id="book-mark-message"
                name="message"
                data-cy="message"
                label={translate('adminApp.bookMark.message')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="book-mark-user"
                name="user"
                data-cy="user"
                label={translate('adminApp.bookMark.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/book-mark" replace color="info">
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

export default BookMarkUpdate;
