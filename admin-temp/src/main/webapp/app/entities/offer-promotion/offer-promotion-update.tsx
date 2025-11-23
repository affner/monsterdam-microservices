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
import { IOfferPromotion } from 'app/shared/model/offer-promotion.model';
import { OfferPromotionType } from 'app/shared/model/enumerations/offer-promotion-type.model';
import { getEntity, updateEntity, createEntity, reset } from './offer-promotion.reducer';

export const OfferPromotionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const offerPromotionEntity = useAppSelector(state => state.admin.offerPromotion.entity);
  const loading = useAppSelector(state => state.admin.offerPromotion.loading);
  const updating = useAppSelector(state => state.admin.offerPromotion.updating);
  const updateSuccess = useAppSelector(state => state.admin.offerPromotion.updateSuccess);
  const offerPromotionTypeValues = Object.keys(OfferPromotionType);

  const handleClose = () => {
    navigate('/offer-promotion' + location.search);
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
    if (values.discountPercentage !== undefined && typeof values.discountPercentage !== 'number') {
      values.discountPercentage = Number(values.discountPercentage);
    }
    if (values.subscriptionsLimit !== undefined && typeof values.subscriptionsLimit !== 'number') {
      values.subscriptionsLimit = Number(values.subscriptionsLimit);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...offerPromotionEntity,
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          promotionType: 'SPECIAL',
          ...offerPromotionEntity,
          createdDate: convertDateTimeFromServer(offerPromotionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(offerPromotionEntity.lastModifiedDate),
          creator: offerPromotionEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.offerPromotion.home.createOrEditLabel" data-cy="OfferPromotionCreateUpdateHeading">
            <Translate contentKey="adminApp.offerPromotion.home.createOrEditLabel">Create or edit a OfferPromotion</Translate>
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
                  id="offer-promotion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.offerPromotion.freeDaysDuration')}
                id="offer-promotion-freeDaysDuration"
                name="freeDaysDuration"
                data-cy="freeDaysDuration"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.discountPercentage')}
                id="offer-promotion-discountPercentage"
                name="discountPercentage"
                data-cy="discountPercentage"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.startDate')}
                id="offer-promotion-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.endDate')}
                id="offer-promotion-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.subscriptionsLimit')}
                id="offer-promotion-subscriptionsLimit"
                name="subscriptionsLimit"
                data-cy="subscriptionsLimit"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.linkCode')}
                id="offer-promotion-linkCode"
                name="linkCode"
                data-cy="linkCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.isFinished')}
                id="offer-promotion-isFinished"
                name="isFinished"
                data-cy="isFinished"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.promotionType')}
                id="offer-promotion-promotionType"
                name="promotionType"
                data-cy="promotionType"
                type="select"
              >
                {offerPromotionTypeValues.map(offerPromotionType => (
                  <option value={offerPromotionType} key={offerPromotionType}>
                    {translate('adminApp.OfferPromotionType.' + offerPromotionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.offerPromotion.createdDate')}
                id="offer-promotion-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.lastModifiedDate')}
                id="offer-promotion-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.createdBy')}
                id="offer-promotion-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.lastModifiedBy')}
                id="offer-promotion-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.offerPromotion.isDeleted')}
                id="offer-promotion-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="offer-promotion-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.offerPromotion.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/offer-promotion" replace color="info">
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

export default OfferPromotionUpdate;
