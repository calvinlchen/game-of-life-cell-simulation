package cellsociety.model.util.darwin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarwinProgramTest {

  private DarwinProgram darwinProgram;

  @BeforeEach
  void setUp() {
    darwinProgram = new DarwinProgram("test_species"); // Assuming test_species.txt exists for testing
  }

  @Test
  void testProgramStoresCommandsCorrectly() {
    // Ensure commands were correctly parsed and stored
    assertEquals(4, darwinProgram.size());

    // Check first command
    DarwinCommand command1 = darwinProgram.getDarwinCommand(0);
    assertEquals(DarwinCommandType.IF_ENEMY, command1.getType());
    assertEquals("4", command1.getArgument());

    // Check second command
    DarwinCommand command2 = darwinProgram.getDarwinCommand(1);
    assertEquals(DarwinCommandType.LEFT, command2.getType());
    assertEquals("90", command2.getArgument());

    // Check third command
    DarwinCommand command3 = darwinProgram.getDarwinCommand(2);
    assertEquals(DarwinCommandType.GO, command3.getType());
    assertEquals("1", command3.getArgument());

    // Check fourth command
    DarwinCommand command4 = darwinProgram.getDarwinCommand(3);
    assertEquals(DarwinCommandType.INFECT, command4.getType());
    assertEquals("12", command4.getArgument());
  }

  @Test
  void testIteratorWorksCorrectly() {
    int count = 0;
    for (DarwinCommand command : darwinProgram) {
      assertNotNull(command);
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  void testToStringFormat() {
    String expectedOutput =
        "0: IF_ENEMY 4\n" +
            "1: LEFT 90\n" +
            "2: GO 1\n" +
            "3: INFECT 12\n";

    assertEquals(expectedOutput, darwinProgram.toString());
  }
}
