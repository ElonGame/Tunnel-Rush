package com.noxalus.tunnelrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.noxalus.tunnelrush.Assets;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.TunnelRush;
import com.noxalus.tunnelrush.entities.Tunnel;
import com.noxalus.tunnelrush.utils.OverlapTester;

public class MainMenuScreen implements Screen
{
	TunnelRush game;
	
	Rectangle playBounds;
	Rectangle leaderboardBounds;
	Rectangle achievementsBounds;
	Rectangle rateBounds;
	Vector3 touchPoint;
	Sprite title;
	float counter = 0;
	Tunnel tunnel;
	
	public MainMenuScreen (TunnelRush game) {
		this.game = game;
		
		playBounds = new Rectangle(
				(Config.GameWidth / 2) - (Assets.play.getRegionWidth() / 2), 
				Config.GameHeight - 3 * (Config.GameHeight / 4.f),
				Assets.play.getRegionWidth(),
				Assets.play.getRegionHeight());
		
		leaderboardBounds = new Rectangle(
				((Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 4)) / 2 - (Assets.leaderboard.getRegionWidth() / 4), 
				Config.GameHeight / 10,
				Assets.leaderboard.getRegionWidth() / 2,
				Assets.leaderboard.getRegionHeight() / 2);
		
		achievementsBounds = new Rectangle(
				(Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 4), 
				Config.GameHeight / 10,
				Assets.achievement.getRegionWidth() / 2,
				Assets.achievement.getRegionHeight() / 2);
		
		rateBounds = new Rectangle(
				(3 * ((Config.GameWidth / 2) - (Assets.achievement.getRegionWidth() / 4)) / 2) + (Assets.rate.getRegionWidth() / 4),
				Config.GameHeight / 10,
				Assets.rate.getRegionWidth() / 2,
				Assets.rate.getRegionHeight() / 2);
		
		touchPoint = new Vector3();
		
		title = new Sprite(Assets.title);
		
		Assets.menu.play();
	}

	public void update(float deltaTime) {
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
	
	public void draw(float deltaTime) {
		Gdx.gl.glClearColor(0.32f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.SpriteBatch.begin();
		
		game.SpriteBatch.draw(Assets.title, 
				Config.GameWidth / 2 - (Assets.title.getWidth() / 2), 
				Config.GameHeight - (Assets.title.getHeight() / 2) - (Config.GameHeight / 6));
		
//		game.SpriteBatch.draw(Assets.playerTexture, 
//				(Config.GameWidth / 2) - (320 / 2), 
//				Config.GameHeight - 320 - (Config.GameHeight / 3),
//				320, 320);
		
		counter += deltaTime;
		TextureRegion keyFrame = Assets.playerAnimation.getKeyFrame(counter, true);
		
		game.SpriteBatch.draw(keyFrame, (Config.GameWidth / 2) - (320 / 2), 
				Config.GameHeight - 320 - (Config.GameHeight / 3),
				320, 320);
		
		game.SpriteBatch.draw(Assets.play, playBounds.x, playBounds.y, playBounds.width, playBounds.height);
		
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
		// TODO Auto-generated method stub
		
	}

}
