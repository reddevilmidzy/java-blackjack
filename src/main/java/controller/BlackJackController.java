package controller;

import domain.Answer;
import domain.CardDeck;
import domain.Game;
import domain.participant.Dealer;
import domain.participant.Player;
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
        final Players players = Players.from(inputView.readNames());
        final CardDeck cardDeck = CardDeck.generate();
        final Dealer dealer = new Dealer(cardDeck);
        final Game game = new Game(dealer, players);

        dealer.startDeal(players);
        outputView.printStartDeal(DealerHandsDto.from(dealer), ParticipantsDto.of(players));

        for (Player player : players.getPlayers()) {
            deal(player, dealer);
        }

        if (players.isAllBust()) {
            outputView.printHandsResult(ParticipantsDto.of(dealer, players));
            outputView.printGameResult(game.getDealerResult(), game.getPlayersResult());
            return;
        }

        int turn = dealer.turn();
        for (int i = 0; i < turn; i++) {
            outputView.printDealerCard();
        }

        outputView.printHandsResult(ParticipantsDto.of(dealer, players));
        outputView.printGameResult(game.getDealerResult(), game.getPlayersResult());
    }

    private void deal(final Player player, final Dealer dealer) {
        boolean changed = false;
        Answer answer = Answer.HIT;

        while (Answer.HIT.equals(answer)) {
            answer = Answer.from(inputView.readAnswer(player.getName()));
            dealer.deal(player, answer);

            if (handsChanged(changed, answer)) {
                outputView.printHands(ParticipantDto.from(player));
            }
            if (player.isBust()) {
                outputView.printBustMessage();
                break;
            }
            changed = true;

            if (player.isBlackJack()) {
                outputView.printBlackJack();
                break;
            }
        }
    }

    private boolean handsChanged(final boolean changed, final Answer answer) {
        return (Answer.STAY.equals(answer) && !changed) || Answer.HIT.equals(answer);
    }
}
