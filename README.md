# Simulacao de Combate D&D

**Disciplina:** Qualidade e Teste de Software | UFF - 2026/1

---

## Descricao do Sistema

O **Trabalho-POO-Simulacao-de-combate** e uma aplicacao Java de console (CLI) para criacao de personagem e simulacao de combate em estilo RPG. O sistema permite selecionar raca e classe, distribuir atributos por rolagem de dados, escolher equipamentos e enfrentar inimigos em batalhas por turno.

### Funcionalidades principais

| Modulo | Descricao |
|---|---|
| **Criacao de Personagem** | Escolha de raca, classe e nome do personagem |
| **Atributos e Dados** | Rolagem de dados, descarte do menor valor e distribuicao de atributos |
| **Equipamentos** | Escolha de armas e armaduras com validacao de compatibilidade |
| **Combate** | Turnos, ataque, distancia, defesa, critico, vitoria/derrota |
| **Poderes** | Habilidades por classe/raca com consumo de mana |
| **Inimigos** | Banco de inimigos com progressao de dificuldade |

### Tecnologias do sistema

- **Linguagem:** Java 18+
- **Build:** Maven
- **Testes:** JUnit 5 + Mockito
- **Execucao:** Console (terminal)

### Modulos testados neste trabalho

As classes priorizadas para testes unitarios sao as de regra de negocio:

- `Atributos` - calculo de modificadores e bonus
- `Dados` - rolagem, soma e validacao de entradas
- `Combate` - regras de turno, ataque e poderes
- `Personagem` - calculo de status e progressao de nivel

---

## Artefatos da Entrega 1 (26/04/2026)

> Todos os artefatos estao na branch `main` deste repositorio.

### 1. Plano de Teste

| Artefato | Link |
|---|---|
| Plano de Teste (Markdown) | [`plano-de-teste.md`](plano-de-teste.md) |
| Casos de Teste Manuais (Markdown) | [`TESTES-MANUAIS.md`](TESTES-MANUAIS.md) |

---

### 2. Codigo-Fonte Original

| Modulo | Link |
|---|---|
| Codigo principal | [`src/main/java/`](src/main/java/) |
| Atributos | [`src/main/java/atributos/`](src/main/java/atributos/) |
| Combate e inimigos | [`src/main/java/combate/`](src/main/java/combate/) |
| Equipamentos | [`src/main/java/equipamentos/`](src/main/java/equipamentos/) |
| Ficha/Menu/Main | [`src/main/java/ficha/`](src/main/java/ficha/) |
| Poderes | [`src/main/java/poderes/`](src/main/java/poderes/) |
| Racas e classes | [`src/main/java/racasClasses/`](src/main/java/racasClasses/) |

---

### 3. Testes Unitarios Automatizados

**Ferramentas utilizadas nos testes:**

| Ferramenta | Versao | Finalidade |
|---|---|---|
| **JUnit Jupiter** | 5.10.2 | Framework principal de testes unitarios |
| **Mockito Core** | 5.11.0 | Mocks e isolamento de dependencias |
| **Maven Surefire Plugin** | 3.2.5 | Execucao dos testes no ciclo `mvn test` |

**Como executar:**

```bash
mvn test
```

**Arquivos de teste:**

| Arquivo | Classe testada | Link direto |
|---|---|---|
| `AtributosTest.java` | `Atributos` | [`src/test/java/atributos/AtributosTest.java`](src/test/java/atributos/AtributosTest.java) |
| `CombateTest.java` | `Combate` | [`src/test/java/combate/CombateTest.java`](src/test/java/combate/CombateTest.java) |
| `DadosTest.java` | `Dados` | [`src/test/java/ficha/DadosTest.java`](src/test/java/ficha/DadosTest.java) |
| `PersonagemTest.java` | `Personagem` | [`src/test/java/ficha/PersonagemTest.java`](src/test/java/ficha/PersonagemTest.java) |

---

### 4. Configuracao de Teste

| Arquivo | Link | Descricao |
|---|---|---|
| `pom.xml` | [`pom.xml`](pom.xml) | Dependencias e plugins de build/teste |

---

---

### 4. Testes Manuais - Simulação de Combate
Este documento detalha os processos de QA do sistema.


 Documento completo: [`Acesse os Testes Manuais aqui`](https://docs.google.com/document/d/1kNf6tNtkxJ-7kMwsXe0t5jLLEZdVhzWCr9jAtzic4jI/edit?tab=t.0#heading=h.to8swo4m7y83)

---

## Estrutura do Repositorio

```
Trabalho-POO-Simula-o-de-combate/
├── plano-de-teste.md
├── TESTES-MANUAIS.md
├── src/
│   ├── main/java/
│   │   ├── atributos/
│   │   ├── combate/
│   │   ├── equipamentos/
│   │   ├── ficha/
│   │   ├── poderes/
│   │   └── racasClasses/
│   └── test/java/
│       ├── atributos/AtributosTest.java
│       ├── combate/CombateTest.java
│       └── ficha/{DadosTest,PersonagemTest}.java
└── pom.xml
```

---

## Como Executar

### Executar testes unitarios

```bash
mvn test
```

### Executar a aplicacao (Maven)

```bash
mvn exec:java "-Dexec.mainClass=ficha.TrabalhoPOO"
```

### Executar a aplicacao (sem Maven)

```powershell
if (!(Test-Path target/classes)) { New-Item -ItemType Directory -Path target/classes | Out-Null }
$sources = Get-ChildItem -Recurse -Path src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -d target/classes $sources
java -cp target/classes ficha.TrabalhoPOO
```

---

## Historico

| Versao | Data | Descricao |
|---|---|---|
| 1.0 | 2026-04-26 | Entrega 1: testes unitarios, plano de teste e casos manuais |
