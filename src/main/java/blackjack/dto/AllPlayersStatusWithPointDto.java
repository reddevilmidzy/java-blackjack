package blackjack.dto;

import blackjack.domain.player.Challenger;
import blackjack.domain.player.Players;

import java.util.List;
import java.util.stream.Collectors;

public class AllPlayersStatusWithPointDto {

    private final List<PlayerStatusWithPointDto> challengersDto;
    private final PlayerStatusWithPointDto dealerDto;

    private AllPlayersStatusWithPointDto(
            final List<PlayerStatusWithPointDto> challengersDto,
            final PlayerStatusWithPointDto dealerDto
    ) {
        this.challengersDto = challengersDto;
        this.dealerDto = dealerDto;
    }

    public static AllPlayersStatusWithPointDto of(final Players players) {
        List<PlayerStatusWithPointDto> challengersDto = toChallengersDto(players.getChallengers());
        PlayerStatusWithPointDto dealerDto = PlayerStatusWithPointDto.from(players.getDealer());
        return new AllPlayersStatusWithPointDto(challengersDto, dealerDto);
    }

    private static List<PlayerStatusWithPointDto> toChallengersDto(final List<Challenger> challengers) {
        return challengers.stream()
                .map(PlayerStatusWithPointDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<PlayerStatusWithPointDto> getChallengersDto() {
        return challengersDto;
    }

    public PlayerStatusWithPointDto getDealerDto() {
        return dealerDto;
    }
}
