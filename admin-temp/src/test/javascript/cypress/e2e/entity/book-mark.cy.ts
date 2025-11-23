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

describe('BookMark e2e test', () => {
  const bookMarkPageUrl = '/book-mark';
  const bookMarkPageUrlPattern = new RegExp('/book-mark(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const bookMarkSample = {"createdDate":"2024-02-29T19:02:38.542Z","isDeleted":false};

  let bookMark;
  // let postFeed;
  // let directMessage;
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
      body: {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isHidden":false,"pinnedPost":true,"likeCount":2556,"createdDate":"2024-02-29T13:35:55.548Z","lastModifiedDate":"2024-02-29T10:08:49.462Z","createdBy":"the","lastModifiedBy":"absent hilarious","isDeleted":true},
    }).then(({ body }) => {
      postFeed = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/direct-messages',
      body: {"messageContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","readDate":"2024-02-29T12:42:34.536Z","likeCount":5551,"isHidden":false,"createdDate":"2024-02-29T07:57:23.295Z","lastModifiedDate":"2024-02-29T09:46:58.324Z","createdBy":"vacant delicious bah","lastModifiedBy":"anenst novel","isDeleted":true},
    }).then(({ body }) => {
      directMessage = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"w","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"nor","coverPhotoS3Key":"deactivate ginger react","mainContentUrl":"at artist","mobilePhone":"+6395314531","websiteUrl":"W@c.q0","amazonWishlistUrl":"VKc$A@Js`.8Ac'7","lastLoginDate":"2024-02-29T13:43:26.815Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T03:26:51.530Z","lastModifiedDate":"2024-02-29T17:41:07.053Z","createdBy":"geez meanwhile biodegradable","lastModifiedBy":"truth distinct jar","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/book-marks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/book-marks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/book-marks/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [postFeed],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [directMessage],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (bookMark) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/book-marks/${bookMark.id}`,
      }).then(() => {
        bookMark = undefined;
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
    if (directMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/direct-messages/${directMessage.id}`,
      }).then(() => {
        directMessage = undefined;
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

  it('BookMarks menu should load BookMarks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('book-mark');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BookMark').should('exist');
    cy.url().should('match', bookMarkPageUrlPattern);
  });

  describe('BookMark page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bookMarkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BookMark page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/book-mark/new$'));
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/book-marks',
          body: {
            ...bookMarkSample,
            post: postFeed,
            message: directMessage,
            user: userProfile,
          },
        }).then(({ body }) => {
          bookMark = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/book-marks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/book-marks?page=0&size=20>; rel="last",<http://localhost/api/book-marks?page=0&size=20>; rel="first"',
              },
              body: [bookMark],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bookMarkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(bookMarkPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details BookMark page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bookMark');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it('edit button click should load edit BookMark page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it('edit button click should load edit BookMark page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of BookMark', () => {
        cy.intercept('GET', '/api/book-marks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('bookMark').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);

        bookMark = undefined;
      });
    });
  });

  describe('new BookMark page', () => {
    beforeEach(() => {
      cy.visit(`${bookMarkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BookMark');
    });

    it.skip('should create an instance of BookMark', () => {
      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T15:01');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T15:01');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T00:11');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T00:11');

      cy.get(`[data-cy="createdBy"]`).type('betray burst consequently');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'betray burst consequently');

      cy.get(`[data-cy="lastModifiedBy"]`).type('duh worriedly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'duh worriedly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="post"]`).select(1);
      cy.get(`[data-cy="message"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        bookMark = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', bookMarkPageUrlPattern);
    });
  });
});
