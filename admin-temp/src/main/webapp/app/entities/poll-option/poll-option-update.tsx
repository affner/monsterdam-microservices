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
import { IPollOption } from 'app/shared/model/poll-option.model';
import { getEntity, updateEntity, createEntity, reset } from './poll-option.reducer';

export const PollOptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postPolls = useAppSelector(state => state.admin.postPoll.entities);
  const pollOptionEntity = useAppSelector(state => state.admin.pollOption.entity);
  const loading = useAppSelector(state => state.admin.pollOption.loading);
  const updating = useAppSelector(state => state.admin.pollOption.updating);
  const updateSuccess = useAppSelector(state => state.admin.pollOption.updateSuccess);

  const handleClose = () => {
    navigate('/poll-option' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostPolls({}));
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
    if (values.voteCount !== undefined && typeof values.voteCount !== 'number') {
      values.voteCount = Number(values.voteCount);
    }

    const entity = {
      ...pollOptionEntity,
      ...values,
      poll: postPolls.find(it => it.id.toString() === values.poll.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pollOptionEntity,
          poll: pollOptionEntity?.poll?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.pollOption.home.createOrEditLabel" data-cy="PollOptionCreateUpdateHeading">
            <Translate contentKey="adminApp.pollOption.home.createOrEditLabel">Create or edit a PollOption</Translate>
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
                  id="poll-option-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.pollOption.optionDescription')}
                id="poll-option-optionDescription"
                name="optionDescription"
                data-cy="optionDescription"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.pollOption.voteCount')}
                id="poll-option-voteCount"
                name="voteCount"
                data-cy="voteCount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="poll-option-poll"
                name="poll"
                data-cy="poll"
                label={translate('adminApp.pollOption.poll')}
                type="select"
                required
              >
                <option value="" key="0" />
                {postPolls
                  ? postPolls.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.question}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poll-option" replace color="info">
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

export default PollOptionUpdate;
