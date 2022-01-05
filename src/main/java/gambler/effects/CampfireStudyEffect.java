package gambler.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import gambler.relics.StudyGuide;

public class CampfireStudyEffect extends AbstractGameEffect {
    private static final float DUR = 2.0F;
    private boolean hasTrained = false;
    private final Color screenColor;

    public CampfireStudyEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = DUR;
        this.screenColor.a = 0.0F;
        if (AbstractDungeon.getCurrRoom() instanceof RestRoom)
            ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.updateBlackScreenColor();
        if (this.duration < 1.0F && !this.hasTrained) {
            this.hasTrained = true;
            AbstractRelic r = AbstractDungeon.player.getRelic(StudyGuide.ID);
            if (r instanceof StudyGuide) {
                r.flash();
                ++r.counter;
                CardCrawlGame.sound.play("MAP_SELECT_" + MathUtils.random(1, 4));
            }

            AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(0.1F, 0.4F, 0.8F, 0.0F)));
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
                ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.5F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.5F) * 2.0F);
        } else if (this.duration < 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        } else {
            this.screenColor.a = 1.0F;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);
    }

    public void dispose() {
    }
}