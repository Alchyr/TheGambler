package gambler.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gambler.GamblerMod;
import gambler.actions.playingcards.PlayingCardAction;
import gambler.cards.PlayingCard;
import gambler.powers.interfaces.PostShowdownPower;

import static gambler.GamblerMod.makeID;

public class QSPower extends BasePower implements NonStackablePower, PostShowdownPower {
    public static final String NAME = "QSPower";
    public static final String POWER_ID = makeID(NAME);
    public static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = true;

    private final PlayingCard c;

    public QSPower(final AbstractCreature owner, int amount, PlayingCard c)
    {
        super(NAME, TYPE, TURN_BASED, owner, owner, amount);

        this.c = c;
        updateDescription();
    }

    public void updateDescription() {
        if (c == null) {
            this.description = "";
        }
        else {
            this.description = DESCRIPTIONS[0] + FontHelper.colorString(c.name, "y") + (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3]);
        }
    }

    @Override
    public void postShowdown() {
        AbstractCard card = c.makeStatEquivalentCopy();
        if (card instanceof PlayingCard) {
            c.current_x = c.target_x = AbstractDungeon.player.hb.cX;
            c.current_y = c.target_y = -500; //wheeeee
            addToTop(new PlayingCardAction((PlayingCard) c.makeStatEquivalentCopy()));
        }
        else {
            //what the Fuck
            GamblerMod.logger.error("What the Fuck (QSPower)");
        }
        addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    }
}