package atributos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class AtributosTest {

    // -------------------------------------------------------------------------
    // setModificadores() — calcula modificadores a partir dos atributos D&D
    // Fórmula: (atributo - 10) / 2 (divisão inteira Java), exceto atributo=9 → -1
    // -------------------------------------------------------------------------

    @Test
    void testSetModificadores_atributo10_retornaModificador0() {
        // Arrange
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        // Act
        atributos.setModificadores();
        // Assert — valor neutro D&D
        assertEquals(0, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo9_retornaModificadorNegativo1() {
        // Arrange — caso especial: sem o if(==9), Java faria -1/2 = 0 (truncação)
        Atributos atributos = new Atributos(9, 9, 9, 9, 9, 9);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(-1, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo8_retornaModificadorNegativo1() {
        // Arrange — boundary: 8 é o menor valor com mod -1 pela fórmula
        Atributos atributos = new Atributos(8, 8, 8, 8, 8, 8);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(-1, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo11_retornaModificador0() {
        // Arrange — divisão inteira Java: (11-10)/2 = 0
        Atributos atributos = new Atributos(11, 11, 11, 11, 11, 11);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(0, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo12_retornaModificador1() {
        // Arrange — happy path: atributo típico acima da média
        Atributos atributos = new Atributos(12, 12, 12, 12, 12, 12);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(1, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo16_retornaModificador3() {
        // Arrange
        Atributos atributos = new Atributos(16, 16, 16, 16, 16, 16);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(3, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_atributo18_retornaModificador4() {
        // Arrange — boundary alto: valor máximo padrão D&D na criação
        Atributos atributos = new Atributos(18, 18, 18, 18, 18, 18);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(4, atributos.getModificador(0));
    }

    @Test
    void testSetModificadores_todosOsSeisAtributos_calculadosCorretamente() {
        // Arrange — happy path com valores distintos para todos os slots
        Atributos atributos = new Atributos(10, 12, 8, 14, 16, 18);
        // Act
        atributos.setModificadores();
        // Assert
        assertEquals(0,  atributos.getModificador(0)); // FOR 10
        assertEquals(1,  atributos.getModificador(1)); // DES 12
        assertEquals(-1, atributos.getModificador(2)); // CON  8
        assertEquals(2,  atributos.getModificador(3)); // INT 14
        assertEquals(3,  atributos.getModificador(4)); // SAB 16
        assertEquals(4,  atributos.getModificador(5)); // CAR 18
    }

    // -------------------------------------------------------------------------
    // setAtributosRaca() — soma bônus raciais ao vetor de atributos
    // -------------------------------------------------------------------------

    @Test
    void testSetAtributosRaca_bonusPositivo_somaCorretamente() {
        // Arrange
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        List<Integer> bonusRaca = Arrays.asList(2, 0, 0, 0, 0, 0);
        // Act
        atributos.setAtributosRaca(bonusRaca);
        // Assert
        assertEquals(12, atributos.getAtributos().get(0));
    }

    @Test
    void testSetAtributosRaca_multiplosBonus_todosSlotsSomados() {
        // Arrange
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        List<Integer> bonusRaca = Arrays.asList(2, 1, 0, 0, 2, 0);
        // Act
        atributos.setAtributosRaca(bonusRaca);
        // Assert
        assertEquals(12, atributos.getAtributos().get(0));
        assertEquals(11, atributos.getAtributos().get(1));
        assertEquals(10, atributos.getAtributos().get(2)); // sem bônus
        assertEquals(12, atributos.getAtributos().get(4));
    }

    @Test
    void testSetAtributosRaca_todosZeros_mantemAtributosOriginais() {
        // Arrange — edge case: raça sem bônus
        Atributos atributos = new Atributos(14, 12, 10, 8, 10, 10);
        List<Integer> bonusRaca = Arrays.asList(0, 0, 0, 0, 0, 0);
        // Act
        atributos.setAtributosRaca(bonusRaca);
        // Assert — valores inalterados
        assertEquals(14, atributos.getAtributos().get(0));
        assertEquals(12, atributos.getAtributos().get(1));
        assertEquals(10, atributos.getAtributos().get(2));
    }

    @Test
    void testSetAtributosRaca_bonusNegativo_subtraiAtributo() {
        // Arrange — edge case: penalidade racial (ex: goblin com CON negativa)
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        List<Integer> bonusRaca = Arrays.asList(0, 0, -2, 0, 0, 0);
        // Act
        atributos.setAtributosRaca(bonusRaca);
        // Assert
        assertEquals(8, atributos.getAtributos().get(2));
    }

    // -------------------------------------------------------------------------
    // setAtributo() — atualiza um atributo individual por índice
    // -------------------------------------------------------------------------

    @Test
    void testSetAtributo_atualizaValorNaPosicaoCorreta() {
        // Arrange
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        // Act
        atributos.setAtributo(0, 16);
        // Assert
        assertEquals(16, atributos.getAtributos().get(0));
    }

    @Test
    void testSetAtributo_naoAlteraOutrosPosicoes() {
        // Arrange
        Atributos atributos = new Atributos(10, 12, 14, 8, 10, 10);
        // Act
        atributos.setAtributo(2, 16);
        // Assert — vizinhos inalterados
        assertEquals(10, atributos.getAtributos().get(0));
        assertEquals(12, atributos.getAtributos().get(1));
        assertEquals(16, atributos.getAtributos().get(2));
        assertEquals(8,  atributos.getAtributos().get(3));
    }

    @Test
    void testSetAtributo_ultimoIndice_atualizaSlotCarisma() {
        // Arrange — boundary: índice 5 (carisma)
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        // Act
        atributos.setAtributo(5, 18);
        // Assert
        assertEquals(18, atributos.getAtributos().get(5));
    }

    // -------------------------------------------------------------------------
    // getModificador() — retorna modificador após setModificadores()
    // -------------------------------------------------------------------------

    @Test
    void testGetModificador_aposSetModificadores_retornaValorCorreto() {
        // Arrange
        Atributos atributos = new Atributos(14, 10, 12, 8, 16, 18);
        atributos.setModificadores();
        // Assert todos os modificadores
        assertEquals(2,  atributos.getModificador(0)); // FOR 14
        assertEquals(0,  atributos.getModificador(1)); // DES 10
        assertEquals(1,  atributos.getModificador(2)); // CON 12
        assertEquals(-1, atributos.getModificador(3)); // INT  8
        assertEquals(3,  atributos.getModificador(4)); // SAB 16
        assertEquals(4,  atributos.getModificador(5)); // CAR 18
    }

    @Test
    void testGetModificador_semSetModificadores_lancaIndexOutOfBounds() {
        // Arrange — negative case: modificadores nunca foram calculados
        Atributos atributos = new Atributos(10, 10, 10, 10, 10, 10);
        // Assert
        assertThrows(IndexOutOfBoundsException.class, () -> atributos.getModificador(0));
    }
}
