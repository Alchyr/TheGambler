package gambler.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import gambler.PokerHand;
import gambler.cards.PlayingCard;

import java.util.ArrayList;

import static gambler.GamblerMod.logger;
import static gambler.GamblerMod.makeID;
import static gambler.patches.CustomScreen.POKER_HAND_CHANGE;

public class PokerHandChangeScreen implements ScrollBarListener {
    public static final PokerHandChangeScreen screen = new PokerHandChangeScreen();

    private static final float OPTION_SPACING = 100.0f * Settings.scale;

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private static final int CARDS_PER_LINE = 5;
    private static final float SCROLL_BAR_THRESHOLD;
    private float grabStartY = 0.0F;
    private float currentDiffY = 0.0F;

    private boolean forValue;

    private PlayingCard controllerCard;

    private PlayingCard hoveredCard = null;
    private PlayingCard selectedHandCard = null;
    private ArrayList<PlayingCard> options = new ArrayList<>();

    private int numCards = 0;
    private boolean calculateScrollBounds = true;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private boolean grabbedScreen;
    public PeekButton peekButton;
    private String tipMsg;
    private ScrollBar scrollBar;

    public PokerHandChangeScreen() {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.grabbedScreen = false;
        this.peekButton = new PeekButton();
        this.tipMsg = "";
        this.controllerCard = null;

        drawStartX = (float)Settings.WIDTH;
        drawStartX -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;

        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    public void update() {
        this.updateControllerInput();
        this.updatePeekButton();
        if (!PeekButton.isPeeking) {
            if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen) {
                if ((float) Gdx.input.getY() > (float)Settings.HEIGHT * 0.75F) {
                    this.currentDiffY += Settings.SCROLL_SPEED;
                } else if ((float)Gdx.input.getY() < (float)Settings.HEIGHT * 0.25F) {
                    this.currentDiffY -= Settings.SCROLL_SPEED;
                }
            }

            boolean isDraggingScrollBar = false;
            if (this.shouldShowScrollBar()) {
                isDraggingScrollBar = this.scrollBar.update();
            }

            if (!isDraggingScrollBar) {
                this.updateScrolling();
            }

            this.updateCardPositionsAndHoverLogic();
            if (this.hoveredCard != null && InputHelper.justClickedLeft) {
                this.hoveredCard.hb.clickStarted = true;
            }

            if (this.hoveredCard != null && (this.hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())) {
                this.hoveredCard.hb.clicked = false;

                if (options.contains(hoveredCard)) {
                    //Selected a card.
                    int index = PokerHand.cards.indexOf(selectedHandCard);
                    if (index >= 0) {
                        PokerHand.cards.remove(selectedHandCard);
                        PlayingCard c = (PlayingCard) hoveredCard.makeCopy();
                        PokerHand.addCardAtIndex(c, index);

                        c.targetDrawScale = c.drawScale = 0.75f;

                        --numCards;
                        setTip();

                        selectedHandCard.stopGlowing();
                        selectedHandCard = null;
                        options.clear();

                        calculateScrollBounds = true;
                    }
                    else {
                        //wtf
                        if (selectedHandCard != null) {
                            this.selectedHandCard.stopGlowing();
                            this.selectedHandCard = null;
                        }
                        options.clear();

                        calculateScrollBounds = true;
                    }

                    if (numCards <= 0) {
                        CInputActionSet.select.unpress();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.closeCurrentScreen();

                        for (AbstractCard c : PokerHand.cards)
                            c.targetDrawScale = PokerHand.SMALL_SCALE;
                        PokerHand.updatePositions();
                    }
                }
                else if (PokerHand.cards.contains(hoveredCard)) {
                    if (hoveredCard.equals(selectedHandCard)) {
                        options.clear();
                        this.selectedHandCard.stopGlowing();
                        this.selectedHandCard = null;
                        calculateScrollBounds = true;
                        return;
                    }
                    else if (selectedHandCard != null) {
                        if ((selectedHandCard.value != hoveredCard.value) || ((selectedHandCard.suit != hoveredCard.suit)))
                        {
                            generateOptions(hoveredCard);
                        }

                        selectedHandCard.stopGlowing();
                    }
                    else {
                        generateOptions(hoveredCard);
                    }

                    selectedHandCard = hoveredCard;

                    selectedHandCard.beginGlowing();
                    selectedHandCard.targetDrawScale = 0.75F;
                    selectedHandCard.drawScale = 0.875F;
                    CardCrawlGame.sound.play("CARD_SELECT");
                    this.tipMsg = TEXT[4];

                    return;
                }

                return;
            }

            if (Settings.isControllerMode) {
                if (this.controllerCard != null) {
                    CInputHelper.setCursor(this.controllerCard.hb);
                }
            }
        }
    }

    private void generateOptions(PlayingCard c) {
        options.clear();

        if (c.suit == PlayingCard.Suit.ANY) {
            String[] prefixes = new String[] { makeID("H"), makeID("D"), makeID("C"), makeID("S") };

            for (String base : prefixes) {
                for (int i = 1; i <= 13; ++i) {
                    AbstractCard cd = CardLibrary.getCard(base + i);
                    if (cd instanceof PlayingCard)
                        options.add((PlayingCard) cd.makeCopy());
                    else
                        logger.warn("Failed to get Playing card with ID " + base + i);
                }
            }
        }
        else if (forValue) {
            String prefix;
            switch (c.suit) {
                case DIAMOND:
                    prefix = makeID("D");
                    break;
                case SPADE:
                    prefix = makeID("S");
                    break;
                case HEART:
                    prefix = makeID("H");
                    break;
                default:
                    prefix = makeID("C");
                    break;
            }
            for (int i = 1; i <= 13; ++i) {
                if (i != c.value) {
                    AbstractCard cd = CardLibrary.getCard(prefix + i);
                    if (cd instanceof PlayingCard)
                        options.add((PlayingCard) cd.makeCopy());
                    else
                        logger.warn("Failed to get Playing card with ID " + prefix + i);
                }
            }
        }
        else {
            String[] prefixes;
            switch (c.suit) {
                case DIAMOND:
                    prefixes = new String[]{
                            makeID("H"),
                            makeID("C"),
                            makeID("S")
                    };
                    break;
                case SPADE:
                    prefixes = new String[]{
                            makeID("H"),
                            makeID("D"),
                            makeID("C")
                    };
                    break;
                case HEART:
                    prefixes = new String[]{
                            makeID("D"),
                            makeID("C"),
                            makeID("S")
                    };
                    break;
                default: //club
                    prefixes = new String[]{
                            makeID("H"),
                            makeID("D"),
                            makeID("S")
                    };
                    break;
            }

            for (String suit : prefixes) {
                AbstractCard cd = CardLibrary.getCard(suit + c.value);
                if (cd instanceof PlayingCard)
                    options.add((PlayingCard) cd.makeCopy());
                else
                    logger.warn("Failed to get Playing card with ID " + suit + c.value);
            }
        }

        calculateScrollBounds = true;

        if (!options.isEmpty())
            this.controllerCard = options.get(0);
    }

    private void setTip() {
        this.tipMsg = TEXT[0] + (forValue ? TEXT[2] : TEXT[1]);
    }

    private void updatePeekButton() {
        this.peekButton.update();
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode) {
            if (controllerCard == null) {
                if (hoveredCard != null) {
                    this.controllerCard = hoveredCard;
                    CInputHelper.setCursor(hoveredCard.hb);
                }
                else {
                    CInputHelper.setCursor((PokerHand.cards.get(0)).hb);
                    this.controllerCard = PokerHand.cards.get(0);
                }
            }

            int index = PokerHand.cards.indexOf(controllerCard);

            if (index == -1)
                index = options.indexOf(controllerCard);

            if (index == -1)
                index = 0;

            if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && !options.isEmpty()) {
                if (index < 5) {
                    index += (options.size() + 4) - ((options.size() + 4) % 5);
                    while (index >= options.size() + 5) {
                        index -= 5;
                    }

                    if (index < 0) {
                        index = 0;
                    }
                } else {
                    index -= 5;
                }
            } else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && !options.isEmpty()) {
                if (index < options.size()) {
                    index += 5;
                } else {
                    index %= 5;
                }
            } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    if (index % 5 < 4) {
                        ++index;
                        if (index < 5 && index > PokerHand.cards.size() - 1) {
                            index = 0;
                        }
                        else if (index >= 5 && index >= options.size() + 5) {
                            index -= options.size() % 5;
                        }
                    } else { //index % 5 = 4, at the end of row
                        index -= 4;
                    }
                }
                //left is Not pressed
            } else { //left Is pressed
                if (index % 5 > 0) {
                    --index;
                } else {
                    index += 4;
                    if (index < 5 && index > PokerHand.cards.size() - 1) {
                        index = 0;
                    }
                    else if (index >= 5 && index >= options.size() + 5) {
                        index = options.size() + 4;
                    }
                }
            }

            if (index < 5) {
                if (index >= PokerHand.cards.size())
                    index = PokerHand.cards.size() - 1;
                CInputHelper.setCursor(PokerHand.cards.get(index).hb);
                this.controllerCard = PokerHand.cards.get(index);
            }
            else {
                CInputHelper.setCursor(options.get(index - 5).hb);
                this.controllerCard = options.get(index - 5);
            }

        }
    }

    private void updateCardPositionsAndHoverLogic() {
        this.hoveredCard = null;

        int lineNum = 0;
        int mod;
        for(int i = 0; i < PokerHand.cards.size(); ++i) {
            mod = i % CARDS_PER_LINE;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }

            PokerHand.cards.get(i).target_x = drawStartX + (float)mod * padX;
            PokerHand.cards.get(i).target_y = drawStartY + this.currentDiffY - (float)lineNum * padY;
            PokerHand.cards.get(i).fadingOut = false;
            PokerHand.cards.get(i).update();
            PokerHand.cards.get(i).updateHoverLogic();

            if (PokerHand.cards.get(i).hb.hovered) {
                this.hoveredCard = PokerHand.cards.get(i);
            }
        }

        int i = 0;
        for (PlayingCard c : options) {
            mod = i % CARDS_PER_LINE;
            if (mod == 0) {
                ++lineNum;
            }

            c.target_x = drawStartX + (float)mod * padX;
            c.target_y = drawStartY + this.currentDiffY - (float)lineNum * padY - OPTION_SPACING;
            c.fadingOut = false;
            c.update();
            c.updateHoverLogic();

            if (c.hb.hovered) {
                this.hoveredCard = c;
            }

            ++i;
        }
    }

    public void open(int numCards, boolean forValue) {
        this.callOnOpen();

        this.forValue = forValue;

        setTip();
        this.numCards = numCards;

        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.peekButton.hideInstantly();
            this.peekButton.show();
        }

        this.calculateScrollBounds();
    }

    private void callOnOpen() {
        if (Settings.isControllerMode) {
            Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            this.controllerCard = null;
        }

        this.calculateScrollBounds = true;
        this.forValue = false;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.selectedHandCard = null;
        this.controllerCard = null;
        this.hoveredCard = null;
        this.options.clear();
        AbstractDungeon.topPanel.unhoverHitboxes();
        this.currentDiffY = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = POKER_HAND_CHANGE;
        AbstractDungeon.overlayMenu.showBlackScreen(0.75F);

        for (AbstractCard c : PokerHand.cards) {
            c.targetDrawScale = 0.75F;
            c.lighten(false);
            c.stopGlowing();
        }

        this.peekButton.hideInstantly();

        drawStartY = (float)Settings.HEIGHT * 0.66F;
    }

    public void reopen() {
        AbstractDungeon.overlayMenu.showBlackScreen(0.75F);
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = POKER_HAND_CHANGE;
        AbstractDungeon.topPanel.unhoverHitboxes();

        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);

        for (AbstractCard c : PokerHand.cards) {
            c.targetDrawScale = 0.75F;
            c.lighten(false);
        }
        for (AbstractCard c : options) {
            c.targetDrawScale = 0.75F;
            c.drawScale = 0.75F;
            c.lighten(false);
        }

        this.scrollBar.reset();
    }

    private void updateScrolling() {
        if (!PeekButton.isPeeking) {
            int y = InputHelper.mY;
            boolean isDraggingScrollBar = this.scrollBar.update();
            if (!isDraggingScrollBar) {
                if (!this.grabbedScreen) {
                    if (InputHelper.scrolledDown) {
                        this.currentDiffY += Settings.SCROLL_SPEED;
                    } else if (InputHelper.scrolledUp) {
                        this.currentDiffY -= Settings.SCROLL_SPEED;
                    }

                    if (InputHelper.justClickedLeft) {
                        this.grabbedScreen = true;
                        this.grabStartY = (float)y - this.currentDiffY;
                    }
                } else if (InputHelper.isMouseDown) {
                    this.currentDiffY = (float)y - this.grabStartY;
                } else {
                    this.grabbedScreen = false;
                }
            }

            if (calculateScrollBounds) {
                this.calculateScrollBounds();
            }

            this.resetScrolling();
            this.updateBarPosition();
        }
    }

    private void calculateScrollBounds() {
        if (PokerHand.cards.size() + options.size() > 10) {
            int scrollTmp = -2;
            scrollTmp += PokerHand.cards.size() / 5;
            scrollTmp += options.size() / 5;
            if (PokerHand.cards.size() % 5 != 0) {
                ++scrollTmp;
            }
            if (options.size() % 5 != 0) {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;
            if (options.size() > 0)
                this.scrollUpperBound += OPTION_SPACING;
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        calculateScrollBounds = false;
    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }
    }

    public void render(SpriteBatch sb) {
        if (this.shouldShowScrollBar()) {
            this.scrollBar.render(sb);
        }

        if (!PeekButton.isPeeking) {
            for (AbstractCard c : PokerHand.cards) {
                if (c.equals(hoveredCard))
                    c.renderHoverShadow(sb);
                c.render(sb);
            }
            for (AbstractCard c : options) {
                if (c.equals(hoveredCard))
                    c.renderHoverShadow(sb);
                c.render(sb);
            }
            if (this.hoveredCard != null) {
                this.hoveredCard.renderCardTip(sb);
            }
        }

        boolean wasPeeking = PeekButton.isPeeking;
        this.peekButton.render(sb);
        if (wasPeeking != PeekButton.isPeeking) {
            if (PeekButton.isPeeking) {
                if (PokerHand.hoveredCard != null) {
                    PokerHand.hoveredCard.unhover();
                    PokerHand.hoveredCard = null;
                }
                for (AbstractCard c : PokerHand.cards)
                    c.targetDrawScale = PokerHand.SMALL_SCALE;
            }
            else {
                for (AbstractCard c : PokerHand.cards)
                    c.targetDrawScale = 0.75f;
            }
        }
        if (!PeekButton.isPeeking) {
            FontHelper.renderDeckViewTip(sb, this.tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
        }
        else {
            PokerHand.updatePositions();
        }
    }

    public void scrolledUsingBar(float newPercent) {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return this.scrollUpperBound > SCROLL_BAR_THRESHOLD && !PeekButton.isPeeking;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("HandChangeScreen"));
        TEXT = uiStrings.TEXT;
        SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
    }
}