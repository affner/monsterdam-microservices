import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBudget } from 'app/shared/model/budget.model';
import { getEntity, updateEntity, createEntity, reset } from './budget.reducer';

export const BudgetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const budgetEntity = useAppSelector(state => state.admin.budget.entity);
  const loading = useAppSelector(state => state.admin.budget.loading);
  const updating = useAppSelector(state => state.admin.budget.updating);
  const updateSuccess = useAppSelector(state => state.admin.budget.updateSuccess);

  const handleClose = () => {
    navigate('/budget' + location.search);
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
    if (values.year !== undefined && typeof values.year !== 'number') {
      values.year = Number(values.year);
    }
    if (values.totalBudget !== undefined && typeof values.totalBudget !== 'number') {
      values.totalBudget = Number(values.totalBudget);
    }
    if (values.spentAmount !== undefined && typeof values.spentAmount !== 'number') {
      values.spentAmount = Number(values.spentAmount);
    }
    if (values.remainingAmount !== undefined && typeof values.remainingAmount !== 'number') {
      values.remainingAmount = Number(values.remainingAmount);
    }

    const entity = {
      ...budgetEntity,
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
      ? {}
      : {
          ...budgetEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.budget.home.createOrEditLabel" data-cy="BudgetCreateUpdateHeading">
            <Translate contentKey="adminApp.budget.home.createOrEditLabel">Create or edit a Budget</Translate>
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
                  id="budget-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.budget.year')}
                id="budget-year"
                name="year"
                data-cy="year"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.budget.totalBudget')}
                id="budget-totalBudget"
                name="totalBudget"
                data-cy="totalBudget"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adminApp.budget.spentAmount')}
                id="budget-spentAmount"
                name="spentAmount"
                data-cy="spentAmount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.budget.remainingAmount')}
                id="budget-remainingAmount"
                name="remainingAmount"
                data-cy="remainingAmount"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.budget.budgetDetails')}
                id="budget-budgetDetails"
                name="budgetDetails"
                data-cy="budgetDetails"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/budget" replace color="info">
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

export default BudgetUpdate;
