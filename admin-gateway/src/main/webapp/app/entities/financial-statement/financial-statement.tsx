import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './financial-statement.reducer';

export const FinancialStatement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const financialStatementList = useAppSelector(state => state.admin.financialStatement.entities);
  const loading = useAppSelector(state => state.admin.financialStatement.loading);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, search]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="financial-statement-heading" data-cy="FinancialStatementHeading">
        <Translate contentKey="adminApp.financialStatement.home.title">Financial Statements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.financialStatement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/financial-statement/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.financialStatement.home.createLabel">Create new Financial Statement</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('adminApp.financialStatement.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {financialStatementList && financialStatementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.financialStatement.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('statementType')}>
                  <Translate contentKey="adminApp.financialStatement.statementType">Statement Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statementType')} />
                </th>
                <th className="hand" onClick={sort('periodStartDate')}>
                  <Translate contentKey="adminApp.financialStatement.periodStartDate">Period Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('periodStartDate')} />
                </th>
                <th className="hand" onClick={sort('periodEndDate')}>
                  <Translate contentKey="adminApp.financialStatement.periodEndDate">Period End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('periodEndDate')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.financialStatement.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.financialStatement.accountingRecords">Accounting Records</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {financialStatementList.map((financialStatement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/financial-statement/${financialStatement.id}`} color="link" size="sm">
                      {financialStatement.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.StatementType.${financialStatement.statementType}`} />
                  </td>
                  <td>
                    {financialStatement.periodStartDate ? (
                      <TextFormat type="date" value={financialStatement.periodStartDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {financialStatement.periodEndDate ? (
                      <TextFormat type="date" value={financialStatement.periodEndDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {financialStatement.createdDate ? (
                      <TextFormat type="date" value={financialStatement.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {financialStatement.accountingRecords
                      ? financialStatement.accountingRecords.map((val, j) => (
                          <span key={j}>
                            <Link to={`/accounting-record/${val.id}`}>{val.id}</Link>
                            {j === financialStatement.accountingRecords.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/financial-statement/${financialStatement.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/financial-statement/${financialStatement.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/financial-statement/${financialStatement.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="adminApp.financialStatement.home.notFound">No Financial Statements found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FinancialStatement;
