package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H8 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H8",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 8;
    private static final Suit SUIT = Suit.HEART;

    public H8() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        drawCards(this.magicNumber);
        applySelf(new EquilibriumPower(p, 1));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H8();
    }
}