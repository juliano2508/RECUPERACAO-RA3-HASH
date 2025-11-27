# Análise Experimental de Tabela Hash (Encadeamento Separado)

Este projeto implementa uma Tabela Hash utilizando a estratégia de **tratamento de colisão por encadeamento separado** (Separate Chaining). O código foi desenvolvido em Java puro ("Java Básico"), sem a utilização de frameworks, coleções prontas (como `java.util.HashMap` ou `ArrayList`) ou tratamento de exceções, conforme os requisitos rigorosos da disciplina.

O objetivo é comparar o desempenho de diferentes funções de hash e analisar o impacto do fator de carga no tempo de execução e no número de colisões.

## 📋 Funcionalidades

O experimento executa testes automatizados variando:
* **3 Tamanhos de Tabela ($M$):** 1009, 10007, 100003 (números primos).
* **3 Funções de Hash:**
    * `H_DIV`: Método da Divisão (Resto).
    * `H_MUL`: Método da Multiplicação (com constante A ≈ 0.618).
    * `H_FOLD`: Método do Dobramento (soma de blocos de 3 dígitos).
* **3 Tamanhos de Dataset ($N$):** 1.000, 10.000 e 100.000 chaves inteiras.
* **3 Sementes (Seeds):** Para garantir a reprodutibilidade dos números aleatórios.

## 🚀 Como Compilar e Executar

Como o projeto não utiliza gerenciadores de dependência (Maven/Gradle) e consiste em um único arquivo fonte, a compilação é feita via terminal.

### Pré-requisitos
* Java JDK 8 ou superior instalado.

### Passo 1: Compilação
Abra o terminal na pasta do projeto e execute:

```bash
javac TabelaHashExperimento.java
