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

As informações de Filial foram mockadas através do próprio banco de dados. No arquivo queryCrudVendedores.txt situado na raiz do projeto, está a query que foi utilizada para criação de tabelas e para simulação de possíveis valores para essa tabela.

### 🛠️ Pré-requisitos

Downloads necessários para execução do projeto:
- Java 11 e a sugestão de IDE é o [IntelliJ](https://www.jetbrains.com/pt-br/idea/download/?section=windows);
- O banco de dados utilizado foi um banco relacional, o [PostgreSQL](https://www.postgresql.org/).
- Para consumir as APIs utilizou-se o [Postman](https://www.postman.com/);
