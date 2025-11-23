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

describe('UserLite e2e test', () => {
  const userLitePageUrl = '/user-lite';
  const userLitePageUrlPattern = new RegExp('/user-lite(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userLiteSample = {
    birthDate: '2024-02-29',
    gender: 'TRANS_FEMALE',
    createdDate: '2024-02-29T06:03:53.505Z',
    isDeleted: false,
    nickName: '8mb6hy5y2xsd',
    fullName: '6zkf',
    contentPreference: 'GAY',
  };

  let userLite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-lites+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-lites').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-lites/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
      });
    }
  });

  it('UserLites menu should load UserLites page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-lite');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserLite').should('exist');
    cy.url().should('match', userLitePageUrlPattern);
  });

  describe('UserLite page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userLitePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserLite page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-lite/new$'));
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-lites',
          body: userLiteSample,
        }).then(({ body }) => {
          userLite = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-lites+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-lites?page=0&size=20>; rel="last",<http://localhost/api/user-lites?page=0&size=20>; rel="first"',
              },
              body: [userLite],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userLitePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserLite page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userLite');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      it('edit button click should load edit UserLite page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      it('edit button click should load edit UserLite page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      it('last delete button click should delete instance of UserLite', () => {
        cy.intercept('GET', '/api/user-lites/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userLite').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);

        userLite = undefined;
      });
    });
  });

  describe('new UserLite page', () => {
    beforeEach(() => {
      cy.visit(`${userLitePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserLite');
    });

    it('should create an instance of UserLite', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('glass');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'glass');

      cy.get(`[data-cy="birthDate"]`).type('2024-02-29');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="gender"]`).select('TRANS_MALE');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T01:49');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T01:49');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T11:16');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T11:16');

      cy.get(`[data-cy="createdBy"]`).type('arid');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'arid');

      cy.get(`[data-cy="lastModifiedBy"]`).type('furthermore pare');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'furthermore pare');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="nickName"]`).type('bqkulj_');
      cy.get(`[data-cy="nickName"]`).should('have.value', 'bqkulj_');

      cy.get(`[data-cy="fullName"]`).type('u');
      cy.get(`[data-cy="fullName"]`).should('have.value', 'u');

      cy.get(`[data-cy="contentPreference"]`).select('STRAIGHT');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userLite = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userLitePageUrlPattern);
    });
  });
});
