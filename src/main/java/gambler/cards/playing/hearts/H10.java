package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.powers.H10Power;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H10 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H10",
            2,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 2;
    private static final int VALUE = 10;
    private static final Suit SUIT = Suit.HEART;

    public H10() {
        super(cardInfo, true, VALUE, SUIT);

        setMagic(MAGIC);
        setInnate(false, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new H10Power(p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H10();
    }
}