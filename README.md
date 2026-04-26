# Simulação de Combate D&D — Trabalho Prático de Qualidade e Teste

Projeto Java de simulação de combate no estilo D&D desenvolvido para a disciplina **Qualidade e Teste de Software**.

---

## Artefatos da Entrega 1

| Artefato | Localização |
|---|---|
| Código-fonte original | [`src/main/java/`](src/main/java/) |
| Casos de teste unitários | [`src/test/java/`](src/test/java/) |
| Plano de Teste | *(link para o Google Docs — adicionar aqui)* |

---

## Casos de Teste Unitários

Os testes cobrem as principais classes não-CRUD do sistema, seguindo o padrão **AAA (Arrange, Act, Assert)** e incluindo cenários de sucesso, valores-limite, casos negativos e edge cases.

| Classe sob teste | Arquivo de teste | Principais aspectos testados |
|---|---|---|
| `Atributos` | [`src/test/java/atributos/AtributosTest.java`](src/test/java/atributos/AtributosTest.java) | Cálculo de modificadores D&D, bônus raciais, caso especial atributo=9 |
| `Dados` | [`src/test/java/ficha/DadosTest.java`](src/test/java/ficha/DadosTest.java) | Intervalos de rolagem de dados, descarte do menor valor, soma, validação de entradas |
| `Combate` | [`src/test/java/combate/CombateTest.java`](src/test/java/combate/CombateTest.java) | Lógica de turno do inimigo, caminhos de ataque (crítico/acerto/bloqueio), poderes |
| `Personagem` | [`src/test/java/ficha/PersonagemTest.java`](src/test/java/ficha/PersonagemTest.java) | Cálculo de PV/Mana/Defesa/Ataque por nível, poderes raciais, subir de nível |

---

## Como Executar os Testes

**Pré-requisito:** Maven instalado (versão 3.x).

```bash
# Todos os testes
mvn test

# Classe específica
mvn test -Dtest=AtributosTest
mvn test -Dtest=DadosTest
mvn test -Dtest=CombateTest
mvn test -Dtest=PersonagemTest
```

---

## Como Compilar e Executar o Sistema

```bash
# Compilar (sem Maven)
javac -d target/classes $(find src/main/java -name "*.java")

# Executar
java -cp target/classes ficha.TrabalhoPOO

# Com Maven
mvn compile
mvn exec:java -Dexec.mainClass="ficha.TrabalhoPOO"
```

---

## Estrutura do Projeto

```
src/
├── main/java/
│   ├── atributos/      # Atributos D&D e modificadores
│   ├── combate/        # Motor de combate e inimigos
│   ├── equipamentos/   # Armas e armaduras
│   ├── ficha/          # Personagem, dados, menus e ponto de entrada
│   ├── poderes/        # Descrições de habilidades
│   └── racasClasses/   # Raças e classes do personagem
└── test/java/
    ├── atributos/      # AtributosTest
    ├── combate/        # CombateTest
    └── ficha/          # DadosTest, PersonagemTest
```

---

## Dependências de Teste

Configuradas em [`pom.xml`](pom.xml):

- **JUnit Jupiter 5.10.2** — framework de testes unitários
- **Mockito 5.11.0** — isolamento de dependências
- **Maven Surefire 3.2.5** — runner JUnit 5 no Maven
