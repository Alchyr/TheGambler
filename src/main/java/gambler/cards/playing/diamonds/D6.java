package gambler.cards.playing.diamonds;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.GamblerMod;
import gambler.actions.generic.ChangeGoldAction;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.effects.VulnerableEffect;
import gambler.cards.showdown.effects.WeakEffect;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class D6 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "D6",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 6;
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    private static final int GOLD = 6;

    private static final int VALUE = 6;
    private static final Suit SUIT = Suit.DIAMOND;

    public D6() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        setGoldCost(GOLD);
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (!GamblerMod.canAfford(this.goldCost)) {
                canUse = false;
                this.cantUseMessage = cantAffordText;
            }

            return canUse;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGoldAction(-this.goldCost, this.hb));
        for (int i = 0; i < this.magicNumber; ++i)
            damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect[] getShowdownEffects() {
        return new ShowdownEffect[] {
                new WeakEffect(1),
                new VulnerableEffect(1)
        };
    }

    @Override
    public AbstractCard makeCopy() {
        return new D6();
    }
}