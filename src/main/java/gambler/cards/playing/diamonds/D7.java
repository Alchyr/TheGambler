package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.StrengthEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D7 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D7",
            1,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 1;
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 1;

    private static final int VALUE = 7;
    private static final Suit SUIT = Suit.DIAMOND;

    public D7() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);

        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i)
            damageAll(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new StrengthEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D7();
    }
}