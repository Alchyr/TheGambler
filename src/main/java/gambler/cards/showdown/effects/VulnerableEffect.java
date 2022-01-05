package gambler.cards.showdown.effects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import gambler.cards.showdown.ShowdownEffect;

public class VulnerableEffect extends ShowdownEffect {
    private static final String ID = "Vulnerable";
    private static final int PER_VULN = 2;

    private static final String[] TEXT = getText(ID);

    public VulnerableEffect(int amount) {
        super(ID, 15, amount, PER_VULN);
    }

    @Override
    public void applyEffect(AbstractPlayer p, AbstractCard c) {
        if (active()) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, getFinalAmount(), false), getFinalAmount(), true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public String getDescription() {
        return TEXT[0] + getFinalAmount() + TEXT[1];
    }
}
