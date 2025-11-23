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
  const userReportSample = {
    status: 'REVIEWED',
    createdDate: '2024-03-02T12:43:20.173Z',
    isDeleted: false,
    reportCategory: 'POST_REPORT',
    reporterId: 15389,
    reportedId: 28331,
  };

  let userReport;
  let assistanceTicket;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/assistance-tickets',
      body: {
        subject: 'while rudely consequently',
        description: 'since',
        status: 'OPEN',
        type: 'CONTENT_ISSUE',
        openedAt: '2024-03-02T00:50:40.749Z',
        closedAt: '2024-03-02T13:08:38.297Z',
        comments: 'alongside investigation',
        createdDate: '2024-03-02T03:08:18.997Z',
        lastModifiedDate: '2024-03-01T22:04:59.007Z',
        createdBy: 'dip',
        lastModifiedBy: 'how',
        userId: 25498,
      },
    }).then(({ body }) => {
      assistanceTicket = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-reports/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/assistance-tickets', {
      statusCode: 200,
      body: [assistanceTicket],
    });
  });

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

  afterEach(() => {
    if (assistanceTicket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assistance-tickets/${assistanceTicket.id}`,
      }).then(() => {
        assistanceTicket = undefined;
      });
    }
  });

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
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-reports',
          body: {
            ...userReportSample,
            ticket: assistanceTicket,
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
              body: [userReport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userReportPageUrl);

        cy.wait('@entitiesRequestInternal');
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

      it('last delete button click should delete instance of UserReport', () => {
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

    it('should create an instance of UserReport', () => {
      cy.get(`[data-cy="reportDescription"]`).type('whoa');
      cy.get(`[data-cy="reportDescription"]`).should('have.value', 'whoa');

      cy.get(`[data-cy="status"]`).select('REVIEWED');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T12:57');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T12:57');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-02T00:57');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-02T00:57');

      cy.get(`[data-cy="createdBy"]`).type('mechanic total');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'mechanic total');

      cy.get(`[data-cy="lastModifiedBy"]`).type('abaft');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'abaft');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="reportCategory"]`).select('MESSAGE_REPORT');

      cy.get(`[data-cy="reporterId"]`).type('23244');
      cy.get(`[data-cy="reporterId"]`).should('have.value', '23244');

      cy.get(`[data-cy="reportedId"]`).type('26820');
      cy.get(`[data-cy="reportedId"]`).should('have.value', '26820');

      cy.get(`[data-cy="multimediaId"]`).type('7859');
      cy.get(`[data-cy="multimediaId"]`).should('have.value', '7859');

      cy.get(`[data-cy="messageId"]`).type('17443');
      cy.get(`[data-cy="messageId"]`).should('have.value', '17443');

      cy.get(`[data-cy="postId"]`).type('241');
      cy.get(`[data-cy="postId"]`).should('have.value', '241');

      cy.get(`[data-cy="commentId"]`).type('14830');
      cy.get(`[data-cy="commentId"]`).should('have.value', '14830');

      cy.get(`[data-cy="ticket"]`).select(1);

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
