/// <reference types="cypress" />

context('Calc', () => {
  beforeEach(() => {
    cy.visit('http://calc-web')
  })

  it('get the title', () => {
    cy.title().should('include', 'Calculator')
  })

  it('can type operands', () => {
    cy.get('#in-op1').clear().should('have.value', '')
      .type('5').should('have.value', '5')
    cy.get('#in-op2').clear().should('have.value', '')
      .type('-5').should('have.value', '-5')
  })

  it('can click add', () => {
    cy.intercept('GET', '**/calc/add/2/3').as('addOperation')
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-add').click()
    cy.wait('@addOperation')
    cy.get('#result-area').should('have.text', "Result: 5")
    cy.screenshot()
  })

  it.skip('can click multiply', () => {
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-multiply').click()
    cy.get('#result-area').should('have.text', "Result: 6")
    cy.screenshot()
  })

  it('can click substract (using fixture)', () => {
    cy.intercept('GET', '**/calc/substract/4/-4', {
      statusCode: 200,
      body: "8"
    }).as('getResult')

    cy.get('#in-op1').clear().type('4')
    cy.get('#in-op2').clear().type('-4')
    cy.get('#button-substract').click()

    cy.wait('@getResult')
    cy.get('#result-area').should('have.text', "Result: 8")
    cy.screenshot()
  })

  it('increases the history log', () => {
    cy.intercept('GET', '**/calc/add/1/1').as('add1')
    cy.intercept('GET', '**/calc/add/2/2').as('add2')
    cy.intercept('GET', '**/calc/add/3/3').as('add3')

    cy.get('#in-op1').clear().type('1')
    cy.get('#in-op2').clear().type('1')
    cy.get('#button-add').click()
    cy.wait('@add1')

    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('2')
    cy.get('#button-add').click()
    cy.wait('@add2')

    cy.get('#in-op1').clear().type('3')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-add').click()
    cy.wait('@add3')

    cy.get('#history-log').children().should('have.length', 3)
    cy.screenshot()
  })

})
