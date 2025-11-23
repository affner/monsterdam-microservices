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

describe('FinancialStatement e2e test', () => {
  const financialStatementPageUrl = '/financial-statement';
  const financialStatementPageUrlPattern = new RegExp('/financial-statement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const financialStatementSample = {
    statementType: 'BALANCE_SHEET',
    periodStartDate: '2024-03-02',
    periodEndDate: '2024-03-02',
    createdDate: '2024-03-02T05:08:25.432Z',
  };

  let financialStatement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/financial-statements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/financial-statements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/financial-statements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (financialStatement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/financial-statements/${financialStatement.id}`,
      }).then(() => {
        financialStatement = undefined;
      });
    }
  });

  it('FinancialStatements menu should load FinancialStatements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('financial-statement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FinancialStatement').should('exist');
    cy.url().should('match', financialStatementPageUrlPattern);
  });

  describe('FinancialStatement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(financialStatementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FinancialStatement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/financial-statement/new$'));
        cy.getEntityCreateUpdateHeading('FinancialStatement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', financialStatementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/financial-statements',
          body: financialStatementSample,
        }).then(({ body }) => {
          financialStatement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/financial-statements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [financialStatement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(financialStatementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FinancialStatement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('financialStatement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', financialStatementPageUrlPattern);
      });

      it('edit button click should load edit FinancialStatement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialStatement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', financialStatementPageUrlPattern);
      });

      it('edit button click should load edit FinancialStatement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialStatement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', financialStatementPageUrlPattern);
      });

      it('last delete button click should delete instance of FinancialStatement', () => {
        cy.intercept('GET', '/api/financial-statements/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('financialStatement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', financialStatementPageUrlPattern);

        financialStatement = undefined;
      });
    });
  });

  describe('new FinancialStatement page', () => {
    beforeEach(() => {
      cy.visit(`${financialStatementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FinancialStatement');
    });

    it('should create an instance of FinancialStatement', () => {
      cy.get(`[data-cy="statementType"]`).select('INCOME_STATEMENT');

      cy.get(`[data-cy="periodStartDate"]`).type('2024-03-01');
      cy.get(`[data-cy="periodStartDate"]`).blur();
      cy.get(`[data-cy="periodStartDate"]`).should('have.value', '2024-03-01');

      cy.get(`[data-cy="periodEndDate"]`).type('2024-03-02');
      cy.get(`[data-cy="periodEndDate"]`).blur();
      cy.get(`[data-cy="periodEndDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T11:16');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T11:16');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        financialStatement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', financialStatementPageUrlPattern);
    });
  });
});
