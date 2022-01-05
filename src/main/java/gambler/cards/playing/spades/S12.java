package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.EnergyEffect;
import gambler.powers.QSPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S12 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S12",
            -1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int VALUE = 12;
    private static final Suit SUIT = Suit.SPADE;

    public S12() {
        super(cardInfo, true, VALUE, SUIT);

        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            amt += params[0];

            addToTop(new ApplyPowerAction(p, p, new QSPower(p, amt, S12.this), amt));

            return true;
        }, upgraded ? 1 : 0));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new EnergyEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S12();
    }
}