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
import { IPostPoll } from 'app/shared/model/post-poll.model';
import { getEntity, updateEntity, createEntity, reset } from './post-poll.reducer';

export const PostPollUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const postPollEntity = useAppSelector(state => state.admin.postPoll.entity);
  const loading = useAppSelector(state => state.admin.postPoll.loading);
  const updating = useAppSelector(state => state.admin.postPoll.updating);
  const updateSuccess = useAppSelector(state => state.admin.postPoll.updateSuccess);

  const handleClose = () => {
    navigate('/post-poll' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostFeeds({}));
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
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);

    const entity = {
      ...postPollEntity,
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
          lastModifiedDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
        }
      : {
          ...postPollEntity,
          lastModifiedDate: convertDateTimeFromServer(postPollEntity.lastModifiedDate),
          createdDate: convertDateTimeFromServer(postPollEntity.createdDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.postPoll.home.createOrEditLabel" data-cy="PostPollCreateUpdateHeading">
            <Translate contentKey="adminApp.postPoll.home.createOrEditLabel">Create or edit a PostPoll</Translate>
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
                  id="post-poll-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.postPoll.question')}
                id="post-poll-question"
                name="question"
                data-cy="question"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postPoll.isMultiChoice')}
                id="post-poll-isMultiChoice"
                name="isMultiChoice"
                data-cy="isMultiChoice"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.postPoll.lastModifiedDate')}
                id="post-poll-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.postPoll.endDate')}
                id="post-poll-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postPoll.postPollDuration')}
                id="post-poll-postPollDuration"
                name="postPollDuration"
                data-cy="postPollDuration"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postPoll.createdDate')}
                id="post-poll-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.postPoll.createdBy')}
                id="post-poll-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postPoll.lastModifiedBy')}
                id="post-poll-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.postPoll.isDeleted')}
                id="post-poll-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/post-poll" replace color="info">
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

export default PostPollUpdate;
