package gambler.cards.showdown.effects;

import gambler.cards.showdown.ShowdownEffect;

public class DoubleDamageEffect extends ShowdownEffect {
    private static final String ID = "DoubleDamage";
    private static final int PER_EFFECT = 1;

    private static final String[] TEXT = getText(ID);

    public DoubleDamageEffect() {
        super(ID, -1, 1, PER_EFFECT);
    }

    @Override
    public float modifyDamage(float dmg) {
        return dmg * (getFinalAmount() + 1);
    }

    @Override
    public String getDescription() {
        return TEXT[0] + (getFinalAmount() + 1) + TEXT[1];
    }
}