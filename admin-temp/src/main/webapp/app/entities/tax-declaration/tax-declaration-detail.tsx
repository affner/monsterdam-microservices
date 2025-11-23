import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tax-declaration.reducer';

export const TaxDeclarationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const taxDeclarationEntity = useAppSelector(state => state.admin.taxDeclaration.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taxDeclarationDetailsHeading">
          <Translate contentKey="adminApp.taxDeclaration.detail.title">TaxDeclaration</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.id}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="adminApp.taxDeclaration.year">Year</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.year}</dd>
          <dt>
            <span id="declarationType">
              <Translate contentKey="adminApp.taxDeclaration.declarationType">Declaration Type</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.declarationType}</dd>
          <dt>
            <span id="submittedDate">
              <Translate contentKey="adminApp.taxDeclaration.submittedDate">Submitted Date</Translate>
            </span>
          </dt>
          <dd>
            {taxDeclarationEntity.submittedDate ? (
              <TextFormat value={taxDeclarationEntity.submittedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="adminApp.taxDeclaration.status">Status</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.status}</dd>
          <dt>
            <span id="totalIncome">
              <Translate contentKey="adminApp.taxDeclaration.totalIncome">Total Income</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.totalIncome}</dd>
          <dt>
            <span id="totalTaxableIncome">
              <Translate contentKey="adminApp.taxDeclaration.totalTaxableIncome">Total Taxable Income</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.totalTaxableIncome}</dd>
          <dt>
            <span id="totalTaxPaid">
              <Translate contentKey="adminApp.taxDeclaration.totalTaxPaid">Total Tax Paid</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.totalTaxPaid}</dd>
          <dt>
            <span id="supportingDocumentsKey">
              <Translate contentKey="adminApp.taxDeclaration.supportingDocumentsKey">Supporting Documents Key</Translate>
            </span>
          </dt>
          <dd>{taxDeclarationEntity.supportingDocumentsKey}</dd>
          <dt>
            <Translate contentKey="adminApp.taxDeclaration.accountingRecords">Accounting Records</Translate>
          </dt>
          <dd>
            {taxDeclarationEntity.accountingRecords
              ? taxDeclarationEntity.accountingRecords.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {taxDeclarationEntity.accountingRecords && i === taxDeclarationEntity.accountingRecords.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/tax-declaration" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tax-declaration/${taxDeclarationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaxDeclarationDetail;
