package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C4 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C4",
            1,
            CardType.SKILL,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int MAGIC = 6;
    private static final int UPG_MAGIC = 2;

    private static final int VALUE = 4;
    private static final Suit SUIT = Suit.CLUB;

    public C4() {
        super(cardInfo, false, VALUE, SUIT);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySingle(m, new Ante(m, p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new VulnerableEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C4();
    }
}