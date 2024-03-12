package domain.participant;

import domain.Answer;
import domain.Result;
import domain.card.CardDeck;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Players {

    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 8;

    private final CardDeck cardDeck;
    private final List<Hands> hands;

    public Players(CardDeck cardDeck, List<Hands> hands) {
        this.cardDeck = cardDeck;
        this.hands = hands;
    }

    public static Players from(CardDeck cardDeck, DealerHands dealerHands, List<String> strings) {
        List<Hands> result = new ArrayList<>();

        result.add(dealerHands);
        result.addAll(mapToHands(strings));

        return new Players(cardDeck, result);
    }


    public void initHands() {
        for (int i = 0; i < 2; i++) {
            for (Hands hand : hands) {
                hand.add(cardDeck.pop());
            }
        }
    }

    public DealerHands getDealerHands() {
        return (DealerHands) hands.get(0);
    }

    public List<Hands> getPlayerHands() {
        return hands.subList(1, hands.size()); //todo
    }

    public void deal(final Hands hand, final Answer answer) {
        if (answer.isHit()) {
            hand.add(cardDeck.pop());
        }
    }

    public void deal() {
        Hands dealer = hands.get(0);
        while (dealer.sum() <= 16) {
            dealer.add(cardDeck.pop());
        }
    }

    public boolean isAllBust() {
        return hands.stream()
                .filter(hand -> !hand.isDealer())
                .allMatch(Hands::isBust);
    }

    public Map<Hands, Result> calculateResultByDealer() {
        Hands dealer = hands.get(0);
        final Map<Hands, Result> result = new LinkedHashMap<>();

        for (Hands hand : hands.subList(1, hands.size())) {
            result.put(hand, hand.calculateResultBy(dealer));
        }

        return result;
    }

    public List<Hands> getAll() {
        return hands;
    }

    public Map<Result, Integer> calculateResultByPlayers() {
        final Map<Result, Integer> result = new EnumMap<>(Result.class);

        for (Result value : calculateResultByDealer().values()) {
            Result reversed = value.reverse();
            result.put(reversed, result.getOrDefault(reversed, 0) + 1);
        }

        return result;
    }

    private static List<Hands> mapToHands(final List<String> names) {
        return names.stream()
                .map(String::trim)
                .map(Hands::createEmptyHands)
                .toList();
    }

    public void dealerDeal() {
        DealerHands dealerHands = getDealerHands();

        while (dealerHands.sum() <= 16) {
            dealerHands.add(cardDeck.pop());
        }
    }


    public int countAddedHands() {
        return getDealerHands().countAddedHands();
    }

    public String getDealerName() {
        return getDealerHands().getName();
    }
}
