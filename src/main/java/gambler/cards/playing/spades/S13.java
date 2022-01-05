package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.DrawEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S13 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S13",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 13;
    private static final Suit SUIT = Suit.SPADE;

    public S13() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new DrawEffect(this.magicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S13();
    }
}