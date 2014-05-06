package com.example.helloand;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;

public class ResourceManager {
 // ResourceManager Singleton instance
    private static ResourceManager INSTANCE;

    /* The variables listed should be kept public, allowing us easy access
       to them when creating new Sprites, Text objects and to play sound files */
    public ITextureRegion mRectangleOneTextureRegion;
    public ITextureRegion mRectangleTwoTextureRegion;
    public ITextureRegion mRectangleThreeTextureRegion;
    public ITextureRegion mSquareTextureRegion;

    Sound mSound;
    Music mMusic;

    public Font mFont;

    ResourceManager(){
        // The constructor is of no use to us
    }

    public synchronized static ResourceManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ResourceManager();
        }
        return INSTANCE;
    }

    /* Each scene within a game should have a loadTextures method as well
     * as an accompanying unloadTextures method. This way, we can display
     * a loading image during scene swapping, unload the first scene's textures
     * then load the next scenes textures.
     */
    public synchronized void loadGameTextures(Engine pEngine, Context pContext){
        // Set our game assets folder in "assets/gfx/game/"
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

        // -----------------my
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        // Create the texture atlas at a size of 120x120 pixels
        BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(pEngine.getTextureManager(), 120, 120,
                                                        BitmapTextureFormat.RGB_565, TextureOptions.BILINEAR);
        /* Create rectangle one at position (10, 10) on the
        mBitmapTextureAtlas */
        mRectangleOneTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "rectangle_one.png", 10, 10);
        /* Create rectangle two at position (50, 10) on the
        mBitmapTextureAtlas */
        mRectangleTwoTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "rectangle_two.png", 50, 10);
        /* Create rectangle three at position (10, 60) on the
        mBitmapTextureAtlas */
        mRectangleThreeTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "rectangle_three.png", 10, 60);

        // load our ITextureRegion objects into memory. 
        mBitmapTextureAtlas.load();

        /* Create our repeating texture. Repeating textures require width/
        height which are a power of two */
        BuildableBitmapTextureAtlas terrainTexture = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 32, 32,
                                                            BitmapTextureFormat.RGB_565, TextureOptions.REPEATING_BILINEAR);

        // Create texture region - nothing new here
        mSquareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(terrainTexture, pContext, "square.png");
        try {
            // Repeating textures should not have padding
            terrainTexture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            terrainTexture.load();
        } catch (TextureAtlasBuilderException e) {
            Debug.e(e);
        }

        /* Increase the texture region's size, allowing repeating textures to
        stretch up to 800x480 */
        mSquareTextureRegion.setTextureSize(800, 480);
    }

    /* All textures should have a method call for unloading once
     * they're no longer needed; ie. a level transition. */
    public synchronized void unloadGameTextures(){
        // call unload to remove the corresponding texture atlas from memory
        BitmapTextureAtlas mBitmapTextureAtlas = (BitmapTextureAtlas) mRectangleOneTextureRegion.getTexture();
        mBitmapTextureAtlas.unload();
        mBitmapTextureAtlas = (BitmapTextureAtlas) mRectangleTwoTextureRegion.getTexture();
        mBitmapTextureAtlas.unload();
        mBitmapTextureAtlas = (BitmapTextureAtlas) mRectangleThreeTextureRegion.getTexture();
        mBitmapTextureAtlas.unload();

        BuildableBitmapTextureAtlas mBitmapBuildableTextureAtlas = (BuildableBitmapTextureAtlas) mSquareTextureRegion.getTexture();
        mBitmapBuildableTextureAtlas.unload();
        
        // ... Continue to unload all textures related to the 'Game' scene

        // Once all textures have been unloaded, attempt to invoke the Garbage Collector
        System.gc();
    }

    /* As with textures, we can create methods to load sound/music objects
     * for different scene's within our games.
     */
    public synchronized void loadSounds(Engine pEngine, Context pContext){
        // Set the SoundFactory's base path
        SoundFactory.setAssetBasePath("sfx/");
        MusicFactory.setAssetBasePath("sfx/");

        // Load our "sound.mp3" file into a Sound object
        try {
            mSound = SoundFactory.createSoundFromAsset(pEngine.getSoundManager(), pContext, "sound.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load our "music.mp3" file into a music object
        try {
            mMusic = MusicFactory.createMusicFromAsset(pEngine.getMusicManager(), pContext, "music.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* In some cases, we may only load one set of sounds throughout
     * our entire game's life-cycle. If that's the case, we may not
     * need to include an unloadSounds() method. Of course, this all
     * depends on how much variance we have in terms of sound
     */
    public synchronized void unloadSounds(){
        // we call the release() method on sounds to remove them from memory
        if(!mSound.isReleased())mSound.release();
        if(!mMusic.isReleased())mMusic.release();
    }
}
