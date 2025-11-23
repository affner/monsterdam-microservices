import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IModerationAction } from 'app/shared/model/moderation-action.model';
import { getEntities as getModerationActions } from 'app/entities/moderation-action/moderation-action.reducer';
import { IUserReport } from 'app/shared/model/user-report.model';
import { getEntities as getUserReports } from 'app/entities/user-report/user-report.reducer';
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';
import { getEntities as getIdentityDocumentReviews } from 'app/entities/identity-document-review/identity-document-review.reducer';
import { IAdminUserProfile } from 'app/shared/model/admin-user-profile.model';
import { getEntities as getAdminUserProfiles } from 'app/entities/admin-user-profile/admin-user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { TicketType } from 'app/shared/model/enumerations/ticket-type.model';
import { getEntity, updateEntity, createEntity, reset } from './assistance-ticket.reducer';

export const AssistanceTicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const moderationActions = useAppSelector(state => state.admin.moderationAction.entities);
  const userReports = useAppSelector(state => state.admin.userReport.entities);
  const identityDocumentReviews = useAppSelector(state => state.admin.identityDocumentReview.entities);
  const adminUserProfiles = useAppSelector(state => state.admin.adminUserProfile.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const assistanceTicketEntity = useAppSelector(state => state.admin.assistanceTicket.entity);
  const loading = useAppSelector(state => state.admin.assistanceTicket.loading);
  const updating = useAppSelector(state => state.admin.assistanceTicket.updating);
  const updateSuccess = useAppSelector(state => state.admin.assistanceTicket.updateSuccess);
  const ticketStatusValues = Object.keys(TicketStatus);
  const ticketTypeValues = Object.keys(TicketType);

  const handleClose = () => {
    navigate('/assistance-ticket' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getModerationActions({}));
    dispatch(getUserReports({}));
    dispatch(getIdentityDocumentReviews({}));
    dispatch(getAdminUserProfiles({}));
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
    values.openedAt = convertDateTimeToServer(values.openedAt);
    values.closedAt = convertDateTimeToServer(values.closedAt);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...assistanceTicketEntity,
      ...values,
      moderationAction: moderationActions.find(it => it.id.toString() === values.moderationAction.toString()),
      assignedAdmin: adminUserProfiles.find(it => it.id.toString() === values.assignedAdmin.toString()),
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
          openedAt: displayDefaultDateTime(),
          closedAt: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'OPEN',
          type: 'ACCESS_ISSUE',
          ...assistanceTicketEntity,
          openedAt: convertDateTimeFromServer(assistanceTicketEntity.openedAt),
          closedAt: convertDateTimeFromServer(assistanceTicketEntity.closedAt),
          createdDate: convertDateTimeFromServer(assistanceTicketEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(assistanceTicketEntity.lastModifiedDate),
          moderationAction: assistanceTicketEntity?.moderationAction?.id,
          assignedAdmin: assistanceTicketEntity?.assignedAdmin?.id,
          user: assistanceTicketEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.assistanceTicket.home.createOrEditLabel" data-cy="AssistanceTicketCreateUpdateHeading">
            <Translate contentKey="adminApp.assistanceTicket.home.createOrEditLabel">Create or edit a AssistanceTicket</Translate>
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
                  id="assistance-ticket-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.assistanceTicket.subject')}
                id="assistance-ticket-subject"
                name="subject"
                data-cy="subject"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.description')}
                id="assistance-ticket-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.status')}
                id="assistance-ticket-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {ticketStatusValues.map(ticketStatus => (
                  <option value={ticketStatus} key={ticketStatus}>
                    {translate('adminApp.TicketStatus.' + ticketStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.assistanceTicket.type')}
                id="assistance-ticket-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {ticketTypeValues.map(ticketType => (
                  <option value={ticketType} key={ticketType}>
                    {translate('adminApp.TicketType.' + ticketType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.assistanceTicket.openedAt')}
                id="assistance-ticket-openedAt"
                name="openedAt"
                data-cy="openedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.closedAt')}
                id="assistance-ticket-closedAt"
                name="closedAt"
                data-cy="closedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.comments')}
                id="assistance-ticket-comments"
                name="comments"
                data-cy="comments"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.createdDate')}
                id="assistance-ticket-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.lastModifiedDate')}
                id="assistance-ticket-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.createdBy')}
                id="assistance-ticket-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.assistanceTicket.lastModifiedBy')}
                id="assistance-ticket-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                id="assistance-ticket-moderationAction"
                name="moderationAction"
                data-cy="moderationAction"
                label={translate('adminApp.assistanceTicket.moderationAction')}
                type="select"
              >
                <option value="" key="0" />
                {moderationActions
                  ? moderationActions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="assistance-ticket-assignedAdmin"
                name="assignedAdmin"
                data-cy="assignedAdmin"
                label={translate('adminApp.assistanceTicket.assignedAdmin')}
                type="select"
              >
                <option value="" key="0" />
                {adminUserProfiles
                  ? adminUserProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="assistance-ticket-user"
                name="user"
                data-cy="user"
                label={translate('adminApp.assistanceTicket.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/assistance-ticket" replace color="info">
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

export default AssistanceTicketUpdate;
