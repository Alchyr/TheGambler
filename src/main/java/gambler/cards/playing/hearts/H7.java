package gambler.cards.playing.hearts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.showdown.ShowdownEffect;
import gambler.cards.PlayingCard;
import gambler.powers.Ante;
import gambler.util.CardInfo;

import static gambler.GamblerMod.makeID;

public class H7 extends PlayingCard {
    private final static CardInfo cardInfo = new CardInfo(
            "H7",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON);

    public static final String ID = makeID(cardInfo.cardName);


    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 2;

    private static final int VALUE = 7;
    private static final Suit SUIT = Suit.HEART;

    public H7() {
        super(cardInfo, false, VALUE, SUIT);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo dmg = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m == null || m.isDeadOrEscaped()) {
                    this.isDone = true;
                } else {
                    this.tickDuration();
                    if (this.isDone) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.FIRE, false));
                        m.damage(dmg);
                        if (m.lastDamageTaken > 0) {
                            this.addToTop(new ApplyPowerAction(m, p, new Ante(m, p, m.lastDamageTaken), m.lastDamageTaken, true));
                        }

                        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                            AbstractDungeon.actionManager.clearPostCombatActions();
                        }
                    }
                }
            }
        });
        addToBot(new PlayingCardAction(this));
    }

    @Override
    public ShowdownEffect getShowdownEffect() {
        return null;
    }

    @Override
    public AbstractCard makeCopy() {
        return new H7();
    }
}