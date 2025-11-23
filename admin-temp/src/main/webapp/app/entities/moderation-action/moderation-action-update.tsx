import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { getEntities as getAssistanceTickets } from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import { IModerationAction } from 'app/shared/model/moderation-action.model';
import { ModerationActionType } from 'app/shared/model/enumerations/moderation-action-type.model';
import { getEntity, updateEntity, createEntity, reset } from './moderation-action.reducer';

export const ModerationActionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assistanceTickets = useAppSelector(state => state.admin.assistanceTicket.entities);
  const moderationActionEntity = useAppSelector(state => state.admin.moderationAction.entity);
  const loading = useAppSelector(state => state.admin.moderationAction.loading);
  const updating = useAppSelector(state => state.admin.moderationAction.updating);
  const updateSuccess = useAppSelector(state => state.admin.moderationAction.updateSuccess);
  const moderationActionTypeValues = Object.keys(ModerationActionType);

  const handleClose = () => {
    navigate('/moderation-action' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssistanceTickets({}));
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
    values.actionDate = convertDateTimeToServer(values.actionDate);

    const entity = {
      ...moderationActionEntity,
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
          actionDate: displayDefaultDateTime(),
        }
      : {
          actionType: 'WARNING',
          ...moderationActionEntity,
          actionDate: convertDateTimeFromServer(moderationActionEntity.actionDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.moderationAction.home.createOrEditLabel" data-cy="ModerationActionCreateUpdateHeading">
            <Translate contentKey="adminApp.moderationAction.home.createOrEditLabel">Create or edit a ModerationAction</Translate>
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
                  id="moderation-action-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.moderationAction.actionType')}
                id="moderation-action-actionType"
                name="actionType"
                data-cy="actionType"
                type="select"
              >
                {moderationActionTypeValues.map(moderationActionType => (
                  <option value={moderationActionType} key={moderationActionType}>
                    {translate('adminApp.ModerationActionType.' + moderationActionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.moderationAction.reason')}
                id="moderation-action-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.moderationAction.actionDate')}
                id="moderation-action-actionDate"
                name="actionDate"
                data-cy="actionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.moderationAction.durationDays')}
                id="moderation-action-durationDays"
                name="durationDays"
                data-cy="durationDays"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/moderation-action" replace color="info">
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

export default ModerationActionUpdate;
