package gambler.relics;

import basemod.abstracts.CustomRelic;
import gambler.util.TextureLoader;

import static gambler.GamblerMod.makeRelicPath;

public abstract class BaseRelic extends CustomRelic {
    public BaseRelic(String setId, String textureID, RelicTier tier, LandingSound sfx) {
        super(setId, TextureLoader.getTexture(makeRelicPath(textureID + ".png")), tier, sfx);
        outlineImg = TextureLoader.getTexture(makeRelicPath(textureID + "Outline.png"));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}