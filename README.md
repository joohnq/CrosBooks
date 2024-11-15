# CrosBooks

**CrosBooks** é um aplicativo de gerenciamento de livros que permite ao usuário realizar operações básicas de CRUD (Create, Read, e Delete) em um acervo de livros, integrando-se à API do Swagger.

<img width="1920" alt="Community Cover0" src="https://github.com/user-attachments/assets/ac553d14-d1ce-4123-8103-c47f83eabe8f">

### Funcionalidades:
- 📚 **Pegar livros (GET)** – Exibe uma lista de livros.
- ➕ **Enviar livros (POST)** – Adiciona ou atualiza um livro.
- ❌ **Deletar livros (DELETE)** – Remove um livro da lista.

## 🛠 Tecnologias e Ferramentas Utilizadas

- **Kotlin** 🟢
- **MVVM** (Model-View-Viewmodel) 🖥️
- **Koin** (Injeção de Dependências) 🔌
- **RecyclerView** 📜
- **Retrofit** 🔄
- **OkHttp** 🌐
- **Gson** 📦
- **Coroutine** ⏳
- **Flow** 🔄
- **LiveData** 📡
- **Datastore** 💾

## 📱 Telas

- 🔑 **Login** – Login com o email e a senha do usuário. Após o login, o token gerado pela API é armazenado localmente com **DataStore** e usado em futuras requisições por meio de um **Interceptor**.
- 📝 **Registro** – Registrar um novo usuário com nome, email e senha.
- 🏠 **Home (com Paginação)** – Listagem dos livros do usuário e das categorias disponíveis.
- 📖 **Detalhe do Livro** – Exibição detalhada do livro.
- ➕ **Adicionar Livro** – Etapas: 1. Pegar a imagem do livro e enviá-la para o servidor. 2. Enviar os dados do livro junto com a URL da imagem.
- 🔍 **Search (com Paginação)** – Tela de pesquisa para filtrar livros pelo nome.
- 👤 **Profile** – Tela para logout.

