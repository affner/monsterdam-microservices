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

import { searchEntities, getEntities } from './notification.reducer';

export const Notification = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const notificationList = useAppSelector(state => state.admin.notification.entities);
  const loading = useAppSelector(state => state.admin.notification.loading);
  const totalItems = useAppSelector(state => state.admin.notification.totalItems);

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
      <h2 id="notification-heading" data-cy="NotificationHeading">
        <Translate contentKey="adminApp.notification.home.title">Notifications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.notification.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/notification/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.notification.home.createLabel">Create new Notification</Translate>
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
                  placeholder={translate('adminApp.notification.home.search')}
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
        {notificationList && notificationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.notification.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('readDate')}>
                  <Translate contentKey="adminApp.notification.readDate">Read Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('readDate')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.notification.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.notification.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.notification.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.notification.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.notification.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('postCommentId')}>
                  <Translate contentKey="adminApp.notification.postCommentId">Post Comment Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postCommentId')} />
                </th>
                <th className="hand" onClick={sort('postFeedId')}>
                  <Translate contentKey="adminApp.notification.postFeedId">Post Feed Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postFeedId')} />
                </th>
                <th className="hand" onClick={sort('directMessageId')}>
                  <Translate contentKey="adminApp.notification.directMessageId">Direct Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('directMessageId')} />
                </th>
                <th className="hand" onClick={sort('userMentionId')}>
                  <Translate contentKey="adminApp.notification.userMentionId">User Mention Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userMentionId')} />
                </th>
                <th className="hand" onClick={sort('likeMarkId')}>
                  <Translate contentKey="adminApp.notification.likeMarkId">Like Mark Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likeMarkId')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.notification.commentedUser">Commented User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.notification.messagedUser">Messaged User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.notification.mentionerUserInPost">Mentioner User In Post</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.notification.mentionerUserInComment">Mentioner User In Comment</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {notificationList.map((notification, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/notification/${notification.id}`} color="link" size="sm">
                      {notification.id}
                    </Button>
                  </td>
                  <td>
                    {notification.readDate ? <TextFormat type="date" value={notification.readDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {notification.createdDate ? <TextFormat type="date" value={notification.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {notification.lastModifiedDate ? (
                      <TextFormat type="date" value={notification.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{notification.createdBy}</td>
                  <td>{notification.lastModifiedBy}</td>
                  <td>{notification.isDeleted ? 'true' : 'false'}</td>
                  <td>{notification.postCommentId}</td>
                  <td>{notification.postFeedId}</td>
                  <td>{notification.directMessageId}</td>
                  <td>{notification.userMentionId}</td>
                  <td>{notification.likeMarkId}</td>
                  <td>
                    {notification.commentedUser ? (
                      <Link to={`/user-profile/${notification.commentedUser.id}`}>{notification.commentedUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {notification.messagedUser ? (
                      <Link to={`/user-profile/${notification.messagedUser.id}`}>{notification.messagedUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {notification.mentionerUserInPost ? (
                      <Link to={`/user-profile/${notification.mentionerUserInPost.id}`}>{notification.mentionerUserInPost.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {notification.mentionerUserInComment ? (
                      <Link to={`/user-profile/${notification.mentionerUserInComment.id}`}>{notification.mentionerUserInComment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/notification/${notification.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/notification/${notification.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/notification/${notification.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.notification.home.notFound">No Notifications found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={notificationList && notificationList.length > 0 ? '' : 'd-none'}>
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

export default Notification;
