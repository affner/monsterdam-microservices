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

describe('SpecialTitle e2e test', () => {
  const specialTitlePageUrl = '/special-title';
  const specialTitlePageUrlPattern = new RegExp('/special-title(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specialTitleSample = { createdDate: '2024-02-29T16:18:21.852Z', isDeleted: true };

  let specialTitle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/special-titles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/special-titles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/special-titles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (specialTitle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/special-titles/${specialTitle.id}`,
      }).then(() => {
        specialTitle = undefined;
      });
    }
  });

  it('SpecialTitles menu should load SpecialTitles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('special-title');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SpecialTitle').should('exist');
    cy.url().should('match', specialTitlePageUrlPattern);
  });

  describe('SpecialTitle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specialTitlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SpecialTitle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/special-title/new$'));
        cy.getEntityCreateUpdateHeading('SpecialTitle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialTitlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/special-titles',
          body: specialTitleSample,
        }).then(({ body }) => {
          specialTitle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/special-titles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/special-titles?page=0&size=20>; rel="last",<http://localhost/api/special-titles?page=0&size=20>; rel="first"',
              },
              body: [specialTitle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(specialTitlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SpecialTitle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specialTitle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialTitlePageUrlPattern);
      });

      it('edit button click should load edit SpecialTitle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialTitle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialTitlePageUrlPattern);
      });

      it('edit button click should load edit SpecialTitle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialTitle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialTitlePageUrlPattern);
      });

      it('last delete button click should delete instance of SpecialTitle', () => {
        cy.intercept('GET', '/api/special-titles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('specialTitle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialTitlePageUrlPattern);

        specialTitle = undefined;
      });
    });
  });

  describe('new SpecialTitle page', () => {
    beforeEach(() => {
      cy.visit(`${specialTitlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SpecialTitle');
    });

    it('should create an instance of SpecialTitle', () => {
      cy.get(`[data-cy="description"]`).type('not');
      cy.get(`[data-cy="description"]`).should('have.value', 'not');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T06:54');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T06:54');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T08:13');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T08:13');

      cy.get(`[data-cy="createdBy"]`).type('miskey');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'miskey');

      cy.get(`[data-cy="lastModifiedBy"]`).type('and given');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'and given');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specialTitle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specialTitlePageUrlPattern);
    });
  });
});
