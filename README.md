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
- OkHttp
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
- Adicionar Livro (Responsável adicionar um livro tendo como etapas: 1. Pegar a imagem do livro e enviar para o servidor. 2. Enviar o livro já com a url da imagem)
- Search (Com Paginação) (Tela de pesquisa para filtrar pelo nome do livro)
- Profile (Tela para logout)
  
![WhatsApp Image 2024-10-25 at 11 58 19](https://github.com/user-attachments/assets/4c89d030-4c00-4421-8a64-657bd1e4a492)
![WhatsApp Image 2024-10-25 at 11 58 35](https://github.com/user-attachments/assets/11d8e3d0-d6c4-40f9-a5f7-1e797e11b352)
![WhatsApp Image 2024-10-25 at 11 59 32](https://github.com/user-attachments/assets/109fc7c4-c87c-4fdb-ba7c-c6c152f55dab)
![WhatsApp Image 2024-10-25 at 11 59 46](https://github.com/user-attachments/assets/4d5af70c-877f-4ce8-821f-a1b7bf94689f)
![WhatsApp Image 2024-10-25 at 12 00 14](https://github.com/user-attachments/assets/d18156ad-bbcf-4e99-974e-70399a255fa0)
![WhatsApp Image 2024-10-25 at 12 02 31](https://github.com/user-attachments/assets/348d4208-852d-469b-913a-c0833de118ba)
![WhatsApp Image 2024-10-25 at 12 02 52](https://github.com/user-attachments/assets/9fe3861e-d1e4-4f5d-b72e-1f2bf0ceb93d)
