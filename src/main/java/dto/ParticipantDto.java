package dto;

import domain.participant.Hands;
import java.util.List;
import view.message.RankView;
import view.message.ShapeView;

public record ParticipantDto(String name, List<String> cards, int totalSum) {

    public static ParticipantDto from(final Hands hands) {
        final List<String> cards = hands.getCards().stream()
                .map(card -> RankView.from(card.getRank()) + ShapeView.from(card.getShape()))
                .toList();

        return new ParticipantDto(hands.getName(), cards, hands.sum());
    }
}
