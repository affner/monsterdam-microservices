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

import { searchEntities, getEntities } from './single-live-stream.reducer';

export const SingleLiveStream = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const singleLiveStreamList = useAppSelector(state => state.admin.singleLiveStream.entities);
  const loading = useAppSelector(state => state.admin.singleLiveStream.loading);
  const totalItems = useAppSelector(state => state.admin.singleLiveStream.totalItems);

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
      <h2 id="single-live-stream-heading" data-cy="SingleLiveStreamHeading">
        <Translate contentKey="adminApp.singleLiveStream.home.title">Single Live Streams</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.singleLiveStream.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/single-live-stream/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.singleLiveStream.home.createLabel">Create new Single Live Stream</Translate>
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
                  placeholder={translate('adminApp.singleLiveStream.home.search')}
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
        {singleLiveStreamList && singleLiveStreamList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.singleLiveStream.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="adminApp.singleLiveStream.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="adminApp.singleLiveStream.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('thumbnail')}>
                  <Translate contentKey="adminApp.singleLiveStream.thumbnail">Thumbnail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnail')} />
                </th>
                <th className="hand" onClick={sort('thumbnailS3Key')}>
                  <Translate contentKey="adminApp.singleLiveStream.thumbnailS3Key">Thumbnail S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnailS3Key')} />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="adminApp.singleLiveStream.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="adminApp.singleLiveStream.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th className="hand" onClick={sort('liveContent')}>
                  <Translate contentKey="adminApp.singleLiveStream.liveContent">Live Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('liveContent')} />
                </th>
                <th className="hand" onClick={sort('liveContentS3Key')}>
                  <Translate contentKey="adminApp.singleLiveStream.liveContentS3Key">Live Content S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('liveContentS3Key')} />
                </th>
                <th className="hand" onClick={sort('isRecorded')}>
                  <Translate contentKey="adminApp.singleLiveStream.isRecorded">Is Recorded</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isRecorded')} />
                </th>
                <th className="hand" onClick={sort('likeCount')}>
                  <Translate contentKey="adminApp.singleLiveStream.likeCount">Like Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.singleLiveStream.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.singleLiveStream.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.singleLiveStream.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.singleLiveStream.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.singleLiveStream.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {singleLiveStreamList.map((singleLiveStream, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/single-live-stream/${singleLiveStream.id}`} color="link" size="sm">
                      {singleLiveStream.id}
                    </Button>
                  </td>
                  <td>{singleLiveStream.title}</td>
                  <td>{singleLiveStream.description}</td>
                  <td>
                    {singleLiveStream.thumbnail ? (
                      <div>
                        {singleLiveStream.thumbnailContentType ? (
                          <a onClick={openFile(singleLiveStream.thumbnailContentType, singleLiveStream.thumbnail)}>
                            <img
                              src={`data:${singleLiveStream.thumbnailContentType};base64,${singleLiveStream.thumbnail}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singleLiveStream.thumbnailContentType}, {byteSize(singleLiveStream.thumbnail)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singleLiveStream.thumbnailS3Key}</td>
                  <td>
                    {singleLiveStream.startTime ? (
                      <TextFormat type="date" value={singleLiveStream.startTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {singleLiveStream.endTime ? <TextFormat type="date" value={singleLiveStream.endTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {singleLiveStream.liveContent ? (
                      <div>
                        {singleLiveStream.liveContentContentType ? (
                          <a onClick={openFile(singleLiveStream.liveContentContentType, singleLiveStream.liveContent)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singleLiveStream.liveContentContentType}, {byteSize(singleLiveStream.liveContent)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singleLiveStream.liveContentS3Key}</td>
                  <td>{singleLiveStream.isRecorded ? 'true' : 'false'}</td>
                  <td>{singleLiveStream.likeCount}</td>
                  <td>
                    {singleLiveStream.createdDate ? (
                      <TextFormat type="date" value={singleLiveStream.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {singleLiveStream.lastModifiedDate ? (
                      <TextFormat type="date" value={singleLiveStream.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{singleLiveStream.createdBy}</td>
                  <td>{singleLiveStream.lastModifiedBy}</td>
                  <td>{singleLiveStream.isDeleted ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/single-live-stream/${singleLiveStream.id}`}
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
                        to={`/single-live-stream/${singleLiveStream.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/single-live-stream/${singleLiveStream.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.singleLiveStream.home.notFound">No Single Live Streams found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={singleLiveStreamList && singleLiveStreamList.length > 0 ? '' : 'd-none'}>
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

export default SingleLiveStream;
