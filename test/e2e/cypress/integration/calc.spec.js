/// <reference types="cypress" />

context('Calc', () => {
  beforeEach(() => {
    cy.visit('http://calc-web/')
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
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-add').click()
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
    cy.fixture('result8.txt').as('result')
    cy.server()
    cy.route('GET', 'calc/substract/4/-4', '@result').as('getResult')

    cy.get('#in-op1').clear().type('4')
    cy.get('#in-op2').clear().type('-4')
    cy.get('#button-substract').click()

    cy.wait('@getResult')

    cy.get('#result-area').should('have.text', "Result: 8")
    cy.screenshot()
  })

  it('increases the history log', () => {
    // Establecer valores para los operandos
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    
    // Hacer clic en el botón de suma y esperar que se actualice el resultado
    cy.get('#button-add').click()
    cy.get('#result-area').should('have.text', "Result: 5")
    
    // Hacer clic nuevamente y esperar el resultado
    cy.get('#button-add').click()
    cy.get('#result-area').should('have.text', "Result: 5")
    
    // Hacer clic una tercera vez y esperar el resultado
    cy.get('#button-add').click()
    cy.get('#result-area').should('have.text', "Result: 5")
    
    // Verificar que el historial tenga 3 entradas
    cy.get('#history-log').children().its('length').should('eq', 3)
    cy.screenshot()
  })

})
