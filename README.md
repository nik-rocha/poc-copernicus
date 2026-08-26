<h1 align="center"> 🌌 Copernicus Brasil - Sistema de Gerenciamento 🌌 </h1>

**Nota:** este projeto utiliza MySQL como banco de dados, portanto não há políticas RLS (Row-Level Security) do PostgreSQL. As regras de autorização (MANAGER/OPERATOR) são implementadas na camada de serviço (`service/`) da aplicação.

<p align="center">
Desenvolvido como uma forma de desafio em Kotlin.
</p>

<p align="center">
  <a href="#-descrição-do-projeto">Descrição do Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-requisitos">Requisitos</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-tecnologias-utilizadas">Tecnologias Utilizadas</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-instalacao-e-iniciacao">Instalação e Iniciação</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <a href="#-se-localizando">Se Localizando</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-primeiro-acesso">Primeiro Acesso</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-regras-de-acesso">Regras de Acesso</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
</p>

<p align="center">
<img src="https://img.shields.io/static/v1?label=STATUS&message=%20FINALIZADO&color=GREEN&style=for-the-badge)"/>
</p>

<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/bbc1b9d3-507a-4f64-87be-b45e6e592803" />
<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/e4d81ca4-965e-4366-adbf-f00015dd3ee4" />
<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/d5b7f1cb-e20a-40ba-bb9e-72799d258988" />

## 🗂 Tópicos

* Descrição do Projeto
* Requisitos
* Tecnologias Utilizadas
* Instalação e Iniciação
* Se localizando
* Primeiro Acesso
* Regras de Acesso

## 🧾 Descrição do Projeto

O Copernicus Brasil é um sistema de gerenciamento de empresas de tecnologia. Nele, você consegue organizar organizações, seus dispositivos e seus colaboradores.

## 💼 Requisitos

Para rodar esse projeto, é necessário possuir:

* Java JDK 21 ou superior
* Node.js
* NPM
* Git, permitindo a clonagem do repositório
* Uma IDE de sua preferência (recomendado: IntelliJ para o backend, VSCode para o frontend)
* MySQL, contendo MySQL Server e opcionalmente MySQL Workbench para visualização do banco

## 💻 Tecnologias Utilizadas

* JWT: Usado para autenticação e armazenamento de senhas em tokens de forma segura
* Kotlin: Linguagem de programação com base em Java para a produção da API
* Spring Boot: Principal ferramenta para a geração e desenvolvimento da API
* Angular: Framework utilizado para a construção do site e a comunicação entre o sistema e a API
* Algumas bibliotecas para Angular, como:
  
  | NPM | PACKAGE |
  | ------ | ------ |
  | angular-fontawesome, utilizado para ícones do front-end | [Angular Fontawesome](https://www.npmjs.com/package/@fortawesome/angular-fontawesome) |
  | axios, utilizado para consumo de API | [Axios](https://www.npmjs.com/package/axios) |
  | animate.css, utilizado inserir animações nos elementos HTML | [Animate.css](https://www.npmjs.com/package/animate.css) |

## 📦 Instalação e Iniciação

Para instalar o sistema, siga os seguintes passos:

1. No botão em verde "Code" do repositório na página inicial, clique nele e copie a URL que é lhe dado
   
<img width="437" height="256" alt="image" src="https://github.com/user-attachments/assets/7b5f2b60-77c4-4e64-aea0-a1ca0ab694e4" />

2. Abra um diretório (se usar OneDrive, crie em uma área onde o mesmo não esteja no caminho, como no seu próprio disco local) e dentro dele, abra o prompt de comando, e após isso digite `git clone link-do-repositorio-copiado` e dê enter.

3. O comando irá gerar a pasta poc-copernicus a seguir.

### Abrindo o back-end

1. Abra o IntelliJ, e selecione a pasta `copernicus.api` que veio junto do git clone em poc-copernicus.
   
   <img width="534" height="99" alt="image" src="https://github.com/user-attachments/assets/611b03b7-c921-4b56-849b-f67e29b848d6" />

2. Antes de rodar o backend, edite o arquivo `src/main/resources/application.properties` com suas credenciais do MySQL:

​```properties
spring.datasource.url=jdbc:mysql://localhost:3306/copernicus_db
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
​```

O banco `copernicus_db` será criado automaticamente na primeira execução.

3. Espere a barra azul no canto inferior direito do IntelliJ carregar (ela está carregando as importações do projeto), e após isso, dentro de `src > main > kotlin > com.example.copernicus.api`, clique duas vezes no arquivo Application.kt
   
   <img width="613" height="504" alt="image" src="https://github.com/user-attachments/assets/d7153b59-361a-4379-8e91-d20bb4b63a47" />

4. Após isso, clique no botão de play no canto superior direito do aplicativo e espere a API iniciar, ela já irá criar o banco de dados utilizado e rodar a API em http://localhost/8080.
   
   <img width="799" height="313" alt="image" src="https://github.com/user-attachments/assets/739d5711-f76f-4f08-ab0a-827ce1ad0ef4" />

### Abrindo o front-end

1. Com o IntelliJ aberto rodando a API, abra a pasta `copernicus.api` que veio dentro do git clone e digite `code .` para abrir o projeto Angular no VSCode.

2. Após isso, no terminal do VSCode (abra com Ctrl + '), digite `npm i` para instalar as dependências do projeto, e depois de instaladas, digite `ng serve` para rodar o aplicativo.

3. Ele provavelmente estará rodando em http://localhost:4200, se não, copie da mensagem que aparece no terminal. Lembre-se de deixar tanto o Visual Studio Code quanto o IntelliJ abertos.

## 🗺️ Se Localizando

### Back-end

Dentro da API no IntelliJ, tem as seguintes pastas, utilizando o método MVC:

<img width="619" height="882" alt="image" src="https://github.com/user-attachments/assets/a0da0f20-ed26-4dd6-84f6-770b0d31e3d4" />

* controller: Aqui ficam armazenados os arquivos que recebem as requisições feitas pelo usuário, processando a lógica de controle.
  
* dto: Aqui fica os DTOs (Data Transfer Objects), usados para transportar dados tanto de requisições quanto de respostas do sistema
  
  <img width="787" height="422" alt="image" src="https://github.com/user-attachments/assets/0d17fea2-54ae-4d31-a627-225e7ce578cd" />
  
  (CollaboratorCreateRequest transportando os dados de uma requisição para criar um colaborador)

* exception: Os arquivos aqui dentro tratam das exceções do projeto, erros que ocorrem que devolvem um status HTTP (400, 403, 200... etc)

* model: Os arquivos aqui dentro definem o básico de como é a modelagem de cada tabela do banco de dados, incluindo suas regras e tipos de dados.
  
  <img width="895" height="652" alt="image" src="https://github.com/user-attachments/assets/a3a24031-317f-4d82-8601-44613855b5db" />
  
  (Dispositivos)

* repository: Aqui ficam armazenados arquivos que servem de ponte entre a aplicação e o banco de dados.
  
  <img width="285" height="105" alt="image" src="https://github.com/user-attachments/assets/98c485c4-50f1-45c7-8833-176d521ae751" />
  
  (Repositórios)

* security: Os arquivos encontrados aqui dentro definem as regras de segurança da API, como as rotas permitidas e as regras de autenticação.

* service: Aqui ficam os arquivos que definem como vai funcionar o CRUD, construindo os métodos GET, POST, PUT e DELETE da API.

### Front-end

Dentro do aplicativo Angular, tem a seguinte organização:

<img width="271" height="355" alt="image" src="https://github.com/user-attachments/assets/acf9ce20-380f-42d9-b2f6-5be23431648c" />

* guards: Nessa pasta é guardada arquivos que definem a segurança das rotas do sistema.

* models: Aqui ficam a modelagem das tabelas do banco de dados no sistema.

* pages: Aqui são armazenados os componentes que definem as páginas do aplicativo (Login e Gerenciamento)

* services: Aqui são armazenados os serviços que definem as rotas para as requisições HTTP do sistema.

## 🚹 Primeiro acesso

Como o sistema não vem com dados pré-cadastrados, crie sua primeira conta pela tela de "Cadastro" (Bem-vindo → Clique aqui), colocando seu perfil como Gerente e escolhendo a opção "Ainda não participo" para criar sua própria organização como MANAGER.

<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/136af7f0-9157-4e08-ab3a-f3d020accc38" />
<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/23449a73-8e39-40ae-ae90-6fe4281d3c42" />
<img width="1920" height="951" alt="image" src="https://github.com/user-attachments/assets/0cb9c28b-43be-46d9-bab0-ba5105cf424f" />

## 🚷 Regras de Acesso

- **OPERATOR**: visualiza apenas sua própria organização, colaboradores e dispositivos dela.
- **MANAGER**: visualiza e gerencia todos os registros do sistema, podendo criar/editar/excluir organizações, colaboradores e dispositivos de qualquer empresa.

## 👨‍💼 Autor

| [<img src="https://avatars.githubusercontent.com/u/117852880?v=4" width=115><br><sub>Nicollas Rocha</sub>](https://github.com/nik-rocha) |
| :---: |
