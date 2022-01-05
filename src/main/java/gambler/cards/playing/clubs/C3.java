package gambler.cards.playing.clubs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.powers.Ante;
import gambler.powers.C3Power;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class C3 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "C3",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 2;

    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 3;
    private static final Suit SUIT = Suit.CLUB;

    public C3() {
        super(cardInfo, false, VALUE, SUIT);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        applySelf(new C3Power(p, this.magicNumber));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new VulnerableEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new C3();
    }
}