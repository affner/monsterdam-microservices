import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { byteSize, Translate, translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities, reset } from './post-feed.reducer';

export const PostFeed = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const postFeedList = useAppSelector(state => state.admin.postFeed.entities);
  const loading = useAppSelector(state => state.admin.postFeed.loading);
  const links = useAppSelector(state => state.admin.postFeed.links);
  const updateSuccess = useAppSelector(state => state.admin.postFeed.updateSuccess);

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

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(reset());
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
    dispatch(reset());
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting, search]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
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
      <h2 id="post-feed-heading" data-cy="PostFeedHeading">
        <Translate contentKey="adminApp.postFeed.home.title">Post Feeds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.postFeed.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/post-feed/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.postFeed.home.createLabel">Create new Post Feed</Translate>
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
                  placeholder={translate('adminApp.postFeed.home.search')}
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
        <InfiniteScroll
          dataLength={postFeedList ? postFeedList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {postFeedList && postFeedList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="adminApp.postFeed.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('postContent')}>
                    <Translate contentKey="adminApp.postFeed.postContent">Post Content</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('postContent')} />
                  </th>
                  <th className="hand" onClick={sort('isHidden')}>
                    <Translate contentKey="adminApp.postFeed.isHidden">Is Hidden</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('isHidden')} />
                  </th>
                  <th className="hand" onClick={sort('pinnedPost')}>
                    <Translate contentKey="adminApp.postFeed.pinnedPost">Pinned Post</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('pinnedPost')} />
                  </th>
                  <th className="hand" onClick={sort('likeCount')}>
                    <Translate contentKey="adminApp.postFeed.likeCount">Like Count</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                  </th>
                  <th className="hand" onClick={sort('createdDate')}>
                    <Translate contentKey="adminApp.postFeed.createdDate">Created Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedDate')}>
                    <Translate contentKey="adminApp.postFeed.lastModifiedDate">Last Modified Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                  </th>
                  <th className="hand" onClick={sort('createdBy')}>
                    <Translate contentKey="adminApp.postFeed.createdBy">Created By</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedBy')}>
                    <Translate contentKey="adminApp.postFeed.lastModifiedBy">Last Modified By</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                  </th>
                  <th className="hand" onClick={sort('isDeleted')}>
                    <Translate contentKey="adminApp.postFeed.isDeleted">Is Deleted</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postFeed.poll">Poll</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postFeed.contentPackage">Content Package</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postFeed.creator">Creator</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {postFeedList.map((postFeed, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/post-feed/${postFeed.id}`} color="link" size="sm">
                        {postFeed.id}
                      </Button>
                    </td>
                    <td>{postFeed.postContent}</td>
                    <td>{postFeed.isHidden ? 'true' : 'false'}</td>
                    <td>{postFeed.pinnedPost ? 'true' : 'false'}</td>
                    <td>{postFeed.likeCount}</td>
                    <td>
                      {postFeed.createdDate ? <TextFormat type="date" value={postFeed.createdDate} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>
                      {postFeed.lastModifiedDate ? (
                        <TextFormat type="date" value={postFeed.lastModifiedDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{postFeed.createdBy}</td>
                    <td>{postFeed.lastModifiedBy}</td>
                    <td>{postFeed.isDeleted ? 'true' : 'false'}</td>
                    <td>{postFeed.poll ? <Link to={`/post-poll/${postFeed.poll.id}`}>{postFeed.poll.question}</Link> : ''}</td>
                    <td>
                      {postFeed.contentPackage ? (
                        <Link to={`/content-package/${postFeed.contentPackage.id}`}>{postFeed.contentPackage.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{postFeed.creator ? <Link to={`/user-profile/${postFeed.creator.id}`}>{postFeed.creator.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/post-feed/${postFeed.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/post-feed/${postFeed.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/post-feed/${postFeed.id}/delete`)}
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
                <Translate contentKey="adminApp.postFeed.home.notFound">No Post Feeds found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default PostFeed;
