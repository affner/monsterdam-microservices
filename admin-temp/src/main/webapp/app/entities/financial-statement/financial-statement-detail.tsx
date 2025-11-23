import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './financial-statement.reducer';

export const FinancialStatementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const financialStatementEntity = useAppSelector(state => state.admin.financialStatement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="financialStatementDetailsHeading">
          <Translate contentKey="adminApp.financialStatement.detail.title">FinancialStatement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{financialStatementEntity.id}</dd>
          <dt>
            <span id="statementType">
              <Translate contentKey="adminApp.financialStatement.statementType">Statement Type</Translate>
            </span>
          </dt>
          <dd>{financialStatementEntity.statementType}</dd>
          <dt>
            <span id="periodStartDate">
              <Translate contentKey="adminApp.financialStatement.periodStartDate">Period Start Date</Translate>
            </span>
          </dt>
          <dd>
            {financialStatementEntity.periodStartDate ? (
              <TextFormat value={financialStatementEntity.periodStartDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="periodEndDate">
              <Translate contentKey="adminApp.financialStatement.periodEndDate">Period End Date</Translate>
            </span>
          </dt>
          <dd>
            {financialStatementEntity.periodEndDate ? (
              <TextFormat value={financialStatementEntity.periodEndDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.financialStatement.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {financialStatementEntity.createdDate ? (
              <TextFormat value={financialStatementEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.financialStatement.accountingRecords">Accounting Records</Translate>
          </dt>
          <dd>
            {financialStatementEntity.accountingRecords
              ? financialStatementEntity.accountingRecords.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {financialStatementEntity.accountingRecords && i === financialStatementEntity.accountingRecords.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/financial-statement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/financial-statement/${financialStatementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FinancialStatementDetail;
