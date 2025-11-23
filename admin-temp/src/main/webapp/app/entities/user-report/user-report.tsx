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

import { searchEntities, getEntities } from './user-report.reducer';

export const UserReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userReportList = useAppSelector(state => state.admin.userReport.entities);
  const loading = useAppSelector(state => state.admin.userReport.loading);
  const totalItems = useAppSelector(state => state.admin.userReport.totalItems);

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
      <h2 id="user-report-heading" data-cy="UserReportHeading">
        <Translate contentKey="adminApp.userReport.home.title">User Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.userReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-report/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.userReport.home.createLabel">Create new User Report</Translate>
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
                  placeholder={translate('adminApp.userReport.home.search')}
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
        {userReportList && userReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.userReport.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('reportDescription')}>
                  <Translate contentKey="adminApp.userReport.reportDescription">Report Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportDescription')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="adminApp.userReport.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.userReport.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.userReport.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.userReport.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.userReport.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.userReport.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('reportCategory')}>
                  <Translate contentKey="adminApp.userReport.reportCategory">Report Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportCategory')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.ticket">Ticket</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.reporter">Reporter</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.reported">Reported</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.story">Story</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.video">Video</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.photo">Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.audio">Audio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.liveStream">Live Stream</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.message">Message</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.post">Post</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.postComment">Post Comment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userReportList.map((userReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-report/${userReport.id}`} color="link" size="sm">
                      {userReport.id}
                    </Button>
                  </td>
                  <td>{userReport.reportDescription}</td>
                  <td>
                    <Translate contentKey={`adminApp.ReportStatus.${userReport.status}`} />
                  </td>
                  <td>
                    {userReport.createdDate ? <TextFormat type="date" value={userReport.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userReport.lastModifiedDate ? (
                      <TextFormat type="date" value={userReport.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userReport.createdBy}</td>
                  <td>{userReport.lastModifiedBy}</td>
                  <td>{userReport.isDeleted ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`adminApp.ReportCategory.${userReport.reportCategory}`} />
                  </td>
                  <td>{userReport.ticket ? <Link to={`/assistance-ticket/${userReport.ticket.id}`}>{userReport.ticket.id}</Link> : ''}</td>
                  <td>{userReport.reporter ? <Link to={`/user-profile/${userReport.reporter.id}`}>{userReport.reporter.id}</Link> : ''}</td>
                  <td>{userReport.reported ? <Link to={`/user-profile/${userReport.reported.id}`}>{userReport.reported.id}</Link> : ''}</td>
                  <td>{userReport.story ? <Link to={`/video-story/${userReport.story.id}`}>{userReport.story.duration}</Link> : ''}</td>
                  <td>{userReport.video ? <Link to={`/single-video/${userReport.video.id}`}>{userReport.video.duration}</Link> : ''}</td>
                  <td>{userReport.photo ? <Link to={`/single-photo/${userReport.photo.id}`}>{userReport.photo.thumbnail}</Link> : ''}</td>
                  <td>{userReport.audio ? <Link to={`/single-audio/${userReport.audio.id}`}>{userReport.audio.duration}</Link> : ''}</td>
                  <td>
                    {userReport.liveStream ? (
                      <Link to={`/single-live-stream/${userReport.liveStream.id}`}>{userReport.liveStream.thumbnail}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {userReport.message ? (
                      <Link to={`/direct-message/${userReport.message.id}`}>{userReport.message.messageContent}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{userReport.post ? <Link to={`/post-feed/${userReport.post.id}`}>{userReport.post.postContent}</Link> : ''}</td>
                  <td>
                    {userReport.postComment ? (
                      <Link to={`/post-comment/${userReport.postComment.id}`}>{userReport.postComment.commentContent}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-report/${userReport.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-report/${userReport.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-report/${userReport.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.userReport.home.notFound">No User Reports found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userReportList && userReportList.length > 0 ? '' : 'd-none'}>
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

export default UserReport;
