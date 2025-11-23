import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPollOption } from 'app/shared/model/poll-option.model';
import { getEntities as getPollOptions } from 'app/entities/poll-option/poll-option.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPollVote } from 'app/shared/model/poll-vote.model';
import { getEntity, updateEntity, createEntity, reset } from './poll-vote.reducer';

export const PollVoteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pollOptions = useAppSelector(state => state.admin.pollOption.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const pollVoteEntity = useAppSelector(state => state.admin.pollVote.entity);
  const loading = useAppSelector(state => state.admin.pollVote.loading);
  const updating = useAppSelector(state => state.admin.pollVote.updating);
  const updateSuccess = useAppSelector(state => state.admin.pollVote.updateSuccess);

  const handleClose = () => {
    navigate('/poll-vote' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPollOptions({}));
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

    const entity = {
      ...pollVoteEntity,
      ...values,
      pollOption: pollOptions.find(it => it.id.toString() === values.pollOption.toString()),
      votingUser: userProfiles.find(it => it.id.toString() === values.votingUser.toString()),
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
        }
      : {
          ...pollVoteEntity,
          createdDate: convertDateTimeFromServer(pollVoteEntity.createdDate),
          pollOption: pollVoteEntity?.pollOption?.id,
          votingUser: pollVoteEntity?.votingUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.pollVote.home.createOrEditLabel" data-cy="PollVoteCreateUpdateHeading">
            <Translate contentKey="adminApp.pollVote.home.createOrEditLabel">Create or edit a PollVote</Translate>
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
                  id="poll-vote-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.pollVote.createdDate')}
                id="poll-vote-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="poll-vote-pollOption"
                name="pollOption"
                data-cy="pollOption"
                label={translate('adminApp.pollVote.pollOption')}
                type="select"
                required
              >
                <option value="" key="0" />
                {pollOptions
                  ? pollOptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.optionDescription}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="poll-vote-votingUser"
                name="votingUser"
                data-cy="votingUser"
                label={translate('adminApp.pollVote.votingUser')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poll-vote" replace color="info">
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

export default PollVoteUpdate;
