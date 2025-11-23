import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { openFile, byteSize, Translate, translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './admin-system-configs.reducer';

export const AdminSystemConfigs = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const adminSystemConfigsList = useAppSelector(state => state.admin.adminSystemConfigs.entities);
  const loading = useAppSelector(state => state.admin.adminSystemConfigs.loading);
  const totalItems = useAppSelector(state => state.admin.adminSystemConfigs.totalItems);

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
      <h2 id="admin-system-configs-heading" data-cy="AdminSystemConfigsHeading">
        <Translate contentKey="adminApp.adminSystemConfigs.home.title">Admin System Configs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.adminSystemConfigs.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/admin-system-configs/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.adminSystemConfigs.home.createLabel">Create new Admin System Configs</Translate>
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
                  placeholder={translate('adminApp.adminSystemConfigs.home.search')}
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
        {adminSystemConfigsList && adminSystemConfigsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('configKey')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.configKey">Config Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('configKey')} />
                </th>
                <th className="hand" onClick={sort('configValue')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.configValue">Config Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('configValue')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('configValueType')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.configValueType">Config Value Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('configValueType')} />
                </th>
                <th className="hand" onClick={sort('configCategory')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.configCategory">Config Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('configCategory')} />
                </th>
                <th className="hand" onClick={sort('configFile')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.configFile">Config File</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('configFile')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="adminApp.adminSystemConfigs.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {adminSystemConfigsList.map((adminSystemConfigs, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/admin-system-configs/${adminSystemConfigs.id}`} color="link" size="sm">
                      {adminSystemConfigs.id}
                    </Button>
                  </td>
                  <td>{adminSystemConfigs.configKey}</td>
                  <td>{adminSystemConfigs.configValue}</td>
                  <td>{adminSystemConfigs.description}</td>
                  <td>
                    <Translate contentKey={`adminApp.ConfigurationValueType.${adminSystemConfigs.configValueType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.ConfigurationCategory.${adminSystemConfigs.configCategory}`} />
                  </td>
                  <td>
                    {adminSystemConfigs.configFile ? (
                      <div>
                        {adminSystemConfigs.configFileContentType ? (
                          <a onClick={openFile(adminSystemConfigs.configFileContentType, adminSystemConfigs.configFile)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {adminSystemConfigs.configFileContentType}, {byteSize(adminSystemConfigs.configFile)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {adminSystemConfigs.createdDate ? (
                      <TextFormat type="date" value={adminSystemConfigs.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {adminSystemConfigs.lastModifiedDate ? (
                      <TextFormat type="date" value={adminSystemConfigs.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{adminSystemConfigs.createdBy}</td>
                  <td>{adminSystemConfigs.lastModifiedBy}</td>
                  <td>{adminSystemConfigs.isActive ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/admin-system-configs/${adminSystemConfigs.id}`}
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
                        to={`/admin-system-configs/${adminSystemConfigs.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/admin-system-configs/${adminSystemConfigs.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.adminSystemConfigs.home.notFound">No Admin System Configs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={adminSystemConfigsList && adminSystemConfigsList.length > 0 ? '' : 'd-none'}>
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

export default AdminSystemConfigs;
