# Análise Experimental de Tabela Hash (Encadeamento Separado)

Este repositório contém a implementação de uma **Tabela Hash com tratamento de colisão por Encadeamento Separado** (Separate Chaining).

O projeto foi desenvolvido em **Java puro** ("Java Básico"), seguindo rigorosamente as restrições da disciplina:
- 🚫 **Sem uso de Collections** (`HashMap`, `ArrayList`, `LinkedList`, etc.).
- 🚫 **Sem tratamento de exceções** (`try-catch` ou `throws`).
- 🚫 **Sem bibliotecas externas** (apenas `java.lang` e `java.util.Random`).
- ✅ **Implementação manual** da lista encadeada e da estrutura de nós.

O objetivo é analisar empiricamente o desempenho de diferentes funções de hashing sob variados fatores de carga.

## 📋 Funcionalidades Implementadas

O experimento executa automaticamente 81 combinações de testes (3 tamanhos de tabela × 3 funções × 3 tamanhos de dados × 3 seeds), coletando métricas precisas de tempo e colisões.

### Parâmetros do Experimento
* **Tamanhos da Tabela ($M$):** 1009, 10007, 100003 (Números primos para minimizar colisões).
* **Funções de Hash:**
    1.  `H_DIV`: Método da Divisão (Resto).
    2.  `H_MUL`: Método da Multiplicação (constante A ≈ 0.618).
    3.  `H_FOLD`: Método do Dobramento (soma de blocos de 3 dígitos).
* **Datasets ($N$):** 1.000, 10.000 e 100.000 chaves inteiras.
* **Reprodutibilidade:** Uso de sementes fixas (137, 271828, 314159).

## 🚀 Como Executar o Projeto

Como o projeto não utiliza ferramentas de build (Maven/Gradle), a execução é feita via linha de comando.

### Pré-requisitos
* Java JDK 8 ou superior instalado.

### 1. Compilação
Abra o terminal na pasta do arquivo e execute:

```bash
javac TabelaHashExperimento.java
