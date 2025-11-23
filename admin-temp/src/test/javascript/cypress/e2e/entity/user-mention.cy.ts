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

describe('UserMention e2e test', () => {
  const userMentionPageUrl = '/user-mention';
  const userMentionPageUrlPattern = new RegExp('/user-mention(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userMentionSample = {"createdDate":"2024-02-29T20:02:26.833Z","isDeleted":false};

  let userMention;
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
      body: {"emailContact":"88jbv5","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"which polished toe","coverPhotoS3Key":"warmly","mainContentUrl":"by throughout","mobilePhone":"+992810077498999","websiteUrl":"f1.bS@#FqA?0.n","amazonWishlistUrl":"I@!W.6C","lastLoginDate":"2024-02-29T15:28:08.310Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T19:28:02.734Z","lastModifiedDate":"2024-02-29T03:12:58.684Z","createdBy":"zebrafish span psst","lastModifiedBy":"looks towards drat","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-mentions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-mentions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-mentions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-comments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (userMention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-mentions/${userMention.id}`,
      }).then(() => {
        userMention = undefined;
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

  it('UserMentions menu should load UserMentions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mention');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserMention').should('exist');
    cy.url().should('match', userMentionPageUrlPattern);
  });

  describe('UserMention page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userMentionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserMention page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-mention/new$'));
        cy.getEntityCreateUpdateHeading('UserMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userMentionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-mentions',
          body: {
            ...userMentionSample,
            mentionedUser: userProfile,
          },
        }).then(({ body }) => {
          userMention = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-mentions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-mentions?page=0&size=20>; rel="last",<http://localhost/api/user-mentions?page=0&size=20>; rel="first"',
              },
              body: [userMention],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userMentionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userMentionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserMention page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userMention');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userMentionPageUrlPattern);
      });

      it('edit button click should load edit UserMention page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userMentionPageUrlPattern);
      });

      it('edit button click should load edit UserMention page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserMention');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userMentionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of UserMention', () => {
        cy.intercept('GET', '/api/user-mentions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userMention').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userMentionPageUrlPattern);

        userMention = undefined;
      });
    });
  });

  describe('new UserMention page', () => {
    beforeEach(() => {
      cy.visit(`${userMentionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserMention');
    });

    it.skip('should create an instance of UserMention', () => {
      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:00');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:00');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T04:24');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T04:24');

      cy.get(`[data-cy="createdBy"]`).type('scrutinize guidance harmonise');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'scrutinize guidance harmonise');

      cy.get(`[data-cy="lastModifiedBy"]`).type('sunbonnet what');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'sunbonnet what');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="mentionedUser"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userMention = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userMentionPageUrlPattern);
    });
  });
});
