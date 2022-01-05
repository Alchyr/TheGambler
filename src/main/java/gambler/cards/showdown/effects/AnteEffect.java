package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.cards.showdown.ShowdownEffect;
import gambler.powers.Ante;

public class AnteEffect extends ShowdownEffect {
    private static final String ID = "Ante";
    private static final int PER_EFFECT = 1;

    private static final String[] TEXT = getText(ID);

    public AnteEffect(int amount) {
        super(ID, 16, amount, PER_EFFECT);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active()) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(m, p, new Ante(m, p, getFinalAmount()), getFinalAmount(), true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}