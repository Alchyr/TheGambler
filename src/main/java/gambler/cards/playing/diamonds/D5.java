package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D5 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D5",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    private static final int MAGIC = 8;

    private static final int VALUE = 5;
    private static final Suit SUIT = Suit.DIAMOND;

    public D5() {
        super(cardInfo, false, VALUE, SUIT);

        setCostUpgrade(UPG_COST);
        setMagic(MAGIC);
        this.tags.add(CardTags.HEALING);

        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGoldAction(this.magicNumber, this.hb));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new WeakEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D5();
    }
}