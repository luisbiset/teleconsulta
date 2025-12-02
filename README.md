## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Jakarta EE 10**
- **Tomcat 10**
- **JSF 4 (Mojarra 4.0.2)**
- **PrimeFaces 14 (Jakarta)**
- **Hibernate ORM 6**
- **JPA Criteria API**
- **SQLite**
- **Docker**
- **Docker Compose**

- ## Pré-requisitos

Antes de iniciar, instale:

- **Docker**
- **Docker Compose**
- **Git**

- ## Como rodar o projeto:

1. Clonar este repositório para o seu ambiente local:
```bash
git clone https://github.com/luisbiset/teleconsulta.git
cd teleconsulta
```
2. Criar a pasta data
```bash
mkdir data
```
3. Copiar o arquivo .env.example para criar o seu .env
 ```bash
  cp .env.example .env
   ```
4. Dar  permissão para executar o docker-run.sh
```bash
chmod +x docker-run.sh
```
5. Executar o docker-run.sh no terminal
```bash
./docker-run.sh
```
6. Após iniciar o contêiner da aplicação, voce já pode acessar através do navegador pelo endereço:
```bash
http://localhost:8080
```

## Credenciais de Acesso ao Sistema
1.CPF
```bash

000.000.000-00
```
2. Senha
```bash
123456
```








