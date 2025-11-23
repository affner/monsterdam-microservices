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

describe('PersonalSocialLinks e2e test', () => {
  const personalSocialLinksPageUrl = '/personal-social-links';
  const personalSocialLinksPageUrlPattern = new RegExp('/personal-social-links(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const personalSocialLinksSample = {"socialLink":"defiantly yowza evangelise","createdDate":"2024-02-29T12:02:25.549Z","isDeleted":true};

  let personalSocialLinks;
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
      body: {"emailContact":"7q","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"unlike vide","coverPhotoS3Key":"since","mainContentUrl":"intensely","mobilePhone":"+347453030241993","websiteUrl":"wFa^%8@OQY.Q-","amazonWishlistUrl":"l;q@'.Gsi]+z","lastLoginDate":"2024-02-29T22:53:12.522Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T04:50:48.814Z","lastModifiedDate":"2024-02-29T23:15:04.976Z","createdBy":"finally explicate","lastModifiedBy":"lovingly","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/personal-social-links+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/personal-social-links').as('postEntityRequest');
    cy.intercept('DELETE', '/api/personal-social-links/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/social-networks', {
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
    if (personalSocialLinks) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/personal-social-links/${personalSocialLinks.id}`,
      }).then(() => {
        personalSocialLinks = undefined;
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

  it('PersonalSocialLinks menu should load PersonalSocialLinks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('personal-social-links');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PersonalSocialLinks').should('exist');
    cy.url().should('match', personalSocialLinksPageUrlPattern);
  });

  describe('PersonalSocialLinks page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personalSocialLinksPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PersonalSocialLinks page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/personal-social-links/new$'));
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/personal-social-links',
          body: {
            ...personalSocialLinksSample,
            user: userProfile,
          },
        }).then(({ body }) => {
          personalSocialLinks = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/personal-social-links+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/personal-social-links?page=0&size=20>; rel="last",<http://localhost/api/personal-social-links?page=0&size=20>; rel="first"',
              },
              body: [personalSocialLinks],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(personalSocialLinksPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(personalSocialLinksPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PersonalSocialLinks page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personalSocialLinks');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it('edit button click should load edit PersonalSocialLinks page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it('edit button click should load edit PersonalSocialLinks page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PersonalSocialLinks', () => {
        cy.intercept('GET', '/api/personal-social-links/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('personalSocialLinks').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);

        personalSocialLinks = undefined;
      });
    });
  });

  describe('new PersonalSocialLinks page', () => {
    beforeEach(() => {
      cy.visit(`${personalSocialLinksPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
    });

    it.skip('should create an instance of PersonalSocialLinks', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('normalImage', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="normalImageS3Key"]`).type('inasmuch provided');
      cy.get(`[data-cy="normalImageS3Key"]`).should('have.value', 'inasmuch provided');

      cy.get(`[data-cy="thumbnailIconS3Key"]`).type('karate gadzooks change');
      cy.get(`[data-cy="thumbnailIconS3Key"]`).should('have.value', 'karate gadzooks change');

      cy.get(`[data-cy="socialLink"]`).type('idiom pfft praise');
      cy.get(`[data-cy="socialLink"]`).should('have.value', 'idiom pfft praise');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T16:47');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T16:47');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T04:25');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T04:25');

      cy.get(`[data-cy="createdBy"]`).type('overrate');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'overrate');

      cy.get(`[data-cy="lastModifiedBy"]`).type('gee open');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'gee open');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        personalSocialLinks = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', personalSocialLinksPageUrlPattern);
    });
  });
});
