package dto;

import domain.participant.Hands;
import domain.participant.Players;
import java.util.ArrayList;
import java.util.List;

public record ParticipantsDto(List<ParticipantDto> participants) {

    public static ParticipantsDto from(final Players players) {
        final List<ParticipantDto> result = new ArrayList<>();

        for (Hands player : players.getAll()) {
            result.add(ParticipantDto.from(player));
        }

        return new ParticipantsDto(result);
    }

    public List<String> getNames() {
        return participants.stream()
                .map(ParticipantDto::name)
                .toList();
    }

    public List<ParticipantDto> getParticipants() {
        return participants;
    }
}
