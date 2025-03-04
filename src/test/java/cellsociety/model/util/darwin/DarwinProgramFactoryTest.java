package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarwinProgramFactoryTest {

  @Test
  void testSameInstanceForSameSpecies() {
    DarwinProgram program1 = DarwinProgramFactory.getProgram("flytrap");
    DarwinProgram program2 = DarwinProgramFactory.getProgram("flytrap");

    assertSame(program1, program2, "Factory should return the same instance for the same species");
  }

  @Test
  void testDifferentInstancesForDifferentSpecies() {
    DarwinProgram program1 = DarwinProgramFactory.getProgram("flytrap");
    DarwinProgram program2 = DarwinProgramFactory.getProgram("test_species");

    assertNotSame(program1, program2,
        "Factory should return different instances for different species");
  }

  @Test
  void testMultipleCallsReturnSameObject() {
    DarwinProgram program1 = DarwinProgramFactory.getProgram("flytrap");
    DarwinProgram program2 = DarwinProgramFactory.getProgram("flytrap");
    DarwinProgram program3 = DarwinProgramFactory.getProgram("flytrap");

    assertSame(program1, program2);
    assertSame(program2, program3);
    assertSame(program1, program3);
  }

  @Test
  void testConcurrentAccess() throws InterruptedException {
    final DarwinProgram[] program1 = new DarwinProgram[1];
    final DarwinProgram[] program2 = new DarwinProgram[1];

    Thread thread1 = new Thread(() -> program1[0] = DarwinProgramFactory.getProgram("flytrap"));
    Thread thread2 = new Thread(() -> program2[0] = DarwinProgramFactory.getProgram("flytrap"));

    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();

    assertNotNull(program1[0]);
    assertNotNull(program2[0]);
    assertSame(program1[0], program2[0],
        "Factory should return the same instance in concurrent access");
  }

  @Test
  void noFileForSpecies() {
    assertThrows(SimulationException.class, () -> DarwinProgramFactory.getProgram("randomSpecies"));
  }
}
