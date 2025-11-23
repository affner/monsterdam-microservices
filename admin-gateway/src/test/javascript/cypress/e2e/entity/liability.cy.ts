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

describe('Liability e2e test', () => {
  const liabilityPageUrl = '/liability';
  const liabilityPageUrlPattern = new RegExp('/liability(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const liabilitySample = { name: 'imbue', amount: 5150.77, type: 'CURRENT' };

  let liability;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/liabilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/liabilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/liabilities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (liability) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/liabilities/${liability.id}`,
      }).then(() => {
        liability = undefined;
      });
    }
  });

  it('Liabilities menu should load Liabilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('liability');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Liability').should('exist');
    cy.url().should('match', liabilityPageUrlPattern);
  });

  describe('Liability page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(liabilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Liability page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/liability/new$'));
        cy.getEntityCreateUpdateHeading('Liability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/liabilities',
          body: liabilitySample,
        }).then(({ body }) => {
          liability = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/liabilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [liability],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(liabilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Liability page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('liability');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityPageUrlPattern);
      });

      it('edit button click should load edit Liability page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Liability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityPageUrlPattern);
      });

      it('edit button click should load edit Liability page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Liability');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityPageUrlPattern);
      });

      it('last delete button click should delete instance of Liability', () => {
        cy.intercept('GET', '/api/liabilities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('liability').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', liabilityPageUrlPattern);

        liability = undefined;
      });
    });
  });

  describe('new Liability page', () => {
    beforeEach(() => {
      cy.visit(`${liabilityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Liability');
    });

    it('should create an instance of Liability', () => {
      cy.get(`[data-cy="name"]`).type('nearly across');
      cy.get(`[data-cy="name"]`).should('have.value', 'nearly across');

      cy.get(`[data-cy="amount"]`).type('564.04');
      cy.get(`[data-cy="amount"]`).should('have.value', '564.04');

      cy.get(`[data-cy="dueDate"]`).type('2024-03-02');
      cy.get(`[data-cy="dueDate"]`).blur();
      cy.get(`[data-cy="dueDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="type"]`).select('CURRENT');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        liability = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', liabilityPageUrlPattern);
    });
  });
});
