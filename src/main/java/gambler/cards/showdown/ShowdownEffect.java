package gambler.cards.showdown;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static gambler.GamblerMod.makeID;

public abstract class ShowdownEffect implements Comparable<ShowdownEffect> {
    public final String id;
    protected int amount;
    protected int scale;
    protected int priority;

    protected static String[] getText(String ID) {
        return CardCrawlGame.languagePack.getUIString(makeID("Showdown"+ID)).TEXT;
    }

    public ShowdownEffect(String id, int priority, int amount, int scale) {
        this.id = id;
        this.priority = priority;

        this.amount = amount;
        this.scale = scale;
    }

    public float modifyDamage(float dmg) {
        return dmg;
    }
    public void applyEffect(AbstractPlayer p, AbstractCard c) {

    }

    public boolean active() {
        return amount >= scale;
    }

    protected int getFinalAmount() {
        return amount / scale;
    }

    public void combine(ShowdownEffect other) {
        this.amount += other.amount;
    }

    public abstract String getDescription();

    @Override
    public int compareTo(ShowdownEffect o) {
        return this.priority - o.priority;
    }

    protected static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }
}
