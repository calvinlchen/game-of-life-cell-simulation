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
    assertEquals(DarwinCommandType.IF_EMPTY, DarwinCommandType.fromString("emp?"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("ifwall"));
    assertEquals(DarwinCommandType.IF_WALL, DarwinCommandType.fromString("wl?"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("ifsame"));
    assertEquals(DarwinCommandType.IF_SAME, DarwinCommandType.fromString("sm?"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("ifenemy"));
    assertEquals(DarwinCommandType.IF_ENEMY, DarwinCommandType.fromString("emy?"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("ifrandom"));
    assertEquals(DarwinCommandType.IF_RANDOM, DarwinCommandType.fromString("rnd?"));
    assertEquals(DarwinCommandType.GO, DarwinCommandType.fromString("go"));
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
  }
}
