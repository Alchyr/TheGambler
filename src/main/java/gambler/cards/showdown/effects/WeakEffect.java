package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import gambler.cards.showdown.ShowdownEffect;

public class WeakEffect extends ShowdownEffect {
    private static final String ID = "Weak";
    private static final int PER_EFFECT = 2;

    private static final String[] TEXT = getText(ID);

    public WeakEffect(int amount) {
        super(ID, 10, amount, PER_EFFECT);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active()) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(m, p, new WeakPower(m, getFinalAmount(), false), getFinalAmount(), true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}
