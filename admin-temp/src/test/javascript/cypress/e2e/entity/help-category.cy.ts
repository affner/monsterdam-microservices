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

describe('HelpCategory e2e test', () => {
  const helpCategoryPageUrl = '/help-category';
  const helpCategoryPageUrlPattern = new RegExp('/help-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const helpCategorySample = { name: 'natural since', isDeleted: true };

  let helpCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/help-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/help-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/help-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (helpCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/help-categories/${helpCategory.id}`,
      }).then(() => {
        helpCategory = undefined;
      });
    }
  });

  it('HelpCategories menu should load HelpCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('help-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HelpCategory').should('exist');
    cy.url().should('match', helpCategoryPageUrlPattern);
  });

  describe('HelpCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(helpCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HelpCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/help-category/new$'));
        cy.getEntityCreateUpdateHeading('HelpCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/help-categories',
          body: helpCategorySample,
        }).then(({ body }) => {
          helpCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/help-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/help-categories?page=0&size=20>; rel="last",<http://localhost/api/help-categories?page=0&size=20>; rel="first"',
              },
              body: [helpCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(helpCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HelpCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('helpCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpCategoryPageUrlPattern);
      });

      it('edit button click should load edit HelpCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpCategoryPageUrlPattern);
      });

      it('edit button click should load edit HelpCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of HelpCategory', () => {
        cy.intercept('GET', '/api/help-categories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('helpCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpCategoryPageUrlPattern);

        helpCategory = undefined;
      });
    });
  });

  describe('new HelpCategory page', () => {
    beforeEach(() => {
      cy.visit(`${helpCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HelpCategory');
    });

    it('should create an instance of HelpCategory', () => {
      cy.get(`[data-cy="name"]`).type('oh plaintive');
      cy.get(`[data-cy="name"]`).should('have.value', 'oh plaintive');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        helpCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', helpCategoryPageUrlPattern);
    });
  });
});
