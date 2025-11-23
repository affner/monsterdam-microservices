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

describe('UserEvent e2e test', () => {
  const userEventPageUrl = '/user-event';
  const userEventPageUrlPattern = new RegExp('/user-event(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userEventSample = {"title":"during whoever","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","startDate":"2024-02-29","endDate":"2024-02-29","createdDate":"2024-02-29T16:23:16.303Z","isDeleted":false};

  let userEvent;
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
      body: {"emailContact":"hi9","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"unless","coverPhotoS3Key":"impala narrow fairly","mainContentUrl":"nor","mobilePhone":"61361476396","websiteUrl":"F@*GMNB.<byRv","amazonWishlistUrl":"XI<?;C@N~.o/","lastLoginDate":"2024-02-29T22:38:49.861Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T01:27:07.438Z","lastModifiedDate":"2024-02-28T23:58:40.142Z","createdBy":"caterwaul of","lastModifiedBy":"vision ouch bet","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-events+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-events').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-events/*').as('deleteEntityRequest');
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
    if (userEvent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-events/${userEvent.id}`,
      }).then(() => {
        userEvent = undefined;
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

  it('UserEvents menu should load UserEvents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-event');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserEvent').should('exist');
    cy.url().should('match', userEventPageUrlPattern);
  });

  describe('UserEvent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userEventPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserEvent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-event/new$'));
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-events',
          body: {
            ...userEventSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          userEvent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-events+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-events?page=0&size=20>; rel="last",<http://localhost/api/user-events?page=0&size=20>; rel="first"',
              },
              body: [userEvent],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userEventPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userEventPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserEvent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userEvent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it('edit button click should load edit UserEvent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it('edit button click should load edit UserEvent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of UserEvent', () => {
        cy.intercept('GET', '/api/user-events/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userEvent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);

        userEvent = undefined;
      });
    });
  });

  describe('new UserEvent page', () => {
    beforeEach(() => {
      cy.visit(`${userEventPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserEvent');
    });

    it.skip('should create an instance of UserEvent', () => {
      cy.get(`[data-cy="title"]`).type('cruelly pish prevalence');
      cy.get(`[data-cy="title"]`).should('have.value', 'cruelly pish prevalence');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="startDate"]`).type('2024-02-29');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="creatorEventStatus"]`).select('ACTIVE');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:35');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:35');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T19:44');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T19:44');

      cy.get(`[data-cy="createdBy"]`).type('whoa download upon');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'whoa download upon');

      cy.get(`[data-cy="lastModifiedBy"]`).type('trash');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'trash');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userEvent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userEventPageUrlPattern);
    });
  });
});
