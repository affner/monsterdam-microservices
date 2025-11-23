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

import { searchEntities, getEntities } from './accounting-record.reducer';

export const AccountingRecord = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const accountingRecordList = useAppSelector(state => state.admin.accountingRecord.entities);
  const loading = useAppSelector(state => state.admin.accountingRecord.loading);

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
      <h2 id="accounting-record-heading" data-cy="AccountingRecordHeading">
        <Translate contentKey="adminApp.accountingRecord.home.title">Accounting Records</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.accountingRecord.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/accounting-record/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.accountingRecord.home.createLabel">Create new Accounting Record</Translate>
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
                  placeholder={translate('adminApp.accountingRecord.home.search')}
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
        {accountingRecordList && accountingRecordList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.accountingRecord.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="adminApp.accountingRecord.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="adminApp.accountingRecord.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('debit')}>
                  <Translate contentKey="adminApp.accountingRecord.debit">Debit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('debit')} />
                </th>
                <th className="hand" onClick={sort('credit')}>
                  <Translate contentKey="adminApp.accountingRecord.credit">Credit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('credit')} />
                </th>
                <th className="hand" onClick={sort('balance')}>
                  <Translate contentKey="adminApp.accountingRecord.balance">Balance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('balance')} />
                </th>
                <th className="hand" onClick={sort('accountType')}>
                  <Translate contentKey="adminApp.accountingRecord.accountType">Account Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountType')} />
                </th>
                <th className="hand" onClick={sort('paymentId')}>
                  <Translate contentKey="adminApp.accountingRecord.paymentId">Payment Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentId')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.accountingRecord.budget">Budget</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.accountingRecord.asset">Asset</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.accountingRecord.liability">Liability</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {accountingRecordList.map((accountingRecord, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/accounting-record/${accountingRecord.id}`} color="link" size="sm">
                      {accountingRecord.id}
                    </Button>
                  </td>
                  <td>
                    {accountingRecord.date ? <TextFormat type="date" value={accountingRecord.date} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{accountingRecord.description}</td>
                  <td>{accountingRecord.debit}</td>
                  <td>{accountingRecord.credit}</td>
                  <td>{accountingRecord.balance}</td>
                  <td>
                    <Translate contentKey={`adminApp.AccountingType.${accountingRecord.accountType}`} />
                  </td>
                  <td>{accountingRecord.paymentId}</td>
                  <td>
                    {accountingRecord.budget ? <Link to={`/budget/${accountingRecord.budget.id}`}>{accountingRecord.budget.id}</Link> : ''}
                  </td>
                  <td>
                    {accountingRecord.asset ? <Link to={`/asset/${accountingRecord.asset.id}`}>{accountingRecord.asset.id}</Link> : ''}
                  </td>
                  <td>
                    {accountingRecord.liability ? (
                      <Link to={`/liability/${accountingRecord.liability.id}`}>{accountingRecord.liability.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/accounting-record/${accountingRecord.id}`}
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
                        to={`/accounting-record/${accountingRecord.id}/edit`}
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
                        onClick={() => (window.location.href = `/accounting-record/${accountingRecord.id}/delete`)}
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
              <Translate contentKey="adminApp.accountingRecord.home.notFound">No Accounting Records found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AccountingRecord;
