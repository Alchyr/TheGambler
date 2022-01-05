package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D3 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D3",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    private static final int MAGIC = 2;

    private static final int VALUE = 3;
    private static final Suit SUIT = Suit.DIAMOND;

    public D3() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        drawCards(this.magicNumber);

        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new WeakEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new D3();
    }
}