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

describe('HelpSubcategory e2e test', () => {
  const helpSubcategoryPageUrl = '/help-subcategory';
  const helpSubcategoryPageUrlPattern = new RegExp('/help-subcategory(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const helpSubcategorySample = { name: 'or considering', isDeleted: false };

  let helpSubcategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/help-subcategories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/help-subcategories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/help-subcategories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (helpSubcategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/help-subcategories/${helpSubcategory.id}`,
      }).then(() => {
        helpSubcategory = undefined;
      });
    }
  });

  it('HelpSubcategories menu should load HelpSubcategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('help-subcategory');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HelpSubcategory').should('exist');
    cy.url().should('match', helpSubcategoryPageUrlPattern);
  });

  describe('HelpSubcategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(helpSubcategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HelpSubcategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/help-subcategory/new$'));
        cy.getEntityCreateUpdateHeading('HelpSubcategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpSubcategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/help-subcategories',
          body: helpSubcategorySample,
        }).then(({ body }) => {
          helpSubcategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/help-subcategories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/help-subcategories?page=0&size=20>; rel="last",<http://localhost/api/help-subcategories?page=0&size=20>; rel="first"',
              },
              body: [helpSubcategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(helpSubcategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HelpSubcategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('helpSubcategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpSubcategoryPageUrlPattern);
      });

      it('edit button click should load edit HelpSubcategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpSubcategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpSubcategoryPageUrlPattern);
      });

      it('edit button click should load edit HelpSubcategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpSubcategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpSubcategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of HelpSubcategory', () => {
        cy.intercept('GET', '/api/help-subcategories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('helpSubcategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpSubcategoryPageUrlPattern);

        helpSubcategory = undefined;
      });
    });
  });

  describe('new HelpSubcategory page', () => {
    beforeEach(() => {
      cy.visit(`${helpSubcategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HelpSubcategory');
    });

    it('should create an instance of HelpSubcategory', () => {
      cy.get(`[data-cy="name"]`).type('who project');
      cy.get(`[data-cy="name"]`).should('have.value', 'who project');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        helpSubcategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', helpSubcategoryPageUrlPattern);
    });
  });
});
