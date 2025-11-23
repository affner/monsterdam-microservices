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
import { IMoneyPayout } from 'app/shared/model/money-payout.model';
import { getEntities as getMoneyPayouts } from 'app/entities/money-payout/money-payout.reducer';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { getEntities as getPurchasedContents } from 'app/entities/purchased-content/purchased-content.reducer';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { getEntities as getPurchasedSubscriptions } from 'app/entities/purchased-subscription/purchased-subscription.reducer';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { getEntities as getPurchasedTips } from 'app/entities/purchased-tip/purchased-tip.reducer';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { getEntity, updateEntity, createEntity, reset } from './creator-earning.reducer';

export const CreatorEarningUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const moneyPayouts = useAppSelector(state => state.admin.moneyPayout.entities);
  const purchasedContents = useAppSelector(state => state.admin.purchasedContent.entities);
  const purchasedSubscriptions = useAppSelector(state => state.admin.purchasedSubscription.entities);
  const purchasedTips = useAppSelector(state => state.admin.purchasedTip.entities);
  const creatorEarningEntity = useAppSelector(state => state.admin.creatorEarning.entity);
  const loading = useAppSelector(state => state.admin.creatorEarning.loading);
  const updating = useAppSelector(state => state.admin.creatorEarning.updating);
  const updateSuccess = useAppSelector(state => state.admin.creatorEarning.updateSuccess);

  const handleClose = () => {
    navigate('/creator-earning' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getMoneyPayouts({}));
    dispatch(getPurchasedContents({}));
    dispatch(getPurchasedSubscriptions({}));
    dispatch(getPurchasedTips({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...creatorEarningEntity,
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
          ...creatorEarningEntity,
          createdDate: convertDateTimeFromServer(creatorEarningEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(creatorEarningEntity.lastModifiedDate),
          creator: creatorEarningEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.creatorEarning.home.createOrEditLabel" data-cy="CreatorEarningCreateUpdateHeading">
            <Translate contentKey="adminApp.creatorEarning.home.createOrEditLabel">Create or edit a CreatorEarning</Translate>
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
                  id="creator-earning-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.creatorEarning.amount')}
                id="creator-earning-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.creatorEarning.createdDate')}
                id="creator-earning-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.creatorEarning.lastModifiedDate')}
                id="creator-earning-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.creatorEarning.createdBy')}
                id="creator-earning-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.creatorEarning.lastModifiedBy')}
                id="creator-earning-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.creatorEarning.isDeleted')}
                id="creator-earning-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="creator-earning-creator"
                name="creator"
                data-cy="creator"
                label={translate('adminApp.creatorEarning.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/creator-earning" replace color="info">
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

export default CreatorEarningUpdate;
