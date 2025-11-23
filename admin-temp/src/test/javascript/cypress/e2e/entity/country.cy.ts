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

describe('Country e2e test', () => {
  const countryPageUrl = '/country';
  const countryPageUrlPattern = new RegExp('/country(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const countrySample = {
    name: 'yowza punctually',
    alpha2Code: 'si',
    alpha3Code: 'and',
    createdDate: '2024-02-29T20:11:20.363Z',
    isDeleted: false,
  };

  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/countries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/countries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/countries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });

  it('Countries menu should load Countries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('country');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Country').should('exist');
    cy.url().should('match', countryPageUrlPattern);
  });

  describe('Country page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(countryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Country page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/country/new$'));
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/countries',
          body: countrySample,
        }).then(({ body }) => {
          country = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/countries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/countries?page=0&size=20>; rel="last",<http://localhost/api/countries?page=0&size=20>; rel="first"',
              },
              body: [country],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(countryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Country page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('country');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it('edit button click should load edit Country page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it('edit button click should load edit Country page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it('last delete button click should delete instance of Country', () => {
        cy.intercept('GET', '/api/countries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('country').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);

        country = undefined;
      });
    });
  });

  describe('new Country page', () => {
    beforeEach(() => {
      cy.visit(`${countryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Country');
    });

    it('should create an instance of Country', () => {
      cy.get(`[data-cy="name"]`).type('discrete artificer abaft');
      cy.get(`[data-cy="name"]`).should('have.value', 'discrete artificer abaft');

      cy.get(`[data-cy="alpha2Code"]`).type('wh');
      cy.get(`[data-cy="alpha2Code"]`).should('have.value', 'wh');

      cy.get(`[data-cy="alpha3Code"]`).type('plu');
      cy.get(`[data-cy="alpha3Code"]`).should('have.value', 'plu');

      cy.get(`[data-cy="phoneCode"]`).type('coevolution steeple when');
      cy.get(`[data-cy="phoneCode"]`).should('have.value', 'coevolution steeple when');

      cy.setFieldImageAsBytesOfEntity('thumbnailCountry', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T00:52');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T00:52');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T12:45');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T12:45');

      cy.get(`[data-cy="createdBy"]`).type('yum phew bulky');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yum phew bulky');

      cy.get(`[data-cy="lastModifiedBy"]`).type('phooey though amidst');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'phooey though amidst');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        country = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', countryPageUrlPattern);
    });
  });
});
