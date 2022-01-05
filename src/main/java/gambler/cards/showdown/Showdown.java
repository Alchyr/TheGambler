package gambler.cards.showdown;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gambler.PokerHand;
import gambler.actions.playingcards.ClearPokerHandAction;
import gambler.cards.BaseCard;
import gambler.util.CardInfo;

import java.util.Iterator;
import java.util.TreeSet;

import static gambler.GamblerMod.makeID;
import static gambler.util.TextureLoader.getCardTextureString;


//Showdown card is created based on current hand and can be played with at least 1 card in hand.
//Costs 1 normally, costs 0 if there are 5 cards in showdown.
//Then cards in hand go wherever they should go.
//

/*
VFX:
High card: none
One Pair: Normal attack animation


Full House: Random-ish rain of cards?

Straight: cards falling across the screen
Flush: flurry of whatever suit the flush is?
Straight Flush: Straight + some suit particles
Royal flush: Very Big cards that explode into the suit?
5 of a kind:
 */

public class Showdown extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Showdown",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.SPECIAL);

    public static final String ID = makeID(cardInfo.cardName);

    private static final String HIGH_CARD = getCardTextureString(cardInfo.cardName, CardType.SKILL);
    private static final String ONE_PAIR = getCardTextureString("OnePair", CardType.ATTACK);
    private static final String THREE_KIND = getCardTextureString("ThreeKind", CardType.ATTACK);
    private static final String TWO_PAIR = getCardTextureString("TwoPair", CardType.ATTACK);
    private static final String STRAIGHT = getCardTextureString("Straight", CardType.ATTACK);
    private static final String FLUSH = getCardTextureString("Flush", CardType.ATTACK);
    private static final String FULL_HOUSE = getCardTextureString("FullHouse", CardType.ATTACK);
    private static final String FOUR_KIND = getCardTextureString("FourKind", CardType.ATTACK);
    private static final String STRAIGHT_FLUSH = getCardTextureString("StraightFlush", CardType.ATTACK);
    private static final String ROYAL_FLUSH = getCardTextureString("RoyalFlush", CardType.ATTACK);
    private static final String FIVE_KIND = getCardTextureString("FiveKind", CardType.ATTACK);

    private static final int DAMAGE = 0;
    private static final int BLOCK = 0;

    private final TreeSet<ShowdownEffect> bonusEffects = new TreeSet<>();

    public Showdown() {
        super(cardInfo, false);

        setDamage(DAMAGE);
        setBlock(BLOCK);

        isMultiDamage = true;

        this.purgeOnUse = true;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (this.cost == 0 && this.costForTurn == 0 && PokerHand.cards.size() >= PokerHand.cardsForFree()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        float tmp;

        for (int i = 0; i < multiDamage.length; ++i) {
            tmp = multiDamage[i];
            for (ShowdownEffect e : bonusEffects) {
                tmp = e.modifyDamage(tmp);
            }
            multiDamage[i] = (int)tmp;
        }

        tmp = this.damage;
        for (ShowdownEffect e : bonusEffects) {
            tmp = e.modifyDamage(tmp);
        }
        this.damage = (int)tmp;
        this.isDamageModified = isDamageModified || (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);

        float tmp;

        for (int i = 0; i < multiDamage.length; ++i) {
            tmp = multiDamage[i];
            for (ShowdownEffect e : bonusEffects) {
                tmp = e.modifyDamage(tmp);
            }
            multiDamage[i] = (int)tmp;
        }

        tmp = this.damage;
        for (ShowdownEffect e : bonusEffects) {
            tmp = e.modifyDamage(tmp);
        }
        this.damage = (int)tmp;
        this.isDamageModified = isDamageModified || (this.damage != this.baseDamage);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator<ShowdownEffect> effectIterator = bonusEffects.iterator();
        ShowdownEffect e = null;

        if (effectIterator.hasNext())
            e = effectIterator.next();

        while (e != null && e.priority < 0) {
            e.applyEffect(p, this);

            e = effectIterator.hasNext() ? effectIterator.next() :  null;
        }

        if (baseDamage > 0) {
            damageAll(AbstractGameAction.AttackEffect.LIGHTNING);
        }

        while (e != null) {
            e.applyEffect(p, this);

            e = effectIterator.hasNext() ? effectIterator.next() :  null;
        }

        addToBot(new ClearPokerHandAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Showdown();
    }

    public void setValue(String s, int power) {
        this.name = s;
        this.baseDamage = power;
        applyPowers();
        updateCard();

        String newImage = HIGH_CARD;
        switch (power) {
            case 50:
                newImage = FIVE_KIND;
                break;
            case 45:
                newImage = ROYAL_FLUSH;
                break;
            case 40:
                newImage = STRAIGHT_FLUSH;
                break;
            case 35:
                newImage = FOUR_KIND;
                break;
            case 30:
                newImage = FULL_HOUSE;
                break;
            case 25:
                newImage = FLUSH;
                break;
            case 20:
                newImage = STRAIGHT;
                break;
            case 15:
                newImage = TWO_PAIR;
                break;
            case 10:
                newImage = THREE_KIND;
                break;
            case 0:
                break;
            default:
                newImage = ONE_PAIR;
                break;
        }

        if (!newImage.equals(textureImg)) {
            this.textureImg = newImage;
            loadCardImage(textureImg);
        }

        if (power >= 30) {
            setDisplayRarity(CardRarity.RARE);
        }
        else if (power >= 10) {
            setDisplayRarity(CardRarity.UNCOMMON);
        }
        else {
            setDisplayRarity(CardRarity.SPECIAL);
        }
    }

    public void clearBonuses() {
        bonusEffects.clear();
    }

    public void addBonus(ShowdownEffect e) {
        if (e == null)
            return;

        for (ShowdownEffect effect : bonusEffects) {
            if (effect.id.equals(e.id)) {
                effect.combine(e);
                return;
            }
        }
        bonusEffects.add(e);
    }

    public void updateCard() {
        String main = "";
        if (baseDamage > 0) {
            this.type = CardType.ATTACK;
            main = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        else {
            this.type = CardType.SKILL;
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;

        Iterator<ShowdownEffect> effectIterator = bonusEffects.iterator();
        ShowdownEffect e = null;

        if (effectIterator.hasNext())
            e = effectIterator.next();

        while (e != null && e.priority < 0) {
            if (first) {
                first = false;

                if (e.active())
                    sb.append(e.getDescription());
            }
            else if (e.active()) {
                sb.append(newLine());
                sb.append(e.getDescription());
            }

            e = effectIterator.hasNext() ? effectIterator.next() :  null;
        }

        if (!"".equals(main) && !first) {
            sb.append(newLine());
        }

        sb.append(main);

        if (!"".equals(main)) {
            first = false;
        }


        while (e != null) {
            if (first) {
                first = false;

                if (e.active())
                    sb.append(e.getDescription());
            }
            else if (e.active()) {
                sb.append(newLine());
                sb.append(e.getDescription());
            }

            e = effectIterator.hasNext() ? effectIterator.next() :  null;
        }

        this.rawDescription = sb.toString();

        if (this.rawDescription.equals("")) {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }

    private static String newLine() {
        return " NL ";
    }
}