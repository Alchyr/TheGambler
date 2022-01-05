package gambler.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomCard;
import basemod.interfaces.CloneablePowerInterface;
import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import gambler.GamblerMod;
import gambler.powers.interfaces.CostChangePower;
import gambler.util.CardInfo;

import java.util.HashMap;
import java.util.function.Consumer;

import static gambler.GamblerMod.makeID;
import static gambler.character.Gambler.Enums.GAMBLER_CARD_COLOR;
import static gambler.util.TextureLoader.getCardTextureString;


public abstract class BaseCard extends CustomCard {
    public static final CardColor COLOR = GAMBLER_CARD_COLOR;

    protected CardStrings cardStrings;

    protected boolean upgradesDescription;

    protected int baseCost;

    public int baseGoldCost;
    public int goldCost;
    public boolean goldCostModified;

    protected boolean upgradeCost;
    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    public boolean upgradeGoldCost;

    protected int costUpgrade;
    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected int goldCostUpgrade;

    protected boolean baseExhaust;
    protected boolean upgExhaust;
    protected boolean baseInnate;
    protected boolean upgInnate;

    public BaseCard(CardInfo cardInfo, boolean upgradesDescription)
    {
        this(cardInfo, upgradesDescription, COLOR);
    }

    public BaseCard(CardInfo cardInfo, boolean upgradesDescription, CardColor color)
    {
        this(cardInfo.cardName, cardInfo.cardCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, upgradesDescription, color);
    }

    public BaseCard(String cardName, int cost, CardType cardType, CardTarget target, CardRarity rarity, boolean upgradesDescription, CardColor color)
    {
        super(makeID(cardName), "", getCardTextureString(cardName, cardType), cost, "", cardType, color, rarity, target);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;

        this.baseCost = cost;

        this.baseGoldCost = this.goldCost = 0;

        this.upgradesDescription = upgradesDescription;

        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.upgradeGoldCost = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;

        this.goldCostUpgrade = 0;

        initializeCard();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (this.baseGoldCost != 0) {
            this.goldCost = this.baseGoldCost;

            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof CostChangePower) {
                    this.goldCost = ((CostChangePower) p).modifyCost(this.goldCost);
                }
            }

            this.goldCostModified = this.goldCost != this.baseGoldCost;
        }
    }

    public static CardTarget combineTargets(CardTarget a, CardTarget b)
    {
        return resultMap.getOrDefault(targetMap.getOrDefault(a, 8) | targetMap.getOrDefault(b, 8), a);
    }

    private static final HashMap<CardTarget, Integer> targetMap;
    private static final HashMap<Integer, CardTarget> resultMap;

    static {
        targetMap = new HashMap<>();
        targetMap.put(CardTarget.NONE, 0); //0
        targetMap.put(CardTarget.SELF, 1); //1
        targetMap.put(CardTarget.ENEMY, 2); //10
        targetMap.put(CardTarget.SELF_AND_ENEMY, 3); //11
        targetMap.put(CardTarget.ALL_ENEMY, 6); //110
        targetMap.put(CardTarget.ALL, 7); //111

        resultMap = new HashMap<>();
        resultMap.put(0, CardTarget.NONE);
        resultMap.put(1, CardTarget.SELF);
        resultMap.put(2, CardTarget.ENEMY);
        resultMap.put(3, CardTarget.SELF_AND_ENEMY);
        resultMap.put(6, CardTarget.ALL_ENEMY);
        resultMap.put(7, CardTarget.ALL);
    }

    //Methods meant for constructor use
    protected void setDamage(int damage)
    {
        this.setDamage(damage, 0);
    }
    protected void setBlock(int block)
    {
        this.setBlock(block, 0);
    }
    protected void setMagic(int magic)
    {
        this.setMagic(magic, 0);
    }
    protected void setGoldCost(int goldCost) { this.setGoldCost(goldCost, 0); }
    protected void setCostUpgrade(int costUpgrade)
    {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }
    protected void setExhaust(boolean exhaust) { this.setExhaust(exhaust, exhaust); }
    protected void setInnate(boolean innate) {this.setInnate(innate, innate); }
    protected void setDamage(int damage, int damageUpgrade)
    {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0)
        {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }
    protected void setBlock(int block, int blockUpgrade)
    {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0)
        {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }
    protected void setMagic(int magic, int magicUpgrade)
    {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0)
        {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }
    protected void setGoldCost(int goldCost, int goldCostUpgrade) {
        this.baseGoldCost = this.goldCost = goldCost;
        if (goldCostUpgrade != 0) {
            this.upgradeGoldCost = true;
            this.goldCostUpgrade = goldCostUpgrade;
        }
    }
    protected void setExhaust(boolean baseExhaust, boolean upgExhaust)
    {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }
    protected void setInnate(boolean baseInnate, boolean upgInnate)
    {
        this.baseInnate = baseInnate;
        this.isInnate = baseInnate;
        this.upgInnate = upgInnate;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof BaseCard)
        {
            card.rawDescription = this.rawDescription;
            ((BaseCard) card).upgradesDescription = this.upgradesDescription;

            ((BaseCard) card).baseCost = this.baseCost;

            ((BaseCard) card).baseGoldCost = this.baseGoldCost;
            ((BaseCard) card).goldCost = this.goldCost;
            ((BaseCard) card).goldCostModified = this.goldCostModified;

            ((BaseCard) card).upgradeCost = this.upgradeCost;
            ((BaseCard) card).upgradeDamage = this.upgradeDamage;
            ((BaseCard) card).upgradeBlock = this.upgradeBlock;
            ((BaseCard) card).upgradeMagic = this.upgradeMagic;
            ((BaseCard) card).upgradeGoldCost = this.upgradeGoldCost;

            ((BaseCard) card).costUpgrade = this.costUpgrade;
            ((BaseCard) card).damageUpgrade = this.damageUpgrade;
            ((BaseCard) card).blockUpgrade = this.blockUpgrade;
            ((BaseCard) card).magicUpgrade = this.magicUpgrade;
            ((BaseCard) card).goldCostUpgrade = this.goldCostUpgrade;

            ((BaseCard) card).baseExhaust = this.baseExhaust;
            ((BaseCard) card).upgExhaust = this.upgExhaust;
            ((BaseCard) card).baseInnate = this.baseInnate;
            ((BaseCard) card).upgInnate = this.upgInnate;
        }

        return card;
    }

    @Override
    public void upgrade()
    {
        if (!upgraded)
        {
            this.upgradeName();

            if (this.upgradesDescription)
            {
                if (cardStrings.UPGRADE_DESCRIPTION == null)
                {
                    GamblerMod.logger.error("Card " + cardID + " upgrades description and has null upgrade description.");
                }
                else
                {
                    this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
                }
            }

            if (upgradeCost)
            {
                int diff = this.baseCost - this.cost; //positive if cost is reduced

                this.upgradeBaseCost(costUpgrade);
                this.cost -= diff;
                this.costForTurn -= diff;
                if (cost < 0)
                    cost = 0;

                if (costForTurn < 0)
                    costForTurn = 0;
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            if (upgradeGoldCost)
                this.upgradeGoldCost(goldCostUpgrade);

            if (baseExhaust ^ upgExhaust) //different
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate) //different
                this.isInnate = upgInnate;


            this.initializeDescription();
        }
    }

    public void initializeCard()
    {
        //FontHelper.cardDescFont_N.getData().setScale(1.0f);
        this.initializeTitle();
        this.initializeDescription();
    }


    protected void upgradeGoldCost(int amount) {
        this.baseGoldCost += amount;
        if (this.baseGoldCost < 1)
            this.baseGoldCost = 1;
        this.goldCost = this.baseGoldCost;
    }
    public boolean isGoldCostModified()
    {
        return goldCostModified;
    }

    //utility methods
    protected void gainEnergy(int amount) {
        addToBot(new GainEnergyAction(amount));
    }
    protected void drawCards(int amount) {
        addToBot(new DrawCardAction(amount));
    }
    protected void drawCards(int amount, AbstractGameAction followup) {
        addToBot(new DrawCardAction(amount, followup));
    }
    protected void block() {
        block(this.block);
    }
    protected void block(int amt) {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amt));
    }
    protected AbstractGameAction getBlockAction(int amount) {
        return new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount);
    }
    protected void giveBlock(AbstractCreature target, int amount) {
        addToBot(new GainBlockAction(target, AbstractDungeon.player, amount));
    }
    protected void damageSingle(AbstractCreature target, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), effect));
    }
    protected void damageSingle(AbstractCreature target, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), effect, isFast));
    }
    protected void damageSingle(AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, this.damageTypeForTurn), effect));
    }
    protected void damageSingle(AbstractCreature target, int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, type), effect));
    }
    protected AbstractGameAction getDamageSingle(AbstractCreature target, int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        return new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, type), effect);
    }
    protected void damageRandom(AbstractGameAction.AttackEffect effect)
    {
        addToBot(new AttackDamageRandomEnemyAction(this, effect));
    }
    protected void damageAll(AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect));
    }
    protected void damageAll(AbstractGameAction.AttackEffect effect, boolean fast)
    {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect, fast));
    }
    protected void damageAll(int amount, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, amount, this.damageTypeForTurn, effect));
    }
    protected void damageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        if (type != DamageInfo.DamageType.NORMAL)
        {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(amount, true), type, effect));
        }
        else
        {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, amount, type, effect));
        }
    }
    protected AbstractGameAction getDamageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        return getDamageAll(amount, type, effect, false);
    }
    protected AbstractGameAction getDamageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        if (type != DamageInfo.DamageType.NORMAL)
        {
            return new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(amount, true), type, effect, isFast);
        }
        else
        {
            return new DamageAllEnemiesAction(AbstractDungeon.player, amount, type, effect);
        }
    }


    protected void applySingle(AbstractCreature c, AbstractPower power)
    {
        applySingle(c, power, false);
    }
    protected void applySingle(AbstractCreature c, AbstractPower power, boolean isFast)
    {
        addToBot(new ApplyPowerAction(c, AbstractDungeon.player, power, power.amount, isFast));
    }

    protected void applySelf(AbstractPower power)
    {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power, power.amount));
    }

    protected<T extends AbstractPower & CloneablePowerInterface> void applyAll(T power, AbstractGameAction.AttackEffect effect)
    {
        applyAll(power, false, effect);
    }
    protected<T extends AbstractPower & CloneablePowerInterface> void applyAll(T power, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            AbstractPower toApply = power.makeCopy();

            toApply.owner = m;
            toApply.updateDescription();

            addToBot(new ApplyPowerAction(m, AbstractDungeon.player, toApply, toApply.amount, isFast, effect));
        }
    }

    protected WeakPower getWeak(AbstractCreature c, int amount)
    {
        return new WeakPower(c, amount, false);
    }
    protected VulnerablePower getVuln(AbstractCreature c, int amount)
    {
        return new VulnerablePower(c, amount, false);
    }

    public static void forEachCard(Consumer<AbstractCard> method) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            method.accept(c);
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            method.accept(c);
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            method.accept(c);
        }
    }

    protected int applyXModifiers(int x) {
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c instanceof XCostModifier)
                if (((XCostModifier) c).xCostModifierActive(this))
                    x += ((XCostModifier) c).modifyX(this);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            if (c instanceof XCostModifier)
                if (((XCostModifier) c).xCostModifierActive(this))
                    x += ((XCostModifier) c).modifyX(this);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group)
            if (c instanceof XCostModifier)
                if (((XCostModifier) c).xCostModifierActive(this))
                    x += ((XCostModifier) c).modifyX(this);
        for (AbstractPower p : AbstractDungeon.player.powers)
            if (p instanceof XCostModifier)
                if (((XCostModifier) p).xCostModifierActive(this))
                    x += ((XCostModifier) p).modifyX(this);
        for (AbstractRelic r : AbstractDungeon.player.relics)
            if (r instanceof XCostModifier) {
                if (((XCostModifier) r).xCostModifierActive(this))
                    x += ((XCostModifier) r).modifyX(this);
            }
            else if (r.relicId.equals(ChemicalX.ID)) {
                x += 2;
            }

        for (AbstractCardModifier c : CardModifierPatches.CardModifierFields.cardModifiers.get(this))
            if (c instanceof XCostModifier)
                if (((XCostModifier) c).xCostModifierActive(this))
                    x += ((XCostModifier) c).modifyX(this);
        return x;
    }
}