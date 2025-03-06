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
}
