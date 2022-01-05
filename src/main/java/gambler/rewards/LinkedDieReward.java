package gambler.rewards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.RewardGlowEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import gambler.effects.FloatyTextEffect;
import gambler.relics.FairDie;
import gambler.relics.LoadedDie;
import gambler.relics.PokerChip;
import gambler.util.TextureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.megacrit.cardcrawl.events.exordium.LivingWall.OPTIONS;
import static gambler.GamblerMod.makeID;
import static gambler.GamblerMod.makeRelicPath;

public class LinkedDieReward extends RewardItem {
    private static final Texture reroll = TextureLoader.getTexture(makeRelicPath("PokerChip.png"));

    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("DieReward")).TEXT;
    private static final String[] LINKED_TEXT = CardCrawlGame.languagePack.getUIString(makeID("LinkedReward")).TEXT;

    protected boolean goodReward;
    private Texture icon;
    private ArrayList<AbstractGameEffect> effects;

    protected int reward;

    public List<RewardItem> linkedRewards = new ArrayList<>();
    public boolean claimed = false;

    //Replaces the original with revealed result
    public LinkedDieReward(DieReward original) {
        this.goodReward = original.goodReward;
        this.icon = original.icon;
        this.type = DieReward.RewardType.DIE_REWARD;
        this.effects = original.effects;

        reward = original.reward;

        //Generate reward text
        int textIndex = reward;
        if (textIndex < 0) {
            textIndex *= -1;
        }
        else {
            textIndex += 6;
        }

        if (textIndex > 0 && textIndex < TEXT.length) {
            this.text = TEXT[textIndex];
        }
        else {
            this.text = TEXT[0];
        }
    }

    //For the poker chip reroll
    public LinkedDieReward(LinkedDieReward setLink)
    {
        this.goodReward = setLink.goodReward;
        this.icon = reroll;
        this.text = LINKED_TEXT[0];
        this.type = DieReward.RewardType.DIE_REWARD;
        this.effects = new ArrayList<>();

        reward = AbstractDungeon.cardRng.random(1, 6);

        if (!goodReward) {
            reward *= -1;
        }

        addRelicLink(setLink);
    }

    public void addRelicLink(LinkedDieReward setLink)
    {
        if (!linkedRewards.contains(setLink)) {
            linkedRewards.add(setLink);
        }
        if (!setLink.linkedRewards.contains(this)) {
            setLink.linkedRewards.add(this);
        }
    }

    private boolean isFirst()
    {
        int thisIndexOf = AbstractDungeon.combatRewardScreen.rewards.indexOf(this);
        for (RewardItem link : linkedRewards) {
            if (AbstractDungeon.combatRewardScreen.rewards.indexOf(link) < thisIndexOf) {
                return claimed;
            }
        }
        return true;
    }

    @Override
    public boolean claimReward() {
        if (!ignoreReward) {
            switch (reward) {
                //fair die
                case -1:
                    AbstractDungeon.player.decreaseMaxHealth(1);
                    CardCrawlGame.sound.play("ATTACK_POISON");
                    break;
                case -2:
                    AbstractDungeon.player.damage(new DamageInfo(null, 2, DamageInfo.DamageType.HP_LOSS));
                    CardCrawlGame.sound.play("BLUNT_FAST");
                    break;
                case -3:
                    AbstractDungeon.player.gainGold(15);
                    CardCrawlGame.sound.play("GOLD_GAIN");
                    break;
                case -4:
                    AbstractDungeon.player.heal(4, true);
                    break;
                case -5:
                    List<AbstractCard> upgradable = new ArrayList<>();
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                        if (c.canUpgrade())
                            upgradable.add(c);
                    }

                    if (!upgradable.isEmpty()) {
                        Collections.shuffle(upgradable, new Random(AbstractDungeon.miscRng.randomLong()));

                        upgradable.get(0).upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(upgradable.get(0));
                        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect((upgradable.get(0)).makeStatEquivalentCopy()));
                        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }
                    break;
                case -6:
                    AbstractDungeon.player.increaseMaxHp(3, true);
                    break;
                //loaded die
                case 1: //Bonus effects for 1 and 2 are given by the relic, tracked using counter
                    CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
                    break;
                case 2:
                    CardCrawlGame.sound.play("STANCE_ENTER_CALM");
                    break;
                case 3:
                    AbstractDungeon.player.gainGold(30);
                    AbstractDungeon.effectList.add(new RainingGoldEffect(6));
                    CardCrawlGame.sound.play("GOLD_GAIN");
                    break;
                case 4:
                    AbstractDungeon.player.heal(12, true);
                    break;
                case 5:
                    LoadedDie die = null;
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        if (r instanceof LoadedDie) {
                            die = (LoadedDie) r;
                            break;
                        }
                    }
                    if (die != null && AbstractDungeon.player.masterDeck.getUpgradableCards().size() > 0) {
                        die.listenForUpgrade();
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[5], true, false, false, false);
                    }
                    break;
                case 6:
                    AbstractDungeon.player.increaseMaxHp(6, true);
                    break;
            }

            if (reward < 0) {
                AbstractRelic r = AbstractDungeon.player.getRelic(FairDie.ID);
                if (r != null)
                    r.counter = reward * -1;
            }
            else {
                AbstractRelic r = AbstractDungeon.player.getRelic(LoadedDie.ID);
                if (r != null)
                    r.counter = reward;
            }

            //Non-revealed
            if (this.text.equals(TEXT[0]) || this.text.equals(LINKED_TEXT[0])) {
                boolean good;
                int textIndex = reward;
                //Generate reward text
                if (textIndex < 0) {
                    textIndex *= -1;
                    good = textIndex >= 3;
                }
                else {
                    good = true;
                    textIndex += 6;
                }

                if (textIndex > 0 && textIndex < TEXT.length) {
                    AbstractDungeon.topLevelEffects.add(new FloatyTextEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, TEXT[textIndex], good ? Color.GREEN : Color.RED));
                }
            }
        }

        this.claimed = true;

        for (RewardItem link : linkedRewards) {
            link.isDone = true;
            link.ignoreReward = true;
        }

        return true;
    }

    @Override
    public void update()
    {
        if (this.flashTimer > 0.0f) {
            this.flashTimer -= Gdx.graphics.getDeltaTime();
            if (this.flashTimer < 0.0f) {
                this.flashTimer = 0.0f;
            }
        }

        this.hb.update();

        if (this.effects.size() == 0) {
            this.effects.add(new RewardGlowEffect(this.hb.cX, this.hb.cY));
        }

        for (AbstractGameEffect effect : effects) {
            effect.update();
        }

        effects.removeIf((effect) -> effect.isDone);

        if (this.hb.justHovered) {
            CardCrawlGame.sound.play("UI_HOVER");
        }

        if (this.hb.hovered && InputHelper.justClickedLeft && !this.isDone) {
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.1f);
            this.hb.clickStarted = true;
        }

        if (this.hb.hovered && CInputActionSet.select.isJustPressed() && !this.isDone) {
            this.hb.clicked = true;
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.1f);
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.isDone = true;
        }

        if (isFirst()) {
            redText = false;
            for (RewardItem link : linkedRewards) {
                link.redText = false;
            }
        }
        for (RewardItem link : linkedRewards) {
            if (hb.hovered) {
                link.redText = true;
            }
            if (!AbstractDungeon.combatRewardScreen.rewards.contains(link) && !this.claimed) //the linked item has vanished but not because this got claimed
            {
                this.isDone = true;
                this.ignoreReward = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (this.hb.hovered) {
            sb.setColor(new Color(0.4f, 0.6f, 0.6f, 1.0f));
        } else {
            sb.setColor(new Color(0.5f, 0.6f, 0.6f, 0.8f));
        }

        if (this.hb.clickStarted) {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.xScale * 0.98f, Settings.scale * 0.98f, 0.0f, 0, 0, 464, 98, false, false);
        } else {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.xScale, Settings.scale, 0.0f, 0, 0, 464, 98, false, false);
        }

        if (this.flashTimer != 0.0f) {
            sb.setColor(0.6f, 1.0f, 1.0f, this.flashTimer * 1.5f);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale * 1.03f, Settings.scale * 1.15f, 0.0f, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        sb.setColor(Color.WHITE.cpy());

        sb.draw(this.icon, RewardItem.REWARD_ITEM_X - 64.0f, this.y - 65.0f, 64.0f, 64.0f, 128.0f, 128.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 128, 128, false, false);

        Color c = Settings.CREAM_COLOR.cpy();
        if (this.hb.hovered) {
            c = Settings.GOLD_COLOR.cpy();
        }

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, this.text, Settings.WIDTH * 0.434F, this.y + 5.0f * Settings.scale, 1000.0f * Settings.scale, 0.0f, c);

        if (!this.hb.hovered) {
            for (AbstractGameEffect e : this.effects) {
                e.render(sb);
            }
        }

        if (!linkedRewards.isEmpty()) {
            if (!isFirst()) {
                renderRelicLink(sb);
            }
        }

        this.hb.render(sb);
    }

    @SpireOverride
    protected void renderRelicLink(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }
}