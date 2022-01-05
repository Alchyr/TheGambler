package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DoubleDamageEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S1 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S1",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 1;

    private static final int VALUE = 1;
    private static final Suit SUIT = Suit.SPADE;

    public S1() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DoubleDamageEffect();
    }

    @Override
    public AbstractCard makeCopy() {
        return new S1();
    }
}