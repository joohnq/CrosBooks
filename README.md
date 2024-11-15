# CrosBooks

**CrosBooks** é um aplicativo de gerenciamento de livros que permite ao usuário realizar operações básicas de CRUD (Create, Read, e Delete) em um acervo de livros, integrando-se à API do Swagger.

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

## 📸 Telas

![Tela 1](https://github.com/user-attachments/assets/4c89d030-4c00-4421-8a64-657bd1e4a492)
![Tela 2](https://github.com/user-attachments/assets/11d8e3d0-d6c4-40f9-a5f7-1e797e11b352)
![Tela 3](https://github.com/user-attachments/assets/109fc7c4-c87c-4fdb-ba7c-c6c152f55dab)
![Tela 4](https://github.com/user-attachments/assets/4d5af70c-877f-4ce8-821f-a1b7bf94689f)
![Tela 5](https://github.com/user-attachments/assets/d18156ad-bbcf-4e99-974e-70399a255fa0)
![Tela 6](https://github.com/user-attachments/assets/348d4208-852d-469b-913a-c0833de118ba)
![Tela 7](https://github.com/user-attachments/assets/9fe3861e-d1e4-4f5d-b72e-1f2bf0ceb93d)
