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

describe('GlobalEvent e2e test', () => {
  const globalEventPageUrl = '/global-event';
  const globalEventPageUrlPattern = new RegExp('/global-event(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const globalEventSample = {
    eventName: 'whenever consequently',
    startDate: '2024-02-29',
    createdDate: '2024-02-29T11:46:51.459Z',
    isDeleted: false,
  };

  let globalEvent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/global-events+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/global-events').as('postEntityRequest');
    cy.intercept('DELETE', '/api/global-events/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (globalEvent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/global-events/${globalEvent.id}`,
      }).then(() => {
        globalEvent = undefined;
      });
    }
  });

  it('GlobalEvents menu should load GlobalEvents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('global-event');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GlobalEvent').should('exist');
    cy.url().should('match', globalEventPageUrlPattern);
  });

  describe('GlobalEvent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(globalEventPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GlobalEvent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/global-event/new$'));
        cy.getEntityCreateUpdateHeading('GlobalEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', globalEventPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/global-events',
          body: globalEventSample,
        }).then(({ body }) => {
          globalEvent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/global-events+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/global-events?page=0&size=20>; rel="last",<http://localhost/api/global-events?page=0&size=20>; rel="first"',
              },
              body: [globalEvent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(globalEventPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GlobalEvent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('globalEvent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', globalEventPageUrlPattern);
      });

      it('edit button click should load edit GlobalEvent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GlobalEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', globalEventPageUrlPattern);
      });

      it('edit button click should load edit GlobalEvent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GlobalEvent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', globalEventPageUrlPattern);
      });

      it('last delete button click should delete instance of GlobalEvent', () => {
        cy.intercept('GET', '/api/global-events/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('globalEvent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', globalEventPageUrlPattern);

        globalEvent = undefined;
      });
    });
  });

  describe('new GlobalEvent page', () => {
    beforeEach(() => {
      cy.visit(`${globalEventPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GlobalEvent');
    });

    it('should create an instance of GlobalEvent', () => {
      cy.get(`[data-cy="eventName"]`).type('tightly fiercely');
      cy.get(`[data-cy="eventName"]`).should('have.value', 'tightly fiercely');

      cy.get(`[data-cy="startDate"]`).type('2024-02-29');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="description"]`).type('duplexer');
      cy.get(`[data-cy="description"]`).should('have.value', 'duplexer');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:13');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:13');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T00:53');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T00:53');

      cy.get(`[data-cy="createdBy"]`).type('among as which');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'among as which');

      cy.get(`[data-cy="lastModifiedBy"]`).type('yuck notify colorfully');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'yuck notify colorfully');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        globalEvent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', globalEventPageUrlPattern);
    });
  });
});
