# TicketBot AI

Este projeto é um sistema de gerenciamento de chamados de suporte técnico. O objetivo principal da aplicação é permitir que usuários relatem problemas e, através de uma lógica interna, o sistema classifique automaticamente o ticket e o atribua à equipe técnica correta (Hardware, Software, Redes, etc.).

O projeto foi desenvolvido como um sistema completo (Full Stack), conectando um visual moderno no navegador com um servidor robusto e um banco de dados.

## Tecnologias utilizadas

- Frontend: React com TypeScript e CSS.
- Backend: Java com Spring Boot.
- Banco de Dados: PostgreSQL.
- Infraestrutura: Docker (para rodar o banco de dados).

## Pré-requisitos

Para rodar este projeto na sua máquina, você precisará ter instalado:

- Java (JDK 17 ou superior).
- Node.js (para rodar o site).
- Docker Desktop (para rodar o banco de dados sem complicação).
- Git (para baixar o código).

## Como rodar o projeto

O processo é dividido em três partes: iniciar o banco de dados, ligar o servidor (backend) e abrir o site (frontend). Siga a ordem abaixo.

### 1. Iniciando o Banco de Dados

Nós usamos o Docker para não precisar instalar o PostgreSQL manualmente no Windows. Certifique-se de que o Docker Desktop está aberto e rodando.

Abra o terminal na pasta raiz do projeto e execute:

docker compose up -d

Isso vai baixar e iniciar o banco de dados em segundo plano.

### 2. Iniciando o Backend (Java)

Com o banco de dados rodando, precisamos ligar o servidor que vai processar os dados.

Ainda na pasta raiz do projeto, execute o comando:

./mvnw spring-boot:run

(Se estiver no Windows e o comando acima não funcionar, tente apenas: mvn spring-boot:run)

Aguarde até aparecer uma mensagem no terminal informando que a aplicação iniciou (geralmente diz "Started TicketBotAiApplication"). Não feche esse terminal.

### 3. Iniciando o Frontend (React)

Agora vamos abrir a parte visual. Abra um novo terminal (não feche o do Java) e navegue até a pasta onde estão os arquivos do site (geralmente a pasta raiz neste projeto, mas verifique se você vê o arquivo package.json).

Primeiro, instale as dependências necessárias:

npm install

Depois, inicie o site:

npm run dev

O terminal vai mostrar um endereço local, normalmente http://localhost:5173.

## Como usar

1. Abra o seu navegador e acesse o endereço fornecido no passo anterior (http://localhost:5173).
2. Você verá um formulário escuro centralizado na tela.
3. No campo Título, coloque um resumo do problema (exemplo: "Impressora não conecta").
4. No campo Descrição, detalhe o que está acontecendo.
5. Clique em "Enviar Ticket".

Se tudo estiver funcionando, o sistema vai processar sua solicitação e devolver logo abaixo um cartão confirmando a criação do ticket, mostrando qual equipe foi designada para resolver o problema.
