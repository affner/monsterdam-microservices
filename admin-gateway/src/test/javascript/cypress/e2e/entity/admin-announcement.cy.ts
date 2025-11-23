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

describe('AdminAnnouncement e2e test', () => {
  const adminAnnouncementPageUrl = '/admin-announcement';
  const adminAnnouncementPageUrlPattern = new RegExp('/admin-announcement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adminAnnouncementSample = {
    announcementType: 'SYSTEM_UPDATE',
    title: 'whose whose buck',
    content: 'marketing',
    createdDate: '2024-03-02T09:15:14.904Z',
    directMessageId: 26300,
  };

  let adminAnnouncement;
  let adminUserProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/admin-user-profiles',
      body: {
        fullName: '_3b',
        emailAddress: "3@'.!~O",
        nickName: 'wvyoi687xllr',
        gender: 'MALE',
        mobilePhone: '40205214097',
        lastLoginDate: '2024-03-01T21:03:08.403Z',
        birthDate: '2024-03-02',
        createdDate: '2024-03-01T22:55:30.960Z',
        lastModifiedDate: '2024-03-02T11:32:56.446Z',
        createdBy: 'where',
        lastModifiedBy: 'quaintly',
        isDeleted: false,
      },
    }).then(({ body }) => {
      adminUserProfile = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/admin-announcements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/admin-announcements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/admin-announcements/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/admin-user-profiles', {
      statusCode: 200,
      body: [adminUserProfile],
    });
  });

  afterEach(() => {
    if (adminAnnouncement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admin-announcements/${adminAnnouncement.id}`,
      }).then(() => {
        adminAnnouncement = undefined;
      });
    }
  });

  afterEach(() => {
    if (adminUserProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admin-user-profiles/${adminUserProfile.id}`,
      }).then(() => {
        adminUserProfile = undefined;
      });
    }
  });

  it('AdminAnnouncements menu should load AdminAnnouncements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('admin-announcement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AdminAnnouncement').should('exist');
    cy.url().should('match', adminAnnouncementPageUrlPattern);
  });

  describe('AdminAnnouncement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adminAnnouncementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AdminAnnouncement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/admin-announcement/new$'));
        cy.getEntityCreateUpdateHeading('AdminAnnouncement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminAnnouncementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/admin-announcements',
          body: {
            ...adminAnnouncementSample,
            admin: adminUserProfile,
          },
        }).then(({ body }) => {
          adminAnnouncement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/admin-announcements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [adminAnnouncement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adminAnnouncementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AdminAnnouncement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adminAnnouncement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminAnnouncementPageUrlPattern);
      });

      it('edit button click should load edit AdminAnnouncement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminAnnouncement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminAnnouncementPageUrlPattern);
      });

      it('edit button click should load edit AdminAnnouncement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminAnnouncement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminAnnouncementPageUrlPattern);
      });

      it('last delete button click should delete instance of AdminAnnouncement', () => {
        cy.intercept('GET', '/api/admin-announcements/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('adminAnnouncement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminAnnouncementPageUrlPattern);

        adminAnnouncement = undefined;
      });
    });
  });

  describe('new AdminAnnouncement page', () => {
    beforeEach(() => {
      cy.visit(`${adminAnnouncementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AdminAnnouncement');
    });

    it('should create an instance of AdminAnnouncement', () => {
      cy.get(`[data-cy="announcementType"]`).select('COMMUNITY_ALERT');

      cy.get(`[data-cy="title"]`).type('brr');
      cy.get(`[data-cy="title"]`).should('have.value', 'brr');

      cy.get(`[data-cy="content"]`).type('donor kaleidoscopic but');
      cy.get(`[data-cy="content"]`).should('have.value', 'donor kaleidoscopic but');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T11:12');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T11:12');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-01T18:31');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-01T18:31');

      cy.get(`[data-cy="createdBy"]`).type('sadly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'sadly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('circa yet scholarly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'circa yet scholarly');

      cy.get(`[data-cy="directMessageId"]`).type('23054');
      cy.get(`[data-cy="directMessageId"]`).should('have.value', '23054');

      cy.get(`[data-cy="admin"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        adminAnnouncement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adminAnnouncementPageUrlPattern);
    });
  });
});
