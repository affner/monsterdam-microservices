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

describe('UserUIPreferences e2e test', () => {
  const userUIPreferencesPageUrl = '/user-ui-preferences';
  const userUIPreferencesPageUrlPattern = new RegExp('/user-ui-preferences(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userUIPreferencesSample = { createdDate: '2024-03-01T20:58:03.299Z' };

  let userUIPreferences;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-ui-preferences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-ui-preferences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-ui-preferences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userUIPreferences) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-ui-preferences/${userUIPreferences.id}`,
      }).then(() => {
        userUIPreferences = undefined;
      });
    }
  });

  it('UserUIPreferences menu should load UserUIPreferences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-ui-preferences');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserUIPreferences').should('exist');
    cy.url().should('match', userUIPreferencesPageUrlPattern);
  });

  describe('UserUIPreferences page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userUIPreferencesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserUIPreferences page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-ui-preferences/new$'));
        cy.getEntityCreateUpdateHeading('UserUIPreferences');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userUIPreferencesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-ui-preferences',
          body: userUIPreferencesSample,
        }).then(({ body }) => {
          userUIPreferences = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-ui-preferences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-ui-preferences?page=0&size=20>; rel="last",<http://localhost/api/user-ui-preferences?page=0&size=20>; rel="first"',
              },
              body: [userUIPreferences],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userUIPreferencesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserUIPreferences page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userUIPreferences');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userUIPreferencesPageUrlPattern);
      });

      it('edit button click should load edit UserUIPreferences page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserUIPreferences');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userUIPreferencesPageUrlPattern);
      });

      it('edit button click should load edit UserUIPreferences page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserUIPreferences');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userUIPreferencesPageUrlPattern);
      });

      it('last delete button click should delete instance of UserUIPreferences', () => {
        cy.intercept('GET', '/api/user-ui-preferences/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userUIPreferences').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userUIPreferencesPageUrlPattern);

        userUIPreferences = undefined;
      });
    });
  });

  describe('new UserUIPreferences page', () => {
    beforeEach(() => {
      cy.visit(`${userUIPreferencesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserUIPreferences');
    });

    it('should create an instance of UserUIPreferences', () => {
      cy.get(`[data-cy="preferences"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="preferences"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T08:14');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T08:14');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-02T02:25');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-02T02:25');

      cy.get(`[data-cy="createdBy"]`).type('bravely around reason');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'bravely around reason');

      cy.get(`[data-cy="lastModifiedBy"]`).type('lest if loudly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'lest if loudly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userUIPreferences = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userUIPreferencesPageUrlPattern);
    });
  });
});
