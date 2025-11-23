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

describe('Budget e2e test', () => {
  const budgetPageUrl = '/budget';
  const budgetPageUrlPattern = new RegExp('/budget(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const budgetSample = { year: 9040, totalBudget: 30464.72 };

  let budget;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/budgets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/budgets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/budgets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (budget) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/budgets/${budget.id}`,
      }).then(() => {
        budget = undefined;
      });
    }
  });

  it('Budgets menu should load Budgets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('budget');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Budget').should('exist');
    cy.url().should('match', budgetPageUrlPattern);
  });

  describe('Budget page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(budgetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Budget page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/budget/new$'));
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/budgets',
          body: budgetSample,
        }).then(({ body }) => {
          budget = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/budgets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/budgets?page=0&size=20>; rel="last",<http://localhost/api/budgets?page=0&size=20>; rel="first"',
              },
              body: [budget],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(budgetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Budget page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('budget');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it('edit button click should load edit Budget page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it('edit button click should load edit Budget page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it('last delete button click should delete instance of Budget', () => {
        cy.intercept('GET', '/api/budgets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('budget').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);

        budget = undefined;
      });
    });
  });

  describe('new Budget page', () => {
    beforeEach(() => {
      cy.visit(`${budgetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Budget');
    });

    it('should create an instance of Budget', () => {
      cy.get(`[data-cy="year"]`).type('18321');
      cy.get(`[data-cy="year"]`).should('have.value', '18321');

      cy.get(`[data-cy="totalBudget"]`).type('18296.81');
      cy.get(`[data-cy="totalBudget"]`).should('have.value', '18296.81');

      cy.get(`[data-cy="spentAmount"]`).type('26252.22');
      cy.get(`[data-cy="spentAmount"]`).should('have.value', '26252.22');

      cy.get(`[data-cy="remainingAmount"]`).type('8098.81');
      cy.get(`[data-cy="remainingAmount"]`).should('have.value', '8098.81');

      cy.get(`[data-cy="budgetDetails"]`).type('by');
      cy.get(`[data-cy="budgetDetails"]`).should('have.value', 'by');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        budget = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', budgetPageUrlPattern);
    });
  });
});
