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

describe('DirectMessage e2e test', () => {
  const directMessagePageUrl = '/direct-message';
  const directMessagePageUrlPattern = new RegExp('/direct-message(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const directMessageSample = {"messageContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","createdDate":"2024-02-29T08:27:07.696Z","isDeleted":false};

  let directMessage;
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
      body: {"emailContact":"gfhco","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"urgently needle founder","coverPhotoS3Key":"irresponsible","mainContentUrl":"varnish","mobilePhone":"1973787260","websiteUrl":"Gb@7-dA>v.fuEx7%","amazonWishlistUrl":"]@%$43.GX9.","lastLoginDate":"2024-02-29T13:30:43.088Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T21:55:20.117Z","lastModifiedDate":"2024-02-29T21:27:47.083Z","createdBy":"who rebound","lastModifiedBy":"utter ace","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/direct-messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/direct-messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/direct-messages/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/video-stories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/book-marks', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/chat-rooms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/admin-announcements', {
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
    if (directMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/direct-messages/${directMessage.id}`,
      }).then(() => {
        directMessage = undefined;
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

  it('DirectMessages menu should load DirectMessages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('direct-message');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DirectMessage').should('exist');
    cy.url().should('match', directMessagePageUrlPattern);
  });

  describe('DirectMessage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(directMessagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DirectMessage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/direct-message/new$'));
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/direct-messages',
          body: {
            ...directMessageSample,
            user: userProfile,
          },
        }).then(({ body }) => {
          directMessage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/direct-messages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/direct-messages?page=0&size=20>; rel="last",<http://localhost/api/direct-messages?page=0&size=20>; rel="first"',
              },
              body: [directMessage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(directMessagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(directMessagePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DirectMessage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('directMessage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it('edit button click should load edit DirectMessage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it('edit button click should load edit DirectMessage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DirectMessage', () => {
        cy.intercept('GET', '/api/direct-messages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('directMessage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);

        directMessage = undefined;
      });
    });
  });

  describe('new DirectMessage page', () => {
    beforeEach(() => {
      cy.visit(`${directMessagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DirectMessage');
    });

    it.skip('should create an instance of DirectMessage', () => {
      cy.get(`[data-cy="messageContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="messageContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="readDate"]`).type('2024-02-29T09:06');
      cy.get(`[data-cy="readDate"]`).blur();
      cy.get(`[data-cy="readDate"]`).should('have.value', '2024-02-29T09:06');

      cy.get(`[data-cy="likeCount"]`).type('30746');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '30746');

      cy.get(`[data-cy="isHidden"]`).should('not.be.checked');
      cy.get(`[data-cy="isHidden"]`).click();
      cy.get(`[data-cy="isHidden"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T23:05');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T23:05');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T03:31');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T03:31');

      cy.get(`[data-cy="createdBy"]`).type('drat');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'drat');

      cy.get(`[data-cy="lastModifiedBy"]`).type('yippee');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'yippee');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        directMessage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', directMessagePageUrlPattern);
    });
  });
});
