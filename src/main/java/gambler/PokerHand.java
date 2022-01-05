package gambler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import gambler.cards.PlayingCard;
import gambler.cards.showdown.Showdown;
import gambler.cards.showdown.ShowdownEffect;
import gambler.patches.showdown.HoverShowdown;
import gambler.patches.showdown.UpdateAndTrackShowdownCard;
import gambler.powers.ACPower;

import java.util.*;
import java.util.stream.Collectors;

import static gambler.GamblerMod.makeID;
import static gambler.util.MathHelper.dist;


public class PokerHand {
    public static boolean enabled = false;

    public static float x, y, showdownY;

    public static int maxSize = 5;
    public static List<PlayingCard> cards = new ArrayList<>();
    public static Showdown showdown = null;

    public static PlayingCard hoveredCard = null;

    //public static final SealIntent[] sealIntents = { new SealIntent(), new SealIntent(), new SealIntent(), new SealIntent(), new SealIntent() };

    private static boolean recalculate = false;


    // Consts
    public static final float SMALL_SCALE = 0.2f;

    private static final float HOVER_SCALE = 0.66f;

    //all positions are relative to center of player hitbox.
    private static final float VERTICAL_SPACING = 120.0f * Settings.scale;
    private static final float CARD_SPACING = 80.0f * Settings.scale;
    private static final float HOVER_BONUS_SPACING = 40.0f * Settings.scale;
    private static final float HOVER_UNCENTERING_Y = 80.0f * Settings.scale;
    private static final float SHOWDOWN_Y_SHIFT = 80.0f * Settings.scale;

    //Showdown stuff
    private static final int HIGH_CARD = 0;
    private static final int ONE_PAIR = 1;
    private static final int THREE_KIND = 2; //three of a kind is too easy, so it's placed under two pair now.
    private static final int TWO_PAIR = 3;
    private static final int STRAIGHT = 4;
    private static final int FLUSH = 5;
    private static final int FULL_HOUSE = 6;
    private static final int FOUR_KIND = 7;
    private static final int STRAIGHT_FLUSH = 8;
    private static final int ROYAL_FLUSH = 9;
    private static final int FIVE_KIND = 10;
    /*private static final int[] SHOWDOWN_VALUES = new int[] { just multiply by 5
            0,
            5,
            10,
            15,
            20,
            25,
            30,
            35,
            40,
            45,
            50
    };*/
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("Showdown")).TEXT;



    public static void reset()
    {
        hoveredCard = null;
        recalculate = false;
        showdown = null;
        cards.clear();
        enabled = false;
        x = 0;
        y = 0;
        showdownY = 0;
    }

    public static boolean addCard(PlayingCard c)
    {
        enabled = true;

        if (cards.size() < maxSize) {
            setSmallScale(c);
            c.stopGlowing();
            cards.add(c);
            checkHand();
            updatePositions();
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean addCardAtIndex(PlayingCard c, int index)
    {
        enabled = true;

        if (cards.size() < maxSize) {
            setSmallScale(c);
            c.stopGlowing();
            cards.add(index, c);
            checkHand();
            updatePositions();
            return true;
        }
        else
        {
            return false;
        }
    }
    public static void setSmallScale(PlayingCard c) {
        c.targetDrawScale = SMALL_SCALE;
    }

    public static void setPositionForCombat()
    {
        //sets the base position so that it doesn't wiggle around when player moves around for some things like the attack animation
        x = AbstractDungeon.player.hb.cX;
        y = AbstractDungeon.player.hb.y + AbstractDungeon.player.hb.height + VERTICAL_SPACING / 2.0f;
        showdownY = y + VERTICAL_SPACING * 1.4f;

        UpdateAndTrackShowdownCard.x = x;
        UpdateAndTrackShowdownCard.y = showdownY;
        HoverShowdown.hoveredX = x;
        HoverShowdown.hoveredY = showdownY - SHOWDOWN_Y_SHIFT;
    }

    public static void updatePositions()
    {
        float startX = x - ((cards.size() - 1) / 2.0f) * CARD_SPACING - (hoveredCard != null ? HOVER_BONUS_SPACING : 0);
        for (int i = 0; i < cards.size(); ++i) {
            if (cards.get(i) == hoveredCard)
                startX += HOVER_BONUS_SPACING;

            cards.get(i).target_x = startX + i * CARD_SPACING;
            cards.get(i).target_y = y;

            if (cards.get(i) == hoveredCard)
                startX += HOVER_BONUS_SPACING;
        }
    }

    public static void render(SpriteBatch sb)
    {
        if (AbstractDungeon.player == null || !enabled || (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT))
            return;

        if (recalculate)
            checkHand();

        if (hoveredCard != null)
        {
            hoveredCard.updateHoverLogic();
            if (AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard != null)
            {
                hoveredCard.unhover();
            }
            hoveredCard.update();

            if (!hoveredCard.publicHovered)
            {
                hoveredCard.targetDrawScale = SMALL_SCALE;
                hoveredCard = null;
                updatePositions();
            }
        }


        for (PlayingCard c : cards) {
            if (!AbstractDungeon.isScreenUp && hoveredCard == null && AbstractDungeon.player.hoveredCard == null && !AbstractDungeon.player.isDraggingCard)
            {
                boolean isHovered = c.publicHovered;
                c.updateHoverLogic();
                if (c.publicHovered)
                {
                    c.drawScale = (SMALL_SCALE + HOVER_SCALE) / 2;
                    c.targetDrawScale = HOVER_SCALE;

                    if (!isHovered) //just hovered
                    {
                        hoveredCard = c;
                        hoveredCard.target_y -= HOVER_UNCENTERING_Y;
                        updatePositions();
                        //Update hovered card.
                        c.update();
                        continue; //skip render, hovered card will be updated and rendered later so that it appears on top
                    }
                }
            }

            c.update();
            c.render(sb);
        }

        if (showdown != null && !showdown.equals(AbstractDungeon.player.hoveredCard))
        {
            showdown.render(sb);
        }
    }

    public static void renderHovered(SpriteBatch sb)
    {
        if (hoveredCard != null)
        {
            hoveredCard.render(sb);
        }
    }

    public static void checkHand()
    {
        cards.sort(null);

        if (!cards.isEmpty())
        {
            recalculate = false;

            //Initial hand check
            HashMap<Integer, Integer> cardCounts = new HashMap<>();
            HashMap<PlayingCard.Suit, Integer> suitCounts = new HashMap<>();
            List<PlayingCard> jokers = new ArrayList<>();
            List<PlayingCard> aces = new ArrayList<>();

            boolean isStraight = false;
            boolean isFlush = false;
            boolean isRoyal = false; //sadly a royal straight alone means nothing

            List<PlayingCard> straight = new ArrayList<>();
            int jokerIndex = 0;

                for (PlayingCard c : cards) { //Cards should be sorted from smallest to largest.
                if (c.suit == PlayingCard.Suit.ANY) {
                    jokers.add(c);
                }
                else {
                    if (c.value == 1) {
                        aces.add(c);
                    }

                    cardCounts.put(c.value, cardCounts.getOrDefault(c.value, 0) + 1);
                    suitCounts.put(c.suit, suitCounts.getOrDefault(c.suit, 0) + 1);

                    if (straight.isEmpty()) {
                        straight.add(c);
                    }
                    else {
                        int gap = c.value - straight.get(straight.size() - 1).value;

                        while (gap > 1 && jokerIndex < jokers.size()) {
                            straight.add(jokers.get(jokerIndex));
                            --gap;
                            ++jokerIndex;
                        }

                        if (gap == 1) {
                            straight.add(c);
                        }
                        else if (gap > 1) {
                            straight.clear();
                            straight.add(c);
                            jokerIndex = 0;
                        }
                    }
                }
            }

            if (jokers.size() >= 5) {
                //well congratulations, that's a 5 of a kind
                setShowdown(FIVE_KIND);
            }

            if (!aces.isEmpty() && !straight.isEmpty()) { //Check for royal straight
                int gap = 14 - straight.get(straight.size() - 1).value;

                while (gap > 1 && jokerIndex < jokers.size()) {
                    straight.add(jokers.get(jokerIndex));
                    --gap;
                    ++jokerIndex;
                }

                if (gap == 1) {
                    straight.add(aces.get(0));
                    isRoyal = true;
                }
            }

            while (straight.size() < 5 && jokerIndex < jokers.size()) { //add jokers onto the starts of straights
                straight.add(0, jokers.get(jokerIndex));
                ++jokerIndex;
            }

            if (straight.size() >= 5) {
                isStraight = true;
            }

            //Check for flush
            for (int count : suitCounts.values()) {
                if (count + jokers.size() >= 5) {
                    isFlush = true;
                    break;
                }
            }

            int fullHouseDistance = 5 - jokers.size();
            int twoPairDistance = 4 - jokers.size();
            int fullHouseOptions = 2;
            int highCount = jokers.size();

            for (int count : cardCounts.values().stream().sorted((x, y) -> Integer.compare(y, x)).collect(Collectors.toList())) {
                if (count + jokers.size() > highCount) {
                    highCount = count + jokers.size();
                }

                if (highCount > 3) { //other hands are irrelevant if you have a 4 or 5 of a kind
                    break;
                }

                if (fullHouseOptions > 0) {
                    --fullHouseOptions;
                    fullHouseDistance -= count;
                    twoPairDistance -= Math.min(count, 2);
                }
            }

            //if fullHouseDistance is <= 0, full house is an option
            if (highCount == 5) {
                //5 of a kind is the highest, done checking.
                setShowdown(FIVE_KIND);
            }
            else if (isStraight && isFlush) {
                //Sort hand as a straight
                List<PlayingCard> straightSort = new ArrayList<>(straight);
                cards.removeAll(straight);
                straightSort.addAll(cards);
                cards = straightSort;

                if (isRoyal) {
                    setShowdown(ROYAL_FLUSH);
                }
                else {
                    setShowdown(STRAIGHT_FLUSH);
                }
            }
            else if (highCount == 4) {
                setShowdown(FOUR_KIND);
            }
            else if (fullHouseDistance <= 0) {
                setShowdown(FULL_HOUSE);
            }
            else if (isFlush) {
                setShowdown(FLUSH);
            }
            else if (isStraight) {
                setShowdown(STRAIGHT);
            }
            else if (twoPairDistance <= 0) {
                setShowdown(TWO_PAIR);
            }
            else {
                switch (highCount) {
                    case 3:
                        setShowdown(THREE_KIND);
                        break;
                    case 2:
                        setShowdown(ONE_PAIR);
                        break;
                    default: //nice high card
                        setShowdown(HIGH_CARD);
                        break;
                }
            }

            showdown.clearBonuses();
            for (PlayingCard card : cards) {
                for (ShowdownEffect e : card.getShowdownEffects())
                    showdown.addBonus(e);
            }
            showdown.updateCard();


            if (cards.size() >= cardsForFree()) {
                showdown.cost = showdown.costForTurn = 0;
            }
            else {
                showdown.cost = showdown.costForTurn = 1;
            }
            showdown.applyPowers();
        }
        else {
            setShowdown(-1);
        }
    }

    private static void setShowdown(int hand) {
        if (hand < 0) {
            showdown = null;
        }
        else {
            if (showdown == null)
            {
                showdown = new Showdown();
                UpdateAndTrackShowdownCard.instantSetPosition(showdown);
            }

            showdown.setValue(TEXT[hand], hand * 5);
        }
    }

    public static int cardsForFree() {
        int amt = 5;
        AbstractPower p = AbstractDungeon.player.getPower(ACPower.POWER_ID);
        if (p != null)
            amt -= p.amount;
        return amt;
    }
}