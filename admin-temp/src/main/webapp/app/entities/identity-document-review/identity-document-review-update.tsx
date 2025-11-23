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
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';
import { getEntity, updateEntity, createEntity, reset } from './identity-document-review.reducer';

export const IdentityDocumentReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assistanceTickets = useAppSelector(state => state.admin.assistanceTicket.entities);
  const identityDocumentReviewEntity = useAppSelector(state => state.admin.identityDocumentReview.entity);
  const loading = useAppSelector(state => state.admin.identityDocumentReview.loading);
  const updating = useAppSelector(state => state.admin.identityDocumentReview.updating);
  const updateSuccess = useAppSelector(state => state.admin.identityDocumentReview.updateSuccess);
  const documentStatusValues = Object.keys(DocumentStatus);
  const reviewStatusValues = Object.keys(ReviewStatus);

  const handleClose = () => {
    navigate('/identity-document-review' + location.search);
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
    values.resolutionDate = convertDateTimeToServer(values.resolutionDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...identityDocumentReviewEntity,
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
          resolutionDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          documentStatus: 'PENDING',
          reviewStatus: 'REVIEWING',
          ...identityDocumentReviewEntity,
          resolutionDate: convertDateTimeFromServer(identityDocumentReviewEntity.resolutionDate),
          createdDate: convertDateTimeFromServer(identityDocumentReviewEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(identityDocumentReviewEntity.lastModifiedDate),
          ticket: identityDocumentReviewEntity?.ticket?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.identityDocumentReview.home.createOrEditLabel" data-cy="IdentityDocumentReviewCreateUpdateHeading">
            <Translate contentKey="adminApp.identityDocumentReview.home.createOrEditLabel">
              Create or edit a IdentityDocumentReview
            </Translate>
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
                  id="identity-document-review-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.documentStatus')}
                id="identity-document-review-documentStatus"
                name="documentStatus"
                data-cy="documentStatus"
                type="select"
              >
                {documentStatusValues.map(documentStatus => (
                  <option value={documentStatus} key={documentStatus}>
                    {translate('adminApp.DocumentStatus.' + documentStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.resolutionDate')}
                id="identity-document-review-resolutionDate"
                name="resolutionDate"
                data-cy="resolutionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.reviewStatus')}
                id="identity-document-review-reviewStatus"
                name="reviewStatus"
                data-cy="reviewStatus"
                type="select"
              >
                {reviewStatusValues.map(reviewStatus => (
                  <option value={reviewStatus} key={reviewStatus}>
                    {translate('adminApp.ReviewStatus.' + reviewStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.createdDate')}
                id="identity-document-review-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.lastModifiedDate')}
                id="identity-document-review-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.createdBy')}
                id="identity-document-review-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.identityDocumentReview.lastModifiedBy')}
                id="identity-document-review-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                id="identity-document-review-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('adminApp.identityDocumentReview.ticket')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/identity-document-review" replace color="info">
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

export default IdentityDocumentReviewUpdate;
