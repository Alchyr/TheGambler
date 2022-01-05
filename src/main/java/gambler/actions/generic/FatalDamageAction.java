package gambler.actions.generic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.function.Consumer;

public class FatalDamageAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;

    private final DamageInfo info;
    private boolean first = true;
    private final Consumer<AbstractCreature> onFatal;

    public FatalDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, Consumer<AbstractCreature> onFatal) {
        this.info = info;
        this.setValues(target, info);
        this.attackEffect = effect;

        this.onFatal = onFatal;

        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    public void update() {
        if (first && this.target != null && target instanceof AbstractMonster) {
            first = false;

            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));
            this.target.damage(this.info);

            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower(MinionPower.POWER_ID)) {
                onFatal.accept(this.target);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
