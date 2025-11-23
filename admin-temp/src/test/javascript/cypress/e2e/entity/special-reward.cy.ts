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

describe('SpecialReward e2e test', () => {
  const specialRewardPageUrl = '/special-reward';
  const specialRewardPageUrlPattern = new RegExp('/special-reward(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specialRewardSample = {
    description: 'fitting',
    createdDate: '2024-02-29T07:51:37.130Z',
    isDeleted: false,
    contentPackageId: 7159,
    viewerId: 1821,
    offerPromotionId: 16190,
  };

  let specialReward;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/special-rewards+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/special-rewards').as('postEntityRequest');
    cy.intercept('DELETE', '/api/special-rewards/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (specialReward) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/special-rewards/${specialReward.id}`,
      }).then(() => {
        specialReward = undefined;
      });
    }
  });

  it('SpecialRewards menu should load SpecialRewards page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('special-reward');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SpecialReward').should('exist');
    cy.url().should('match', specialRewardPageUrlPattern);
  });

  describe('SpecialReward page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specialRewardPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SpecialReward page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/special-reward/new$'));
        cy.getEntityCreateUpdateHeading('SpecialReward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialRewardPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/special-rewards',
          body: specialRewardSample,
        }).then(({ body }) => {
          specialReward = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/special-rewards+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/special-rewards?page=0&size=20>; rel="last",<http://localhost/api/special-rewards?page=0&size=20>; rel="first"',
              },
              body: [specialReward],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(specialRewardPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SpecialReward page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specialReward');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialRewardPageUrlPattern);
      });

      it('edit button click should load edit SpecialReward page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialReward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialRewardPageUrlPattern);
      });

      it('edit button click should load edit SpecialReward page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialReward');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialRewardPageUrlPattern);
      });

      it('last delete button click should delete instance of SpecialReward', () => {
        cy.intercept('GET', '/api/special-rewards/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('specialReward').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialRewardPageUrlPattern);

        specialReward = undefined;
      });
    });
  });

  describe('new SpecialReward page', () => {
    beforeEach(() => {
      cy.visit(`${specialRewardPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SpecialReward');
    });

    it('should create an instance of SpecialReward', () => {
      cy.get(`[data-cy="description"]`).type('warlike cue');
      cy.get(`[data-cy="description"]`).should('have.value', 'warlike cue');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T10:30');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T10:30');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T13:11');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T13:11');

      cy.get(`[data-cy="createdBy"]`).type('blissfully beside uselessly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'blissfully beside uselessly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('uh-huh');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'uh-huh');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="contentPackageId"]`).type('18597');
      cy.get(`[data-cy="contentPackageId"]`).should('have.value', '18597');

      cy.get(`[data-cy="viewerId"]`).type('20875');
      cy.get(`[data-cy="viewerId"]`).should('have.value', '20875');

      cy.get(`[data-cy="offerPromotionId"]`).type('12970');
      cy.get(`[data-cy="offerPromotionId"]`).should('have.value', '12970');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specialReward = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specialRewardPageUrlPattern);
    });
  });
});
