package domain.participant;

public class DealerHands extends Hands {

    public static final int INIT_HANDS_SIZE = 2;
    public static final int THRESHOLD = 16;
    public static final String NAME = "딜러";

    public DealerHands() {
        super(NAME);
    }

    public int countAddedHands() {
        return super.size() - INIT_HANDS_SIZE;
    }
}
