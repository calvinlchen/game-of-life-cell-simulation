package cellsociety.model.util.darwin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DarwinCommandTest {

  @Test
  void testCommandParsing() {
    DarwinCommand command1 = new DarwinCommand(DarwinCommandType.MOVE, "10");
    assertEquals(DarwinCommandType.MOVE, command1.getType());
    assertEquals("10", command1.getArgument());

    DarwinCommand command2 = new DarwinCommand(DarwinCommandType.IF_ENEMY, "4");
    assertEquals(DarwinCommandType.IF_ENEMY, command2.getType());
    assertEquals("4", command2.getArgument());

    DarwinCommand command3 = new DarwinCommand(DarwinCommandType.GO, null);
    assertEquals(DarwinCommandType.GO, command3.getType());
    assertNull(command3.getArgument());
  }

  @Test
  void testUnknownCommandHandling() {
    DarwinCommand invalidCommand = new DarwinCommand(DarwinCommandType.UNKNOWN, "5");
    assertEquals(DarwinCommandType.UNKNOWN, invalidCommand.getType());
    assertEquals("5", invalidCommand.getArgument());
  }

  @Test
  void testToStringFormat() {
    DarwinCommand command1 = new DarwinCommand(DarwinCommandType.LEFT, "90");
    assertEquals("LEFT 90", command1.toString());

    DarwinCommand command2 = new DarwinCommand(DarwinCommandType.GO, null);
    assertEquals("GO", command2.toString());
  }
}
