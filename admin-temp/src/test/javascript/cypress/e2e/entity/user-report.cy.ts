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

describe('UserReport e2e test', () => {
  const userReportPageUrl = '/user-report';
  const userReportPageUrlPattern = new RegExp('/user-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userReportSample = {"status":"REVIEWED","createdDate":"2024-02-29T18:08:10.746Z","isDeleted":false,"reportCategory":"POST_REPORT"};

  let userReport;
  // let assistanceTicket;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/assistance-tickets',
      body: {"subject":"tortoise","description":"minus","status":"OPEN","type":"CONTENT_ISSUE","openedAt":"2024-02-29T21:31:31.615Z","closedAt":"2024-02-29T18:26:49.024Z","comments":"fertilizer deadly","createdDate":"2024-02-29T21:18:28.649Z","lastModifiedDate":"2024-02-29T01:14:42.356Z","createdBy":"strictly or clap","lastModifiedBy":"for divalent jovially"},
    }).then(({ body }) => {
      assistanceTicket = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"tl","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"revitalisation oh","coverPhotoS3Key":"wherever","mainContentUrl":"bah","mobilePhone":"4497549954828","websiteUrl":".>g,+@F.ec","amazonWishlistUrl":"Xs?@[R0.#W'dV","lastLoginDate":"2024-02-29T09:32:09.471Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T16:57:46.394Z","lastModifiedDate":"2024-02-29T11:29:41.206Z","createdBy":"cloak bludgeon yippee","lastModifiedBy":"deceive","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/assistance-tickets', {
      statusCode: 200,
      body: [assistanceTicket],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/video-stories', {
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

    cy.intercept('GET', '/api/single-audios', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/single-live-streams', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-comments', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (userReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-reports/${userReport.id}`,
      }).then(() => {
        userReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (assistanceTicket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assistance-tickets/${assistanceTicket.id}`,
      }).then(() => {
        assistanceTicket = undefined;
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

  it('UserReports menu should load UserReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserReport').should('exist');
    cy.url().should('match', userReportPageUrlPattern);
  });

  describe('UserReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-report/new$'));
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-reports',
          body: {
            ...userReportSample,
            ticket: assistanceTicket,
            reporter: userProfile,
            reported: userProfile,
          },
        }).then(({ body }) => {
          userReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-reports?page=0&size=20>; rel="last",<http://localhost/api/user-reports?page=0&size=20>; rel="first"',
              },
              body: [userReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of UserReport', () => {
        cy.intercept('GET', '/api/user-reports/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);

        userReport = undefined;
      });
    });
  });

  describe('new UserReport page', () => {
    beforeEach(() => {
      cy.visit(`${userReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserReport');
    });

    it.skip('should create an instance of UserReport', () => {
      cy.get(`[data-cy="reportDescription"]`).type('bow aha');
      cy.get(`[data-cy="reportDescription"]`).should('have.value', 'bow aha');

      cy.get(`[data-cy="status"]`).select('PENDING');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T09:38');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T09:38');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T12:09');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T12:09');

      cy.get(`[data-cy="createdBy"]`).type('excepting aggressive abaft');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'excepting aggressive abaft');

      cy.get(`[data-cy="lastModifiedBy"]`).type('nor pfft');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'nor pfft');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="reportCategory"]`).select('MESSAGE_REPORT');

      cy.get(`[data-cy="ticket"]`).select(1);
      cy.get(`[data-cy="reporter"]`).select(1);
      cy.get(`[data-cy="reported"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userReportPageUrlPattern);
    });
  });
});
