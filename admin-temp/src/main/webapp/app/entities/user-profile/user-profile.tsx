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

import { searchEntities, getEntities } from './user-profile.reducer';

export const UserProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userProfileList = useAppSelector(state => state.admin.userProfile.entities);
  const loading = useAppSelector(state => state.admin.userProfile.loading);
  const totalItems = useAppSelector(state => state.admin.userProfile.totalItems);

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
      <h2 id="user-profile-heading" data-cy="UserProfileHeading">
        <Translate contentKey="adminApp.userProfile.home.title">User Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.userProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.userProfile.home.createLabel">Create new User Profile</Translate>
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
                  placeholder={translate('adminApp.userProfile.home.search')}
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
        {userProfileList && userProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.userProfile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('emailContact')}>
                  <Translate contentKey="adminApp.userProfile.emailContact">Email Contact</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailContact')} />
                </th>
                <th className="hand" onClick={sort('profilePhoto')}>
                  <Translate contentKey="adminApp.userProfile.profilePhoto">Profile Photo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profilePhoto')} />
                </th>
                <th className="hand" onClick={sort('coverPhoto')}>
                  <Translate contentKey="adminApp.userProfile.coverPhoto">Cover Photo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverPhoto')} />
                </th>
                <th className="hand" onClick={sort('profilePhotoS3Key')}>
                  <Translate contentKey="adminApp.userProfile.profilePhotoS3Key">Profile Photo S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profilePhotoS3Key')} />
                </th>
                <th className="hand" onClick={sort('coverPhotoS3Key')}>
                  <Translate contentKey="adminApp.userProfile.coverPhotoS3Key">Cover Photo S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverPhotoS3Key')} />
                </th>
                <th className="hand" onClick={sort('mainContentUrl')}>
                  <Translate contentKey="adminApp.userProfile.mainContentUrl">Main Content Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mainContentUrl')} />
                </th>
                <th className="hand" onClick={sort('mobilePhone')}>
                  <Translate contentKey="adminApp.userProfile.mobilePhone">Mobile Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobilePhone')} />
                </th>
                <th className="hand" onClick={sort('websiteUrl')}>
                  <Translate contentKey="adminApp.userProfile.websiteUrl">Website Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('websiteUrl')} />
                </th>
                <th className="hand" onClick={sort('amazonWishlistUrl')}>
                  <Translate contentKey="adminApp.userProfile.amazonWishlistUrl">Amazon Wishlist Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amazonWishlistUrl')} />
                </th>
                <th className="hand" onClick={sort('lastLoginDate')}>
                  <Translate contentKey="adminApp.userProfile.lastLoginDate">Last Login Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastLoginDate')} />
                </th>
                <th className="hand" onClick={sort('biography')}>
                  <Translate contentKey="adminApp.userProfile.biography">Biography</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('biography')} />
                </th>
                <th className="hand" onClick={sort('isFree')}>
                  <Translate contentKey="adminApp.userProfile.isFree">Is Free</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isFree')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.userProfile.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.userProfile.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.userProfile.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.userProfile.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.userProfile.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.userProfile.userLite">User Lite</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userProfile.settings">Settings</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userProfile.countryOfBirth">Country Of Birth</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userProfile.stateOfResidence">State Of Residence</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userProfileList.map((userProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="link" size="sm">
                      {userProfile.id}
                    </Button>
                  </td>
                  <td>{userProfile.emailContact}</td>
                  <td>
                    {userProfile.profilePhoto ? (
                      <div>
                        {userProfile.profilePhotoContentType ? (
                          <a onClick={openFile(userProfile.profilePhotoContentType, userProfile.profilePhoto)}>
                            <img
                              src={`data:${userProfile.profilePhotoContentType};base64,${userProfile.profilePhoto}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {userProfile.profilePhotoContentType}, {byteSize(userProfile.profilePhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {userProfile.coverPhoto ? (
                      <div>
                        {userProfile.coverPhotoContentType ? (
                          <a onClick={openFile(userProfile.coverPhotoContentType, userProfile.coverPhoto)}>
                            <img
                              src={`data:${userProfile.coverPhotoContentType};base64,${userProfile.coverPhoto}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {userProfile.coverPhotoContentType}, {byteSize(userProfile.coverPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{userProfile.profilePhotoS3Key}</td>
                  <td>{userProfile.coverPhotoS3Key}</td>
                  <td>{userProfile.mainContentUrl}</td>
                  <td>{userProfile.mobilePhone}</td>
                  <td>{userProfile.websiteUrl}</td>
                  <td>{userProfile.amazonWishlistUrl}</td>
                  <td>
                    {userProfile.lastLoginDate ? (
                      <TextFormat type="date" value={userProfile.lastLoginDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userProfile.biography}</td>
                  <td>{userProfile.isFree ? 'true' : 'false'}</td>
                  <td>
                    {userProfile.createdDate ? <TextFormat type="date" value={userProfile.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userProfile.lastModifiedDate ? (
                      <TextFormat type="date" value={userProfile.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userProfile.createdBy}</td>
                  <td>{userProfile.lastModifiedBy}</td>
                  <td>{userProfile.isDeleted ? 'true' : 'false'}</td>
                  <td>{userProfile.userLite ? <Link to={`/user-lite/${userProfile.userLite.id}`}>{userProfile.userLite.id}</Link> : ''}</td>
                  <td>
                    {userProfile.settings ? <Link to={`/user-settings/${userProfile.settings.id}`}>{userProfile.settings.id}</Link> : ''}
                  </td>
                  <td>
                    {userProfile.countryOfBirth ? (
                      <Link to={`/country/${userProfile.countryOfBirth.id}`}>{userProfile.countryOfBirth.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {userProfile.stateOfResidence ? (
                      <Link to={`/state/${userProfile.stateOfResidence.id}`}>{userProfile.stateOfResidence.stateName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-profile/${userProfile.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-profile/${userProfile.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.userProfile.home.notFound">No User Profiles found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userProfileList && userProfileList.length > 0 ? '' : 'd-none'}>
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

export default UserProfile;
