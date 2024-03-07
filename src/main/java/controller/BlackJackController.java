package controller;

import domain.Answer;
import domain.CardDeck;
import domain.Dealer;
import domain.Player;
import domain.Players;
import dto.DealerHandsDto;
import dto.PlayerDto;
import dto.PlayersDto;
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
        final Players players = Players.from(inputView.readPlayerNames());
        final CardDeck cardDeck = CardDeck.generate();
        final Dealer dealer = new Dealer(cardDeck);

        dealer.startDeal(players);
        outputView.printStartDeal(DealerHandsDto.from(dealer), PlayersDto.from(players));

        for (Player player : players.getNames()) {
            deal(player, dealer);
        }

        while (dealer.getTotalCardSum() <= 16) {  //TODO 테스트 코드 고민
            dealer.deal();
            outputView.printDealerCard();
        }

        outputView.printHandsResult(PlayersDto.from(dealer, players));
    }

    private void deal(final Player player, final Dealer dealer) {
        boolean changed = false;
        Answer answer = Answer.HIT;

        while (Answer.HIT.equals(answer)) {
            answer = Answer.from(inputView.readAnswer(player.getName()));
            dealer.deal(player, answer);

            if (handsChanged(changed, answer)) {
                outputView.printHands(PlayerDto.from(player));
            }
            changed = true;
        }
    }

    private boolean handsChanged(final boolean changed, final Answer answer) {
        return (Answer.STAY.equals(answer) && !changed) || Answer.HIT.equals(answer);
    }
}
