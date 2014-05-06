package com.example.helloand;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.Typeface;

public class PacktRecipesActivity extends BaseGameActivity {

    // The following constants will be used to define the width and height
    // of our game's camera view
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;

    // Declare a Camera object for our activity
    private Camera mCamera;

    // Declare a Scene object for our activity
    private Scene mScene;

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
        ResourceManager.getInstance().loadSounds(mEngine, this);

        // FIXME: font doesn't work
        // Add font
        Font mFont = FontFactory.create(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32f, true, org.andengine.util.color.Color.WHITE_ABGR_PACKED_INT);
        mFont.load();
        mFont.prepareLetters("AAAABBBBBAAAAAA".toCharArray());
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
        if(ResourceManager.getInstance().mMusic != null && !ResourceManager.getInstance().mMusic.isPlaying()){
            UserData.getInstance().setSoundMuted(false);
            if(UserData.getInstance().isSoundMuted() == false)
                ResourceManager.getInstance().mMusic.play();
        }
        super.onResumeGame();
    }

    /* Music objects which loop continuously should be paused in
    * onPauseGame() of the activity life cycle
    */
    @Override
    public synchronized void onPauseGame() {
        if(ResourceManager.getInstance().mMusic != null && ResourceManager.getInstance().mMusic.isPlaying()){
            ResourceManager.getInstance().mMusic.pause();
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

        ResourceManager.getInstance().loadGameTextures(mEngine, this);
        ResourceManager.getInstance().mSquareTextureRegion.setTextureSize(800, 480);
        // Create a sprite which stretches across the full screen
        Sprite sprite = new Sprite(0, 0, 800, 480, ResourceManager.getInstance().mSquareTextureRegion, mEngine.getVertexBufferObjectManager());
        mScene.attachChild(sprite);
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

//    @Override
//    protected int getLayoutID() {
//    return R.layout.activity_packt_recipes;
//    }
//    @Override
//    protected int getRenderSurfaceViewID() {
//    return R.id.gameSurfaceView;
//    }

}