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

describe('SingleLiveStream e2e test', () => {
  const singleLiveStreamPageUrl = '/single-live-stream';
  const singleLiveStreamPageUrlPattern = new RegExp('/single-live-stream(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singleLiveStreamSample = {
    startTime: '2024-02-29T08:31:57.466Z',
    isRecorded: true,
    createdDate: '2024-02-29T00:33:19.794Z',
    isDeleted: true,
  };

  let singleLiveStream;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-live-streams+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-live-streams').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-live-streams/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (singleLiveStream) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-live-streams/${singleLiveStream.id}`,
      }).then(() => {
        singleLiveStream = undefined;
      });
    }
  });

  it('SingleLiveStreams menu should load SingleLiveStreams page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-live-stream');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleLiveStream').should('exist');
    cy.url().should('match', singleLiveStreamPageUrlPattern);
  });

  describe('SingleLiveStream page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleLiveStreamPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleLiveStream page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-live-stream/new$'));
        cy.getEntityCreateUpdateHeading('SingleLiveStream');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleLiveStreamPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-live-streams',
          body: singleLiveStreamSample,
        }).then(({ body }) => {
          singleLiveStream = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-live-streams+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-live-streams?page=0&size=20>; rel="last",<http://localhost/api/single-live-streams?page=0&size=20>; rel="first"',
              },
              body: [singleLiveStream],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleLiveStreamPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SingleLiveStream page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleLiveStream');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleLiveStreamPageUrlPattern);
      });

      it('edit button click should load edit SingleLiveStream page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleLiveStream');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleLiveStreamPageUrlPattern);
      });

      it('edit button click should load edit SingleLiveStream page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleLiveStream');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleLiveStreamPageUrlPattern);
      });

      it('last delete button click should delete instance of SingleLiveStream', () => {
        cy.intercept('GET', '/api/single-live-streams/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleLiveStream').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleLiveStreamPageUrlPattern);

        singleLiveStream = undefined;
      });
    });
  });

  describe('new SingleLiveStream page', () => {
    beforeEach(() => {
      cy.visit(`${singleLiveStreamPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleLiveStream');
    });

    it('should create an instance of SingleLiveStream', () => {
      cy.get(`[data-cy="title"]`).type('spec encompass times');
      cy.get(`[data-cy="title"]`).should('have.value', 'spec encompass times');

      cy.get(`[data-cy="description"]`).type('customer blah hm');
      cy.get(`[data-cy="description"]`).should('have.value', 'customer blah hm');

      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('groom incidentally');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'groom incidentally');

      cy.get(`[data-cy="startTime"]`).type('2024-02-29T22:12');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-02-29T22:12');

      cy.get(`[data-cy="endTime"]`).type('2024-02-29T22:02');
      cy.get(`[data-cy="endTime"]`).blur();
      cy.get(`[data-cy="endTime"]`).should('have.value', '2024-02-29T22:02');

      cy.setFieldImageAsBytesOfEntity('liveContent', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="liveContentS3Key"]`).type('merrily ring perspective');
      cy.get(`[data-cy="liveContentS3Key"]`).should('have.value', 'merrily ring perspective');

      cy.get(`[data-cy="isRecorded"]`).should('not.be.checked');
      cy.get(`[data-cy="isRecorded"]`).click();
      cy.get(`[data-cy="isRecorded"]`).should('be.checked');

      cy.get(`[data-cy="likeCount"]`).type('10023');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '10023');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T05:30');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T05:30');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T13:35');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T13:35');

      cy.get(`[data-cy="createdBy"]`).type('ew');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'ew');

      cy.get(`[data-cy="lastModifiedBy"]`).type('dearly likewise gah');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'dearly likewise gah');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        singleLiveStream = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', singleLiveStreamPageUrlPattern);
    });
  });
});
