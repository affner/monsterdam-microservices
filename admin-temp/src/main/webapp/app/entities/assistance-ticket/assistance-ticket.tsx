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

import { searchEntities, getEntities } from './assistance-ticket.reducer';

export const AssistanceTicket = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const assistanceTicketList = useAppSelector(state => state.admin.assistanceTicket.entities);
  const loading = useAppSelector(state => state.admin.assistanceTicket.loading);
  const totalItems = useAppSelector(state => state.admin.assistanceTicket.totalItems);

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
      <h2 id="assistance-ticket-heading" data-cy="AssistanceTicketHeading">
        <Translate contentKey="adminApp.assistanceTicket.home.title">Assistance Tickets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.assistanceTicket.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/assistance-ticket/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.assistanceTicket.home.createLabel">Create new Assistance Ticket</Translate>
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
                  placeholder={translate('adminApp.assistanceTicket.home.search')}
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
        {assistanceTicketList && assistanceTicketList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.assistanceTicket.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="adminApp.assistanceTicket.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="adminApp.assistanceTicket.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="adminApp.assistanceTicket.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="adminApp.assistanceTicket.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('openedAt')}>
                  <Translate contentKey="adminApp.assistanceTicket.openedAt">Opened At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('openedAt')} />
                </th>
                <th className="hand" onClick={sort('closedAt')}>
                  <Translate contentKey="adminApp.assistanceTicket.closedAt">Closed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('closedAt')} />
                </th>
                <th className="hand" onClick={sort('comments')}>
                  <Translate contentKey="adminApp.assistanceTicket.comments">Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('comments')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.assistanceTicket.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.assistanceTicket.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.assistanceTicket.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.assistanceTicket.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.assistanceTicket.moderationAction">Moderation Action</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.assistanceTicket.assignedAdmin">Assigned Admin</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.assistanceTicket.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {assistanceTicketList.map((assistanceTicket, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/assistance-ticket/${assistanceTicket.id}`} color="link" size="sm">
                      {assistanceTicket.id}
                    </Button>
                  </td>
                  <td>{assistanceTicket.subject}</td>
                  <td>{assistanceTicket.description}</td>
                  <td>
                    <Translate contentKey={`adminApp.TicketStatus.${assistanceTicket.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.TicketType.${assistanceTicket.type}`} />
                  </td>
                  <td>
                    {assistanceTicket.openedAt ? (
                      <TextFormat type="date" value={assistanceTicket.openedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {assistanceTicket.closedAt ? (
                      <TextFormat type="date" value={assistanceTicket.closedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{assistanceTicket.comments}</td>
                  <td>
                    {assistanceTicket.createdDate ? (
                      <TextFormat type="date" value={assistanceTicket.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {assistanceTicket.lastModifiedDate ? (
                      <TextFormat type="date" value={assistanceTicket.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{assistanceTicket.createdBy}</td>
                  <td>{assistanceTicket.lastModifiedBy}</td>
                  <td>
                    {assistanceTicket.moderationAction ? (
                      <Link to={`/moderation-action/${assistanceTicket.moderationAction.id}`}>{assistanceTicket.moderationAction.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {assistanceTicket.assignedAdmin ? (
                      <Link to={`/admin-user-profile/${assistanceTicket.assignedAdmin.id}`}>{assistanceTicket.assignedAdmin.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {assistanceTicket.user ? <Link to={`/user-profile/${assistanceTicket.user.id}`}>{assistanceTicket.user.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/assistance-ticket/${assistanceTicket.id}`}
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
                        to={`/assistance-ticket/${assistanceTicket.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/assistance-ticket/${assistanceTicket.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adminApp.assistanceTicket.home.notFound">No Assistance Tickets found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={assistanceTicketList && assistanceTicketList.length > 0 ? '' : 'd-none'}>
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

export default AssistanceTicket;
