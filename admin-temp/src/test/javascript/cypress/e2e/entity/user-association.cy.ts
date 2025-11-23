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

describe('UserAssociation e2e test', () => {
  const userAssociationPageUrl = '/user-association';
  const userAssociationPageUrlPattern = new RegExp('/user-association(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userAssociationSample = {"requestedDate":"2024-02-29T02:42:21.104Z","associationToken":"jovially","expiryDate":"2024-02-29T22:18:12.929Z","createdDate":"2024-02-29T08:09:32.046Z","isDeleted":true};

  let userAssociation;
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
      body: {"emailContact":"2i7dmz","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"courageously rebel","coverPhotoS3Key":"sneeze valid regarding","mainContentUrl":"hm solidly","mobilePhone":"8007976436984","websiteUrl":"lbVr($@/.UmM`","amazonWishlistUrl":"9M3S6@4.+~","lastLoginDate":"2024-02-29T11:43:39.782Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T02:27:24.062Z","lastModifiedDate":"2024-02-29T02:37:11.872Z","createdBy":"hmph as wedge","lastModifiedBy":"vice defiantly viciously","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-associations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-associations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-associations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (userAssociation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-associations/${userAssociation.id}`,
      }).then(() => {
        userAssociation = undefined;
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

  it('UserAssociations menu should load UserAssociations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-association');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserAssociation').should('exist');
    cy.url().should('match', userAssociationPageUrlPattern);
  });

  describe('UserAssociation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userAssociationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserAssociation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-association/new$'));
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-associations',
          body: {
            ...userAssociationSample,
            owner: userProfile,
          },
        }).then(({ body }) => {
          userAssociation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-associations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-associations?page=0&size=20>; rel="last",<http://localhost/api/user-associations?page=0&size=20>; rel="first"',
              },
              body: [userAssociation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userAssociationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userAssociationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserAssociation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userAssociation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it('edit button click should load edit UserAssociation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it('edit button click should load edit UserAssociation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of UserAssociation', () => {
        cy.intercept('GET', '/api/user-associations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userAssociation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);

        userAssociation = undefined;
      });
    });
  });

  describe('new UserAssociation page', () => {
    beforeEach(() => {
      cy.visit(`${userAssociationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserAssociation');
    });

    it.skip('should create an instance of UserAssociation', () => {
      cy.get(`[data-cy="requestedDate"]`).type('2024-02-29T11:42');
      cy.get(`[data-cy="requestedDate"]`).blur();
      cy.get(`[data-cy="requestedDate"]`).should('have.value', '2024-02-29T11:42');

      cy.get(`[data-cy="status"]`).select('REJECTED');

      cy.get(`[data-cy="associationToken"]`).type('when infiltrate');
      cy.get(`[data-cy="associationToken"]`).should('have.value', 'when infiltrate');

      cy.get(`[data-cy="expiryDate"]`).type('2024-02-29T14:17');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2024-02-29T14:17');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T06:01');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T06:01');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T11:06');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T11:06');

      cy.get(`[data-cy="createdBy"]`).type('since pucker relieved');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'since pucker relieved');

      cy.get(`[data-cy="lastModifiedBy"]`).type('briefly lightscreen onto');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'briefly lightscreen onto');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="owner"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userAssociation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userAssociationPageUrlPattern);
    });
  });
});
