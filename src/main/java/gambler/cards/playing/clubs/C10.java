package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.AnteEffect;
import gambler.powers.C10Power;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C10 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C10",
            2,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;
    private static final int VALUE = 10;
    private static final Suit SUIT = Suit.CLUB;

    public C10() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new C10Power(p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new AnteEffect(3);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C10();
    }
}