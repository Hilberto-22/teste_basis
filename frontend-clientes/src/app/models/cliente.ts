export interface ClienteLista {
  id: string;
  nome: string;
  email: string;
  cidade: string;
  estado: string;
  siglaEstado: string;
  urlFoto: string;
}

export interface DetalhesCliente extends ClienteLista {
  endereco: string;
  sexo: string;
  telefone: string;
  celular: string;
  cep: string;
}

export interface RespostaClientes {
  total: number;
  pagina: number;
  limite: number;
  dados: ClienteLista[];
}

export interface FiltrosCliente {
  pagina: number;
  limite: number;
  estado?: string;
  nome?: string;
}
