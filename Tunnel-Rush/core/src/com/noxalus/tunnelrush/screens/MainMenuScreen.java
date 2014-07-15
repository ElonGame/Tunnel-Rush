package com.noxalus.tunnelrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.noxalus.tunnelrush.Assets;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.TunnelRush;
import com.noxalus.tunnelrush.entities.Tunnel;
import com.noxalus.tunnelrush.tasks.PulseTask;
import com.noxalus.tunnelrush.utils.OverlapTester;

public class MainMenuScreen implements Screen
{
	TunnelRush game;
	
	Rectangle playerBoyBounds;
	Rectangle playerGirlBounds;
	Rectangle playBounds;
	Rectangle leaderboardBounds;
	Rectangle achievementsBounds;
	Rectangle rateBounds;
	Vector3 touchPoint;
	Sprite title;
	float counter = 0;
	Tunnel tunnel;

	float playButtonScale;
	
	// Particles
	ParticleEffectPool bombEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();
	
	Timer pulseTimer;
	PulseTask pulseTask;
	
	public MainMenuScreen (TunnelRush game) {
		this.game = game;
		
		playerBoyBounds = new Rectangle(
				(Config.GameWidth / 2) - (320 / 2), 
				Config.GameHeight - 320 - (Config.GameHeight / 3),
				320, 320);
		
		playerGirlBounds = new Rectangle(
				);
		
		playButtonScale = 1.2f;
		playBounds = new Rectangle(
				(Config.GameWidth / 2) - ((Assets.play.getRegionWidth() * playButtonScale) / 2), 
				(Config.GameHeight * 0.37f) - Assets.play.getRegionHeight(),
				(Assets.play.getRegionWidth() * playButtonScale),
				(Assets.play.getRegionHeight() * playButtonScale));
		
		leaderboardBounds = new Rectangle(
				((Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 2)) / 2 - (Assets.leaderboard.getRegionWidth() / 2), 
				Config.GameHeight / 42,
				Assets.leaderboard.getRegionWidth(),
				Assets.leaderboard.getRegionHeight());
		
		achievementsBounds = new Rectangle(
				(Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 2), 
				Config.GameHeight / 42,
				Assets.achievement.getRegionWidth(),
				Assets.achievement.getRegionHeight());
		
		rateBounds = new Rectangle(
				(3 * ((Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 2)) / 2) + (Assets.rate.getRegionWidth() / 2),
				Config.GameHeight / 42,
				Assets.rate.getRegionWidth(),
				Assets.rate.getRegionHeight());
		
		touchPoint = new Vector3();
		
		title = new Sprite(Assets.title);
		
		Assets.menu.play();

		ParticleEffect bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.internal("data/graphics/particles/jet-engine"), Gdx.files.internal("data/graphics/particles"));
		bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);
		
		// Create effect:
		PooledEffect effect = bombEffectPool.obtain();
		effect.setPosition(playerBoyBounds.x + (playerBoyBounds.width / 2) - 8, playerBoyBounds.y + (playerBoyBounds.height / 3) + 8);
		effect.flipY();
		effects.add(effect);
		
		// Pulse animation
		pulseTask = new PulseTask(playerBoyBounds);
		
		pulseTimer = new Timer();
		pulseTimer.scheduleTask(pulseTask, 0, 0.1f);
			
		// Tunnel
		tunnel = new Tunnel(new GameData());
	}

	public void update(float delta) {
		tunnel.Update(delta);
		
		if (Gdx.input.justTouched()) {
//			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			touchPoint.set(Gdx.input.getX(), Config.GameHeight - Gdx.input.getY(), 0);
			if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
				Assets.selectSound.play();
//				if (!game.actionResolver.getSignedInGPGS()) game.actionResolver.loginGPGS();
				Assets.menu.stop();
				game.setScreen(new GameScreen(game));
				return;
			}
			if (OverlapTester.pointInRectangle(leaderboardBounds, touchPoint.x, touchPoint.y)) {
				Assets.selectSound.play();
				if (game.actionResolver.getSignedInGPGS()) game.actionResolver.getLeaderboardGPGS();
				else game.actionResolver.loginGPGS();
				return;
			}
			if (OverlapTester.pointInRectangle(achievementsBounds, touchPoint.x, touchPoint.y)) {
				Assets.selectSound.play();
				if (game.actionResolver.getSignedInGPGS()) game.actionResolver.getAchievementsGPGS();
				else game.actionResolver.loginGPGS();
				return;
			}
			if (OverlapTester.pointInRectangle(rateBounds, touchPoint.x, touchPoint.y)) {
				Assets.selectSound.play();
				Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.noxalus.tunnelrush");
			}
		}
	}
	
	public void draw(float delta) {
		Gdx.gl.glClearColor(0.32f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.SpriteBatch.begin();
		
		tunnel.Draw(game.SpriteBatch, delta);
		
		// Particles
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.setPosition(playerBoyBounds.x + (playerBoyBounds.width / 2) - 8, playerBoyBounds.y + (playerBoyBounds.height / 3) + 8);
		    //effect.draw(game.SpriteBatch, deltaTime);
		    if (effect.isComplete()) {
		    	//effect.start();
		    	
		        effect.free();
		        effects.removeIndex(i);
		        /*
		        PooledEffect newEffect = bombEffectPool.obtain();
		        newEffect.setPosition(playerBoyBounds.x + (playerBoyBounds.width / 2), playerBoyBounds.y + (playerBoyBounds.height / 3));
				effects.add(newEffect);*/
		    }
		}

		
//				// Reset all effects:
//				for (int i = effects.size - 1; i >= 0; i--)
//				    effects.get(i).free();
//				effects.clear();
		
		game.SpriteBatch.draw(Assets.title, 
				Config.GameWidth / 2 - (Assets.title.getWidth() / 2), 
				Config.GameHeight - (Assets.title.getHeight() / 2) - (Config.GameHeight / 6));
		
//		game.SpriteBatch.draw(Assets.playerTexture, 
//				(Config.GameWidth / 2) - (320 / 2), 
//				Config.GameHeight - 320 - (Config.GameHeight / 3),
//				320, 320);
		
		counter += delta;
		TextureRegion keyFrame = Assets.playerAnimation.getKeyFrame(counter, true);
		game.SpriteBatch.draw(keyFrame, playerBoyBounds.x, playerBoyBounds.y, playerBoyBounds.width, playerBoyBounds.height);
		game.SpriteBatch.draw(Assets.play, playBounds.x, playBounds.y, playBounds.width / 2, playBounds.height / 2, playBounds.width, playBounds.height, playButtonScale, playButtonScale, 0.f);
		game.SpriteBatch.draw(Assets.leaderboard, leaderboardBounds.x, leaderboardBounds.y, leaderboardBounds.width, leaderboardBounds.height);
		game.SpriteBatch.draw(Assets.achievement, achievementsBounds.x, achievementsBounds.y, achievementsBounds.width, achievementsBounds.height);
		game.SpriteBatch.draw(Assets.rate, rateBounds.x, rateBounds.y, achievementsBounds.width, achievementsBounds.height);
		
		game.SpriteBatch.end();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		pulseTimer.start();
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
		pulseTimer.start();
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
