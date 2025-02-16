package cellsociety.model.util;

public class SimulationTypes {

    /**
     * Enum for representing the types of simulations the program can run
     */
    public enum SimType {
        GameOfLife(false),
        Percolation(false),
        Fire(false),
        Segregation(false),
        WaTor(false),
        FallingSand(false),
        RPS(true),
        Langton(false);

        private final boolean isDynamic;

        SimType(boolean isDynamic) {
            this.isDynamic = isDynamic;
        }

        public boolean isDynamic() {
            return isDynamic;
        }
    }

}
