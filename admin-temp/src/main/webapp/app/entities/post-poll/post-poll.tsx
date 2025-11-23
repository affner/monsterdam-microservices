import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { byteSize, Translate, translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DurationFormat } from 'app/shared/DurationFormat';

import { searchEntities, getEntities } from './post-poll.reducer';

export const PostPoll = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const postPollList = useAppSelector(state => state.admin.postPoll.entities);
  const loading = useAppSelector(state => state.admin.postPoll.loading);
  const totalItems = useAppSelector(state => state.admin.postPoll.totalItems);

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
      <h2 id="post-poll-heading" data-cy="PostPollHeading">
        <Translate contentKey="adminApp.postPoll.home.title">Post Polls</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.postPoll.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/post-poll/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.postPoll.home.createLabel">Create new Post Poll</Translate>
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
                  placeholder={translate('adminApp.postPoll.home.search')}
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
        {postPollList && postPollList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.postPoll.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('question')}>
                  <Translate contentKey="adminApp.postPoll.question">Question</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('question')} />
                </th>
                <th className="hand" onClick={sort('isMultiChoice')}>
                  <Translate contentKey="adminApp.postPoll.isMultiChoice">Is Multi Choice</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isMultiChoice')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.postPoll.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="adminApp.postPoll.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('postPollDuration')}>
                  <Translate contentKey="adminApp.postPoll.postPollDuration">Post Poll Duration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postPollDuration')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.postPoll.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.postPoll.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.postPoll.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.postPoll.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {postPollList.map((postPoll, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/post-poll/${postPoll.id}`} color="link" size="sm">
                      {postPoll.id}
                    </Button>
                  </td>
                  <td>{postPoll.question}</td>
                  <td>{postPoll.isMultiChoice ? 'true' : 'false'}</td>
                  <td>
                    {postPoll.lastModifiedDate ? (
                      <TextFormat type="date" value={postPoll.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{postPoll.endDate ? <TextFormat type="date" value={postPoll.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{postPoll.postPollDuration ? <DurationFormat value={postPoll.postPollDuration} /> : null}</td>
                  <td>{postPoll.createdDate ? <TextFormat type="date" value={postPoll.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{postPoll.createdBy}</td>
                  <td>{postPoll.lastModifiedBy}</td>
                  <td>{postPoll.isDeleted ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/post-poll/${postPoll.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/post-poll/${postPoll.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/post-poll/${postPoll.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.postPoll.home.notFound">No Post Polls found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={postPollList && postPollList.length > 0 ? '' : 'd-none'}>
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

export default PostPoll;
