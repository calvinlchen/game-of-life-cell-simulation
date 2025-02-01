package cellsociety.model.util.constants;

public class StateEnum<S extends Enum<S>> {

    private S state;

    public StateEnum(S state){
        this.state = state;
    }
}
