package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.powers.ACPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C1 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C1",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 1;
    private static final Suit SUIT = Suit.CLUB;

    public C1() {
        super(cardInfo, true, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new ACPower(p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new C1();
    }
}