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
import { IFeedback } from 'app/shared/model/feedback.model';
import { FeedbackType } from 'app/shared/model/enumerations/feedback-type.model';
import { getEntity, updateEntity, createEntity, reset } from './feedback.reducer';

export const FeedbackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const feedbackEntity = useAppSelector(state => state.admin.feedback.entity);
  const loading = useAppSelector(state => state.admin.feedback.loading);
  const updating = useAppSelector(state => state.admin.feedback.updating);
  const updateSuccess = useAppSelector(state => state.admin.feedback.updateSuccess);
  const feedbackTypeValues = Object.keys(FeedbackType);

  const handleClose = () => {
    navigate('/feedback' + location.search);
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
    values.feedbackDate = convertDateTimeToServer(values.feedbackDate);
    if (values.feedbackRating !== undefined && typeof values.feedbackRating !== 'number') {
      values.feedbackRating = Number(values.feedbackRating);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...feedbackEntity,
      ...values,
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
          feedbackDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          feedbackType: 'ERROR',
          ...feedbackEntity,
          feedbackDate: convertDateTimeFromServer(feedbackEntity.feedbackDate),
          createdDate: convertDateTimeFromServer(feedbackEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(feedbackEntity.lastModifiedDate),
          creator: feedbackEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.feedback.home.createOrEditLabel" data-cy="FeedbackCreateUpdateHeading">
            <Translate contentKey="adminApp.feedback.home.createOrEditLabel">Create or edit a Feedback</Translate>
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
                  id="feedback-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.feedback.content')}
                id="feedback-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.feedback.feedbackDate')}
                id="feedback-feedbackDate"
                name="feedbackDate"
                data-cy="feedbackDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.feedback.feedbackRating')}
                id="feedback-feedbackRating"
                name="feedbackRating"
                data-cy="feedbackRating"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.feedback.feedbackType')}
                id="feedback-feedbackType"
                name="feedbackType"
                data-cy="feedbackType"
                type="select"
              >
                {feedbackTypeValues.map(feedbackType => (
                  <option value={feedbackType} key={feedbackType}>
                    {translate('adminApp.FeedbackType.' + feedbackType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.feedback.createdDate')}
                id="feedback-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.feedback.lastModifiedDate')}
                id="feedback-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.feedback.createdBy')}
                id="feedback-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.feedback.lastModifiedBy')}
                id="feedback-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.feedback.isDeleted')}
                id="feedback-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="feedback-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.feedback.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback" replace color="info">
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

export default FeedbackUpdate;
