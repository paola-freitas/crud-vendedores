<h1 align = "center">CRUD Vendedores</h1>

### ❗ Objetivos
    
Criar uma API que faça um CRUD completo considerando os seguintes dados:

Vendedor:
- matricula: obrigatória, única e que termine com "-OUT", "-PJ", "-CLT"
- nome: obrigatória  
- dataNascimento: opcional e válida
- email: obrigatório e válido
- documento: obrigatório, CNPJ válido (para PJ) e CPF válido (para terceirado e CLT)
- tipoContratacao: obrigatório e enum (OUTSOURCING, CLT, PESSOA_JURIDICA)
- filial: obrigatório (id,nome, cnpj, cidade, uf, tipo, ativo, dataCadastro, ultimaAtualizacao)
