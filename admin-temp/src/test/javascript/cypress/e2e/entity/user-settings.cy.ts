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

describe('UserSettings e2e test', () => {
  const userSettingsPageUrl = '/user-settings';
  const userSettingsPageUrlPattern = new RegExp('/user-settings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userSettingsSample = {
    darkMode: true,
    language: 'FR',
    contentFilter: false,
    activityStatusVisibility: false,
    twoFactorAuthentication: false,
    emailNotifications: false,
    importantSubscriptionNotifications: true,
    newMessages: false,
    postReplies: false,
    postLikes: false,
    newFollowers: true,
    smsNewStream: true,
    toastNewComment: false,
    toastNewLikes: false,
    toastNewStream: true,
    siteNewComment: false,
    siteNewLikes: false,
    siteDiscountsFromFollowedUsers: true,
    siteNewStream: true,
    siteUpcomingStreamReminders: true,
    createdDate: '2024-02-29T01:18:44.991Z',
    isDeleted: true,
  };

  let userSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-settings/${userSettings.id}`,
      }).then(() => {
        userSettings = undefined;
      });
    }
  });

  it('UserSettings menu should load UserSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-settings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserSettings').should('exist');
    cy.url().should('match', userSettingsPageUrlPattern);
  });

  describe('UserSettings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userSettingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserSettings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-settings/new$'));
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-settings',
          body: userSettingsSample,
        }).then(({ body }) => {
          userSettings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-settings?page=0&size=20>; rel="last",<http://localhost/api/user-settings?page=0&size=20>; rel="first"',
              },
              body: [userSettings],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userSettingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserSettings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userSettings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('edit button click should load edit UserSettings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('edit button click should load edit UserSettings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('last delete button click should delete instance of UserSettings', () => {
        cy.intercept('GET', '/api/user-settings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);

        userSettings = undefined;
      });
    });
  });

  describe('new UserSettings page', () => {
    beforeEach(() => {
      cy.visit(`${userSettingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserSettings');
    });

    it('should create an instance of UserSettings', () => {
      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T18:39');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T18:39');

      cy.get(`[data-cy="darkMode"]`).should('not.be.checked');
      cy.get(`[data-cy="darkMode"]`).click();
      cy.get(`[data-cy="darkMode"]`).should('be.checked');

      cy.get(`[data-cy="language"]`).select('PT_BR');

      cy.get(`[data-cy="contentFilter"]`).should('not.be.checked');
      cy.get(`[data-cy="contentFilter"]`).click();
      cy.get(`[data-cy="contentFilter"]`).should('be.checked');

      cy.get(`[data-cy="messageBlurIntensity"]`).type('5413');
      cy.get(`[data-cy="messageBlurIntensity"]`).should('have.value', '5413');

      cy.get(`[data-cy="activityStatusVisibility"]`).should('not.be.checked');
      cy.get(`[data-cy="activityStatusVisibility"]`).click();
      cy.get(`[data-cy="activityStatusVisibility"]`).should('be.checked');

      cy.get(`[data-cy="twoFactorAuthentication"]`).should('not.be.checked');
      cy.get(`[data-cy="twoFactorAuthentication"]`).click();
      cy.get(`[data-cy="twoFactorAuthentication"]`).should('be.checked');

      cy.get(`[data-cy="sessionsActiveCount"]`).type('22375');
      cy.get(`[data-cy="sessionsActiveCount"]`).should('have.value', '22375');

      cy.get(`[data-cy="emailNotifications"]`).should('not.be.checked');
      cy.get(`[data-cy="emailNotifications"]`).click();
      cy.get(`[data-cy="emailNotifications"]`).should('be.checked');

      cy.get(`[data-cy="importantSubscriptionNotifications"]`).should('not.be.checked');
      cy.get(`[data-cy="importantSubscriptionNotifications"]`).click();
      cy.get(`[data-cy="importantSubscriptionNotifications"]`).should('be.checked');

      cy.get(`[data-cy="newMessages"]`).should('not.be.checked');
      cy.get(`[data-cy="newMessages"]`).click();
      cy.get(`[data-cy="newMessages"]`).should('be.checked');

      cy.get(`[data-cy="postReplies"]`).should('not.be.checked');
      cy.get(`[data-cy="postReplies"]`).click();
      cy.get(`[data-cy="postReplies"]`).should('be.checked');

      cy.get(`[data-cy="postLikes"]`).should('not.be.checked');
      cy.get(`[data-cy="postLikes"]`).click();
      cy.get(`[data-cy="postLikes"]`).should('be.checked');

      cy.get(`[data-cy="newFollowers"]`).should('not.be.checked');
      cy.get(`[data-cy="newFollowers"]`).click();
      cy.get(`[data-cy="newFollowers"]`).should('be.checked');

      cy.get(`[data-cy="smsNewStream"]`).should('not.be.checked');
      cy.get(`[data-cy="smsNewStream"]`).click();
      cy.get(`[data-cy="smsNewStream"]`).should('be.checked');

      cy.get(`[data-cy="toastNewComment"]`).should('not.be.checked');
      cy.get(`[data-cy="toastNewComment"]`).click();
      cy.get(`[data-cy="toastNewComment"]`).should('be.checked');

      cy.get(`[data-cy="toastNewLikes"]`).should('not.be.checked');
      cy.get(`[data-cy="toastNewLikes"]`).click();
      cy.get(`[data-cy="toastNewLikes"]`).should('be.checked');

      cy.get(`[data-cy="toastNewStream"]`).should('not.be.checked');
      cy.get(`[data-cy="toastNewStream"]`).click();
      cy.get(`[data-cy="toastNewStream"]`).should('be.checked');

      cy.get(`[data-cy="siteNewComment"]`).should('not.be.checked');
      cy.get(`[data-cy="siteNewComment"]`).click();
      cy.get(`[data-cy="siteNewComment"]`).should('be.checked');

      cy.get(`[data-cy="siteNewLikes"]`).should('not.be.checked');
      cy.get(`[data-cy="siteNewLikes"]`).click();
      cy.get(`[data-cy="siteNewLikes"]`).should('be.checked');

      cy.get(`[data-cy="siteDiscountsFromFollowedUsers"]`).should('not.be.checked');
      cy.get(`[data-cy="siteDiscountsFromFollowedUsers"]`).click();
      cy.get(`[data-cy="siteDiscountsFromFollowedUsers"]`).should('be.checked');

      cy.get(`[data-cy="siteNewStream"]`).should('not.be.checked');
      cy.get(`[data-cy="siteNewStream"]`).click();
      cy.get(`[data-cy="siteNewStream"]`).should('be.checked');

      cy.get(`[data-cy="siteUpcomingStreamReminders"]`).should('not.be.checked');
      cy.get(`[data-cy="siteUpcomingStreamReminders"]`).click();
      cy.get(`[data-cy="siteUpcomingStreamReminders"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T22:49');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T22:49');

      cy.get(`[data-cy="createdBy"]`).type('yuck who boohoo');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yuck who boohoo');

      cy.get(`[data-cy="lastModifiedBy"]`).type('humanise whenever');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'humanise whenever');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userSettings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userSettingsPageUrlPattern);
    });
  });
});
