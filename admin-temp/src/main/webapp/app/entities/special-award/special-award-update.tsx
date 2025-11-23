import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpecialAward } from 'app/shared/model/special-award.model';
import { getEntity, updateEntity, createEntity, reset } from './special-award.reducer';

export const SpecialAwardUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const specialAwardEntity = useAppSelector(state => state.admin.specialAward.entity);
  const loading = useAppSelector(state => state.admin.specialAward.loading);
  const updating = useAppSelector(state => state.admin.specialAward.updating);
  const updateSuccess = useAppSelector(state => state.admin.specialAward.updateSuccess);

  const handleClose = () => {
    navigate('/special-award' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.viewerId !== undefined && typeof values.viewerId !== 'number') {
      values.viewerId = Number(values.viewerId);
    }
    if (values.creatorId !== undefined && typeof values.creatorId !== 'number') {
      values.creatorId = Number(values.creatorId);
    }
    if (values.specialTitleId !== undefined && typeof values.specialTitleId !== 'number') {
      values.specialTitleId = Number(values.specialTitleId);
    }

    const entity = {
      ...specialAwardEntity,
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...specialAwardEntity,
          createdDate: convertDateTimeFromServer(specialAwardEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(specialAwardEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.specialAward.home.createOrEditLabel" data-cy="SpecialAwardCreateUpdateHeading">
            <Translate contentKey="adminApp.specialAward.home.createOrEditLabel">Create or edit a SpecialAward</Translate>
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
                  id="special-award-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.specialAward.startDate')}
                id="special-award-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialAward.endDate')}
                id="special-award-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialAward.reason')}
                id="special-award-reason"
                name="reason"
                data-cy="reason"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.altSpecialTitle')}
                id="special-award-altSpecialTitle"
                name="altSpecialTitle"
                data-cy="altSpecialTitle"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.createdDate')}
                id="special-award-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialAward.lastModifiedDate')}
                id="special-award-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.createdBy')}
                id="special-award-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.lastModifiedBy')}
                id="special-award-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.isDeleted')}
                id="special-award-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.specialAward.viewerId')}
                id="special-award-viewerId"
                name="viewerId"
                data-cy="viewerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialAward.creatorId')}
                id="special-award-creatorId"
                name="creatorId"
                data-cy="creatorId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialAward.specialTitleId')}
                id="special-award-specialTitleId"
                name="specialTitleId"
                data-cy="specialTitleId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/special-award" replace color="info">
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

export default SpecialAwardUpdate;
