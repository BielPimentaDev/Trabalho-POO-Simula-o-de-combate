# Plano de Teste - Simulacao de Combate D&D
**Disciplina:** Qualidade e Teste de Software  
**Instituicao:** Universidade Federal Fluminense (UFF)  
**Versao:** 1.0  
**Data:** 2026-04-26

---

## Historico de Versoes

| Versao | Data | Autor(es) | Descricao |
|---|---|---|---|
| 1.0 | 2026-04-26 | Grupo | Criacao inicial - Entrega 1 |

---

## 1. Introducao

### 1.1 Objetivo
Este documento descreve o plano de teste para o sistema **Trabalho-POO-Simulacao-de-combate**, uma aplicacao Java de console para criacao de personagem e batalhas em estilo RPG. O objetivo e garantir qualidade por meio de testes manuais e testes unitarios automatizados sobre as principais regras de negocio.

### 1.2 Escopo
Este plano cobre os fluxos de criacao de personagem e combate, com foco em:
- selecao de raca e classe;
- distribuicao de atributos por rolagem de dados;
- validacao de equipamentos;
- logica de combate (ataque, distancia, mana, cura, critico);
- progressao por vitoria.

### 1.3 Sistema em Teste
- **Nome:** Trabalho-POO-Simulacao-de-combate
- **Tipo:** Aplicacao CLI (console)
- **Tecnologia:** Java 18+, Maven
- **Repositorio:** GitHub do grupo

---

## 2. Itens de Teste

| Componente | Tipo | Complexidade | Justificativa |
|---|---|---|---|
| `Combate` | Regra de negocio | Alta | Concentra regras de turno, ataque, poderes e fim de batalha |
| `Personagem` | Regra de negocio | Alta | Consolidacao de atributos, status e progressao |
| `Dados` | Utilitario de negocio | Media | Rolagem, descarte de menor e atribuicao de valores |
| `Atributos` | Regra de negocio | Media | Modificadores e bonus raciais |
| `Menu` | Fluxo de entrada | Media | Orquestra o fluxo do usuario na aplicacao CLI |

---

## 3. Funcionalidades a Testar

### 3.1 Criacao de Personagem
- Escolha de raca
- Escolha de classe
- Rolagem e distribuicao de atributos
- Definicao de nome

### 3.2 Equipamentos
- Escolha de arma
- Escolha de armadura
- Validacao de incompatibilidade com classe

### 3.3 Combate
- Inicio de combate e iniciativa
- Distancia entre combatentes
- Ataque basico (acerto, erro, critico)
- Uso de poder de classe
- Consumo/validacao de mana
- Encerramento de turno
- Resultado final (vitoria/derrota)

### 3.4 Progressao
- Subida de nivel apos vitoria
- Recalculo de atributos derivados (PV, mana, ataque, defesa)

---

## 4. Funcionalidades Fora do Escopo (Entrega 1)

- Testes de interface grafica (nao existe GUI/web no sistema atual)
- Testes de performance e carga
- Testes de seguranca
- Cobertura total de todas as combinacoes de raca/classe/equipamento

---

## 5. Abordagem de Teste

### 5.1 Tipos de Teste
- **Teste Manual:** validacao do fluxo completo da aplicacao via terminal
- **Teste Unitario Automatizado:** validacao isolada de metodos/classes principais
- **Teste de Regressao:** reexecucao dos casos apos correcoes

### 5.2 Tecnicas Aplicadas
- Particionamento em classes de equivalencia
- Analise de valor de fronteira
- Casos positivos (happy path)
- Casos negativos e entradas invalidas
- Padrao AAA (Arrange / Act / Assert) nos testes unitarios

---

## 6. Ferramentas

| Ferramenta | Versao | Finalidade |
|---|---|---|
| Java (JDK) | 18+ | Compilacao e execucao da aplicacao |
| Maven | 3.x | Build e execucao dos testes |
| JUnit Jupiter | 5.10.2 | Framework de testes unitarios |
| Mockito Core | 5.11.0 | Mock de dependencias nos testes |
| Maven Surefire | 3.2.5 | Execucao dos testes unitarios |
| GitHub Issues | - | Registro de defeitos encontrados |

---

## 7. Criterios de Entrada e Saida

### 7.1 Criterios de Entrada
- Repositorio acessivel e atualizado
- Projeto compilando localmente
- Ambiente Java configurado
- Casos de teste definidos

### 7.2 Criterios de Saida
- Casos manuais executados e documentados
- Testes unitarios executados
- Defeitos registrados como issues
- README atualizado com links para artefatos

### 7.3 Criterios de Suspensao
- Falha de ambiente que impossibilite execucao
- Erros de compilacao bloqueando testes

---

## 8. Ambiente de Teste

- **Sistema Operacional:** Windows 10/11 (ou equivalente)
- **JDK:** 18+ (ou superior)
- **Build Tool:** Maven 3.x
- **IDE:** Eclipse / VS Code / IntelliJ
- **Execucao:** terminal local

### 8.1 Comandos de Execucao

```bash
# Testes unitarios
mvn test

# Execucao da aplicacao
mvn exec:java "-Dexec.mainClass=ficha.TrabalhoPOO"
```

---

## 9. Artefatos Produzidos

| Artefato | Localizacao | Descricao |
|---|---|---|
| Plano de Teste | `plano-de-teste.md` | Estrategia, escopo e criterios de teste |
| Casos Manuais | `TESTES-MANUAIS.md` | Casos CTM com passos e resultados esperados |
| Testes Unitarios | `src/test/java/` | Testes automatizados por classe |
| README | `README.md` | Indice dos artefatos e instrucoes de execucao |

---

## 10. Casos de Teste - Resumo

| ID | Funcionalidade | Cenario | Tipo | Resultado Esperado |
|---|---|---|---|---|
| CTM-001 | Criacao de personagem | Criar personagem completo | Happy Path | Ficha exibida sem erros |
| CTM-002 | Validacao de fluxo | Tentar sair sem pre-requisitos | Negativo | Sistema bloqueia avancar |
| CTM-003 | Entrada de dados | Digitar valores invalidos no menu | Negativo | Sistema trata erro e continua |
| CTM-004 | Equipamentos | Selecionar item incompativel | Negativo | Sistema rejeita selecao |
| CTM-005 | Encerramento | Encerrar antes do combate | Happy Path | Programa finaliza sem excecao |
| CTM-006 | Combate | Fluxo de combate ate o fim | Happy Path | Exibe fim de combate e resultado |
| CTM-007 | Poder | Usar poder com mana | Happy Path | Poder aplicado e mana reduzida |
| CTM-008 | Mana | Usar poder sem mana | Negativo | Mensagem de mana insuficiente |
| CTM-009 | Distancia | Atacar fora de alcance | Negativo | Acao invalida sem quebrar fluxo |

---

## 11. Riscos e Mitigacoes

| Risco | Probabilidade | Impacto | Mitigacao |
|---|---|---|---|
| Entrada interativa por console dificulta repetibilidade | Alta | Medio | Documentar passos padronizados e usar roteiro CTM |
| Aleatoriedade dos dados afeta previsibilidade | Alta | Medio | Validar por comportamento esperado, nao por valor fixo |
| Alto acoplamento entre classes de fluxo | Media | Medio | Priorizar testes unitarios em logica isolada |
| Configuracao do Maven em ambientes Windows | Media | Baixo | Documentar comando especifico para PowerShell |

---

## 12. Defeitos Encontrados

Os defeitos identificados durante a execucao devem ser registrados no GitHub Issues com:
- titulo objetivo;
- passos para reproducao;
- resultado esperado x obtido;
- severidade;
- evidencia (print/log).

---

## 13. Responsabilidades

| Integrante | Responsabilidade |
|---|---|
| Integrante 1 | Plano de teste e escopo |
| Integrante 2 | Casos de teste manuais |
| Integrante 3 | Testes unitarios |
| Integrante 4 | Execucao e evidencias |
| Integrante 5 | Issues e consolidacao no README |

---

## 14. Aprovacao

| Funcao | Nome | Data | Assinatura |
|---|---|---|---|
| Elaborado por | Grupo | 2026-04-26 | - |
| Revisado por | - | - | - |
