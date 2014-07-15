package com.noxalus.tunnelrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Assets;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.TunnelRush;
import com.noxalus.tunnelrush.entities.Player;
import com.noxalus.tunnelrush.entities.Tunnel;
import com.noxalus.tunnelrush.tasks.ChangeCameraRotationTask;
import com.noxalus.tunnelrush.tasks.ChangeWallDistanceTask;

public class GameScreen implements Screen
{
	private TunnelRush game;
	
	// Camera
	public OrthographicCamera camera;
	private OrthographicCamera hudCamera;

	// Game logic
	public Player player;
	private Tunnel tunnel;

	GameData gameData;

	// Timers
	Timer wallDistanceTimer;
	Timer cameraRotationTimer;
	
	// Tasks
	Task changeWallDistanceTask;
	Task changeCameraRotationTask;

	// Sprites
	Sprite blackBackground;
	Sprite gameOverSprite;
	Sprite scoreSprite;
	Sprite bestSprite;
	Sprite deathsSprite;
	
	float interfaceScale;
	
	// Debug
	FPSLogger fpsLogger;

	public GameScreen(final TunnelRush game)
	{
		this.game = game;
		
		fpsLogger = new FPSLogger();
		
		gameData = new GameData();
		gameData.initialize();
		player = new Player(this, gameData);
		tunnel = new Tunnel(gameData);
		tunnel.setPlayer(player);

		// Cameras
		camera = new OrthographicCamera();
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(true, Config.GameWidth, Config.GameHeight);
		
		// Timers
		wallDistanceTimer = new Timer();
		cameraRotationTimer = new Timer();
		
		// Tasks
		changeWallDistanceTask = new ChangeWallDistanceTask(gameData);
		changeCameraRotationTask = new ChangeCameraRotationTask(gameData);
		
		Assets.introMusic.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(Music music) {
				game.actionResolver.unlockAchievementGPGS("CgkIup-0m9sMEAIQAQ");
				gameData.cameraRotationEnabled = true;
				Assets.loopMusic.play();
			}
		});

		blackBackground = new Sprite(Assets.pixelBackground);
		blackBackground.setSize(Config.GameWidth, Config.GameHeight);
		blackBackground.setColor(0, 0, 0, 0.75f);
		
		gameOverSprite = new Sprite(Assets.gameOver);
		gameOverSprite.setPosition(
				Config.GameWidth / 2 - (Assets.gameOver.getWidth() / 2), 
				(Config.GameHeight / 20));
		gameOverSprite.flip(false, true);
		
		interfaceScale = 1.f;
		
		scoreSprite = new Sprite(Assets.score);
		scoreSprite.setPosition(
				Config.GameWidth - (Assets.score.getWidth() * interfaceScale) - Config.GameWidth / 15, 
				Config.GameHeight / 3);
		scoreSprite.flip(false, true);
		scoreSprite.setScale(interfaceScale);
		scoreSprite.setColor(Color.GRAY);
		
		float lag = Config.GameWidth - (Config.GameWidth - (scoreSprite.getX() + scoreSprite.getWidth()));
		
		bestSprite = new Sprite(Assets.best);
		bestSprite.setPosition(
				lag - (Assets.best.getWidth() * interfaceScale), 
				Config.GameHeight / 3 + (3 * (Assets.score.getHeight() * interfaceScale)));
		bestSprite.flip(false, true);
		bestSprite.setScale(interfaceScale);
		bestSprite.setColor(Color.GRAY);
		
		deathsSprite = new Sprite(Assets.deaths);
		deathsSprite.setPosition(
				lag - (Assets.deaths.getWidth() * interfaceScale), 
				Config.GameHeight / 3 + (6 * (Assets.score.getHeight() * interfaceScale)));
		deathsSprite.flip(false, true);
		deathsSprite.setScale(interfaceScale);
		deathsSprite.setColor(Color.GRAY);
		
		initialize();
	}

	public void initialize()
	{
		Assets.digitFont.setColor(Color.BLACK);
		
		wallDistanceTimer.clear();
		cameraRotationTimer.clear();
		
		wallDistanceTimer.scheduleTask(changeWallDistanceTask, 0, 2);
		cameraRotationTimer.scheduleTask(changeCameraRotationTask, 0, 5);
		
		wallDistanceTimer.start();
		cameraRotationTimer.start();
		
		camera.setToOrtho(true, Config.GameWidth, Config.GameHeight);

		Assets.introMusic.play();
		
		gameData.initialize();
	}

	public void reset()
	{
		Assets.introMusic.stop();
		Assets.loopMusic.stop();
		
		wallDistanceTimer.stop();
		cameraRotationTimer.stop();

		initialize();
		
		tunnel.reset();
		player.reset();
	}

	public void update(float delta) {
		fpsLogger.log();

		if (player.isAlive)
		{
			player.Update(delta);

			tunnel.Update(delta);

			if (!Config.DisableRotation && gameData.cameraRotationEnabled)
			{
				switch(gameData.cameraRotationType)
				{
				case NONE:
					break;
				case CLOCKWISE:
					if (gameData.cameraRotationSpeed < 0)
						gameData.cameraRotationSpeed += 50f * delta;

					gameData.cameraRotationSpeed += 5f * delta;
					break;
				case COUNTERCLOCKWISE:
					if (gameData.cameraRotationSpeed > 0)
						gameData.cameraRotationSpeed -= 50f * delta;
					
					gameData.cameraRotationSpeed -= 5f * delta;
					break;
				}

				gameData.cameraRotationAngle += gameData.cameraRotationSpeed * delta;
				camera.rotate(gameData.cameraRotationSpeed * delta);
			}

			camera.update();
			
			// Increase difficulty
			if (gameData.score / Config.ScoreToIncreaseDifficulty > (gameData.difficulty + 1))
				gameData.difficulty++;

			switch(gameData.wallDistanceType)
			{
			case NONE:
				break;
			case INCREASE:
				gameData.maxWallDistance += Config.WallDistanceChangeStep * delta;
				break;
			case DECREASE:
				gameData.maxWallDistance -= Config.WallDistanceChangeStep * delta;
				break;
			}
			
			gameData.maxWallDistance = MathUtils.clamp(gameData.maxWallDistance, Config.MinWallDistance, Config.InitialMaxWallDistance * 1.5f);
		}
		else
		{
			if (!gameData.gameOver)
			{
				// Submit the score with the GPGS
				game.actionResolver.submitScoreGPGS(gameData.score);
				
				// Achivements unlocked ?
				if (gameData.score >= 1000)
					game.actionResolver.unlockAchievementGPGS("CgkIup-0m9sMEAIQAg");
				else if (gameData.score >= 10000)
					game.actionResolver.unlockAchievementGPGS("CgkIup-0m9sMEAIQAw");
				else if (gameData.score >= 50000)
					game.actionResolver.unlockAchievementGPGS("CgkIup-0m9sMEAIQBA");
				else if (gameData.score >= 100000)
					game.actionResolver.unlockAchievementGPGS("CgkIup-0m9sMEAIQBQ");
				
				if (gameData.highscore < gameData.score)
				{
					gameData.highscore = (int)gameData.score;
					
					Config.Settings.putInteger("highscore", gameData.highscore);
				}
	
				gameData.deathNumber++;
				
//				if (gameData.deathNumber >= 0)
//					game.actionResolver.unlockAchievementGPGS()
				
				Config.Settings.putInteger("deathNumber", gameData.deathNumber);
				
				Config.Settings.flush();
				
				gameData.gameOver = true;
				

				Assets.introMusic.stop();
				Assets.loopMusic.stop();
			}
			else
			{
				if (Gdx.input.justTouched())
				{
					reset();
				}
			}
		}
	}
	
	public void draw(float delta) {
		Gdx.gl.glClearColor(0.32f, 0.5f, 1, 1);
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.SpriteBatch.setProjectionMatrix(camera.combined);

		game.SpriteBatch.begin();

		player.Draw(game.SpriteBatch, delta);
		
		tunnel.Draw(game.SpriteBatch, delta);
		
		game.SpriteBatch.end();

		//player.DrawBoundingBox();
		
		game.SpriteBatch.setProjectionMatrix(hudCamera.combined);
		game.SpriteBatch.begin();

		String scoreValueString = Integer.toString((int)gameData.score);
		String highScoreValueString = Integer.toString(gameData.highscore);
		String deathNumberValueString = Integer.toString(gameData.deathNumber);
		
//		Assets.font.draw(game.SpriteBatch, scoreTextString, Config.GameWidth/2 - Assets.font.getBounds(scoreTextString).width / 2, 0);
		Assets.digitFont.draw(game.SpriteBatch, scoreValueString, 
				Config.GameWidth/2 - Assets.digitFont.getBounds(scoreValueString).width / 2, 
				(Config.GameHeight / 10));
		
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
		
		/*font.draw(spriteBatch, "Position: (" + player.getSprite().getX() + ", " + player.getSprite().getY() + ")", 0, 60);
		font.draw(spriteBatch, player.initialSpritePosition.toString(), 0, 120);
		font.draw(spriteBatch, player.initialTouchPosition.toString(), 0, 180);
		font.draw(spriteBatch, player.currentTouchPosition.toString(), 0, 240);
		font.draw(spriteBatch, player.touchMoveDelta.toString(), 0, 280);
		
		if (player.isTouchingScreen)
			font.draw(spriteBatch, "TRUE", 0, 360);
		else
			font.draw(spriteBatch, "FALSE", 0, 360);
	 	*/
		
		if (gameData.gameOver)
		{
			blackBackground.draw(game.SpriteBatch);
		
			gameOverSprite.draw(game.SpriteBatch);
			
			scoreSprite.draw(game.SpriteBatch);
			
			Assets.digitFont.setColor(Color.WHITE);
			Assets.digitFont.setScale(0.5f);
			
			Assets.digitFont.draw(game.SpriteBatch, scoreValueString, 
					scoreSprite.getX() + scoreSprite.getWidth() - (Assets.digitFont.getBounds(scoreValueString).width), 
					scoreSprite.getY() + 1.5f * scoreSprite.getHeight());
			
			bestSprite.draw(game.SpriteBatch);
			Assets.digitFont.draw(game.SpriteBatch, highScoreValueString, 
					bestSprite.getX() + bestSprite.getWidth() - (Assets.digitFont.getBounds(highScoreValueString).width), 
					bestSprite.getY() + 1.5f * bestSprite.getHeight());
			
			deathsSprite.draw(game.SpriteBatch);
			Assets.digitFont.setScale(0.5f);
			Assets.digitFont.draw(game.SpriteBatch, deathNumberValueString, 
					deathsSprite.getX() + deathsSprite.getWidth() - (Assets.digitFont.getBounds(deathNumberValueString).width), 
					deathsSprite.getY() + 1.5f * deathsSprite.getHeight());
		}
		
		game.SpriteBatch.end();
	}
	
	@Override
	public void render(float delta) {
		this.update(delta);
		this.draw(delta);
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
	}

}
