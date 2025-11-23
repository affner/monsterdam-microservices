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
import { IUserReport } from 'app/shared/model/user-report.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { ReportCategory } from 'app/shared/model/enumerations/report-category.model';
import { getEntity, updateEntity, createEntity, reset } from './user-report.reducer';

export const UserReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assistanceTickets = useAppSelector(state => state.admin.assistanceTicket.entities);
  const userReportEntity = useAppSelector(state => state.admin.userReport.entity);
  const loading = useAppSelector(state => state.admin.userReport.loading);
  const updating = useAppSelector(state => state.admin.userReport.updating);
  const updateSuccess = useAppSelector(state => state.admin.userReport.updateSuccess);
  const reportStatusValues = Object.keys(ReportStatus);
  const reportCategoryValues = Object.keys(ReportCategory);

  const handleClose = () => {
    navigate('/user-report');
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.reporterId !== undefined && typeof values.reporterId !== 'number') {
      values.reporterId = Number(values.reporterId);
    }
    if (values.reportedId !== undefined && typeof values.reportedId !== 'number') {
      values.reportedId = Number(values.reportedId);
    }
    if (values.multimediaId !== undefined && typeof values.multimediaId !== 'number') {
      values.multimediaId = Number(values.multimediaId);
    }
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }
    if (values.commentId !== undefined && typeof values.commentId !== 'number') {
      values.commentId = Number(values.commentId);
    }

    const entity = {
      ...userReportEntity,
      ...values,
      ticket: assistanceTickets.find(it => it.id.toString() === values.ticket.toString()),
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
          status: 'PENDING',
          reportCategory: 'POST_REPORT',
          ...userReportEntity,
          createdDate: convertDateTimeFromServer(userReportEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userReportEntity.lastModifiedDate),
          ticket: userReportEntity?.ticket?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userReport.home.createOrEditLabel" data-cy="UserReportCreateUpdateHeading">
            <Translate contentKey="adminApp.userReport.home.createOrEditLabel">Create or edit a UserReport</Translate>
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
                  id="user-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.userReport.reportDescription')}
                id="user-report-reportDescription"
                name="reportDescription"
                data-cy="reportDescription"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.status')}
                id="user-report-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reportStatusValues.map(reportStatus => (
                  <option value={reportStatus} key={reportStatus}>
                    {translate('adminApp.ReportStatus.' + reportStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userReport.createdDate')}
                id="user-report-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userReport.lastModifiedDate')}
                id="user-report-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userReport.createdBy')}
                id="user-report-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.lastModifiedBy')}
                id="user-report-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.isDeleted')}
                id="user-report-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userReport.reportCategory')}
                id="user-report-reportCategory"
                name="reportCategory"
                data-cy="reportCategory"
                type="select"
              >
                {reportCategoryValues.map(reportCategory => (
                  <option value={reportCategory} key={reportCategory}>
                    {translate('adminApp.ReportCategory.' + reportCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userReport.reporterId')}
                id="user-report-reporterId"
                name="reporterId"
                data-cy="reporterId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.userReport.reportedId')}
                id="user-report-reportedId"
                name="reportedId"
                data-cy="reportedId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.userReport.multimediaId')}
                id="user-report-multimediaId"
                name="multimediaId"
                data-cy="multimediaId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.messageId')}
                id="user-report-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.postId')}
                id="user-report-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.commentId')}
                id="user-report-commentId"
                name="commentId"
                data-cy="commentId"
                type="text"
              />
              <ValidatedField
                id="user-report-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('adminApp.userReport.ticket')}
                type="select"
                required
              >
                <option value="" key="0" />
                {assistanceTickets
                  ? assistanceTickets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-report" replace color="info">
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

export default UserReportUpdate;
