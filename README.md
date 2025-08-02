# Athos: Backend

Este repositório contém todos os microsserviços, o API Gateway e a infraestrutura do backend para o aplicativo Athos.

### Serviços
- **ms-autenticacao**: Gerencia o cadastro e login de usuários.
- **ms-evento**: Responsável pela criação e gerenciamento de eventos.
- **ms-notificacao**: Envia notificações aos usuários.
- **ms-campeonato**: Responsável pela criação e gerenciamento de campeonatos.
- **ms-recomendacao**: Responsável por enviar recomendações de eventos.
- **ms-avaliacao**: Responsável pela criação e gerenciamento das avaliações dos usuários.
- **ms-pagamento**: Responsável pelo gerenciamento de pagamentos utilizando API Externa (Stripe). 
### Como Executar
1. Certifique-se de ter o Docker e o Docker Compose instalados.
2. Clone o repositório.
3. Na raiz do projeto, execute o comando: `docker-compose up --build`