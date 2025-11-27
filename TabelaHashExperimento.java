import java.util.Random;

class No {
    public int chave;
    public No proximo;

    public No(int k) {
        this.chave = k;
        this.proximo = null;
    }
}

// implementa o hash com encadeamento separado.
class TabelaHash {
    private static final double CONSTANTE_A = 0.6180339887;
    private No[] tabela;
    private int tamanhoM;

    public long contagemColisoesTabela;
    public long contagemColisoesLista;
    public long contagemComparacoesSucesso;
    public long contagemComparacoesFracasso;

    public TabelaHash(int m) {
        this.tamanhoM = m;
        this.tabela = new No[m];
        this.contagemColisoesTabela = 0;
        this.contagemColisoesLista = 0;
        this.contagemComparacoesSucesso = 0;
        this.contagemComparacoesFracasso = 0;

        int i = 0;
        while (i < m) {
            this.tabela[i] = null;
            i = i + 1;
        }
    }

    private int modulo_resto(int a, int b) {
        return a % b;
    }

    public int calcularDivisao(int chave) {
        return modulo_resto(chave, this.tamanhoM);
    }

    public int calcularMultiplicacao(int chave) {
        double kA = (double) chave * CONSTANTE_A;
        double fracionaria = kA - (long) kA;
        double hashValor = (double) this.tamanhoM * fracionaria;

        return (int) hashValor;
    }

    public int calcularDobramento(int chave) { // separar em 3 dígitos
        int bloco;
        int soma = 0;
        int k = chave;

        while (k > 0) {
            bloco = k % 1000;
            soma = soma + bloco;
            k = k / 1000;
        }

        return modulo_resto(soma, this.tamanhoM);
    }

    public int calcularHash(int chave, int tipoFunc) {
        int indice = 0;
        if (tipoFunc == 1) {
            indice = calcularDivisao(chave);
        } else if (tipoFunc == 2) {
            indice = calcularMultiplicacao(chave);
        } else if (tipoFunc == 3) {
            indice = calcularDobramento(chave);
        }
        return indice;
    }

    public void inserir(int chave, int tipoFunc) {
        int indice = calcularHash(chave, tipoFunc);
        No novoNo = new No(chave);

        if (this.tabela[indice] != null) {
            this.contagemColisoesTabela = this.contagemColisoesTabela + 1;

            No atual = this.tabela[indice];
            long compLista = 0;

            while (atual.proximo != null) {
                atual = atual.proximo;
                compLista = compLista + 1;
            }

            atual.proximo = novoNo;
            this.contagemColisoesLista = this.contagemColisoesLista + compLista;

        } else {
            this.tabela[indice] = novoNo;
        }
    }

    public boolean buscar(int chave, int tipoFunc, boolean isHit) {
        int indice = calcularHash(chave, tipoFunc);
        No atual = this.tabela[indice];
        long comparacoes = 0;
        boolean encontrado = false;

        while (atual != null) {
            comparacoes = comparacoes + 1;
            if (atual.chave == chave) {
                encontrado = true;
                break;
            }
            atual = atual.proximo;
        }

        if (isHit) {
            this.contagemComparacoesSucesso = this.contagemComparacoesSucesso + comparacoes;
        } else {
            this.contagemComparacoesFracasso = this.contagemComparacoesFracasso + comparacoes;
        }

        return encontrado;
    }
}

public class TabelaHashExperimento {
    private static final int[] TAMANHOS_M = {1009, 10007, 100003};
    private static final int QTD_TAMANHOS_M = 3;
    private static final int[] TAMANHOS_N = {1000, 10000, 100000};
    private static final int QTD_TAMANHOS_N = 3;
    private static final int[] SEEDS = {137, 271828, 314159};
    private static final int QTD_SEEDS = 3;

    private static final int TIPO_DIV = 1;
    private static final int TIPO_MUL = 2;
    private static final int TIPO_FOLD = 3;
    private static final int QTD_FUNCOES = 3;
    private static final String NOME_DIV = "H_DIV";
    private static final String NOME_MUL = "H_MUL";
    private static final String NOME_FOLD = "H_FOLD";
    private static final int NUM_REPETICOES = 5;
    private static final int MOD_CHECKSUM = 1000003;

    private static int[] chavesDataset;

    private static void gerarDataset(int n, int seed) {
        chavesDataset = new int[n];
        Random rand = new Random();
        rand.setSeed(seed);

        final int MIN_CHAVE = 100000000;
        final int RANGE_CHAVE = 900000000;

        int i = 0;
        while (i < n) {
            int chave = rand.nextInt(RANGE_CHAVE) + MIN_CHAVE;
            chavesDataset[i] = chave;
            i = i + 1;
        }
    }

    private static long[] Fazer_busca(TabelaHash hash, int tipoFunc, int n, int[] chavesInseridas) {
        long inicio;
        long fim;
        long tempoTotalHitsMs = 0;
        long tempoTotalMissesMs = 0;

        hash.contagemComparacoesSucesso = 0;
        hash.contagemComparacoesFracasso = 0;

        int i = 0;
        while (i < n / 2) {
            hash.buscar(chavesInseridas[i], tipoFunc, true);
            i = i + 1;
        }

        int r = 0;
        while (r < NUM_REPETICOES) {

            inicio = System.currentTimeMillis();
            i = 0;
            while (i < n) {
                hash.buscar(chavesDataset[i], tipoFunc, true);
                i = i + 1;
            }
            fim = System.currentTimeMillis();
            tempoTotalHitsMs = tempoTotalHitsMs + (fim - inicio);

            inicio = System.currentTimeMillis();
            i = 0;
            while (i < n) {
                hash.buscar(chavesDataset[i] + 1, tipoFunc, false);
                i = i + 1;
            }
            fim = System.currentTimeMillis();
            tempoTotalMissesMs = tempoTotalMissesMs + (fim - inicio);

            r = r + 1;
        }

        long[] tempos = new long[2];
        tempos[0] = tempoTotalHitsMs / NUM_REPETICOES;
        tempos[1] = tempoTotalMissesMs / NUM_REPETICOES;

        return tempos;
    }

    // resto de a
    private static int modulo_resto(int a, int b) {
        return a % b;
    }

    public static void main(String[] args) {

        String csvOutput = "m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum\n";

        int i_m = 0;
        int i_func = 0;
        int i_n = 0;
        int i_seed = 0;

        while (i_m < QTD_TAMANHOS_M) {
            int m = TAMANHOS_M[i_m];

            i_func = 1;
            while (i_func <= QTD_FUNCOES) {

                String nome_funcao;
                if (i_func == TIPO_DIV) {
                    nome_funcao = NOME_DIV;
                } else if (i_func == TIPO_MUL) {
                    nome_funcao = NOME_MUL;
                } else if (i_func == TIPO_FOLD) {
                    nome_funcao = NOME_FOLD;
                } else {
                    nome_funcao = "ERRO";
                }

                i_n = 0;
                while (i_n < QTD_TAMANHOS_N) {
                    int n = TAMANHOS_N[i_n];

                    i_seed = 0;
                    while (i_seed < QTD_SEEDS) {
                        int seed = SEEDS[i_seed];

                        System.out.println("Experimento: " + nome_funcao + ", m=" + m + ", n=" + n + ", seed=" + seed);

                        gerarDataset(n, seed);

                        TabelaHash tabelaExperimento = new TabelaHash(m);

                        long tempoTotalInsercaoMs = 0;
                        long checksum = 0;

                        int r = 0;
                        while (r < NUM_REPETICOES) {
                            TabelaHash tabelaRep = new TabelaHash(m);
                            long checksumRep = 0;

                            long inicio = System.currentTimeMillis();

                            int i = 0;
                            while (i < n) {
                                int chave = chavesDataset[i];

                                if (r == 0 && i < 10) {
                                    int hk = tabelaRep.calcularHash(chave, i_func);
                                    checksumRep = checksumRep + hk;
                                }

                                tabelaRep.inserir(chave, i_func);
                                i = i + 1;
                            }

                            long fim = System.currentTimeMillis();
                            tempoTotalInsercaoMs = tempoTotalInsercaoMs + (fim - inicio);

                            if (r == 0) {
                                tabelaExperimento.contagemColisoesTabela = tabelaRep.contagemColisoesTabela;
                                tabelaExperimento.contagemColisoesLista = tabelaRep.contagemColisoesLista;
                                checksum = modulo_resto((int) checksumRep, MOD_CHECKSUM);
                            }

                            r = r + 1;
                        }

                        System.out.println("Checksum: " + checksum + "\n");

                        long tempoMedioInsercaoMs = tempoTotalInsercaoMs / NUM_REPETICOES;
                        long colisoesTabela = tabelaExperimento.contagemColisoesTabela;

                        long colisoesListaMedia = 0;
                        if (n > 0) {
                            colisoesListaMedia = tabelaExperimento.contagemColisoesLista / n;
                        }

                        TabelaHash tabelaFinal = new TabelaHash(m);
                        int k = 0;
                        while (k < n) {
                            tabelaFinal.inserir(chavesDataset[k], i_func);
                            k = k + 1;
                        }

                        long[] temposBusca = Fazer_busca(tabelaFinal, i_func, n, chavesDataset);
                        long tempoBuscaHitsMs = temposBusca[0];
                        long tempoBuscaMissesMs = temposBusca[1];

                        long totalBuscas = (long) n * NUM_REPETICOES;

                        long comparacoesHitsMedia = 0;
                        if (totalBuscas > 0) {
                            comparacoesHitsMedia = tabelaFinal.contagemComparacoesSucesso / totalBuscas;
                        }

                        long comparacoesMissesMedia = 0;
                        if (totalBuscas > 0) {
                            comparacoesMissesMedia = tabelaFinal.contagemComparacoesFracasso / totalBuscas;
                        }

                        String linha = m + "," + n + "," + nome_funcao + "," + seed + "," +
                                tempoMedioInsercaoMs + "," + colisoesTabela + "," + colisoesListaMedia + "," +
                                tempoBuscaHitsMs + "," + tempoBuscaMissesMs + "," +
                                comparacoesHitsMedia + "," + comparacoesMissesMedia + "," + checksum + "\n";

                        csvOutput = csvOutput + linha;

                        i_seed = i_seed + 1;
                    }
                    i_n = i_n + 1;
                }
                i_func = i_func + 1;
            }
            i_m = i_m + 1;
        }

        System.out.println("\nRESULTADOS CSV");
        System.out.print(csvOutput);
    }
}