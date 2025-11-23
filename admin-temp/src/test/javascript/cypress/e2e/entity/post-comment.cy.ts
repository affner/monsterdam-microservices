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

describe('PostComment e2e test', () => {
  const postCommentPageUrl = '/post-comment';
  const postCommentPageUrlPattern = new RegExp('/post-comment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const postCommentSample = {"commentContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","createdDate":"2024-02-28T23:50:37.728Z","isDeleted":false};

  let postComment;
  // let postFeed;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/post-feeds',
      body: {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isHidden":false,"pinnedPost":true,"likeCount":31432,"createdDate":"2024-02-29T00:53:06.771Z","lastModifiedDate":"2024-02-29T10:24:04.902Z","createdBy":"snuffle fooey little","lastModifiedBy":"kindheartedly before","isDeleted":true},
    }).then(({ body }) => {
      postFeed = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"2c_zl","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"onto","coverPhotoS3Key":"hence fight","mainContentUrl":"self-assured purloin into","mobilePhone":"482560917566195","websiteUrl":"E@ZCAA\"P.n","amazonWishlistUrl":"R@8*dudu.(d","lastLoginDate":"2024-02-29T16:29:10.802Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T22:32:45.058Z","lastModifiedDate":"2024-02-29T21:52:05.125Z","createdBy":"nature hype athletic","lastModifiedBy":"ugh ugh abnormally","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/post-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-comments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
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

    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [postFeed],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (postComment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-comments/${postComment.id}`,
      }).then(() => {
        postComment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (postFeed) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-feeds/${postFeed.id}`,
      }).then(() => {
        postFeed = undefined;
      });
    }
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

  it('PostComments menu should load PostComments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-comment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostComment').should('exist');
    cy.url().should('match', postCommentPageUrlPattern);
  });

  describe('PostComment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postCommentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostComment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-comment/new$'));
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-comments',
          body: {
            ...postCommentSample,
            post: postFeed,
            commenter: userProfile,
          },
        }).then(({ body }) => {
          postComment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-comments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-comments?page=0&size=20>; rel="last",<http://localhost/api/post-comments?page=0&size=20>; rel="first"',
              },
              body: [postComment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postCommentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(postCommentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PostComment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postComment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it('edit button click should load edit PostComment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it('edit button click should load edit PostComment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PostComment', () => {
        cy.intercept('GET', '/api/post-comments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postComment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);

        postComment = undefined;
      });
    });
  });

  describe('new PostComment page', () => {
    beforeEach(() => {
      cy.visit(`${postCommentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostComment');
    });

    it.skip('should create an instance of PostComment', () => {
      cy.get(`[data-cy="commentContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="commentContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="likeCount"]`).type('7191');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '7191');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:36');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:36');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T04:33');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T04:33');

      cy.get(`[data-cy="createdBy"]`).type('yawning opposite kindly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yawning opposite kindly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('pronoun');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'pronoun');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="post"]`).select(1);
      cy.get(`[data-cy="commenter"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        postComment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', postCommentPageUrlPattern);
    });
  });
});
