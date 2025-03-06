package cellsociety.model.util.darwin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarwinCommandTypeTest {

  @Test
  void testValidCommandMappings() {
    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("move"));
    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("mv"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("left"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("lt"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("right"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("rt"));
    assertEquals(DarwinCommandType.INFECT, DarwinCommandType.fromString("infect"));
    assertEquals(DarwinCommandType.INFECT, DarwinCommandType.fromString("inf"));
    assertEquals(DarwinCommandType.IF_EMPTY, DarwinCommandType.fromString("ifempty"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("ifwall"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("ifsame"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("ifenemy"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("ifrandom"));
    assertEquals(DarwinCommandType.GO, DarwinCommandType.fromString("go"));
    assertEquals(DarwinCommandType.IF_EMPTY, DarwinCommandType.fromString("emp?"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("rnd?"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("wl?"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("emy?"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("sm?"));

    // Spanish command mappings
    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("avanzar"));
    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("av"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("izquierda"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("zq"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("derecha"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("dr"));
    assertEquals(DarwinCommandType.INFECT, DarwinCommandType.fromString("infectar"));
    assertEquals(DarwinCommandType.INFECT, DarwinCommandType.fromString("inf"));
    assertEquals(DarwinCommandType.IF_EMPTY, DarwinCommandType.fromString("sivacío"));
    assertEquals(DarwinCommandType.IF_EMPTY, DarwinCommandType.fromString("vcí?"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("simuro"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("mur?"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("siigual"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("igl?"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("sienemigo"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("nmg?"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("siazar"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("azr?"));
    assertEquals(DarwinCommandType.GO, DarwinCommandType.fromString("ir"));
  }

  @Test
  void testInvalidCommandReturnsUnknown() {
    assertEquals(DarwinCommandType.UNKNOWN, DarwinCommandType.fromString("randomCommand"));
    assertEquals(DarwinCommandType.UNKNOWN, DarwinCommandType.fromString("123"));
    assertEquals(DarwinCommandType.UNKNOWN, DarwinCommandType.fromString(""));
  }

  @Test
  void testCaseInsensitivity() {
    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("MOVE"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("LeFt"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("RIGHT"));

    assertEquals(DarwinCommandType.MOVE, DarwinCommandType.fromString("AVANZAR"));
    assertEquals(DarwinCommandType.LEFT, DarwinCommandType.fromString("IzQuIeRdA"));
    assertEquals(DarwinCommandType.RIGHT, DarwinCommandType.fromString("DERECHA"));
  }
}
