package cellsociety.model.factories.statefactory;

import cellsociety.model.factories.statefactory.handler.*;
import cellsociety.model.util.SimulationTypes.SimType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CellStateFactoryTest {

    @Test
    @DisplayName("Test Get Handler_Game of Life Simulation")
    void testGetHandlerGameOfLife() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(SimType.GameOfLife);

        // Assert
        assertNotNull(handler);
        assertTrue(handler instanceof GameOfLifeStateHandler);
    }

    @Test
    @DisplayName("Test Get Handler_Fire Simulation")
    void testGetHandlerFire() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(SimType.Fire);

        // Assert
        assertNotNull(handler);
        assertTrue(handler instanceof FireStateHandler);
    }

    @Test
    @DisplayName("Test Get Handler_Percolation Simulation")
    void testGetHandlerPercolation() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(SimType.Percolation);

        // Assert
        assertNotNull(handler);
        assertTrue(handler instanceof PercolationStateHandler);
    }

    @Test
    @DisplayName("Test Get Handler_Segregation Simulation")
    void testGetHandlerSegregation() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(SimType.Segregation);

        // Assert
        assertNotNull(handler);
        assertTrue(handler instanceof SegregationStateHandler);
    }

    @Test
    @DisplayName("Test Get Handler_Wator Simulation")
    void testGetHandlerWaTor() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(SimType.WaTor);

        // Assert
        assertNotNull(handler);
        assertTrue(handler instanceof WaTorStateHandler);
    }

    @Test
    @DisplayName("Test Get Handler_Unknown Simulation")
    void testGetHandlerUnknownSimType() {
        // Arrange
        SimType unknownSimType = SimType.RPS; //creating unknown sim type of RPS

        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(unknownSimType); //might need to switch to assert throws.

        // Assert
        assertNull(handler);
    }

    @Test
    @DisplayName("Test Get Handler_Null Simulation")
    void testGetHandlerNullSimType() {
        // Act
        CellStateHandlerStatic handler = CellStateFactory.getHandler(null); //might need to switch to assert throws.

        // Assert
        assertNull(handler);
    }
}