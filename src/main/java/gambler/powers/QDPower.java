package gambler.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.actions.generic.ChangeGoldAction;

import static gambler.GamblerMod.makeID;

public class QDPower extends BasePower implements NonStackablePower {
    public static final String NAME = "QDPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = true;

    public int gold;
    protected Color greenColor2;

    public QDPower(final AbstractCreature owner, int duration, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, duration);

        this.gold = amount;
        this.greenColor2 = Color.GREEN.cpy();

        updateDescription();
    }

    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new ChangeGoldAction(this.gold, owner.hb));
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, this, 1));
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);

        this.greenColor2.a = c.a;
        c = this.greenColor2;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.gold), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + gold + (amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1] + amount + DESCRIPTIONS[3]);
    }
}