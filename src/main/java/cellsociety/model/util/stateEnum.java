package cellsociety.model.util;

public class stateEnum <S extends Enum<S> {

    private S state;

    public stateEnum(S state){
        this.state = state;
    }
}
