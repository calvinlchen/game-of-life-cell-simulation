package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DarwinParserTest {

  private DarwinProgram mockProgram;
  private DarwinParser parser;

  @BeforeEach
  void setUp() {
    mockProgram = mock(DarwinProgram.class);
  }

  @Test
  void testValidFileParsing() {
    parser = new DarwinParser("test_species", mockProgram);

    verify(mockProgram, atLeastOnce()).addDarwinCommand(any(DarwinCommand.class));
  }

  @Test
  void testInvalidFileThrowsException() {
    Exception exception = assertThrows(SimulationException.class,
        () -> new DarwinParser("invalidSpecies", mockProgram));
  }
}
