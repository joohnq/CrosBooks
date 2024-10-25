# CrosBooks

Este é um aplicativo de gerenciamento de livros que permite ao usuário realizar operações básicas de CRUD (Create, Read, e Delete) em um acervo de livros, integrando-se a API do Swagger.

Pegar livros (GET) – Exibe uma lista de livros. <br>
Enviar livros (POST) – Adiciona ou atualiza um livro. <br>
Deletar livros (DELETE) – Remove um livro da lista. <br>

- Kotlin
- MVVM
- Koin
- RecyclerView
- Retrofit
- OKHttp
- Gson
- Coroutine
- Flow
- LiveData
- Datastore

## Telas
- Login (Login com o email e a senha do usuário) <br>
Logo apos o Login o token, que vêm da API, é armazenado localmente com DataStore e resgatado em futuras requisições, utilizando um Interceptor.
- Registro (Registrar um usuário com nome, email e senha)
- Home (Com Paginação) (Listagem dos livros do usuário e com a listagem das categorias disponíveis)
- Detalhe do Livro (Exibição mais detalhada do livro)
- Search (Com Paginação) (Tela de pesquisa para filtrar pelo nome do livro)
- Profile (Tela para logout)
  
