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

describe('ChatRoom e2e test', () => {
  const chatRoomPageUrl = '/chat-room';
  const chatRoomPageUrlPattern = new RegExp('/chat-room(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const chatRoomSample = {"createdDate":"2024-02-29T15:32:45.008Z","isDeleted":true};

  let chatRoom;
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
      body: {"emailContact":"p","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"blah if","coverPhotoS3Key":"supposing washcloth tenderly","mainContentUrl":"jaunty","mobilePhone":"688832231870","websiteUrl":"%AyG!@$Yj.jy;eot","amazonWishlistUrl":"VqX]@4.K{*U","lastLoginDate":"2024-02-28T23:57:53.383Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T23:34:56.366Z","lastModifiedDate":"2024-02-29T19:01:39.057Z","createdBy":"envy yippee","lastModifiedBy":"or notation","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-rooms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-rooms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-rooms/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/direct-messages', {
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
    if (chatRoom) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-rooms/${chatRoom.id}`,
      }).then(() => {
        chatRoom = undefined;
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

  it('ChatRooms menu should load ChatRooms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-room');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatRoom').should('exist');
    cy.url().should('match', chatRoomPageUrlPattern);
  });

  describe('ChatRoom page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatRoomPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatRoom page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-room/new$'));
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-rooms',
          body: {
            ...chatRoomSample,
            user: userProfile,
          },
        }).then(({ body }) => {
          chatRoom = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-rooms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-rooms?page=0&size=20>; rel="last",<http://localhost/api/chat-rooms?page=0&size=20>; rel="first"',
              },
              body: [chatRoom],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatRoomPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(chatRoomPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ChatRoom page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatRoom');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it('edit button click should load edit ChatRoom page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it('edit button click should load edit ChatRoom page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ChatRoom', () => {
        cy.intercept('GET', '/api/chat-rooms/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatRoom').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);

        chatRoom = undefined;
      });
    });
  });

  describe('new ChatRoom page', () => {
    beforeEach(() => {
      cy.visit(`${chatRoomPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatRoom');
    });

    it.skip('should create an instance of ChatRoom', () => {
      cy.get(`[data-cy="lastAction"]`).type('endure zealous');
      cy.get(`[data-cy="lastAction"]`).should('have.value', 'endure zealous');

      cy.get(`[data-cy="lastConnectionDate"]`).type('2024-02-29T02:15');
      cy.get(`[data-cy="lastConnectionDate"]`).blur();
      cy.get(`[data-cy="lastConnectionDate"]`).should('have.value', '2024-02-29T02:15');

      cy.get(`[data-cy="muted"]`).should('not.be.checked');
      cy.get(`[data-cy="muted"]`).click();
      cy.get(`[data-cy="muted"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T23:00');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T23:00');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T03:14');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T03:14');

      cy.get(`[data-cy="createdBy"]`).type('mope past okra');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'mope past okra');

      cy.get(`[data-cy="lastModifiedBy"]`).type('gah');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'gah');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        chatRoom = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', chatRoomPageUrlPattern);
    });
  });
});
