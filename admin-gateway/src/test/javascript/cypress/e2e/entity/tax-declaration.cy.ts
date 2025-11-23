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

describe('TaxDeclaration e2e test', () => {
  const taxDeclarationPageUrl = '/tax-declaration';
  const taxDeclarationPageUrlPattern = new RegExp('/tax-declaration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const taxDeclarationSample = { year: 6783, declarationType: 'VAT', status: 'PROCESSED' };

  let taxDeclaration;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-declarations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-declarations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-declarations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxDeclaration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-declarations/${taxDeclaration.id}`,
      }).then(() => {
        taxDeclaration = undefined;
      });
    }
  });

  it('TaxDeclarations menu should load TaxDeclarations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-declaration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxDeclaration').should('exist');
    cy.url().should('match', taxDeclarationPageUrlPattern);
  });

  describe('TaxDeclaration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxDeclarationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxDeclaration page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tax-declaration/new$'));
        cy.getEntityCreateUpdateHeading('TaxDeclaration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxDeclarationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-declarations',
          body: taxDeclarationSample,
        }).then(({ body }) => {
          taxDeclaration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-declarations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxDeclaration],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxDeclarationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxDeclaration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxDeclaration');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxDeclarationPageUrlPattern);
      });

      it('edit button click should load edit TaxDeclaration page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxDeclaration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxDeclarationPageUrlPattern);
      });

      it('edit button click should load edit TaxDeclaration page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxDeclaration');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxDeclarationPageUrlPattern);
      });

      it('last delete button click should delete instance of TaxDeclaration', () => {
        cy.intercept('GET', '/api/tax-declarations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('taxDeclaration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxDeclarationPageUrlPattern);

        taxDeclaration = undefined;
      });
    });
  });

  describe('new TaxDeclaration page', () => {
    beforeEach(() => {
      cy.visit(`${taxDeclarationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxDeclaration');
    });

    it('should create an instance of TaxDeclaration', () => {
      cy.get(`[data-cy="year"]`).type('13717');
      cy.get(`[data-cy="year"]`).should('have.value', '13717');

      cy.get(`[data-cy="declarationType"]`).select('INCOME_TAX');

      cy.get(`[data-cy="submittedDate"]`).type('2024-03-02T14:26');
      cy.get(`[data-cy="submittedDate"]`).blur();
      cy.get(`[data-cy="submittedDate"]`).should('have.value', '2024-03-02T14:26');

      cy.get(`[data-cy="status"]`).select('PROCESSED');

      cy.get(`[data-cy="totalIncome"]`).type('19734.34');
      cy.get(`[data-cy="totalIncome"]`).should('have.value', '19734.34');

      cy.get(`[data-cy="totalTaxableIncome"]`).type('24001.22');
      cy.get(`[data-cy="totalTaxableIncome"]`).should('have.value', '24001.22');

      cy.get(`[data-cy="totalTaxPaid"]`).type('32569.49');
      cy.get(`[data-cy="totalTaxPaid"]`).should('have.value', '32569.49');

      cy.get(`[data-cy="supportingDocumentsKey"]`).type('facility prosper beseech');
      cy.get(`[data-cy="supportingDocumentsKey"]`).should('have.value', 'facility prosper beseech');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        taxDeclaration = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', taxDeclarationPageUrlPattern);
    });
  });
});
