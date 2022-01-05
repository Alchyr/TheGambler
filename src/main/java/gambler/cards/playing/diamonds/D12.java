package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.EnergyEffect;
import gambler.powers.QDPower;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D12 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D12",
            -1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 12;
    private static final Suit SUIT = Suit.DIAMOND;

    public D12() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
        this.tags.add(CardTags.HEALING);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            addToTop(new ApplyPowerAction(p, p, new QDPower(p, params[0], amt), amt, true));
            return true;
        }, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new EnergyEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D12();
    }
}