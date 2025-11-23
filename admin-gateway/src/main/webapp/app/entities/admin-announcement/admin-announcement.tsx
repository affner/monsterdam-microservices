import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './admin-announcement.reducer';

export const AdminAnnouncement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const adminAnnouncementList = useAppSelector(state => state.admin.adminAnnouncement.entities);
  const loading = useAppSelector(state => state.admin.adminAnnouncement.loading);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, search]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="admin-announcement-heading" data-cy="AdminAnnouncementHeading">
        <Translate contentKey="adminApp.adminAnnouncement.home.title">Admin Announcements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.adminAnnouncement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/admin-announcement/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.adminAnnouncement.home.createLabel">Create new Admin Announcement</Translate>
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
                  placeholder={translate('adminApp.adminAnnouncement.home.search')}
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
        {adminAnnouncementList && adminAnnouncementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.adminAnnouncement.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('announcementType')}>
                  <Translate contentKey="adminApp.adminAnnouncement.announcementType">Announcement Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('announcementType')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="adminApp.adminAnnouncement.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="adminApp.adminAnnouncement.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.adminAnnouncement.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.adminAnnouncement.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.adminAnnouncement.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.adminAnnouncement.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('directMessageId')}>
                  <Translate contentKey="adminApp.adminAnnouncement.directMessageId">Direct Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('directMessageId')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.adminAnnouncement.admin">Admin</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {adminAnnouncementList.map((adminAnnouncement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/admin-announcement/${adminAnnouncement.id}`} color="link" size="sm">
                      {adminAnnouncement.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.AdminAnnouncementType.${adminAnnouncement.announcementType}`} />
                  </td>
                  <td>{adminAnnouncement.title}</td>
                  <td>{adminAnnouncement.content}</td>
                  <td>
                    {adminAnnouncement.createdDate ? (
                      <TextFormat type="date" value={adminAnnouncement.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {adminAnnouncement.lastModifiedDate ? (
                      <TextFormat type="date" value={adminAnnouncement.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{adminAnnouncement.createdBy}</td>
                  <td>{adminAnnouncement.lastModifiedBy}</td>
                  <td>{adminAnnouncement.directMessageId}</td>
                  <td>
                    {adminAnnouncement.admin ? (
                      <Link to={`/admin-user-profile/${adminAnnouncement.admin.id}`}>{adminAnnouncement.admin.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/admin-announcement/${adminAnnouncement.id}`}
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
                        to={`/admin-announcement/${adminAnnouncement.id}/edit`}
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
                        onClick={() => (window.location.href = `/admin-announcement/${adminAnnouncement.id}/delete`)}
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
              <Translate contentKey="adminApp.adminAnnouncement.home.notFound">No Admin Announcements found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AdminAnnouncement;
