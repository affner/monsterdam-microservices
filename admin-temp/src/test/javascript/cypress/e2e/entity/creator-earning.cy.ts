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

describe('CreatorEarning e2e test', () => {
  const creatorEarningPageUrl = '/creator-earning';
  const creatorEarningPageUrlPattern = new RegExp('/creator-earning(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const creatorEarningSample = {"amount":1161.66,"createdDate":"2024-02-29T13:21:12.040Z","isDeleted":true};

  let creatorEarning;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"eff25","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"show brr evaluator","coverPhotoS3Key":"our","mainContentUrl":"whirlwind satellite answer","mobilePhone":"+32734064814","websiteUrl":"u@h\\5.MiX]6f","amazonWishlistUrl":"H|>Y'@de2c.hM3q","lastLoginDate":"2024-02-29T17:00:04.486Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T07:56:54.967Z","lastModifiedDate":"2024-02-29T03:55:50.071Z","createdBy":"feel","lastModifiedBy":"amidst","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/creator-earnings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/creator-earnings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/creator-earnings/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/money-payouts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-contents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-subscriptions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-tips', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (creatorEarning) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/creator-earnings/${creatorEarning.id}`,
      }).then(() => {
        creatorEarning = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
  });
   */

  it('CreatorEarnings menu should load CreatorEarnings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('creator-earning');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CreatorEarning').should('exist');
    cy.url().should('match', creatorEarningPageUrlPattern);
  });

  describe('CreatorEarning page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(creatorEarningPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CreatorEarning page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/creator-earning/new$'));
        cy.getEntityCreateUpdateHeading('CreatorEarning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', creatorEarningPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/creator-earnings',
          body: {
            ...creatorEarningSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          creatorEarning = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/creator-earnings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/creator-earnings?page=0&size=20>; rel="last",<http://localhost/api/creator-earnings?page=0&size=20>; rel="first"',
              },
              body: [creatorEarning],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(creatorEarningPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(creatorEarningPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CreatorEarning page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('creatorEarning');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', creatorEarningPageUrlPattern);
      });

      it('edit button click should load edit CreatorEarning page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CreatorEarning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', creatorEarningPageUrlPattern);
      });

      it('edit button click should load edit CreatorEarning page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CreatorEarning');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', creatorEarningPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of CreatorEarning', () => {
        cy.intercept('GET', '/api/creator-earnings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('creatorEarning').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', creatorEarningPageUrlPattern);

        creatorEarning = undefined;
      });
    });
  });

  describe('new CreatorEarning page', () => {
    beforeEach(() => {
      cy.visit(`${creatorEarningPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CreatorEarning');
    });

    it.skip('should create an instance of CreatorEarning', () => {
      cy.get(`[data-cy="amount"]`).type('13596.43');
      cy.get(`[data-cy="amount"]`).should('have.value', '13596.43');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T20:44');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T20:44');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-28T23:45');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-28T23:45');

      cy.get(`[data-cy="createdBy"]`).type('sandy');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'sandy');

      cy.get(`[data-cy="lastModifiedBy"]`).type('warmly till');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'warmly till');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        creatorEarning = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', creatorEarningPageUrlPattern);
    });
  });
});
