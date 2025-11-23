import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('AssistanceTicket e2e test', () => {
  const assistanceTicketPageUrl = '/assistance-ticket';
  const assistanceTicketPageUrlPattern = new RegExp('/assistance-ticket(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assistanceTicketSample = {
    subject: 'gadzooks',
    description: 'bravely meanwhile',
    status: 'OPEN',
    type: 'REPORT_REQUEST',
    createdDate: '2024-03-01T18:59:13.323Z',
    userId: 4040,
  };

  let assistanceTicket;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assistance-tickets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assistance-tickets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assistance-tickets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assistanceTicket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assistance-tickets/${assistanceTicket.id}`,
      }).then(() => {
        assistanceTicket = undefined;
      });
    }
  });

  it('AssistanceTickets menu should load AssistanceTickets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('assistance-ticket');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssistanceTicket').should('exist');
    cy.url().should('match', assistanceTicketPageUrlPattern);
  });

  describe('AssistanceTicket page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assistanceTicketPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssistanceTicket page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/assistance-ticket/new$'));
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assistance-tickets',
          body: assistanceTicketSample,
        }).then(({ body }) => {
          assistanceTicket = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assistance-tickets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assistanceTicket],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assistanceTicketPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssistanceTicket page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assistanceTicket');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('edit button click should load edit AssistanceTicket page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('edit button click should load edit AssistanceTicket page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('last delete button click should delete instance of AssistanceTicket', () => {
        cy.intercept('GET', '/api/assistance-tickets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('assistanceTicket').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);

        assistanceTicket = undefined;
      });
    });
  });

  describe('new AssistanceTicket page', () => {
    beforeEach(() => {
      cy.visit(`${assistanceTicketPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssistanceTicket');
    });

    it('should create an instance of AssistanceTicket', () => {
      cy.get(`[data-cy="subject"]`).type('vice wince why');
      cy.get(`[data-cy="subject"]`).should('have.value', 'vice wince why');

      cy.get(`[data-cy="description"]`).type('duh used');
      cy.get(`[data-cy="description"]`).should('have.value', 'duh used');

      cy.get(`[data-cy="status"]`).select('CLOSED');

      cy.get(`[data-cy="type"]`).select('REFUND_REQUEST');

      cy.get(`[data-cy="openedAt"]`).type('2024-03-02T05:57');
      cy.get(`[data-cy="openedAt"]`).blur();
      cy.get(`[data-cy="openedAt"]`).should('have.value', '2024-03-02T05:57');

      cy.get(`[data-cy="closedAt"]`).type('2024-03-02T04:21');
      cy.get(`[data-cy="closedAt"]`).blur();
      cy.get(`[data-cy="closedAt"]`).should('have.value', '2024-03-02T04:21');

      cy.get(`[data-cy="comments"]`).type('psst overshoot');
      cy.get(`[data-cy="comments"]`).should('have.value', 'psst overshoot');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T11:55');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T11:55');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-02T02:41');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-02T02:41');

      cy.get(`[data-cy="createdBy"]`).type('midst');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'midst');

      cy.get(`[data-cy="lastModifiedBy"]`).type('ah uh-huh');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'ah uh-huh');

      cy.get(`[data-cy="userId"]`).type('11144');
      cy.get(`[data-cy="userId"]`).should('have.value', '11144');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assistanceTicket = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assistanceTicketPageUrlPattern);
    });
  });
});
