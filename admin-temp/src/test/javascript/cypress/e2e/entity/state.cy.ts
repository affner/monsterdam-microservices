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

describe('State e2e test', () => {
  const statePageUrl = '/state';
  const statePageUrlPattern = new RegExp('/state(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const stateSample = { stateName: 'sans separately', isoCode: 'yuc', createdDate: '2024-02-29T07:48:50.058Z', isDeleted: true };

  let state;
  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/countries',
      body: {
        name: 'lengthen indeed',
        alpha2Code: 'pr',
        alpha3Code: 'mea',
        phoneCode: 'cut',
        thumbnailCountry: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        thumbnailCountryContentType: 'unknown',
        createdDate: '2024-02-29T02:07:11.434Z',
        lastModifiedDate: '2024-02-29T02:30:45.221Z',
        createdBy: 'oof while',
        lastModifiedBy: 'daintily why why',
        isDeleted: false,
      },
    }).then(({ body }) => {
      country = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/states+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/states').as('postEntityRequest');
    cy.intercept('DELETE', '/api/states/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [country],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (state) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/states/${state.id}`,
      }).then(() => {
        state = undefined;
      });
    }
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

  it('States menu should load States page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('state');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('State').should('exist');
    cy.url().should('match', statePageUrlPattern);
  });

  describe('State page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create State page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/state/new$'));
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/states',
          body: {
            ...stateSample,
            country: country,
          },
        }).then(({ body }) => {
          state = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/states+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/states?page=0&size=20>; rel="last",<http://localhost/api/states?page=0&size=20>; rel="first"',
              },
              body: [state],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details State page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('state');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('last delete button click should delete instance of State', () => {
        cy.intercept('GET', '/api/states/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('state').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);

        state = undefined;
      });
    });
  });

  describe('new State page', () => {
    beforeEach(() => {
      cy.visit(`${statePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('State');
    });

    it('should create an instance of State', () => {
      cy.get(`[data-cy="stateName"]`).type('between till');
      cy.get(`[data-cy="stateName"]`).should('have.value', 'between till');

      cy.get(`[data-cy="isoCode"]`).type('ter');
      cy.get(`[data-cy="isoCode"]`).should('have.value', 'ter');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T21:47');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T21:47');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T04:46');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T04:46');

      cy.get(`[data-cy="createdBy"]`).type('afore when');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'afore when');

      cy.get(`[data-cy="lastModifiedBy"]`).type('french');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'french');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="country"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        state = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', statePageUrlPattern);
    });
  });
});
