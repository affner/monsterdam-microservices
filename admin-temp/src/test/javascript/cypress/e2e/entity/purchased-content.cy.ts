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

describe('PurchasedContent e2e test', () => {
  const purchasedContentPageUrl = '/purchased-content';
  const purchasedContentPageUrlPattern = new RegExp('/purchased-content(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const purchasedContentSample = {"createdDate":"2024-02-29T02:17:27.600Z","isDeleted":true};

  let purchasedContent;
  // let creatorEarning;
  // let userProfile;
  // let contentPackage;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/creator-earnings',
      body: {"amount":7067.68,"createdDate":"2024-02-29T23:00:22.361Z","lastModifiedDate":"2024-02-29T09:46:24.617Z","createdBy":"consequently rim fooey","lastModifiedBy":"drat","isDeleted":false},
    }).then(({ body }) => {
      creatorEarning = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"72r","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"pish psychology","coverPhotoS3Key":"oof","mainContentUrl":"fatally until","mobilePhone":"+6770733792578","websiteUrl":"5.{#@|\\36.KF","amazonWishlistUrl":"y|lk=@sFiUw:.6*+6,","lastLoginDate":"2024-02-29T18:32:21.699Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T20:07:44.450Z","lastModifiedDate":"2024-02-29T09:56:14.538Z","createdBy":"before","lastModifiedBy":"scarcely onto","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/content-packages',
      body: {"amount":3100.08,"videoCount":7073,"imageCount":29804,"isPaidContent":true,"createdDate":"2024-02-29T18:46:45.049Z","lastModifiedDate":"2024-02-29T11:48:54.443Z","createdBy":"suddenly through because","lastModifiedBy":"though","isDeleted":false},
    }).then(({ body }) => {
      contentPackage = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-contents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-contents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-contents/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/wallet-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/creator-earnings', {
      statusCode: 200,
      body: [creatorEarning],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [contentPackage],
    });

  });
   */

  afterEach(() => {
    if (purchasedContent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-contents/${purchasedContent.id}`,
      }).then(() => {
        purchasedContent = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (creatorEarning) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/creator-earnings/${creatorEarning.id}`,
      }).then(() => {
        creatorEarning = undefined;
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
    if (contentPackage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-packages/${contentPackage.id}`,
      }).then(() => {
        contentPackage = undefined;
      });
    }
  });
   */

  it('PurchasedContents menu should load PurchasedContents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-content');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedContent').should('exist');
    cy.url().should('match', purchasedContentPageUrlPattern);
  });

  describe('PurchasedContent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedContentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedContent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-content/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-contents',
          body: {
            ...purchasedContentSample,
            creatorEarning: creatorEarning,
            viewer: userProfile,
            purchasedContentPackage: contentPackage,
          },
        }).then(({ body }) => {
          purchasedContent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-contents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-contents?page=0&size=20>; rel="last",<http://localhost/api/purchased-contents?page=0&size=20>; rel="first"',
              },
              body: [purchasedContent],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedContentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(purchasedContentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PurchasedContent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedContent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it('edit button click should load edit PurchasedContent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it('edit button click should load edit PurchasedContent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PurchasedContent', () => {
        cy.intercept('GET', '/api/purchased-contents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('purchasedContent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);

        purchasedContent = undefined;
      });
    });
  });

  describe('new PurchasedContent page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedContentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedContent');
    });

    it.skip('should create an instance of PurchasedContent', () => {
      cy.get(`[data-cy="rating"]`).type('12613.8');
      cy.get(`[data-cy="rating"]`).should('have.value', '12613.8');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T07:26');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T07:26');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-28T23:43');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-28T23:43');

      cy.get(`[data-cy="createdBy"]`).type('amidst');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'amidst');

      cy.get(`[data-cy="lastModifiedBy"]`).type('palliate meanwhile of');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'palliate meanwhile of');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creatorEarning"]`).select(1);
      cy.get(`[data-cy="viewer"]`).select(1);
      cy.get(`[data-cy="purchasedContentPackage"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        purchasedContent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedContentPageUrlPattern);
    });
  });
});
