import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './tax-declaration.reducer';

export const TaxDeclaration = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const taxDeclarationList = useAppSelector(state => state.admin.taxDeclaration.entities);
  const loading = useAppSelector(state => state.admin.taxDeclaration.loading);
  const totalItems = useAppSelector(state => state.admin.taxDeclaration.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="tax-declaration-heading" data-cy="TaxDeclarationHeading">
        <Translate contentKey="adminApp.taxDeclaration.home.title">Tax Declarations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.taxDeclaration.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/tax-declaration/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.taxDeclaration.home.createLabel">Create new Tax Declaration</Translate>
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
                  placeholder={translate('adminApp.taxDeclaration.home.search')}
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
        {taxDeclarationList && taxDeclarationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.taxDeclaration.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('year')}>
                  <Translate contentKey="adminApp.taxDeclaration.year">Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('year')} />
                </th>
                <th className="hand" onClick={sort('declarationType')}>
                  <Translate contentKey="adminApp.taxDeclaration.declarationType">Declaration Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('declarationType')} />
                </th>
                <th className="hand" onClick={sort('submittedDate')}>
                  <Translate contentKey="adminApp.taxDeclaration.submittedDate">Submitted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('submittedDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="adminApp.taxDeclaration.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('totalIncome')}>
                  <Translate contentKey="adminApp.taxDeclaration.totalIncome">Total Income</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalIncome')} />
                </th>
                <th className="hand" onClick={sort('totalTaxableIncome')}>
                  <Translate contentKey="adminApp.taxDeclaration.totalTaxableIncome">Total Taxable Income</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalTaxableIncome')} />
                </th>
                <th className="hand" onClick={sort('totalTaxPaid')}>
                  <Translate contentKey="adminApp.taxDeclaration.totalTaxPaid">Total Tax Paid</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalTaxPaid')} />
                </th>
                <th className="hand" onClick={sort('supportingDocumentsKey')}>
                  <Translate contentKey="adminApp.taxDeclaration.supportingDocumentsKey">Supporting Documents Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('supportingDocumentsKey')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taxDeclarationList.map((taxDeclaration, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/tax-declaration/${taxDeclaration.id}`} color="link" size="sm">
                      {taxDeclaration.id}
                    </Button>
                  </td>
                  <td>{taxDeclaration.year}</td>
                  <td>
                    <Translate contentKey={`adminApp.TaxDeclarationType.${taxDeclaration.declarationType}`} />
                  </td>
                  <td>
                    {taxDeclaration.submittedDate ? (
                      <TextFormat type="date" value={taxDeclaration.submittedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.TaxDeclarationStatus.${taxDeclaration.status}`} />
                  </td>
                  <td>{taxDeclaration.totalIncome}</td>
                  <td>{taxDeclaration.totalTaxableIncome}</td>
                  <td>{taxDeclaration.totalTaxPaid}</td>
                  <td>{taxDeclaration.supportingDocumentsKey}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/tax-declaration/${taxDeclaration.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/tax-declaration/${taxDeclaration.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/tax-declaration/${taxDeclaration.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="adminApp.taxDeclaration.home.notFound">No Tax Declarations found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={taxDeclarationList && taxDeclarationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default TaxDeclaration;
