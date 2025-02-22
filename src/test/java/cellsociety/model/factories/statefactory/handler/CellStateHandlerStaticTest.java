package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.factories.statefactory.exceptions.CellStateFactoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CellStateHandlerStaticTest {

    private CellStateHandlerStatic handler;
    private Map<Integer, String> testCellStates;

    @BeforeEach
    void setUp() {
        // Initialize a test map of cell states
        testCellStates = new HashMap<>();
        testCellStates.put(0, "DEAD");
        testCellStates.put(1, "ALIVE");
        testCellStates.put(2, "BURNING");

        // Create an instance of CellStateHandlerStatic with the test map
        handler = new CellStateHandlerStatic(testCellStates);
    }

    @Test
    @DisplayName("Test getStateInt")
    void testGetStateInt() {
        // Act
        List<Integer> stateInts = handler.getStateInt();

        // Assert
        assertEquals(3, stateInts.size());
        assertTrue(stateInts.containsAll(List.of(0, 1, 2)));
    }

    @Test
    @DisplayName("Test getStateString")
    void testGetStateString() {
        // Act
        List<String> stateStrings = handler.getStateString();

        // Assert
        assertEquals(3, stateStrings.size());
        assertTrue(stateStrings.containsAll(List.of("DEAD", "ALIVE", "BURNING")));
    }

    @Test
    @DisplayName("Test stateFromString_ valid state")
    void testStateFromStringValid() {
        // Act
        int state = handler.stateFromString("ALIVE");

        // Assert
        assertEquals(1, state);
    }

    @Test
    @DisplayName("Test stateFromString_invalid state")
    void testStateFromStringInvalid() {
        // Act & Assert
        Exception exception = assertThrows(CellStateFactoryException.class, () -> {
            handler.stateFromString("INVALID_STATE");
        });

        assertEquals("Invalid state: INVALID_STATE", exception.getMessage());
    }

    @Test
    @DisplayName("Test stateFromString_ case-insensitive input")
    void testStateFromStringCaseInsensitive() {
        // Act
        int state = handler.stateFromString("dead");

        // Assert
        assertEquals(0, state);
    }

    @Test
    @DisplayName("Test statetoString method_valid state")
    void testStatetoStringValid() {
        // Act
        String stateString = handler.statetoString(2);

        // Assert
        assertEquals("BURNING", stateString);
    }

    @Test
    @DisplayName("Test statetoString method_invalid state")
    void testStatetoStringInvalid() {
        // Act
        String stateString = handler.statetoString(99);

        // Assert
        assertEquals("INVALID", stateString);
    }

    @Test
    @DisplayName("Test isValidState method_alid state")
    void testIsValidStateValid() {
        // Act & Assert
        assertTrue(handler.isValidState(1));
    }

    @Test
    @DisplayName("Test isValidState method_invalid state")
    void testIsValidStateInvalid() {
        // Act & Assert
        assertFalse(handler.isValidState(99));
    }
}