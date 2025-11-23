import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpecialReward } from 'app/shared/model/special-reward.model';
import { getEntity, updateEntity, createEntity, reset } from './special-reward.reducer';

export const SpecialRewardUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const specialRewardEntity = useAppSelector(state => state.admin.specialReward.entity);
  const loading = useAppSelector(state => state.admin.specialReward.loading);
  const updating = useAppSelector(state => state.admin.specialReward.updating);
  const updateSuccess = useAppSelector(state => state.admin.specialReward.updateSuccess);

  const handleClose = () => {
    navigate('/special-reward' + location.search);
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
    if (values.contentPackageId !== undefined && typeof values.contentPackageId !== 'number') {
      values.contentPackageId = Number(values.contentPackageId);
    }
    if (values.viewerId !== undefined && typeof values.viewerId !== 'number') {
      values.viewerId = Number(values.viewerId);
    }
    if (values.offerPromotionId !== undefined && typeof values.offerPromotionId !== 'number') {
      values.offerPromotionId = Number(values.offerPromotionId);
    }

    const entity = {
      ...specialRewardEntity,
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
          ...specialRewardEntity,
          createdDate: convertDateTimeFromServer(specialRewardEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(specialRewardEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.specialReward.home.createOrEditLabel" data-cy="SpecialRewardCreateUpdateHeading">
            <Translate contentKey="adminApp.specialReward.home.createOrEditLabel">Create or edit a SpecialReward</Translate>
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
                  id="special-reward-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.specialReward.description')}
                id="special-reward-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialReward.createdDate')}
                id="special-reward-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialReward.lastModifiedDate')}
                id="special-reward-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.specialReward.createdBy')}
                id="special-reward-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialReward.lastModifiedBy')}
                id="special-reward-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.specialReward.isDeleted')}
                id="special-reward-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.specialReward.contentPackageId')}
                id="special-reward-contentPackageId"
                name="contentPackageId"
                data-cy="contentPackageId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialReward.viewerId')}
                id="special-reward-viewerId"
                name="viewerId"
                data-cy="viewerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.specialReward.offerPromotionId')}
                id="special-reward-offerPromotionId"
                name="offerPromotionId"
                data-cy="offerPromotionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/special-reward" replace color="info">
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

export default SpecialRewardUpdate;
