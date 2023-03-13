package blackjack.view;

import blackjack.dto.AllPlayersStatusWithPointDto;
import blackjack.dto.ProfitDto;
import blackjack.dto.PlayerStatusDto;
import blackjack.dto.PlayerStatusWithPointDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OutputView {

    private static final String GIVE_START_CARD_COMPLETE_MESSAGE = "에게 2장을 나누었습니다.";
    private static final String DEALER_CAN_PICK_MESSAGE = "딜러는 16이하라 한장의 카드를 더 받았습니다.";
    private static final String DEALER_CAN_NOT_PICK_MESSAGE = "딜러는 17이상이라 한장의 카드를 더 받지 못했습니다.";
    private static final String BLACKJACK_MESSAGE = "는 블랙잭이어서 카드를 받지 않았습니다.";
    private static final String FINAL_PROFIT_HEADER_MESSAGE = "## 최종 수익";
    private static final String CARD = "카드";
    private static final String ITEM_DELIMITER = ", ";
    private static final String PLAYER_NAME_PARTITION = ": ";
    private static final String RESULT_PREFIX = " - 결과: ";

    public static void printErrorMessage(final Exception exception) {
        System.out.println(exception.getMessage());
    }

    public static void printStartStatus(
            final PlayerStatusDto dealerStatus, final List<PlayerStatusDto> challengersStatus) {
        System.out.println();
        printGivenMessage(dealerStatus, challengersStatus);
        printDealerOpenedStatus(dealerStatus);
        System.out.println();
        printChallengersStatus(challengersStatus);
    }

    private static void printGivenMessage(
            final PlayerStatusDto dealerStatus, final List<PlayerStatusDto> challengersStatus) {
        System.out.print(dealerStatus.getName() + "와 ");
        String challengerNames = String.join(ITEM_DELIMITER, toChallengerNames(challengersStatus));
        System.out.print(challengerNames);
        System.out.println(GIVE_START_CARD_COMPLETE_MESSAGE);
    }

    private static void printDealerOpenedStatus(final PlayerStatusDto dealerStatus) {
        printDealerName(dealerStatus.getName());
        printOneCardFromDealer(dealerStatus.getOneCard());
    }

    private static void printDealerName(final String name) {
        System.out.print(name);
        System.out.print(PLAYER_NAME_PARTITION);
    }

    private static void printOneCardFromDealer(final String card) {
        System.out.print(card);
    }

    public static void printChallengersStatus(final List<PlayerStatusDto> challengersStatus) {
        for (PlayerStatusDto challenger : challengersStatus) {
            printChallengerStatusInGame(challenger);
        }
    }

    public static void printChallengerStatusInGame(final PlayerStatusDto playerStatusDto) {
        printChallengerStatus(playerStatusDto);
        System.out.println();
    }

    public static void printChallengerStatus(final PlayerStatusDto challenger) {
        System.out.print(challenger.getName() + CARD);
        System.out.print(PLAYER_NAME_PARTITION);
        String cards = String.join(ITEM_DELIMITER, challenger.getCards());
        System.out.print(cards);
    }

    public static void printBlackjackMessage(final String name) {
        System.out.println();
        System.out.println(name + BLACKJACK_MESSAGE);
    }

    private static List<String> toChallengerNames(final List<PlayerStatusDto> challengersStatus) {
        return challengersStatus.stream()
                .map(PlayerStatusDto::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    public static void printDealerResult(final boolean dealerCanPick) {
        System.out.println();
        if (dealerCanPick) {
            System.out.println(DEALER_CAN_PICK_MESSAGE);
            System.out.println();
            return;
        }
        System.out.println(DEALER_CAN_NOT_PICK_MESSAGE);
        System.out.println();
    }

    public static void printEndStatus(final AllPlayersStatusWithPointDto allPlayersStatusWithPointDto) {
        printDealerFinalStatus(allPlayersStatusWithPointDto.getDealerDto());
        for (PlayerStatusWithPointDto challenger : allPlayersStatusWithPointDto.getChallengersDto()) {
            printChallengerStatus(challenger);
            printPoint(challenger.getPoint());
        }
    }

    private static void printDealerFinalStatus(final PlayerStatusWithPointDto dealerStatusDto) {
        printDealerName(dealerStatusDto.getName());
        printDealerCards(dealerStatusDto.getCards());
        printPoint(dealerStatusDto.getPoint());
    }

    private static void printDealerCards(final List<String> cards) {
        String joinedCards = String.join(ITEM_DELIMITER, cards);
        System.out.print(joinedCards);
    }

    private static void printPoint(final int point) {
        System.out.println(RESULT_PREFIX + point);
    }

    public static void printProfits(final ProfitDto profitDto) {
        System.out.println();
        System.out.println(FINAL_PROFIT_HEADER_MESSAGE);
        printDealerProfit(profitDto);
        printChallengersProfit(profitDto);
    }

    private static void printDealerProfit(final ProfitDto profitDto) {
        System.out.print(profitDto.getDealerName() + PLAYER_NAME_PARTITION);
        System.out.println(profitDto.getDealerProfit());
    }

    private static void printChallengersProfit(final ProfitDto profitDto) {
        Map<String, Integer> nameAndRanks = profitDto.getChallengersProfit();
        for (Map.Entry<String, Integer> nameAndRank : nameAndRanks.entrySet()) {
            System.out.println(nameAndRank.getKey() + PLAYER_NAME_PARTITION + nameAndRank.getValue());
        }
    }
}
