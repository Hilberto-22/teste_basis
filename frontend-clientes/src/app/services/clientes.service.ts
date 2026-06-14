import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';

import { ClienteLista, DetalhesCliente, FiltrosCliente, RespostaClientes } from '../models/cliente';

interface ClienteListaApi {
  id: string;
  name: string;
  email: string;
  city: string;
  state: string;
  stateCode: string;
  pictureUrl: string;
}

interface DetalhesClienteApi extends ClienteListaApi {
  address: string;
  gender: string;
  phone: string;
  cell: string;
  postcode: string;
}

interface RespostaClientesApi {
  total: number;
  page: number;
  limit: number;
  data: ClienteListaApi[];
}

@Injectable({
  providedIn: 'root'
})
export class ClientesService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = '/api/clients';

  listarClientes(filtros: FiltrosCliente) {
    let parametros = new HttpParams()
      .set('page', filtros.pagina)
      .set('limit', filtros.limite);

    if (filtros.estado) {
      parametros = parametros.set('state', filtros.estado);
    }

    if (filtros.nome) {
      parametros = parametros.set('name', filtros.nome);
    }

    return this.http
      .get<RespostaClientesApi>(this.urlApi, { params: parametros })
      .pipe(map((resposta) => this.mapearRespostaClientes(resposta)));
  }

  buscarClientePorId(idCliente: string) {
    return this.http
      .get<DetalhesClienteApi>(`${this.urlApi}/${idCliente}`)
      .pipe(map((cliente) => this.mapearDetalhesCliente(cliente)));
  }

  private mapearRespostaClientes(resposta: RespostaClientesApi): RespostaClientes {
    return {
      total: resposta.total,
      pagina: resposta.page,
      limite: resposta.limit,
      dados: resposta.data.map((cliente) => this.mapearClienteLista(cliente))
    };
  }

  private mapearClienteLista(cliente: ClienteListaApi): ClienteLista {
    return {
      id: cliente.id,
      nome: cliente.name,
      email: cliente.email,
      cidade: cliente.city,
      estado: cliente.state,
      siglaEstado: cliente.stateCode,
      urlFoto: cliente.pictureUrl
    };
  }

  private mapearDetalhesCliente(cliente: DetalhesClienteApi): DetalhesCliente {
    return {
      ...this.mapearClienteLista(cliente),
      endereco: cliente.address,
      sexo: cliente.gender,
      telefone: cliente.phone,
      celular: cliente.cell,
      cep: cliente.postcode
    };
  }
}
