package combate;

import equipamentos.Armas;
import ficha.Personagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import racasClasses.Classe;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Combate.java.
 *
 * Estratégia para métodos não-determinísticos (usam Dados.dado()):
 *  - Parametrizar def/margem/atq de forma a forçar caminhos específicos:
 *    def=1000  → bloqueio garantido (golpe max 20 < 1000)
 *    margem=21 → sem crítico (d20 nunca chega a 21)
 *    margem=1  → crítico garantido (d20 sempre >= 1)
 *    def=1     → acerto garantido (golpe >= 1 >= def)
 *  - Random.nextInt(n) garante [0,n-1], logo os intervalos de dano são
 *    sempre respeitados e um único @Test é suficiente.
 */
class CombateTest {

    private Classe classeComPm2;

    @BeforeEach
    void setUp() {
        new Personagem();
        classeComPm2 = new Classe("guerreiro", 5, 2, 12, 6, 4, true, true, true);
        Personagem.setClasse(classeComPm2);
    }

    // -------------------------------------------------------------------------
    // turnoInimigo() — decide se o inimigo ataca com base em Combate.dist
    //
    //   dist=1 → ataca imediatamente                         → true
    //   dist=2 → move (mov→0, dist=1), depois ataca (padrao) → true
    //   dist=3 → move duas vezes (mov+padrao), sem ação ataque→ false
    // -------------------------------------------------------------------------

    @Test
    void testTurnoInimigo_distancia1_atacaImediatamente_retornaTrue() {
        // Arrange
        Combate.dist = 1;
        // Act
        boolean atacou = Combate.turnoInimigo();
        // Assert
        assertTrue(atacou);
    }

    @Test
    void testTurnoInimigo_distancia2_moveEAtaca_retornaTrue() {
        // Arrange
        Combate.dist = 2;
        // Act
        boolean atacou = Combate.turnoInimigo();
        // Assert — move para dist=1 e ainda ataca com ação padrão
        assertTrue(atacou);
        assertEquals(1, Combate.dist);
    }

    @Test
    void testTurnoInimigo_distancia3_moveDuasVezes_retornaFalse() {
        // Arrange
        Combate.dist = 3;
        // Act
        boolean atacou = Combate.turnoInimigo();
        // Assert — usa ambas as ações para mover; não sobra ação de ataque
        assertFalse(atacou);
        assertEquals(1, Combate.dist);
    }

    // -------------------------------------------------------------------------
    // ataque(atq, def, pv, dado, margem, critico)
    // -------------------------------------------------------------------------

    @Test
    void testAtaque_bloqueioGarantido_retornaPvOriginal() {
        // Arrange — margem=21 (sem crítico), def=1000 (impossível acertar)
        int pvOriginal = 50;
        // Act
        int resultado = Combate.ataque(0, 1000, pvOriginal, 6, 21, 2);
        // Assert — pv inalterado
        assertEquals(pvOriginal, resultado);
    }

    @Test
    void testAtaque_acertoGarantido_causaDano() {
        // Arrange — margem=21 (sem crítico), def=1 (sempre acerta)
        int pvOriginal = 1000;
        // Act
        int resultado = Combate.ataque(0, 1, pvOriginal, 6, 21, 2);
        // Assert — pv diminuiu
        assertTrue(resultado < pvOriginal);
    }

    @Test
    void testAtaque_acertoNormal_danoEntreLimitesDoGado() {
        // Arrange — def=1 (acerto), margem=21 (sem crítico), 1d6
        int pvOriginal = 1000;
        // Act
        int resultado = Combate.ataque(0, 1, pvOriginal, 6, 21, 2);
        // Assert — dano 1d6: [1, 6]
        assertTrue(resultado >= pvOriginal - 6);
        assertTrue(resultado <= pvOriginal - 1);
    }

    @Test
    void testAtaque_criticoGarantido_danoMultiplicado() {
        // Arrange — margem=1 (crítico sempre, pois d20 >= 1), critico=3 → 3d6
        int pvOriginal = 1000;
        // Act
        int resultado = Combate.ataque(0, 1, pvOriginal, 6, 1, 3);
        // Assert — dano 3d6: [3, 18]
        assertTrue(resultado >= pvOriginal - 18);
        assertTrue(resultado <= pvOriginal - 3);
    }

    @Test
    void testAtaque_pvBaixo_podeRetornarZeroOuNegativo() {
        // Arrange — edge case: pv=1, acerto garantido, dano mínimo=1
        int pvOriginal = 1;
        // Act
        int resultado = Combate.ataque(0, 1, pvOriginal, 6, 21, 2);
        // Assert
        assertTrue(resultado <= 0);
    }

    @Test
    void testAtaque_bonusAtaque_naoInfluenciaResultadoBloqueado() {
        // Arrange — mesmo com atq alto, def=1000 garante bloqueio
        int pvOriginal = 50;
        int resultado = Combate.ataque(100, 1000, pvOriginal, 6, 21, 2);
        assertEquals(pvOriginal, resultado);
    }

    // -------------------------------------------------------------------------
    // curarFerimentos(pvH) — recupera 2d8 PV
    // -------------------------------------------------------------------------

    @Test
    void testCurarFerimentos_sempreAumentaPv() {
        // Arrange
        int pvOriginal = 20;
        // Act
        int resultado = Combate.curarFerimentos(pvOriginal);
        // Assert — 2d8 mínimo = 2
        assertTrue(resultado > pvOriginal);
    }

    @Test
    void testCurarFerimentos_curaEntreLimitesDoGado_2d8() {
        int pvOriginal = 100;
        int resultado = Combate.curarFerimentos(pvOriginal);
        // 2d8: [2, 16]
        assertTrue(resultado >= pvOriginal + 2);
        assertTrue(resultado <= pvOriginal + 16);
    }

    @Test
    void testCurarFerimentos_pvNegativo_aumentaValor() {
        // Arrange — edge case: personagem em PV negativo
        int pvOriginal = -5;
        int resultado = Combate.curarFerimentos(pvOriginal);
        assertTrue(resultado > pvOriginal);
    }

    // -------------------------------------------------------------------------
    // bolaFogo(pvV) — causa 3d6 de dano
    // -------------------------------------------------------------------------

    @Test
    void testBolaFogo_sempreDiminuiPv() {
        int pvOriginal = 100;
        int resultado = Combate.bolaFogo(pvOriginal);
        assertTrue(resultado < pvOriginal);
    }

    @Test
    void testBolaFogo_danoEntreLimitesDoGado_3d6() {
        int pvOriginal = 100;
        int resultado = Combate.bolaFogo(pvOriginal);
        // 3d6: [3, 18]
        assertTrue(resultado >= pvOriginal - 18);
        assertTrue(resultado <= pvOriginal - 3);
    }

    @Test
    void testBolaFogo_pvBaixo_sempreMataInimigo() {
        // Arrange — pv=1, dano mínimo 3d6=3, então resultado <= -2
        int pvOriginal = 1;
        int resultado = Combate.bolaFogo(pvOriginal);
        assertTrue(resultado <= 0);
    }

    @Test
    void testBolaFogo_pvGrande_naoMataPorSiSo() {
        // Arrange — dano máximo 18 não elimina inimigo com 1000 PV
        int pvOriginal = 1000;
        int resultado = Combate.bolaFogo(pvOriginal);
        assertTrue(resultado > 0);
    }

    // -------------------------------------------------------------------------
    // confereMana(manaH) — retorna manaH >= Personagem.getClasse().getPm()
    // Classe configurada no setUp() com pm=2
    // -------------------------------------------------------------------------

    @Test
    void testConfereMana_manaZero_retornaFalse() {
        assertFalse(Combate.confereMana(0));
    }

    @Test
    void testConfereMana_manaInsuficiente_retornaFalse() {
        // mana=1 < pm=2
        assertFalse(Combate.confereMana(1));
    }

    @Test
    void testConfereMana_manaExatamenteIgualAoCusto_retornaTrue() {
        // Arrange — boundary: mana == pm
        assertTrue(Combate.confereMana(2));
    }

    @Test
    void testConfereMana_manaAcimaDoCusto_retornaTrue() {
        assertTrue(Combate.confereMana(10));
    }

    @Test
    void testConfereMana_manaNegativa_retornaFalse() {
        // Arrange — negative case
        assertFalse(Combate.confereMana(-1));
    }

    // -------------------------------------------------------------------------
    // ataqueEspecial() e marcaCacador() — delegam para ataque() com bônus
    // -------------------------------------------------------------------------

    @Test
    void testAtaqueEspecial_acertoGarantido_causaDano() {
        // Arrange — margem=21 (sem crítico), def=1 (acerto garantido)
        Armas arma = new Armas(6, 21, 2, 1, "Espada", "marcial");
        Personagem.setArma(arma);
        int pvOriginal = 1000;
        // Act — ataqueEspecial usa atq+4, def=1
        int resultado = Combate.ataqueEspecial(pvOriginal, 0, 1);
        // Assert — dano 1d6: [1, 6]
        assertTrue(resultado < pvOriginal);
        assertTrue(resultado >= pvOriginal - 6);
    }

    @Test
    void testAtaqueEspecial_bloqueio_retornaPvOriginal() {
        // Arrange — def=1000: bloqueio mesmo com bônus de +4
        Armas arma = new Armas(6, 21, 2, 1, "Espada", "marcial");
        Personagem.setArma(arma);
        int pvOriginal = 50;
        int resultado = Combate.ataqueEspecial(pvOriginal, 0, 1000);
        assertEquals(pvOriginal, resultado);
    }

    @Test
    void testMarcaCacador_acertoGarantido_causaDano() {
        // Arrange — arma.margem=21, marcaCacador usa margem-2=19
        Armas arma = new Armas(6, 21, 2, 1, "Espada", "marcial");
        Personagem.setArma(arma);
        int pvOriginal = 1000;
        int resultado = Combate.marcaCacador(pvOriginal, 0, 1);
        assertTrue(resultado < pvOriginal);
    }
}
