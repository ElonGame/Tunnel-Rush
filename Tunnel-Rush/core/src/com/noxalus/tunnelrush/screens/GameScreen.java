package com.noxalus.tunnelrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.entities.Player;
import com.noxalus.tunnelrush.entities.Tunnel;
import com.noxalus.tunnelrush.tasks.ChangeCameraRotationTask;
import com.noxalus.tunnelrush.tasks.ChangeWallDistanceTask;

public class GameScreen implements Screen
{
	// Camera
	public OrthographicCamera camera;
	private OrthographicCamera hudCamera;

	public boolean cameraRotationEnabled;
	public float cameraRotationAngle;
	private float cameraRotationSpeed;
	
	public enum CameraRotationType {
		NONE,
		CLOCKWISE,
		COUNTERCLOCKWISE
	}
	
	public CameraRotationType cameraRotationType;

	private SpriteBatch spriteBatch;

	// Textures & Sprites
	private Texture texture;

	// Fonts
	private BitmapFont font;

	// Game logic
	public Player player;
	private Tunnel tunnel;

	// Difficulty
	public int difficulty;
	
	public enum WallDistanceType {
		NONE,
		INCREASE,
		DECREASE
	}

	public float maxWallDistance;
	public WallDistanceType wallDistanceType;
	
	
	// Scores
	public float score;
	private int highscore;
	private int deathNumber;

	// Timers
	Timer wallDistanceTimer;
	Timer cameraRotationTimer;
	
	// Tasks
	Task changeWallDistanceTask;
	Task changeCameraRotationTask;
	
	// Music
	Music introMusic;
	Music loopMusic;

	// Text
	TextBounds scoreTextBounds;
	String scoreTextString;
	TextBounds highscoreTextBounds;
	String highscoreTextString;
	TextBounds deathNumberTextBounds;
	String deathNumberTextString;

	// Debug
	FPSLogger fpsLogger;

	public GameScreen()
	{
		fpsLogger = new FPSLogger();
		
		maxWallDistance = Config.InitialMaxWallDistance;
		
		player = new Player(this);
		tunnel = new Tunnel(this);

		highscore = Config.Settings.getInteger("highscore");
		
		deathNumber =  Config.Settings.getInteger("deathNumber");
		
		// Cameras
		camera = new OrthographicCamera();
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(true, Config.GameWidth, Config.GameHeight);
		
		// Timers
		wallDistanceTimer = new Timer();
		cameraRotationTimer = new Timer();
		
		// Tasks
		changeWallDistanceTask = new ChangeWallDistanceTask(this);
		changeCameraRotationTask = new ChangeCameraRotationTask(this);

		spriteBatch = new SpriteBatch();

		font = new BitmapFont(Gdx.files.internal("data/graphics/fonts/classic.fnt"),
				Gdx.files.internal("data/graphics/fonts/classic.png"), true);
		
		// Text
		scoreTextString = "Score";
		scoreTextBounds = font.getBounds(scoreTextString);
		
		highscoreTextString = "Best: ";
		highscoreTextBounds = font.getBounds(highscoreTextString);
		
		deathNumberTextString = "Deaths: ";
		deathNumberTextBounds = font.getBounds(deathNumberTextString);
		
		// Music
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/bgm/intro.mp3"));
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/bgm/loop.mp3"));
		loopMusic.setLooping(true);

		introMusic.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(Music music) {
				cameraRotationEnabled = true;
				loopMusic.play();
			}
		});

		initialize();
	}

	public void initialize()
	{
		difficulty = 0;
		score = 0;
		maxWallDistance = Config.InitialMaxWallDistance;
		wallDistanceType = WallDistanceType.NONE;
		
		wallDistanceTimer.clear();
		cameraRotationTimer.clear();
		
		wallDistanceTimer.scheduleTask(changeWallDistanceTask, 0, 2);
		cameraRotationTimer.scheduleTask(changeCameraRotationTask, 0, 5);
		
		wallDistanceTimer.start();
		cameraRotationTimer.start();
		
		camera.setToOrtho(true, Config.GameWidth, Config.GameHeight);
		
		cameraRotationAngle = 0f;
		cameraRotationEnabled = false;
		cameraRotationSpeed = 0f;
		cameraRotationType = CameraRotationType.CLOCKWISE;

		introMusic.play();
	}

	public void reset()
	{
		introMusic.stop();
		loopMusic.stop();
		
		wallDistanceTimer.stop();
		cameraRotationTimer.stop();

		initialize();
		
		tunnel.reset();
		player.reset();
	}

	@Override
	public void render(float delta) {

		// Update

		fpsLogger.log();

		if (player.isAlive)
		{
			player.Update(delta);

			tunnel.Update(delta);

			if (!Config.DisableRotation && cameraRotationEnabled)
			{
				switch(cameraRotationType)
				{
				case NONE:
					break;
				case CLOCKWISE:
					if (cameraRotationSpeed < 0)
						cameraRotationSpeed += 50f * delta;

					cameraRotationSpeed += 5f * delta;
					break;
				case COUNTERCLOCKWISE:
					if (cameraRotationSpeed > 0)
						cameraRotationSpeed -= 50f * delta;
					
					cameraRotationSpeed -= 5f * delta;
					break;
				}

				cameraRotationAngle += cameraRotationSpeed * delta;
				camera.rotate(cameraRotationSpeed * delta);
			}

			camera.update();
			
			// Increase difficulty
			if (score / Config.ScoreToIncreaseDifficulty > (difficulty + 1))
				difficulty++;

			switch(wallDistanceType)
			{
			case NONE:
				break;
			case INCREASE:
				maxWallDistance += Config.WallDistanceChangeStep * delta;
				break;
			case DECREASE:
				maxWallDistance -= Config.WallDistanceChangeStep * delta;
				break;
			}
			
			maxWallDistance = MathUtils.clamp(maxWallDistance, Config.MinWallDistance, Config.InitialMaxWallDistance * 1.5f);
		}
		else
		{
			if (highscore < score)
			{
				highscore = (int)score;
				
				Config.Settings.putInteger("highscore", highscore);
				
				
			}

			deathNumber++;
			Config.Settings.putInteger("deathNumber", deathNumber);
			
			Config.Settings.flush();
			
			reset();
		}

		// Draw

		Gdx.gl.glClearColor(0.32f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.setProjectionMatrix(camera.combined);

		spriteBatch.begin();

		player.Draw(spriteBatch);

		tunnel.Draw(spriteBatch);

		spriteBatch.end();

		//player.DrawBoundingBox();
		
		spriteBatch.setProjectionMatrix(hudCamera.combined);
		spriteBatch.begin();

		String scoreValueString = Integer.toString((int)score);
		String highScoreValueString = Integer.toString(highscore);
		String deathNumberValueString = Integer.toString(deathNumber);
		
		font.draw(spriteBatch, scoreTextString, Config.GameWidth/2 - font.getBounds(scoreTextString).width / 2, 0);
		font.draw(spriteBatch, scoreValueString, Config.GameWidth/2 - font.getBounds(scoreValueString).width / 2, 
					font.getBounds(scoreTextString).height);

		font.draw(spriteBatch, highscoreTextString, 0, Config.GameHeight - font.getBounds(highscoreTextString).height);
		font.draw(spriteBatch, highScoreValueString, highscoreTextBounds.width, Config.GameHeight - font.getBounds(highScoreValueString).height);
		
		font.draw(spriteBatch, deathNumberTextString, 0, Config.GameHeight - 2 * font.getBounds(deathNumberTextString).height);
		font.draw(spriteBatch, deathNumberValueString, deathNumberTextBounds.width, Config.GameHeight - 2 * font.getBounds(deathNumberValueString).height);
		
		// Debug
		
		//font.draw(spriteBatch, Integer.toString((int)cameraRotationAngle), 0, 0);
		
		/*
		String str = "";
		switch(wallDistanceType)
		{
		case NONE:
			str = "NONE";
			break;
		case INCREASE:
			str = "INCREASE";
			break;
		case DECREASE:
			str = "DECREASE";
			break;
		}
		font.draw(spriteBatch, str, 0, 0);
		
		switch(cameraRotationType)
		{
		case NONE:
			str = "NONE";
			break;
		case CLOCKWISE:
			str = "CLOCKWISE";
			break;
		case COUNTERCLOCKWISE:
			str = "COUNTERCLOCKWISE";
			break;
		}
		font.draw(spriteBatch, str, 0, 40);
		
		str = Integer.toString((int)maxWallDistance);
		font.draw(spriteBatch, str, 0, 80);
		*/
		//font.draw(spriteBatch, "Difficulty: " + Integer.toString(difficulty), 0, 0);
		//font.draw(spriteBatch, "Highscore: " + highscore, 0, 0);
		
		/*
		if (player.isAlive)
			font.draw(spriteBatch, "Dead: FALSE", 0, 80);
		else
			font.draw(spriteBatch, "Dead: TRUE", 0, 80);
		*/
		
		font.draw(spriteBatch, "Position: (" + player.getSprite().getX() + ", " + player.getSprite().getY() + ")", 0, 60);
		/*font.draw(spriteBatch, player.initialSpritePosition.toString(), 0, 120);
		font.draw(spriteBatch, player.initialTouchPosition.toString(), 0, 180);
		font.draw(spriteBatch, player.currentTouchPosition.toString(), 0, 240);
		font.draw(spriteBatch, player.touchMoveDelta.toString(), 0, 280);
		
		if (player.isTouchingScreen)
			font.draw(spriteBatch, "TRUE", 0, 360);
		else
			font.draw(spriteBatch, "FALSE", 0, 360);
	 	*/
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		texture.dispose();
	}

}
