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

describe('PostFeed e2e test', () => {
  const postFeedPageUrl = '/post-feed';
  const postFeedPageUrlPattern = new RegExp('/post-feed(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const postFeedSample = {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","createdDate":"2024-02-29T07:56:38.972Z","isDeleted":false};

  let postFeed;
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
      body: {"emailContact":"gy","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"ouch wildly","coverPhotoS3Key":"attend sympathetically","mainContentUrl":"both uh-huh meanwhile","mobilePhone":"+5614162375","websiteUrl":"Nmzs@pH[.(z","amazonWishlistUrl":"KeDL?@:oF{( .R","lastLoginDate":"2024-02-29T13:33:07.081Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T03:46:56.270Z","lastModifiedDate":"2024-02-29T09:56:59.736Z","createdBy":"until hardcover concerning","lastModifiedBy":"blindly","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/post-feeds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-feeds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-feeds/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-polls', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-comments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-mentions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/hash-tags', {
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

  });
   */

  afterEach(() => {
    if (postFeed) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-feeds/${postFeed.id}`,
      }).then(() => {
        postFeed = undefined;
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

  it('PostFeeds menu should load PostFeeds page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-feed');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostFeed').should('exist');
    cy.url().should('match', postFeedPageUrlPattern);
  });

  describe('PostFeed page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postFeedPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostFeed page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-feed/new$'));
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-feeds',
          body: {
            ...postFeedSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          postFeed = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-feeds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-feeds?page=0&size=20>; rel="last",<http://localhost/api/post-feeds?page=0&size=20>; rel="first"',
              },
              body: [postFeed],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postFeedPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(postFeedPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PostFeed page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postFeed');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it('edit button click should load edit PostFeed page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it('edit button click should load edit PostFeed page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PostFeed', () => {
        cy.intercept('GET', '/api/post-feeds/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postFeed').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);

        postFeed = undefined;
      });
    });
  });

  describe('new PostFeed page', () => {
    beforeEach(() => {
      cy.visit(`${postFeedPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostFeed');
    });

    it.skip('should create an instance of PostFeed', () => {
      cy.get(`[data-cy="postContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="postContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isHidden"]`).should('not.be.checked');
      cy.get(`[data-cy="isHidden"]`).click();
      cy.get(`[data-cy="isHidden"]`).should('be.checked');

      cy.get(`[data-cy="pinnedPost"]`).should('not.be.checked');
      cy.get(`[data-cy="pinnedPost"]`).click();
      cy.get(`[data-cy="pinnedPost"]`).should('be.checked');

      cy.get(`[data-cy="likeCount"]`).type('7513');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '7513');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T18:48');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T18:48');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T13:04');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T13:04');

      cy.get(`[data-cy="createdBy"]`).type('profane likewise diver');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'profane likewise diver');

      cy.get(`[data-cy="lastModifiedBy"]`).type('owlishly oddly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'owlishly oddly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        postFeed = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', postFeedPageUrlPattern);
    });
  });
});
