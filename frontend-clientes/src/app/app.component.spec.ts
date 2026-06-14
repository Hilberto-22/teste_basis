import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { ClientesService } from './services/clientes.service';

describe('AppComponent', () => {
  const servicoClientesFalso = {
    listarClientes: jasmine.createSpy('listarClientes').and.returnValue(
      of({
        total: 1,
        pagina: 1,
        limite: 9,
        dados: [
          {
            id: '1',
            nome: 'Ana Souza',
            email: 'ana.souza@example.com',
            cidade: 'Sao Paulo',
            estado: 'Sao Paulo',
            siglaEstado: 'SP',
            urlFoto: 'https://example.com/ana.jpg'
          }
        ]
      })
    ),
    buscarClientePorId: jasmine.createSpy('buscarClientePorId').and.returnValue(
      of({
        id: '1',
        nome: 'Ana Souza',
        sexo: 'female',
        email: 'ana.souza@example.com',
        telefone: '(11) 1234-5678',
        celular: '(11) 91234-5678',
        endereco: 'Rua das Flores, 123',
        cidade: 'Sao Paulo',
        estado: 'Sao Paulo',
        siglaEstado: 'SP',
        cep: '01000-000',
        urlFoto: 'https://example.com/ana.jpg'
      })
    )
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: ClientesService, useValue: servicoClientesFalso }]
    }).compileComponents();
  });

  it('deve criar o aplicativo', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const aplicativo = fixture.componentInstance;
    expect(aplicativo).toBeTruthy();
  });

  it('deve possuir o titulo esperado', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const aplicativo = fixture.componentInstance;
    expect(aplicativo.titulo).toBeTruthy();
  });

  it('deve renderizar o titulo', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const conteudoRenderizado = fixture.nativeElement as HTMLElement;
    expect(conteudoRenderizado.querySelector('h1')?.textContent).toContain(
      fixture.componentInstance.titulo
    );
  });
});
