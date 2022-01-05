package gambler.actions.generic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import gambler.effects.DropGoldEffect;

public class ChangeGoldAction extends AbstractGameAction {
    private final Hitbox sourceBox;

    public ChangeGoldAction(int amount, Hitbox source)
    {
        this.amount = amount;
        this.actionType = ActionType.DAMAGE; //No cancel at end of combat

        this.sourceBox = source;
    }

    @Override
    public void update() {
        if (amount > 0)
        {
            AbstractDungeon.player.gainGold(amount);

            for(int i = 0; i < this.amount; ++i) {
                AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, sourceBox.cX, sourceBox.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
            }
        }
        else if (amount < 0)
        {
            amount = -amount;
            AbstractDungeon.player.loseGold(amount);

            for(int i = 0; i < this.amount; ++i) {
                AbstractDungeon.effectList.add(new DropGoldEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
            }
        }
        this.isDone = true;
    }
}
