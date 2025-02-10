# 📝 To-Do API (Backend)

Este é o backend da aplicação **To-Do List**, desenvolvido em **Java Spring Boot**. Ele fornece autenticação JWT e gerenciamento de tarefas para os usuários.

## 🚀 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Security (JWT)**
- **JPA / Hibernate**
- **Banco de Dados PostgreSQL**
- **Docker**
- **Swagger**


## 📌 Endpoints da API

### **Autenticação**
- `POST /auth/register` → Registra um novo usuário.
- `POST /auth/login` → Retorna um **JWT Token**.

### **Tarefas**
- `GET /tasks` → Retorna todas as tarefas do usuário autenticado.
- `POST /tasks` → Cria uma nova tarefa.
- `PUT /tasks/{id}` → Atualiza uma tarefa existente.
- `DELETE /tasks/{id}` → Deleta uma tarefa.

## 🛠️ Como Rodar o Backend

1. **Clone o repositório**  
   ```sh
   git clone https://github.com/rob134/todobackfront.git
   cd backend

2. **Configure a URL da API**
   ```sh
   export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'  // Backend API URL
};

3. **Execute o projeto**
   ```sh
   ionic serve



