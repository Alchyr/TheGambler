package gambler.cards.playing.spades;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.GoldEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class S9 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "S9",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 9;
    private static final Suit SUIT = Suit.SPADE;

    public S9() {
        super(cardInfo, true, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
        setExhaust(true);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            drawCards(1);
        }
        else {
            drawCards(this.magicNumber);
        }
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new GoldEffect(5);
    }

    @Override
    public AbstractCard makeCopy() {
        return new S9();
    }
}