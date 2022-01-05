package gambler.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import gambler.cards.BaseCard;

public class GoldCost extends DynamicVariable {
    private static final String KEY = "GMB_GC";
    @Override
    public final String key()
    {
        return KEY;
    }

    @Override
    public boolean isModified(AbstractCard c)
    {
        if (c instanceof BaseCard)
        {
            return ((BaseCard) c).isGoldCostModified();
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard c, boolean v) {
        if (c instanceof BaseCard)
        {
            ((BaseCard) c).goldCostModified = v;
        }
    }

    @Override
    public int value(AbstractCard c) {
        if (c instanceof BaseCard)
        {
            return ((BaseCard) c).goldCost;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard c) {
        if (c instanceof BaseCard)
        {
            return ((BaseCard) c).baseGoldCost;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard c) {
        if (c instanceof BaseCard)
        {
            return ((BaseCard) c).upgradeGoldCost;
        }
        return false;
    }

    public Color getIncreasedValueColor() {
        return Settings.RED_TEXT_COLOR;
    }

    public Color getDecreasedValueColor() {
        return Settings.GREEN_TEXT_COLOR;
    }
}