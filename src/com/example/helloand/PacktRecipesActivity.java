package com.example.helloand;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.LayoutGameActivity;
import org.andengine.util.debug.Debug;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PacktRecipesActivity extends BaseGameActivity {

    // The following constants will be used to define the width and height
    // of our game's camera view
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;

    // Declare a Camera object for our activity
    private Camera mCamera;

    // Declare a Scene object for our activity
    private Scene mScene;

    Sound mSound;
    Music mMusic;
    
    /*
    * The onCreateEngineOptions method is responsible for creating the options to be
    * applied to the Engine object once it is created. The options include,
    * but are not limited to enabling/disable sounds and music, defining multitouch
    * options, changing rendering options and more.
    */
    @Override
    public EngineOptions onCreateEngineOptions() {
        // Define our mCamera object
        mCamera = new Camera(0, 0, WIDTH, HEIGHT);

        // Declare & Define our engine options to be applied to our Engine object
        EngineOptions engineOptions = new EngineOptions(true,
        ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
        mCamera);

        // It is necessary in a lot of applications to define the following
        // wake lock options in order to disable the device's display
        // from turning off during gameplay due to inactivity
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

        // we plan to use Sound and Music objects in our game
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);

        // Return the engineOptions object, passing it to the engine
        return engineOptions;
    }

    /* The onCreateEngine method allows us to return a 'customized' Engine
    object
    * to the Activity which for the most part affects the way frame
    updates are
    * handled. Depending on the Engine object used, the overall feel of
    the
    * gameplay can alter drastically.
    */
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        /* The returned super method above simply calls:
        return new Engine(pEngineOptions);
        */
        return new FixedStepEngine(pEngineOptions, 60);
    }

    /*
    * The onCreateResources method is in place for resource loading, including textures,
    * sounds, and fonts for the most part.
    */
    @Override
    public void onCreateResources(
    OnCreateResourcesCallback pOnCreateResourcesCallback) {
        /* Set the base path for our SoundFactory and MusicFactory to
        * define where they will look for audio files.
        */
        SoundFactory.setAssetBasePath("sfx/");
        MusicFactory.setAssetBasePath("sfx/");
        
        // Load our "sound.mp3" file into a Sound object
        try {
            mSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "sound.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load our "music.mp3" file into a music object
        try {
            mMusic = MusicFactory.createMusicFromAsset(getMusicManager(), this, "music.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* We should notify the pOnCreateResourcesCallback that we've finished
        * loading all of the necessary resources in our game AFTER they are loaded.
        * onCreateResourcesFinished() should be the last method called. */
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    /* Music objects which loop continuously should be played in
    * onResumeGame() of the activity life cycle
    */
    @Override
    public synchronized void onResumeGame() {
        if(mMusic != null && !mMusic.isPlaying()){
            mMusic.play();
        }
        super.onResumeGame();
    }

    /* Music objects which loop continuously should be paused in
    * onPauseGame() of the activity life cycle
    */
    @Override
    public synchronized void onPauseGame() {
        if(mMusic != null && mMusic.isPlaying()){
            mMusic.pause();
        }
        super.onPauseGame();
    }

    /* The onCreateScene method is in place to handle the scene initialization and setup.
    * In this method, we must at least *return our mScene object* which will then
    * be set as our main scene within our Engine object (handled "behind the scenes").
    * This method might also setup touch listeners, update handlers, or more events directly
    * related to the scene.
    */
    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        // Create the Scene object
        mScene = new Scene();

        loadGameTextures();
        // Notify the callback that we're finished creating the scene, returning
        // mScene to the mEngine object (handled automatically)
        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    /* The onPopulateScene method was introduced to AndEngine as a way of separating
    * scene-creation from scene population. This method is in place for attaching
    * child entities to the scene once it has already been returned to the engine and
    * set as our main scene.
    */
    @Override
    public void onPopulateScene(Scene pScene,
        OnPopulateSceneCallback pOnPopulateSceneCallback) {
        // onPopulateSceneFinished(), similar to the resource and scene callback
        // methods, should be called once we are finished populating the scene.
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    
    public void loadGameTextures()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        // Create the texture atlas at a size of 120x120 pixels
        BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(mEngine.getTextureManager(), 120, 120,
                                                        BitmapTextureFormat.RGB_565, TextureOptions.BILINEAR);
        /* Create rectangle one at position (10, 10) on the
        mBitmapTextureAtlas */
        ITextureRegion mRectangleOneTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "rectangle_one.png", 10, 10);
        /* Create rectangle two at position (50, 10) on the
        mBitmapTextureAtlas */
        ITextureRegion mRectangleTwoTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "rectangle_two.png", 50, 10);
        /* Create rectangle three at position (10, 60) on the
        mBitmapTextureAtlas */
        ITextureRegion mRectangleThreeTextureRegion =
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "rectangle_three.png", 10, 60);

        // load our ITextureRegion objects into memory. 
        mBitmapTextureAtlas.load();

        /* Create our repeating texture. Repeating textures require width/
        height which are a power of two */
        BuildableBitmapTextureAtlas terrainTexture = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 32, 32,
                                                            BitmapTextureFormat.RGB_565, TextureOptions.REPEATING_BILINEAR);

        // Create texture region - nothing new here
        ITextureRegion mSquareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(terrainTexture, this, "square.png");
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
        // Create a sprite which stretches across the full screen
        Sprite sprite = new Sprite(0, 0, 800, 480, mSquareTextureRegion, mEngine.getVertexBufferObjectManager());
        mScene.attachChild(sprite);
    }
//    @Override
//    protected int getLayoutID() {
//    return R.layout.activity_packt_recipes;
//    }
//    @Override
//    protected int getRenderSurfaceViewID() {
//    return R.id.gameSurfaceView;
//    }

}