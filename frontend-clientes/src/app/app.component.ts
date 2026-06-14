import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

import { ClienteLista, DetalhesCliente, RespostaClientes } from './models/cliente';
import { ClientesService } from './services/clientes.service';

interface EstadoBrasileiro {
  sigla: string;
  nome: string;
}

const ESTADOS_BRASILEIROS: EstadoBrasileiro[] = [
  { sigla: 'AC', nome: 'Acre' },
  { sigla: 'AL', nome: 'Alagoas' },
  { sigla: 'AP', nome: 'Amapa' },
  { sigla: 'AM', nome: 'Amazonas' },
  { sigla: 'BA', nome: 'Bahia' },
  { sigla: 'CE', nome: 'Ceara' },
  { sigla: 'DF', nome: 'Distrito Federal' },
  { sigla: 'ES', nome: 'Espirito Santo' },
  { sigla: 'GO', nome: 'Goias' },
  { sigla: 'MA', nome: 'Maranhao' },
  { sigla: 'MT', nome: 'Mato Grosso' },
  { sigla: 'MS', nome: 'Mato Grosso do Sul' },
  { sigla: 'MG', nome: 'Minas Gerais' },
  { sigla: 'PA', nome: 'Para' },
  { sigla: 'PB', nome: 'Paraiba' },
  { sigla: 'PR', nome: 'Parana' },
  { sigla: 'PE', nome: 'Pernambuco' },
  { sigla: 'PI', nome: 'Piaui' },
  { sigla: 'RJ', nome: 'Rio de Janeiro' },
  { sigla: 'RN', nome: 'Rio Grande do Norte' },
  { sigla: 'RS', nome: 'Rio Grande do Sul' },
  { sigla: 'RO', nome: 'Rondonia' },
  { sigla: 'RR', nome: 'Roraima' },
  { sigla: 'SC', nome: 'Santa Catarina' },
  { sigla: 'SP', nome: 'Sao Paulo' },
  { sigla: 'SE', nome: 'Sergipe' },
  { sigla: 'TO', nome: 'Tocantins' }
];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  readonly titulo = 'Lista de membros';
  readonly tamanhoPagina = 9;
  readonly cartoesEsqueleto = Array.from({ length: this.tamanhoPagina }, (_, indice) => indice);
  readonly estados = ESTADOS_BRASILEIROS;

  clientes: ClienteLista[] = [];
  clienteSelecionado: DetalhesCliente | null = null;
  idClienteSelecionado: string | null = null;

  termoBusca = '';
  estadoSelecionado = '';
  paginaAtual = 1;
  totalItens = 0;
  totalPaginas = 1;

  estaCarregandoLista = false;
  estaCarregandoDetalhes = false;
  erroLista = '';
  erroDetalhes = '';

  private readonly entradaBusca$ = new Subject<string>();
  private readonly servicoClientes = inject(ClientesService);
  private readonly referenciaDestruicao = inject(DestroyRef);

  ngOnInit(): void {
    this.entradaBusca$
      .pipe(
        debounceTime(350),
        distinctUntilChanged(),
        takeUntilDestroyed(this.referenciaDestruicao)
      )
      .subscribe(() => {
        this.paginaAtual = 1;
        this.carregarClientes();
      });

    this.carregarClientes();
  }

  get possuiFiltrosAtivos(): boolean {
    return this.termoBusca.trim().length > 0 || this.estadoSelecionado.length > 0;
  }

  get inicioPagina(): number {
    if (this.totalItens === 0) {
      return 0;
    }

    return (this.paginaAtual - 1) * this.tamanhoPagina + 1;
  }

  get fimPagina(): number {
    return Math.min(this.paginaAtual * this.tamanhoPagina, this.totalItens);
  }

  get paginasVisiveis(): number[] {
    const ultimaPagina = Math.max(1, this.totalPaginas);
    const inicio = Math.max(1, this.paginaAtual - 2);
    const fim = Math.min(ultimaPagina, inicio + 4);
    const inicioAjustado = Math.max(1, fim - 4);
    const paginas: number[] = [];

    for (let pagina = inicioAjustado; pagina <= fim; pagina += 1) {
      paginas.push(pagina);
    }

    return paginas;
  }

  aoAlterarBusca(valor: string): void {
    this.termoBusca = valor;
    this.entradaBusca$.next(valor.trim());
  }

  aoAlterarEstado(valor: string): void {
    this.estadoSelecionado = valor;
    this.paginaAtual = 1;
    this.carregarClientes();
  }

  limparFiltros(): void {
    if (!this.possuiFiltrosAtivos) {
      return;
    }

    this.termoBusca = '';
    this.estadoSelecionado = '';
    this.paginaAtual = 1;
    this.carregarClientes();
  }

  alterarPagina(pagina: number): void {
    if (
      pagina < 1 ||
      pagina > this.totalPaginas ||
      pagina === this.paginaAtual ||
      this.estaCarregandoLista
    ) {
      return;
    }

    this.paginaAtual = pagina;
    this.carregarClientes();
  }

  selecionarCliente(cliente: ClienteLista): void {
    if (this.idClienteSelecionado === cliente.id && this.clienteSelecionado) {
      return;
    }

    this.idClienteSelecionado = cliente.id;
    this.clienteSelecionado = null;
    this.erroDetalhes = '';
    this.estaCarregandoDetalhes = true;

    this.servicoClientes
      .buscarClientePorId(cliente.id)
      .pipe(takeUntilDestroyed(this.referenciaDestruicao))
      .subscribe({
        next: (detalhes) => {
          this.clienteSelecionado = detalhes;
          this.estaCarregandoDetalhes = false;
        },
        error: (erro: unknown) => {
          this.erroDetalhes = this.montarMensagemErro(
            erro,
            'Nao foi possivel carregar os detalhes deste cliente.'
          );
          this.estaCarregandoDetalhes = false;
        }
      });
  }

  private carregarClientes(): void {
    this.estaCarregandoLista = true;
    this.erroLista = '';

    this.servicoClientes
      .listarClientes({
        pagina: this.paginaAtual,
        limite: this.tamanhoPagina,
        estado: this.estadoSelecionado,
        nome: this.termoBusca.trim()
      })
      .pipe(takeUntilDestroyed(this.referenciaDestruicao))
      .subscribe({
        next: (resposta: RespostaClientes) => {
          this.clientes = resposta.dados;
          this.totalItens = resposta.total;
          this.totalPaginas = Math.max(1, Math.ceil(resposta.total / resposta.limite));
          this.estaCarregandoLista = false;

          if (!this.clientes.some((cliente) => cliente.id === this.idClienteSelecionado)) {
            this.clienteSelecionado = null;
            this.idClienteSelecionado = null;
            this.erroDetalhes = '';
          }
        },
        error: (erro: unknown) => {
          this.clientes = [];
          this.totalItens = 0;
          this.totalPaginas = 1;
          this.clienteSelecionado = null;
          this.idClienteSelecionado = null;
          this.erroLista = this.montarMensagemErro(
            erro,
            'Nao foi possivel carregar a lista de clientes.'
          );
          this.estaCarregandoLista = false;
        }
      });
  }

  private montarMensagemErro(erro: unknown, mensagemPadrao: string): string {
    if (erro instanceof HttpErrorResponse && erro.status === 0) {
      return 'Nao foi possivel conectar ao backend em http://localhost:8080.';
    }

    return mensagemPadrao;
  }
}
