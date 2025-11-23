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

import { searchEntities, getEntities } from './identity-document.reducer';

export const IdentityDocument = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const identityDocumentList = useAppSelector(state => state.admin.identityDocument.entities);
  const loading = useAppSelector(state => state.admin.identityDocument.loading);
  const totalItems = useAppSelector(state => state.admin.identityDocument.totalItems);

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
      <h2 id="identity-document-heading" data-cy="IdentityDocumentHeading">
        <Translate contentKey="adminApp.identityDocument.home.title">Identity Documents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.identityDocument.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/identity-document/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.identityDocument.home.createLabel">Create new Identity Document</Translate>
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
                  placeholder={translate('adminApp.identityDocument.home.search')}
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
        {identityDocumentList && identityDocumentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.identityDocument.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('documentName')}>
                  <Translate contentKey="adminApp.identityDocument.documentName">Document Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentName')} />
                </th>
                <th className="hand" onClick={sort('documentDescription')}>
                  <Translate contentKey="adminApp.identityDocument.documentDescription">Document Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentDescription')} />
                </th>
                <th className="hand" onClick={sort('documentStatus')}>
                  <Translate contentKey="adminApp.identityDocument.documentStatus">Document Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentStatus')} />
                </th>
                <th className="hand" onClick={sort('documentType')}>
                  <Translate contentKey="adminApp.identityDocument.documentType">Document Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentType')} />
                </th>
                <th className="hand" onClick={sort('fileDocument')}>
                  <Translate contentKey="adminApp.identityDocument.fileDocument">File Document</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileDocument')} />
                </th>
                <th className="hand" onClick={sort('fileDocumentS3Key')}>
                  <Translate contentKey="adminApp.identityDocument.fileDocumentS3Key">File Document S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileDocumentS3Key')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.identityDocument.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.identityDocument.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.identityDocument.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.identityDocument.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.identityDocument.review">Review</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {identityDocumentList.map((identityDocument, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/identity-document/${identityDocument.id}`} color="link" size="sm">
                      {identityDocument.id}
                    </Button>
                  </td>
                  <td>{identityDocument.documentName}</td>
                  <td>{identityDocument.documentDescription}</td>
                  <td>
                    <Translate contentKey={`adminApp.DocumentStatus.${identityDocument.documentStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.DocumentType.${identityDocument.documentType}`} />
                  </td>
                  <td>
                    {identityDocument.fileDocument ? (
                      <div>
                        {identityDocument.fileDocumentContentType ? (
                          <a onClick={openFile(identityDocument.fileDocumentContentType, identityDocument.fileDocument)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {identityDocument.fileDocumentContentType}, {byteSize(identityDocument.fileDocument)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{identityDocument.fileDocumentS3Key}</td>
                  <td>
                    {identityDocument.createdDate ? (
                      <TextFormat type="date" value={identityDocument.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {identityDocument.lastModifiedDate ? (
                      <TextFormat type="date" value={identityDocument.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{identityDocument.createdBy}</td>
                  <td>{identityDocument.lastModifiedBy}</td>
                  <td>
                    {identityDocument.review ? (
                      <Link to={`/identity-document-review/${identityDocument.review.id}`}>{identityDocument.review.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/identity-document/${identityDocument.id}`}
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
                        to={`/identity-document/${identityDocument.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/identity-document/${identityDocument.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.identityDocument.home.notFound">No Identity Documents found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={identityDocumentList && identityDocumentList.length > 0 ? '' : 'd-none'}>
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

export default IdentityDocument;
