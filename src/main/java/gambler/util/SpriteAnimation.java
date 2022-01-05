package gambler.util;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpriteAnimation extends AbstractAnimation {
    private float frameTime;
    private float perFrame;

    private int currentFrame;
    private Texture[] textures;
    private int[] width;
    private int[] height;
    private float[] scaledWidth;
    private float[] scaledHeight;
    private Texture backgroundTexture;
    private int backgroundWidth;
    private int backgroundHeight;
    private float scaledBackgroundWidth;
    private float scaledBackgroundHeight;
    private boolean hasBackground;

    public SpriteAnimation(float perFrame, String[] images, String backgroundTexture)
    {
        this.frameTime = this.perFrame = perFrame;
        this.currentFrame = 0;

        this.textures = new Texture[images.length];
        this.width = new int[images.length];
        this.height = new int[images.length];
        this.scaledWidth = new float[images.length];
        this.scaledHeight = new float[images.length];

        for (int i = 0; i < images.length; ++i)
        {
            this.textures[i] = TextureLoader.getTexture(images[i], false);
            this.width[i] = this.textures[i].getWidth();
            this.height[i] = this.textures[i].getHeight();
            this.scaledWidth[i] = this.width[i] * Settings.scale;
            this.scaledHeight[i] = this.height[i] * Settings.scale;
        }

        if (backgroundTexture != null)
        {
            hasBackground = true;
            this.backgroundTexture = TextureLoader.getTexture(backgroundTexture, false);
            this.backgroundWidth = this.backgroundTexture.getWidth();
            this.backgroundHeight = this.backgroundTexture.getHeight();
            this.scaledBackgroundWidth = this.backgroundWidth * Settings.scale;
            this.scaledBackgroundHeight = this.backgroundHeight * Settings.scale;
        }
    }

    @Override
    public Type type() {
        return Type.SPRITE;
    }

    public void renderSprite(SpriteBatch sb, float x, float y) {
        this.frameTime -= Gdx.graphics.getRawDeltaTime(); //sorry superfastmode :)
        while (this.frameTime < 0)
        {
            this.frameTime += this.perFrame;
            this.currentFrame = (this.currentFrame + 1) % this.textures.length;
        }


        sb.setColor(Color.WHITE.cpy());

        if (hasBackground)
            sb.draw(this.backgroundTexture, x - scaledBackgroundWidth / 2.0F, y, scaledBackgroundWidth, scaledBackgroundHeight, 0, 0, backgroundWidth, backgroundHeight, AbstractDungeon.player.flipHorizontal, AbstractDungeon.player.flipVertical);
        sb.draw(this.textures[currentFrame], x - scaledWidth[currentFrame] / 2.0F, y, scaledWidth[currentFrame], scaledHeight[currentFrame], 0, 0, width[currentFrame], height[currentFrame], AbstractDungeon.player.flipHorizontal, AbstractDungeon.player.flipVertical);
    }
}
