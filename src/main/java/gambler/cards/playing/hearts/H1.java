package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.powers.AHPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H1 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H1",
            -1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 0;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 1;
    private static final Suit SUIT = Suit.HEART;

    public H1() {
        super(cardInfo, true, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            amt += params[0];
            addToTop(new ApplyPowerAction(p, p, new AHPower(p, amt), amt, true));
            return true;
        }, upgraded ? 1 : 0));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new H1();
    }
}