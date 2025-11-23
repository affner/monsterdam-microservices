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

import { searchEntities, getEntities, reset } from './post-comment.reducer';

export const PostComment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const postCommentList = useAppSelector(state => state.admin.postComment.entities);
  const loading = useAppSelector(state => state.admin.postComment.loading);
  const links = useAppSelector(state => state.admin.postComment.links);
  const updateSuccess = useAppSelector(state => state.admin.postComment.updateSuccess);

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
      <h2 id="post-comment-heading" data-cy="PostCommentHeading">
        <Translate contentKey="adminApp.postComment.home.title">Post Comments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.postComment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/post-comment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.postComment.home.createLabel">Create new Post Comment</Translate>
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
                  placeholder={translate('adminApp.postComment.home.search')}
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
          dataLength={postCommentList ? postCommentList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {postCommentList && postCommentList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="adminApp.postComment.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('commentContent')}>
                    <Translate contentKey="adminApp.postComment.commentContent">Comment Content</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('commentContent')} />
                  </th>
                  <th className="hand" onClick={sort('likeCount')}>
                    <Translate contentKey="adminApp.postComment.likeCount">Like Count</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                  </th>
                  <th className="hand" onClick={sort('createdDate')}>
                    <Translate contentKey="adminApp.postComment.createdDate">Created Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedDate')}>
                    <Translate contentKey="adminApp.postComment.lastModifiedDate">Last Modified Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                  </th>
                  <th className="hand" onClick={sort('createdBy')}>
                    <Translate contentKey="adminApp.postComment.createdBy">Created By</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedBy')}>
                    <Translate contentKey="adminApp.postComment.lastModifiedBy">Last Modified By</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                  </th>
                  <th className="hand" onClick={sort('isDeleted')}>
                    <Translate contentKey="adminApp.postComment.isDeleted">Is Deleted</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postComment.post">Post</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postComment.responseTo">Response To</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="adminApp.postComment.commenter">Commenter</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {postCommentList.map((postComment, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/post-comment/${postComment.id}`} color="link" size="sm">
                        {postComment.id}
                      </Button>
                    </td>
                    <td>{postComment.commentContent}</td>
                    <td>{postComment.likeCount}</td>
                    <td>
                      {postComment.createdDate ? <TextFormat type="date" value={postComment.createdDate} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>
                      {postComment.lastModifiedDate ? (
                        <TextFormat type="date" value={postComment.lastModifiedDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{postComment.createdBy}</td>
                    <td>{postComment.lastModifiedBy}</td>
                    <td>{postComment.isDeleted ? 'true' : 'false'}</td>
                    <td>{postComment.post ? <Link to={`/post-feed/${postComment.post.id}`}>{postComment.post.postContent}</Link> : ''}</td>
                    <td>
                      {postComment.responseTo ? (
                        <Link to={`/post-comment/${postComment.responseTo.id}`}>{postComment.responseTo.commentContent}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {postComment.commenter ? (
                        <Link to={`/user-profile/${postComment.commenter.id}`}>{postComment.commenter.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/post-comment/${postComment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/post-comment/${postComment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/post-comment/${postComment.id}/delete`)}
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
                <Translate contentKey="adminApp.postComment.home.notFound">No Post Comments found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default PostComment;
