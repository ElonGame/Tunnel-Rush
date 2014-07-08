package com.noxalus.tunnelrush.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.screens.GameScreen;

public class Tunnel implements DrawableGameEntity {
	// References
	private GameScreen gameScreen;
	
	private int realWallNumber;
	private ArrayList<Wall> leftWalls;
	private ArrayList<Wall> rightWalls;
	
	public Tunnel(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
		
		this.realWallNumber = Config.MaxWallNumber + 2;
		
		leftWalls = new ArrayList<Wall>();
		rightWalls = new ArrayList<Wall>();
		
		reset();
	}
	
	public void reset()
	{
		leftWalls.clear();
		rightWalls.clear();
		
		for (int i = 0; i < realWallNumber + 1; i++) {
			leftWalls.add(new Wall(gameScreen, i, true, leftWalls, rightWalls));
			rightWalls.add(new Wall(gameScreen, i, false, leftWalls, rightWalls));
		}
	}
	
	@Override
	public void Update(float delta) {
		for (int i = 0; i < realWallNumber + 1; i++) {
			leftWalls.get(i).Update(delta);
			rightWalls.get(i).Update(delta);
			
			// Intersects ?
			if (leftWalls.get(i).Intersects(gameScreen.player.getBoundingBox()) ||
				rightWalls.get(i).Intersects(gameScreen.player.getBoundingBox()))
				gameScreen.player.Kill();
		}
	}

	@Override
	public void Draw(SpriteBatch spriteBatch) {
		for (int i = realWallNumber; i >= 0; i--) {
			leftWalls.get(i).Draw(spriteBatch);
			rightWalls.get(i).Draw(spriteBatch);
		}
	}

}
