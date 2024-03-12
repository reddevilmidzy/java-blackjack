package controller;

import domain.Answer;
import domain.card.CardDeck;
import domain.participant.DealerHands;
import domain.participant.Hands;
import domain.participant.Players;
import dto.DealerHandsDto;
import dto.ParticipantDto;
import dto.ParticipantsDto;
import view.InputView;
import view.OutputView;

public class BlackJackController {

    private final InputView inputView;
    private final OutputView outputView;

    public BlackJackController(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        final Players players = Players.from(CardDeck.generate(), new DealerHands(), inputView.readNames());

        initHands(players);
        deal(players);
        dealToDealerIfPossible(players);

        printFinalResult(players);
    }

    private void initHands(final Players players) {
        players.initHands();
        outputView.printInitHands(DealerHandsDto.from(players.getDealerHands()), ParticipantsDto.from(players));
    }


    private void dealToDealerIfPossible(Players players) {
        if (players.isAllBust()) {

            return;
        }

        players.dealerDeal();
        printDealerHandsChangedMessage(players.countAddedHands(), players.getDealerName());
    }

    private void printFinalResult(final Players players) {
        outputView.printHandsResult(ParticipantsDto.from(players));
        outputView.printGameResult(players.calculateResultByPlayers(), players.calculateResultByDealer());
    }

    private void printDealerHandsChangedMessage(final int turn, final String name) {
        for (int i = 0; i < turn; i++) {
            outputView.printDealerHandsChangedMessage(name);
        }
    }

    public void deal(final Players players) {
        for (Hands hand : players.getPlayerHands()) {
            boolean handsChanged = false;
            boolean turnEnded = false;

            while (!turnEnded) {
                final Answer answer = inputView.readAnswer(hand.getName());

                players.deal(hand, answer);

                printHandsIfRequired(hand, handsChanged, answer);

                handsChanged = true;
                turnEnded = isTurnEnded(hand, answer);
            }


        }
    }

    private void printHandsIfRequired(final Hands player, final boolean handsChanged, final Answer answer) {
        if (shouldShowHands(handsChanged, answer)) {
            outputView.printHands(ParticipantDto.from(player));
        }
    }

    private boolean isTurnEnded(final Hands player, final Answer answer) {
        if (player.isBust()) {
            outputView.printBust();
            return true;
        }

        if (player.isBlackJack()) {
            outputView.printBlackJack();
            return true;
        }

        return !answer.isHit();
    }

    private boolean shouldShowHands(final boolean handsChanged, final Answer answer) {
        return answer.isHit() || !handsChanged;
    }
}
