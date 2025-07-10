#  Restaurante Monsieur

> Aplicação em Java desenvolvida para a disciplina de Técnicas de Programação 1, focada em Programação Orientada a Objetos, GUI com JavaFX e persistência de dados.

<p align="center">
  <a href="#-tecnologias-utilizadas">
    <img src="https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk" alt="Java">
    <img src="https://img.shields.io/badge/JavaFX-SDK-blueviolet?style=for-the-badge&logo=openjfx" alt="JavaFX">
    <img src="https://img.shields.io/badge/Maven-Builder-red?style=for-the-badge&logo=apache-maven" alt="Maven">
    <img src="https://img.shields.io/badge/Hibernate-ORM-lightcoral?style=for-the-badge&logo=hibernate" alt="Hibernate">
    <img src="https://img.shields.io/badge/MySQL-Database-blue?style=for-the-badge&logo=mysql" alt="MySQL">
    <img src="https://img.shields.io/badge/Docker-Container-informational?style=for-the-badge&logo=docker" alt="Docker">
  </a>
</p>

---

## 🎯 Objetivos de Aprendizagem

-   **Programação Orientada a Objetos (POO):** Aplicar os pilares de encapsulamento, herança, polimorfismo e abstração.
-   **JavaFX:** Desenvolver interfaces gráficas de usuário (GUI) interativas e responsivas.
-   **Maven:** Utilizar como builder para facilitar o gerenciamento de arquivos e dependências.
-   **ORM (Hibernate):** Mapear objetos Java para tabelas de banco de dados, simplificando a persistência de dados.
-   **Banco de Dados MySQL:** Configurar e interagir com um banco de dados relacional.
-   **Docker:** Gerenciar o ambiente do banco de dados MySQL de forma conteinerizada.

<br>

<details>
  <summary>📸 **Preview da Aplicação**</summary>
  <br>
  <p align="center">
    <img src="./TelaInicial.png" alt="Imagem de Referência Tela Inicial" width="80%">
  </p>
</details>

---

## 🏗️ Estrutura do Projeto

O projeto é dividido nas seguintes camadas lógicas:

-   **`classes/`**: Contém as classes de entidade do domínio (`Cliente`, `Funcionario`, `Pedido`, etc.).
-   **`database/`**: Inclui a configuração do Hibernate e a lógica de interação com o banco de dados.
-   **`app/`**: Contém os arquivos nativos e controladores Java para as telas da interface gráfica desenvolvidas com JavaFX.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia                  | Propósito                           |
| --------------------------- | ----------------------------------- |
| **Java** | Linguagem de Programação (Core)     |
| **Maven** | Builder e Gerenciador de Dependências |
| **JavaFX** | Interface Gráfica (GUI)             |
| **Hibernate (JPA)** | Mapeamento Objeto-Relacional (ORM)  |
| **MySQL** | Banco de Dados Relacional           |
| **Docker** | Virtualização/Conteinerização do DB |

> 🔑 **Senha de Administrador:** `PSG5-0`

---

## 🚀 Como Executar o Projeto

<details>
  <summary>Clique para expandir os passos de configuração e execução</summary>
  <br>

### **Pré-requisitos**

-   JDK (Java Development Kit) 17 ou superior
-   Maven
-   Docker Desktop (ou Docker Engine)
-   Um IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)
-   (Opcional) MySQL Workbench

---

### **Passos para Configuração e Execução**

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/RobertorNeto/Restaurante-Monsieur](https://github.com/RobertorNeto/Restaurante-Monsieur)
    cd ./Restaurante-Monsieur
    ```

2.  **Inicie o Banco de Dados com Docker:**
    * Construa e inicie o container MySQL com Docker Compose:
        ```bash
        docker-compose up -d
        ```

3.  **Configure o Banco de Dados (Manual/Opcional):**
    * **Conexão:** Use as credenciais (`root` / `senha123`) e a porta (`3306`) definidas no `docker-compose.yml` para conectar-se via MySQL Workbench.
    * **Schema:** Crie o banco de dados se ele não existir:
        ```sql
        CREATE DATABASE IF NOT EXISTS restaurantemonsieur;
        ```
    * **Persistence.xml:** Verifique se o arquivo `src/main/resources/META-INF/persistence.xml` está com a string de conexão, usuário e senha corretos.
        ```xml
        <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/restaurantemonsieur?createDatabaseIfNotExist=true"/>
        <property name="jakarta.persistence.jdbc.user" value="root"/>
        <property name="jakarta.persistence.jdbc.password" value="senha123"/>
        ```

4.  **Configuração JavaFX para IntelliJ IDEA (Opcional):**
    1.  Baixe o [SDK do JavaFX](https://openjfx.io/) e descompacte.
    2.  No IntelliJ, vá em `File > Project Structure... > Libraries` e adicione a pasta `lib` do SDK do JavaFX como uma biblioteca do projeto.
    3.  Vá em `Run > Edit Configurations...` e adicione as seguintes opções de VM, ajustando o caminho para o seu SDK:
        ```
        --module-path "CAMINHO_PARA_SEU_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml,javafx.web
        ```

5.  **Construa o Projeto com Maven:**
    * Baixe as dependências e compile o código:
        ```bash
        mvn clean install
        ```

6.  **Execute a Aplicação:**
    * Execute a classe principal através do seu IDE ou utilize o JAR gerado na pasta `target`:
        ```bash
        java -jar target/nome-do-seu-jar.jar
        ```
</details>

---

## 👥 Colaboradores

Agradecemos às seguintes pessoas que contribuíram para este projeto:

<br>

<table align="center">
  <tr>
    <td align="center">
      <a href="https://github.com/daviggalvao">
        <img src="https://github.com/daviggalvao.png?size=100" width="100px;" alt="Foto de daviggalvao"/>
        <br />
        <sub>
          <b>daviggalvao</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/RobertorNeto">
        <img src="https://github.com/RobertorNeto.png?size=100" width="100px;" alt="Foto de RobertorNeto"/>
        <br />
        <sub>
          <b>RobertorNeto</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Redondave">
        <img src="https://github.com/Redondave.png?size=100" width="100px;" alt="Foto de Redondave"/>
        <br />
        <sub>
          <b>Redondave</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/TagFernandes">
        <img src="https://github.com/TagFernandes.png?size=100" width="100px;" alt="Foto de TagFernandes"/>
        <br />
        <sub>
          <b>TagFernandes</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
