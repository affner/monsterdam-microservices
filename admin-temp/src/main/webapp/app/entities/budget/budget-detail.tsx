import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './budget.reducer';

export const BudgetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const budgetEntity = useAppSelector(state => state.admin.budget.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="budgetDetailsHeading">
          <Translate contentKey="adminApp.budget.detail.title">Budget</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.id}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="adminApp.budget.year">Year</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.year}</dd>
          <dt>
            <span id="totalBudget">
              <Translate contentKey="adminApp.budget.totalBudget">Total Budget</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.totalBudget}</dd>
          <dt>
            <span id="spentAmount">
              <Translate contentKey="adminApp.budget.spentAmount">Spent Amount</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.spentAmount}</dd>
          <dt>
            <span id="remainingAmount">
              <Translate contentKey="adminApp.budget.remainingAmount">Remaining Amount</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.remainingAmount}</dd>
          <dt>
            <span id="budgetDetails">
              <Translate contentKey="adminApp.budget.budgetDetails">Budget Details</Translate>
            </span>
          </dt>
          <dd>{budgetEntity.budgetDetails}</dd>
        </dl>
        <Button tag={Link} to="/budget" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/budget/${budgetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BudgetDetail;
