package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.SegregationRule;
import cellsociety.model.util.CellStates.SegregationStates;
import java.util.List;

public class SegregationCell extends Cell<SegregationStates, SegregationCell> {

  private final SegregationRule myRule;
  private boolean selected;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   */
  public SegregationCell(SegregationStates state, SegregationRule rule) {
    super(state);
    myRule = rule;
  }

  /**
   * Set the selected state of the cell
   *
   * @param selected - true if cell is selected to be moved to by an agent, false otherwise
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Checks if the cell is already selected by another agent or if it is available to be selected
   *
   * @return true if the cell is selected, false otherwise
   */
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void calcNextState() {
    if (!selected) {
      setNextState(myRule.apply(this));
    }
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
    setSelected(false);
  }
}
