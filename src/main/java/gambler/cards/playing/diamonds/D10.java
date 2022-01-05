package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.powers.D10Power;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D10 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D10",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int VALUE = 10;
    private static final Suit SUIT = Suit.DIAMOND;

    public D10() {
        super(cardInfo, false, VALUE, SUIT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new D10Power(p, 1));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(upgraded ? 10 : 5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D10();
    }
}