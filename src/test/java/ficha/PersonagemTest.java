package ficha;

import equipamentos.Armaduras;
import equipamentos.Armas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import racasClasses.Classe;
import racasClasses.Raca;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Personagem.java.
 *
 * Desafio: todos os campos de Personagem são estáticos.
 * Solução: @BeforeEach reinicializa o estado via `new Personagem()` (que
 * reatribui Personagem.atributos e Personagem.nivel) e configura os objetos
 * de suporte (Raca, Classe, Armas, Armaduras) antes de cada teste.
 *
 * Sequência de setup obrigatória para testes que dependem de modificadores:
 *   1. new Personagem()             → zera atributos (0,0,0,0,0,0) e nivel=1
 *   2. setAtributos(i, valor)       → define valores desejados
 *   3. setRaca() + setClasse()
 *   4. iniciar()                    → aplica bônus racial e calcula modificadores
 *   5. setArma() + setArmadura()    → necessário antes de equipar()
 *   6. equipar()                    → calcula def, deslocamento e atq
 */
class PersonagemTest {

    // Objetos reutilizáveis entre testes
    private Raca racaNeutra;       // sem bônus, deslocamento padrão, poder=4 (goblin)
    private Classe classeGuerreiro; // pv=12, pvNivel=6, mana=4, pm=2
    private Armas armaMelee;       // alcance=1 (corpo a corpo)
    private Armas armaDistancia;   // alcance=2 (distância)
    private Armaduras armaduraLeve;
    private Armaduras armaduraPesada;

    @BeforeEach
    void setUp() {
        new Personagem(); // reinicia campos estáticos
        racaNeutra    = new Raca("humano", 4, false, false, 0, 0, 0, 0, 0, 0);
        classeGuerreiro = new Classe("guerreiro", 5, 2, 12, 6, 4, true, true, true);
        armaMelee     = new Armas(6, 20, 2, 1, "Espada", "marcial");
        armaDistancia = new Armas(8, 20, 2, 2, "Arco",   "marcial");
        armaduraLeve  = new Armaduras(3, 0,  "Couro",  false);
        armaduraPesada = new Armaduras(8, -2, "Placas", true);
    }

    /** Configura FOR=14(+2), DES=12(+1), CON=16(+3), INT/SAB/CAR=10(0). */
    private void setupAtributos() {
        Personagem.setAtributos(0, 14); // FOR
        Personagem.setAtributos(1, 12); // DES
        Personagem.setAtributos(2, 16); // CON
        Personagem.setAtributos(3, 10); // INT
        Personagem.setAtributos(4, 10); // SAB
        Personagem.setAtributos(5, 10); // CAR
        Personagem.setRaca(racaNeutra);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar(); // aplica bônus racial + calcula modificadores
    }

    /** Chama equipar() após definir arma e armadura. */
    private void equiparCom(Armas arma, Armaduras armadura) {
        Personagem.setArma(arma);
        Personagem.setArmadura(armadura);
        Personagem.equipar();
    }

    // -------------------------------------------------------------------------
    // setPv() — pv = classe.pv + mod(CON) + (classe.pvNivel + mod(CON)) * (nivel-1)
    // -------------------------------------------------------------------------

    @Test
    void testSetPv_nivel1_calculaCorreto() {
        // Arrange — CON=16 → mod=+3, classe.pv=12, classe.pvNivel=6, nivel=1
        setupAtributos();
        // Act (iniciar() já chamou setPv())
        // Assert: 12 + 3 + (6+3)*(1-1) = 15
        assertEquals(15, Personagem.getPv());
    }

    @Test
    void testSetPv_nivel2_refletePvNivel() {
        // Arrange
        setupAtributos();
        equiparCom(armaMelee, null);
        // Act
        Personagem.subirNivel(); // nivel 1 → 2, recalcula setPv()
        // Assert: 12 + 3 + (6+3)*(2-1) = 24
        assertEquals(24, Personagem.getPv());
    }

    @Test
    void testSetPv_modificadorConZero_pvIgualAoBaseDaClasse() {
        // Arrange — CON=10 → mod=0
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10); // CON
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaNeutra);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        // Assert: 12 + 0 + 0 = 12
        assertEquals(12, Personagem.getPv());
    }

    @Test
    void testSetPv_modificadorConNegativo_pvAbaixoDoBase() {
        // Arrange — CON=8 → mod=-1 (boundary negativo)
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 8); // CON
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaNeutra);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        // Assert: 12 + (-1) + 0 = 11
        assertEquals(11, Personagem.getPv());
    }

    // -------------------------------------------------------------------------
    // setMana() — mana = classe.mana * nivel [+ mod(INT) se bruxo / mod(SAB) se clérigo]
    // -------------------------------------------------------------------------

    @Test
    void testSetMana_classeGuerreiro_nivel1_semBonusAtributo() {
        // Arrange — classe.mana=4, nivel=1
        setupAtributos();
        // Assert: 4 * 1 = 4
        assertEquals(4, Personagem.getMana());
    }

    @Test
    void testSetMana_classeBruxo_somaModificadorInteligencia() {
        // Arrange — INT=14 → mod=+2; classe.mana=3, nivel=1
        Classe classeBruxo = new Classe("bruxo", 6, 3, 8, 4, 3, false, true, false);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 14); // INT=14 → +2
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaNeutra);
        Personagem.setClasse(classeBruxo);
        Personagem.iniciar();
        // Assert: 3*1 + 2 = 5
        assertEquals(5, Personagem.getMana());
    }

    @Test
    void testSetMana_classeClerigo_somaModificadorSabedoria() {
        // Arrange — SAB=16 → mod=+3; classe.mana=4, nivel=1
        Classe classeClerigo = new Classe("clerigo", 7, 2, 10, 5, 4, true, true, false);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 16); // SAB=16 → +3
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaNeutra);
        Personagem.setClasse(classeClerigo);
        Personagem.iniciar();
        // Assert: 4*1 + 3 = 7
        assertEquals(7, Personagem.getMana());
    }

    @Test
    void testSetMana_nivel2_multiplicaPorNivel() {
        // Arrange — classe.mana=4, nivel=2
        setupAtributos();
        equiparCom(armaMelee, null);
        // Act
        Personagem.subirNivel(); // nivel 1 → 2, recalcula setMana()
        // Assert: 4 * 2 = 8
        assertEquals(8, Personagem.getMana());
    }

    // -------------------------------------------------------------------------
    // setDefesa() — def = 10 + (nivel/2) [+ mod(DES) se sem armadura ou leve]
    //                                    [+ armadura.defesa]
    // -------------------------------------------------------------------------

    @Test
    void testSetDefesa_semArmadura_usaModificadorDestreza() {
        // Arrange — DES=12 → mod=+1, nivel=1 → nivel/2=0
        setupAtributos();
        equiparCom(armaMelee, null);
        // Assert: 10 + 0 + 1 = 11
        assertEquals(11, Personagem.getDefesa());
    }

    @Test
    void testSetDefesa_armaduraLeve_somaDefesaEDestreza() {
        // Arrange — DES=12 → +1, armaduraLeve.defesa=3, pesada=false
        setupAtributos();
        equiparCom(armaMelee, armaduraLeve);
        // Assert: 10 + 0 + 3 + 1 = 14
        assertEquals(14, Personagem.getDefesa());
    }

    @Test
    void testSetDefesa_armaduraPesada_ignoraDestreza() {
        // Arrange — armaduraPesada.defesa=8, pesada=true → sem mod(DES)
        setupAtributos();
        equiparCom(armaMelee, armaduraPesada);
        // Assert: 10 + 0 + 8 = 18 (DES ignorada)
        assertEquals(18, Personagem.getDefesa());
    }

    @Test
    void testSetDefesa_nivel2_somaMeioDo2() {
        // Arrange — nivel=2 → nivel/2=1
        setupAtributos();
        equiparCom(armaMelee, null);
        Personagem.subirNivel(); // recalcula setDefesa() internamente
        // Assert: 10 + 1 + 1(DES) = 12
        assertEquals(12, Personagem.getDefesa());
    }

    // -------------------------------------------------------------------------
    // setAtq() — atq = nivel/2 + mod(FOR) se alcance=1, mod(DES) se alcance>1
    // -------------------------------------------------------------------------

    @Test
    void testSetAtq_armaMelee_nivel1_usaModificadorForca() {
        // Arrange — FOR=14 → +2, alcance=1
        setupAtributos();
        equiparCom(armaMelee, null);
        // Assert: 0 + 2 = 2
        assertEquals(2, Personagem.getAtq());
    }

    @Test
    void testSetAtq_armaDistancia_usaModificadorDestreza() {
        // Arrange — DES=12 → +1, alcance=2
        setupAtributos();
        equiparCom(armaDistancia, null);
        // Assert: 0 + 1 = 1
        assertEquals(1, Personagem.getAtq());
    }

    @Test
    void testSetAtq_nivel2_somaMeioDoNivel() {
        // Arrange — FOR=14 → +2, alcance=1, nivel=2 → nivel/2=1
        setupAtributos();
        equiparCom(armaMelee, null);
        Personagem.subirNivel();
        // Assert: 1 + 2 = 3
        assertEquals(4, Personagem.getAtq());
    }

    // -------------------------------------------------------------------------
    // setDeslocamento() — base=6, +2 se deslocamentoMaior, -2 se deslocamentoMenor
    //                     + armadura.penalidade (pode ser negativo)
    // -------------------------------------------------------------------------

    @Test
    void testSetDeslocamento_racaNormal_semArmadura_retorna6() {
        // Arrange — racaNeutra: sem modificador de deslocamento
        setupAtributos();
        equiparCom(armaMelee, null);
        // Assert
        assertEquals(6, Personagem.getDeslocamento());
    }

    @Test
    void testSetDeslocamento_racaComDeslocamentoMaior_retorna8() {
        // Arrange — elfo: deslocamentoMaior=true
        Raca racaElfo = new Raca("elfo", 1, true, false, 0, 2, 0, 0, 2, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaElfo);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        // Assert: 6 + 2 = 8
        assertEquals(8, Personagem.getDeslocamento());
    }

    @Test
    void testSetDeslocamento_racaComDeslocamentoMenor_retorna4() {
        // Arrange — anão: deslocamentoMenor=true
        Raca racaAnao = new Raca("anao", 2, false, true, 0, 0, 2, 0, 0, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaAnao);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        // Assert: 6 - 2 = 4
        assertEquals(4, Personagem.getDeslocamento());
    }

    @Test
    void testSetDeslocamento_armaduraComPenalidade_reduzDeslocamento() {
        // Arrange — racaNeutra (base=6), armaduraPesada.penalidade=-2
        setupAtributos();
        equiparCom(armaMelee, armaduraPesada);
        // Assert: 6 + (-2) = 4
        assertEquals(4, Personagem.getDeslocamento());
    }

    // -------------------------------------------------------------------------
    // poderRaca() — aplica bônus passivo da raça ao estado atual do personagem
    // -------------------------------------------------------------------------

    @Test
    void testPoderRaca_elfo_poder1_aumentaDeslocamentoEMana() {
        // Arrange — elfo power=1
        Raca racaElfo = new Raca("elfo", 1, true, false, 0, 2, 0, 0, 2, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaElfo);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        int deslocamentoAntes = Personagem.getDeslocamento();
        int manaAntes         = Personagem.getMana();
        // Act
        Personagem.poderRaca();
        // Assert
        assertEquals(deslocamentoAntes + 2, Personagem.getDeslocamento());
        assertEquals(manaAntes + 1,         Personagem.getMana());
    }

    @Test
    void testPoderRaca_anao_poder2_resetaDeslocamentoEaumentaPv() {
        // Arrange — anão power=2
        Raca racaAnao = new Raca("anao", 2, false, true, 0, 0, 2, 0, 0, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaAnao);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        int pvAntes = Personagem.getPv();
        // Act
        Personagem.poderRaca();
        // Assert — deslocamento resetado para 6 (era 4 por anão), pv +3
        assertEquals(6,          Personagem.getDeslocamento());
        assertEquals(pvAntes + 3, Personagem.getPv());
    }

    @Test
    void testPoderRaca_minotauro_poder3_aumentaDefesaEm1() {
        // Arrange — minotauro power=3
        Raca racaMinotauro = new Raca("minotauro", 3, false, false, 2, 0, 2, 0, 0, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaMinotauro);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        int defAntes = Personagem.getDefesa();
        // Act
        Personagem.poderRaca();
        // Assert
        assertEquals(defAntes + 1, Personagem.getDefesa());
    }

    @Test
    void testPoderRaca_goblin_poder4_aumentaAtqEm1() {
        // Arrange — goblin power=4 (caso default no switch)
        Raca racaGoblin = new Raca("goblin", 4, false, false, 0, 2, 0, 0, 0, 0);
        Personagem.setAtributos(0, 10);
        Personagem.setAtributos(1, 10);
        Personagem.setAtributos(2, 10);
        Personagem.setAtributos(3, 10);
        Personagem.setAtributos(4, 10);
        Personagem.setAtributos(5, 10);
        Personagem.setRaca(racaGoblin);
        Personagem.setClasse(classeGuerreiro);
        Personagem.iniciar();
        equiparCom(armaMelee, null);
        int atqAntes = Personagem.getAtq();
        // Act
        Personagem.poderRaca();
        // Assert
        assertEquals(atqAntes + 1, Personagem.getAtq());
    }

    // -------------------------------------------------------------------------
    // subirNivel() — incrementa nivel e recalcula pv, mana, def, atq, poderRaca
    // -------------------------------------------------------------------------

    @Test
    void testSubirNivel_incrementaNivelEm1() {
        // Arrange
        setupAtributos();
        equiparCom(armaMelee, null);
        assertEquals(1, Personagem.getNivel());
        // Act
        Personagem.subirNivel();
        // Assert
        assertEquals(2, Personagem.getNivel());
    }

    @Test
    void testSubirNivel_aumentaPvConforme_pvNivelEConModifier() {
        // Arrange — CON=16 → +3, pvNivel=6
        setupAtributos();
        equiparCom(armaMelee, null);
        int pvNivel1 = Personagem.getPv();
        // Act
        Personagem.subirNivel();
        // Assert — ganho = pvNivel + mod(CON) = 6 + 3 = 9
        assertEquals(pvNivel1 + 9, Personagem.getPv());
    }

    @Test
    void testSubirNivel_triplo_nivel4() {
        // Arrange — sobe 3 vezes até nivel 4
        setupAtributos();
        equiparCom(armaMelee, null);
        // Act
        Personagem.subirNivel();
        Personagem.subirNivel();
        Personagem.subirNivel();
        // Assert
        assertEquals(4, Personagem.getNivel());
    }
}
