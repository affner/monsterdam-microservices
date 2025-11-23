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

describe('ContentPackage e2e test', () => {
  const contentPackagePageUrl = '/content-package';
  const contentPackagePageUrlPattern = new RegExp('/content-package(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const contentPackageSample = {"isPaidContent":true,"createdDate":"2024-02-29T13:01:51.080Z","isDeleted":true};

  let contentPackage;
  // let directMessage;
  // let postFeed;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/direct-messages',
      body: {"messageContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","readDate":"2024-02-29T09:25:55.526Z","likeCount":23999,"isHidden":true,"createdDate":"2024-02-29T19:26:50.174Z","lastModifiedDate":"2024-02-29T20:59:30.229Z","createdBy":"yuck into","lastModifiedBy":"colorful brightly","isDeleted":false},
    }).then(({ body }) => {
      directMessage = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/post-feeds',
      body: {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isHidden":false,"pinnedPost":false,"likeCount":19986,"createdDate":"2024-02-29T05:50:49.665Z","lastModifiedDate":"2024-02-29T20:48:56.286Z","createdBy":"yahoo","lastModifiedBy":"over triumphantly","isDeleted":false},
    }).then(({ body }) => {
      postFeed = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/content-packages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/content-packages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/content-packages/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/single-audios', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-contents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/single-videos', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/single-photos', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [directMessage],
    });

    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [postFeed],
    });

  });
   */

  afterEach(() => {
    if (contentPackage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-packages/${contentPackage.id}`,
      }).then(() => {
        contentPackage = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (directMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/direct-messages/${directMessage.id}`,
      }).then(() => {
        directMessage = undefined;
      });
    }
    if (postFeed) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-feeds/${postFeed.id}`,
      }).then(() => {
        postFeed = undefined;
      });
    }
  });
   */

  it('ContentPackages menu should load ContentPackages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('content-package');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContentPackage').should('exist');
    cy.url().should('match', contentPackagePageUrlPattern);
  });

  describe('ContentPackage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contentPackagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContentPackage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/content-package/new$'));
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/content-packages',
          body: {
            ...contentPackageSample,
            message: directMessage,
            post: postFeed,
          },
        }).then(({ body }) => {
          contentPackage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/content-packages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/content-packages?page=0&size=20>; rel="last",<http://localhost/api/content-packages?page=0&size=20>; rel="first"',
              },
              body: [contentPackage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contentPackagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(contentPackagePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ContentPackage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contentPackage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it('edit button click should load edit ContentPackage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it('edit button click should load edit ContentPackage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ContentPackage', () => {
        cy.intercept('GET', '/api/content-packages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('contentPackage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);

        contentPackage = undefined;
      });
    });
  });

  describe('new ContentPackage page', () => {
    beforeEach(() => {
      cy.visit(`${contentPackagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContentPackage');
    });

    it.skip('should create an instance of ContentPackage', () => {
      cy.get(`[data-cy="amount"]`).type('4589.23');
      cy.get(`[data-cy="amount"]`).should('have.value', '4589.23');

      cy.get(`[data-cy="videoCount"]`).type('17336');
      cy.get(`[data-cy="videoCount"]`).should('have.value', '17336');

      cy.get(`[data-cy="imageCount"]`).type('15181');
      cy.get(`[data-cy="imageCount"]`).should('have.value', '15181');

      cy.get(`[data-cy="isPaidContent"]`).should('not.be.checked');
      cy.get(`[data-cy="isPaidContent"]`).click();
      cy.get(`[data-cy="isPaidContent"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T09:15');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T09:15');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T23:32');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T23:32');

      cy.get(`[data-cy="createdBy"]`).type('meh instead');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'meh instead');

      cy.get(`[data-cy="lastModifiedBy"]`).type('candy oof');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'candy oof');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="message"]`).select(1);
      cy.get(`[data-cy="post"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        contentPackage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', contentPackagePageUrlPattern);
    });
  });
});
