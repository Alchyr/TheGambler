package gambler.cards.playing.hearts;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.PokerHand;
import gambler.actions.generic.XCostAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H5 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H5",
            -1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 6;

    private static final int VALUE = 5;
    private static final Suit SUIT = Suit.HEART;

    public H5() {
        super(cardInfo, true, VALUE, SUIT);

        setDamage(DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, (amt, params)->{
            amt += params[0];
            for (int i = 0; i < amt; ++i) {
                addToTop(new DamageAction(m, new DamageInfo(p, params[1], this.damageTypeForTurn), MathUtils.randomBoolean() ? AbstractGameAction.AttackEffect.BLUNT_LIGHT : AbstractGameAction.AttackEffect.BLUNT_HEAVY, i < amt - 1));
            }
            return true;
        }, upgraded ? 1 : 0, damage));
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return new VulnerableEffect(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new H5();
    }
}