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

import { searchEntities, getEntities } from './user-settings.reducer';

export const UserSettings = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userSettingsList = useAppSelector(state => state.admin.userSettings.entities);
  const loading = useAppSelector(state => state.admin.userSettings.loading);
  const totalItems = useAppSelector(state => state.admin.userSettings.totalItems);

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
      <h2 id="user-settings-heading" data-cy="UserSettingsHeading">
        <Translate contentKey="adminApp.userSettings.home.title">User Settings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.userSettings.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-settings/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.userSettings.home.createLabel">Create new User Settings</Translate>
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
                  placeholder={translate('adminApp.userSettings.home.search')}
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
        {userSettingsList && userSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.userSettings.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.userSettings.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('darkMode')}>
                  <Translate contentKey="adminApp.userSettings.darkMode">Dark Mode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('darkMode')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  <Translate contentKey="adminApp.userSettings.language">Language</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('contentFilter')}>
                  <Translate contentKey="adminApp.userSettings.contentFilter">Content Filter</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contentFilter')} />
                </th>
                <th className="hand" onClick={sort('messageBlurIntensity')}>
                  <Translate contentKey="adminApp.userSettings.messageBlurIntensity">Message Blur Intensity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageBlurIntensity')} />
                </th>
                <th className="hand" onClick={sort('activityStatusVisibility')}>
                  <Translate contentKey="adminApp.userSettings.activityStatusVisibility">Activity Status Visibility</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activityStatusVisibility')} />
                </th>
                <th className="hand" onClick={sort('twoFactorAuthentication')}>
                  <Translate contentKey="adminApp.userSettings.twoFactorAuthentication">Two Factor Authentication</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('twoFactorAuthentication')} />
                </th>
                <th className="hand" onClick={sort('sessionsActiveCount')}>
                  <Translate contentKey="adminApp.userSettings.sessionsActiveCount">Sessions Active Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sessionsActiveCount')} />
                </th>
                <th className="hand" onClick={sort('emailNotifications')}>
                  <Translate contentKey="adminApp.userSettings.emailNotifications">Email Notifications</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailNotifications')} />
                </th>
                <th className="hand" onClick={sort('importantSubscriptionNotifications')}>
                  <Translate contentKey="adminApp.userSettings.importantSubscriptionNotifications">
                    Important Subscription Notifications
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('importantSubscriptionNotifications')} />
                </th>
                <th className="hand" onClick={sort('newMessages')}>
                  <Translate contentKey="adminApp.userSettings.newMessages">New Messages</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('newMessages')} />
                </th>
                <th className="hand" onClick={sort('postReplies')}>
                  <Translate contentKey="adminApp.userSettings.postReplies">Post Replies</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postReplies')} />
                </th>
                <th className="hand" onClick={sort('postLikes')}>
                  <Translate contentKey="adminApp.userSettings.postLikes">Post Likes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postLikes')} />
                </th>
                <th className="hand" onClick={sort('newFollowers')}>
                  <Translate contentKey="adminApp.userSettings.newFollowers">New Followers</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('newFollowers')} />
                </th>
                <th className="hand" onClick={sort('smsNewStream')}>
                  <Translate contentKey="adminApp.userSettings.smsNewStream">Sms New Stream</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('smsNewStream')} />
                </th>
                <th className="hand" onClick={sort('toastNewComment')}>
                  <Translate contentKey="adminApp.userSettings.toastNewComment">Toast New Comment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('toastNewComment')} />
                </th>
                <th className="hand" onClick={sort('toastNewLikes')}>
                  <Translate contentKey="adminApp.userSettings.toastNewLikes">Toast New Likes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('toastNewLikes')} />
                </th>
                <th className="hand" onClick={sort('toastNewStream')}>
                  <Translate contentKey="adminApp.userSettings.toastNewStream">Toast New Stream</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('toastNewStream')} />
                </th>
                <th className="hand" onClick={sort('siteNewComment')}>
                  <Translate contentKey="adminApp.userSettings.siteNewComment">Site New Comment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteNewComment')} />
                </th>
                <th className="hand" onClick={sort('siteNewLikes')}>
                  <Translate contentKey="adminApp.userSettings.siteNewLikes">Site New Likes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteNewLikes')} />
                </th>
                <th className="hand" onClick={sort('siteDiscountsFromFollowedUsers')}>
                  <Translate contentKey="adminApp.userSettings.siteDiscountsFromFollowedUsers">
                    Site Discounts From Followed Users
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteDiscountsFromFollowedUsers')} />
                </th>
                <th className="hand" onClick={sort('siteNewStream')}>
                  <Translate contentKey="adminApp.userSettings.siteNewStream">Site New Stream</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteNewStream')} />
                </th>
                <th className="hand" onClick={sort('siteUpcomingStreamReminders')}>
                  <Translate contentKey="adminApp.userSettings.siteUpcomingStreamReminders">Site Upcoming Stream Reminders</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteUpcomingStreamReminders')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.userSettings.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.userSettings.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.userSettings.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.userSettings.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userSettingsList.map((userSettings, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-settings/${userSettings.id}`} color="link" size="sm">
                      {userSettings.id}
                    </Button>
                  </td>
                  <td>
                    {userSettings.lastModifiedDate ? (
                      <TextFormat type="date" value={userSettings.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userSettings.darkMode ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`adminApp.UserLanguage.${userSettings.language}`} />
                  </td>
                  <td>{userSettings.contentFilter ? 'true' : 'false'}</td>
                  <td>{userSettings.messageBlurIntensity}</td>
                  <td>{userSettings.activityStatusVisibility ? 'true' : 'false'}</td>
                  <td>{userSettings.twoFactorAuthentication ? 'true' : 'false'}</td>
                  <td>{userSettings.sessionsActiveCount}</td>
                  <td>{userSettings.emailNotifications ? 'true' : 'false'}</td>
                  <td>{userSettings.importantSubscriptionNotifications ? 'true' : 'false'}</td>
                  <td>{userSettings.newMessages ? 'true' : 'false'}</td>
                  <td>{userSettings.postReplies ? 'true' : 'false'}</td>
                  <td>{userSettings.postLikes ? 'true' : 'false'}</td>
                  <td>{userSettings.newFollowers ? 'true' : 'false'}</td>
                  <td>{userSettings.smsNewStream ? 'true' : 'false'}</td>
                  <td>{userSettings.toastNewComment ? 'true' : 'false'}</td>
                  <td>{userSettings.toastNewLikes ? 'true' : 'false'}</td>
                  <td>{userSettings.toastNewStream ? 'true' : 'false'}</td>
                  <td>{userSettings.siteNewComment ? 'true' : 'false'}</td>
                  <td>{userSettings.siteNewLikes ? 'true' : 'false'}</td>
                  <td>{userSettings.siteDiscountsFromFollowedUsers ? 'true' : 'false'}</td>
                  <td>{userSettings.siteNewStream ? 'true' : 'false'}</td>
                  <td>{userSettings.siteUpcomingStreamReminders ? 'true' : 'false'}</td>
                  <td>
                    {userSettings.createdDate ? <TextFormat type="date" value={userSettings.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userSettings.createdBy}</td>
                  <td>{userSettings.lastModifiedBy}</td>
                  <td>{userSettings.isDeleted ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-settings/${userSettings.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-settings/${userSettings.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-settings/${userSettings.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.userSettings.home.notFound">No User Settings found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userSettingsList && userSettingsList.length > 0 ? '' : 'd-none'}>
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

export default UserSettings;
