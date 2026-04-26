package ficha;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DadosTest {

    // -------------------------------------------------------------------------
    // dado(qtd, lado) — rola qtd dados de `lado` faces, retorna soma
    // Resultado esperado: [qtd, qtd*lado]
    // Random.nextInt(n) garante [0, n-1], logo os limites são sempre respeitados.
    // -------------------------------------------------------------------------

    @Test
    void testDado_1d6_retornaValorEntre1e6() {
        int resultado = Dados.dado(1, 6);
        assertTrue(resultado >= 1 && resultado <= 6,
                "Esperado em [1,6], obtido: " + resultado);
    }

    @Test
    void testDado_3d6_retornaValorEntre3e18() {
        int resultado = Dados.dado(3, 6);
        assertTrue(resultado >= 3 && resultado <= 18,
                "Esperado em [3,18], obtido: " + resultado);
    }

    @Test
    void testDado_1d20_retornaValorEntre1e20() {
        // Arrange / Act — simula rolagem de iniciativa/ataque
        int resultado = Dados.dado(1, 20);
        assertTrue(resultado >= 1 && resultado <= 20,
                "Esperado em [1,20], obtido: " + resultado);
    }

    @Test
    void testDado_2d8_retornaValorEntre2e16() {
        // Arrange / Act — simula cura (curarFerimentos usa 2d8)
        int resultado = Dados.dado(2, 8);
        assertTrue(resultado >= 2 && resultado <= 16,
                "Esperado em [2,16], obtido: " + resultado);
    }

    @Test
    void testDado_1d1_sempreRetorna1() {
        // Arrange — boundary: dado de uma face, resultado sempre 1
        for (int i = 0; i < 10; i++) {
            assertEquals(1, Dados.dado(1, 1));
        }
    }

    @Test
    void testDado_muitosDados_somaRespeitalimites() {
        // Arrange — 10d6: [10, 60]
        int resultado = Dados.dado(10, 6);
        assertTrue(resultado >= 10 && resultado <= 60,
                "Esperado em [10,60], obtido: " + resultado);
    }

    // -------------------------------------------------------------------------
    // descartaMenor(int[]) — retorna lista de 3 elementos sem o menor valor
    // -------------------------------------------------------------------------

    @Test
    void testDescartaMenor_removeMenorValor() {
        // Arrange
        int[] dados = {3, 1, 4, 2};
        // Act
        List resultado = Dados.descartaMenor(dados);
        // Assert — o menor (1) foi descartado
        assertFalse(resultado.contains(1));
    }

    @Test
    void testDescartaMenor_retornaListaComTresElementos() {
        int[] dados = {4, 5, 3, 6};
        List resultado = Dados.descartaMenor(dados);
        assertEquals(3, resultado.size());
    }

    @Test
    void testDescartaMenor_menorNaPrimeiraPosicao_descartado() {
        // Arrange — boundary: mínimo no início do array
        int[] dados = {1, 5, 5, 5};
        List resultado = Dados.descartaMenor(dados);
        assertFalse(resultado.contains(1));
        assertEquals(3, resultado.size());
    }

    @Test
    void testDescartaMenor_menorNaUltimaPosicao_descartado() {
        // Arrange — boundary: mínimo no final do array
        int[] dados = {5, 5, 5, 1};
        List resultado = Dados.descartaMenor(dados);
        assertFalse(resultado.contains(1));
    }

    @Test
    void testDescartaMenor_todosIguais_removeUmElemento() {
        // Arrange — edge case: todos os valores idênticos
        int[] dados = {4, 4, 4, 4};
        List resultado = Dados.descartaMenor(dados);
        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().allMatch(v -> (int) v == 4));
    }

    @Test
    void testDescartaMenor_valorMaximo_permaneceNaLista() {
        int[] dados = {1, 6, 6, 6};
        List resultado = Dados.descartaMenor(dados);
        assertEquals(3, resultado.stream().filter(v -> (int) v == 6).count());
    }

    // -------------------------------------------------------------------------
    // soma(List<Integer>) — soma os 3 primeiros elementos da lista
    // -------------------------------------------------------------------------

    @Test
    void testSoma_listaComTresValoresPositivos_retornaSomaCorreta() {
        List<Integer> lista = Arrays.asList(4, 3, 5);
        assertEquals(12, Dados.soma(lista));
    }

    @Test
    void testSoma_somaExatamente6_retorna6() {
        // Arrange — boundary inferior para aceitação (soma >= 6)
        List<Integer> lista = Arrays.asList(2, 2, 2);
        assertEquals(6, Dados.soma(lista));
    }

    @Test
    void testSoma_somaAbaixoDe6_retornaValorMenorQue6() {
        // Arrange — edge case: resultado abaixo do mínimo aceito
        List<Integer> lista = Arrays.asList(1, 1, 1);
        int resultado = Dados.soma(lista);
        assertEquals(3, resultado);
        assertTrue(resultado < 6);
    }

    @Test
    void testSoma_somaAlta_retornaValorCorreto() {
        // Arrange — happy path: todos 6
        List<Integer> lista = Arrays.asList(6, 6, 6);
        assertEquals(18, Dados.soma(lista));
    }

    // -------------------------------------------------------------------------
    // confere(List, int) — verifica se valor está na lista
    // -------------------------------------------------------------------------

    @Test
    void testConfere_valorPresenteNaLista_retornaTrue() {
        List<Integer> lista = new ArrayList<>(Arrays.asList(8, 12, 14, 10, 11, 9));
        assertTrue(Dados.confere(lista, 12));
    }

    @Test
    void testConfere_valorAusenteNaLista_retornaFalse() {
        // Arrange — negative case
        List<Integer> lista = new ArrayList<>(Arrays.asList(8, 12, 14, 10, 11, 9));
        assertFalse(Dados.confere(lista, 7));
    }

    @Test
    void testConfere_listaVazia_retornaFalse() {
        // Arrange — edge case: lista sem elementos
        List<Integer> lista = new ArrayList<>();
        assertFalse(Dados.confere(lista, 10));
    }

    @Test
    void testConfere_valorZeroNaLista_retornaTrue() {
        // Arrange — boundary: zero é um valor válido
        List<Integer> lista = new ArrayList<>(Arrays.asList(0, 5, 10));
        assertTrue(Dados.confere(lista, 0));
    }

    @Test
    void testConfere_valorNegativo_retornaFalse() {
        // Arrange — negative case: valor fora do domínio de atributos
        List<Integer> lista = new ArrayList<>(Arrays.asList(8, 10, 12));
        assertFalse(Dados.confere(lista, -1));
    }

    @Test
    void testConfere_unicoElementoNaLista_encontrado() {
        // Arrange — edge case: lista com um único elemento
        List<Integer> lista = new ArrayList<>(Arrays.asList(15));
        assertTrue(Dados.confere(lista, 15));
    }
}
