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

describe('OfferPromotion e2e test', () => {
  const offerPromotionPageUrl = '/offer-promotion';
  const offerPromotionPageUrlPattern = new RegExp('/offer-promotion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const offerPromotionSample = {"startDate":"2024-02-29","endDate":"2024-02-29","linkCode":"label qua photography","isFinished":true,"promotionType":"SPECIAL","createdDate":"2024-02-29T09:46:41.588Z","isDeleted":false};

  let offerPromotion;
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
      body: {"emailContact":"bvp47_","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"eventually quaint like","coverPhotoS3Key":"infantilise whether","mainContentUrl":"radiant","mobilePhone":"+646910565461236","websiteUrl":"_?09h%@V~&[_0.g","amazonWishlistUrl":"9o0K$@yK.rM","lastLoginDate":"2024-02-29T13:52:05.449Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T23:37:05.763Z","lastModifiedDate":"2024-02-29T08:03:42.268Z","createdBy":"bedeck","lastModifiedBy":"nor","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/offer-promotions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/offer-promotions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/offer-promotions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/purchased-subscriptions', {
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
    if (offerPromotion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/offer-promotions/${offerPromotion.id}`,
      }).then(() => {
        offerPromotion = undefined;
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

  it('OfferPromotions menu should load OfferPromotions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('offer-promotion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OfferPromotion').should('exist');
    cy.url().should('match', offerPromotionPageUrlPattern);
  });

  describe('OfferPromotion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(offerPromotionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OfferPromotion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/offer-promotion/new$'));
        cy.getEntityCreateUpdateHeading('OfferPromotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPromotionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/offer-promotions',
          body: {
            ...offerPromotionSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          offerPromotion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/offer-promotions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/offer-promotions?page=0&size=20>; rel="last",<http://localhost/api/offer-promotions?page=0&size=20>; rel="first"',
              },
              body: [offerPromotion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(offerPromotionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(offerPromotionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OfferPromotion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('offerPromotion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPromotionPageUrlPattern);
      });

      it('edit button click should load edit OfferPromotion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OfferPromotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPromotionPageUrlPattern);
      });

      it('edit button click should load edit OfferPromotion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OfferPromotion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPromotionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OfferPromotion', () => {
        cy.intercept('GET', '/api/offer-promotions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('offerPromotion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPromotionPageUrlPattern);

        offerPromotion = undefined;
      });
    });
  });

  describe('new OfferPromotion page', () => {
    beforeEach(() => {
      cy.visit(`${offerPromotionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OfferPromotion');
    });

    it.skip('should create an instance of OfferPromotion', () => {
      cy.get(`[data-cy="freeDaysDuration"]`).type('PT50M');
      cy.get(`[data-cy="freeDaysDuration"]`).blur();
      cy.get(`[data-cy="freeDaysDuration"]`).should('have.value', 'PT50M');

      cy.get(`[data-cy="discountPercentage"]`).type('72.61');
      cy.get(`[data-cy="discountPercentage"]`).should('have.value', '72.61');

      cy.get(`[data-cy="startDate"]`).type('2024-02-29');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="subscriptionsLimit"]`).type('30194');
      cy.get(`[data-cy="subscriptionsLimit"]`).should('have.value', '30194');

      cy.get(`[data-cy="linkCode"]`).type('narrowcast consequently before');
      cy.get(`[data-cy="linkCode"]`).should('have.value', 'narrowcast consequently before');

      cy.get(`[data-cy="isFinished"]`).should('not.be.checked');
      cy.get(`[data-cy="isFinished"]`).click();
      cy.get(`[data-cy="isFinished"]`).should('be.checked');

      cy.get(`[data-cy="promotionType"]`).select('DISCOUNT');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T00:10');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T00:10');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T02:11');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T02:11');

      cy.get(`[data-cy="createdBy"]`).type('opposite');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'opposite');

      cy.get(`[data-cy="lastModifiedBy"]`).type('thorough coverall');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'thorough coverall');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        offerPromotion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', offerPromotionPageUrlPattern);
    });
  });
});
