package blackjack.domain.user;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.card.Shape;
import blackjack.domain.card.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("amazzi", 10000);
    }

    @DisplayName("Player 객체를 생성한다.")
    @Test
    public void createPlayer() {
        assertThat(player).isInstanceOf(Player.class);
    }

    @DisplayName("카드를 두장 분배받는다.")
    @Test
    public void distributeTwoCards() {
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.HEART, Value.NINE),
                new Card(Shape.DIAMOND, Value.JACK)
        )));
        Cards cards = player.cards;

        assertThat(cards.getCards()).hasSize(2);
    }

    @DisplayName("카드 합계가 21 이하인지 확인한다. - 카드를 더 받을 수 있다.")
    @Test
    public void isHitTrue() {
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.HEART, Value.TWO),
                new Card(Shape.DIAMOND, Value.JACK)
        )));

        assertThat(player.isHit()).isTrue();
    }

    @DisplayName("카드 합계가 21 초과인지 확인한다. - 카드를 더 받을 수 없다.")
    @Test
    public void isHitFalse() {
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.HEART, Value.TWO),
                new Card(Shape.DIAMOND, Value.JACK),
                new Card(Shape.CLOVER, Value.QUEEN)
        )));

        assertThat(player.isHit()).isFalse();
    }

    @DisplayName("카드 두장을 공개한다.")
    @Test
    void show() {
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING)
        )));

        assertThat(player.getCards()).hasSize(2);
    }

    @DisplayName("플레이어가 버스트인 경우를 확인한다. - 플레이어 패")
    @Test
    public void decidePlayerBust() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING),
                new Card(Shape.HEART, Value.QUEEN)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(-10000));
    }

    @DisplayName("딜러가 버스트이고 플레이어가 버스트가 아닌 경우를 확인한다. - 플레이어 승")
    @Test
    public void decideDealerBustPlayerNotBust() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING),
                new Card(Shape.HEART, Value.QUEEN)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(10000));
    }

    @DisplayName("딜러가 버스트이고 플레이어가 블랙잭인 경우를 확인한다. - 플레이어 승")
    @Test
    public void decideDealerBustPlayerBlackjack() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.EIGHT),
                new Card(Shape.CLOVER, Value.KING),
                new Card(Shape.HEART, Value.QUEEN)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.SPACE, Value.ACE),
                new Card(Shape.CLOVER, Value.KING)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(15000));
    }

    @DisplayName("딜러와 플레이어 모두 블랙잭인 경우를 확인한다. - 무승부")
    @Test
    public void decideDealerAndPlayerBlackjack() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.KING)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.KING)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(10000));
    }

    @DisplayName("딜러가 블랙잭인 경우를 확인한다. - 플레이어 패")
    @Test
    public void decideDealerBlackjack() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.KING)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.FIVE)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(-10000));
    }

    @DisplayName("플레이어가 블랙잭인 경우를 확인한다. - 플레이어 승")
    @Test
    public void decidePlayerBlackjack() {
        Dealer dealer = new Dealer();
        dealer.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.FIVE)
        )));
        player.distribute(new Cards(Arrays.asList(
                new Card(Shape.DIAMOND, Value.ACE),
                new Card(Shape.CLOVER, Value.KING)
        )));

        assertThat(player.decide(dealer)).isEqualTo(new Money(15000));
    }
}
