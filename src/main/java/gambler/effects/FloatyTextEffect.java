package gambler.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FloatyTextEffect extends AbstractGameEffect {
    private char[] targetString;
    private StringBuilder text;
    private int index;
    private float x;
    private float y;

    private float letterTimer = 0.0f;
    private static final float PER_LETTER = 0.02f;

    public FloatyTextEffect(float x, float y, String display, Color c) {
        this.targetString = display.toCharArray();
        index = 0;

        this.x = x;
        this.y = y;
        this.text = new StringBuilder();

        this.color = c.cpy();

        if (index < targetString.length) {
            text.append(targetString[index]);
            ++index;
        }

        this.duration = this.startingDuration = 1.0f;
    }

    @Override
    public void update() {
        this.color.a = Interpolation.pow5Out.apply(0.0F, 1.0F, this.duration);
        if (index < targetString.length) {
            this.letterTimer += Gdx.graphics.getRawDeltaTime();

            while (letterTimer > PER_LETTER) {
                letterTimer -= PER_LETTER;
                if (index < targetString.length) {
                    text.append(targetString[index]);
                    ++index;
                }
                else {
                    break;
                }
            }
        }
        else {
            this.duration -= Gdx.graphics.getDeltaTime();

            if (this.duration < 0.0f) {
                this.duration = 0.0f;
                this.isDone = true;
                this.color.a = 0.0F;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, text.toString(), this.x, this.y, this.color, 1.0f);
    }

    @Override
    public void dispose() {

    }
}
