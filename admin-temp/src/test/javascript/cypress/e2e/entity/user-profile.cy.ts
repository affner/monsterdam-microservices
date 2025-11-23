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

describe('UserProfile e2e test', () => {
  const userProfilePageUrl = '/user-profile';
  const userProfilePageUrlPattern = new RegExp('/user-profile(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userProfileSample = {
    emailContact: 'tvfcf',
    lastLoginDate: '2024-02-29T06:05:57.969Z',
    createdDate: '2024-02-29T19:01:39.929Z',
    isDeleted: true,
  };

  let userProfile;
  let userLite;
  let userSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-lites',
      body: {
        thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        thumbnailContentType: 'unknown',
        thumbnailS3Key: 'usefully',
        birthDate: '2024-02-29',
        gender: 'FEMALE',
        createdDate: '2024-02-29T22:10:14.565Z',
        lastModifiedDate: '2024-02-29T10:40:57.375Z',
        createdBy: 'playfully blah furthermore',
        lastModifiedBy: 'ew completion',
        isDeleted: false,
        nickName: 'xm_8-q-',
        fullName: '6m',
        contentPreference: 'ALL',
      },
    }).then(({ body }) => {
      userLite = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-settings',
      body: {
        lastModifiedDate: '2024-02-29T21:14:45.732Z',
        darkMode: true,
        language: 'ES',
        contentFilter: false,
        messageBlurIntensity: 28254,
        activityStatusVisibility: false,
        twoFactorAuthentication: false,
        sessionsActiveCount: 23559,
        emailNotifications: false,
        importantSubscriptionNotifications: false,
        newMessages: false,
        postReplies: false,
        postLikes: false,
        newFollowers: true,
        smsNewStream: false,
        toastNewComment: false,
        toastNewLikes: false,
        toastNewStream: false,
        siteNewComment: true,
        siteNewLikes: true,
        siteDiscountsFromFollowedUsers: true,
        siteNewStream: false,
        siteUpcomingStreamReminders: true,
        createdDate: '2024-02-29T04:12:53.858Z',
        createdBy: 'geez',
        lastModifiedBy: 'bah gah reinscription',
        isDeleted: false,
      },
    }).then(({ body }) => {
      userSettings = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-profiles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });

    cy.intercept('GET', '/api/user-settings', {
      statusCode: 200,
      body: [userSettings],
    });

    cy.intercept('GET', '/api/user-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/assistance-tickets', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/money-payouts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/subscription-bundles', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/creator-earnings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/offer-promotions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-contents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-subscriptions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/wallet-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/personal-social-links', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/notifications', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-associations', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-events', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/book-marks', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/feedbacks', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/chat-rooms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-mentions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-comments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/poll-votes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/video-stories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/single-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/states', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/hash-tags', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
      });
    }
    if (userSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-settings/${userSettings.id}`,
      }).then(() => {
        userSettings = undefined;
      });
    }
  });

  it('UserProfiles menu should load UserProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserProfile').should('exist');
    cy.url().should('match', userProfilePageUrlPattern);
  });

  describe('UserProfile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-profile/new$'));
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-profiles',
          body: {
            ...userProfileSample,
            userLite: userLite,
            settings: userSettings,
          },
        }).then(({ body }) => {
          userProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-profiles?page=0&size=20>; rel="last",<http://localhost/api/user-profiles?page=0&size=20>; rel="first"',
              },
              body: [userProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('edit button click should load edit UserProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('edit button click should load edit UserProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('last delete button click should delete instance of UserProfile', () => {
        cy.intercept('GET', '/api/user-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);

        userProfile = undefined;
      });
    });
  });

  describe('new UserProfile page', () => {
    beforeEach(() => {
      cy.visit(`${userProfilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserProfile');
    });

    it('should create an instance of UserProfile', () => {
      cy.get(`[data-cy="emailContact"]`).type('90s');
      cy.get(`[data-cy="emailContact"]`).should('have.value', '90s');

      cy.setFieldImageAsBytesOfEntity('profilePhoto', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('coverPhoto', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="profilePhotoS3Key"]`).type('blindly oof');
      cy.get(`[data-cy="profilePhotoS3Key"]`).should('have.value', 'blindly oof');

      cy.get(`[data-cy="coverPhotoS3Key"]`).type('er');
      cy.get(`[data-cy="coverPhotoS3Key"]`).should('have.value', 'er');

      cy.get(`[data-cy="mainContentUrl"]`).type('whoa sharply funny');
      cy.get(`[data-cy="mainContentUrl"]`).should('have.value', 'whoa sharply funny');

      cy.get(`[data-cy="mobilePhone"]`).type('7162617816');
      cy.get(`[data-cy="mobilePhone"]`).should('have.value', '7162617816');

      cy.get(`[data-cy="websiteUrl"]`).type('sh?@&T.p');
      cy.get(`[data-cy="websiteUrl"]`).should('have.value', 'sh?@&T.p');

      cy.get(`[data-cy="amazonWishlistUrl"]`).type('24{=@%Z$a.!t$');
      cy.get(`[data-cy="amazonWishlistUrl"]`).should('have.value', '24{=@%Z$a.!t$');

      cy.get(`[data-cy="lastLoginDate"]`).type('2024-02-29T17:43');
      cy.get(`[data-cy="lastLoginDate"]`).blur();
      cy.get(`[data-cy="lastLoginDate"]`).should('have.value', '2024-02-29T17:43');

      cy.get(`[data-cy="biography"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="biography"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isFree"]`).should('not.be.checked');
      cy.get(`[data-cy="isFree"]`).click();
      cy.get(`[data-cy="isFree"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T17:28');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T17:28');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T06:19');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T06:19');

      cy.get(`[data-cy="createdBy"]`).type('scrimp besides');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'scrimp besides');

      cy.get(`[data-cy="lastModifiedBy"]`).type('dueling');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'dueling');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="userLite"]`).select(1);
      cy.get(`[data-cy="settings"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userProfilePageUrlPattern);
    });
  });
});
