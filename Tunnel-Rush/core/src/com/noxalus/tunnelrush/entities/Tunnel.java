package com.noxalus.tunnelrush.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;

public class Tunnel implements DrawableGameEntity {
	// References
	private Player player;
	private GameData gameData;
	
	private int realWallNumber;
	private ArrayList<Wall> leftWalls;
	private ArrayList<Wall> rightWalls;
	
	public Tunnel(GameData gameData)
	{
		this.gameData = gameData;
		
		this.realWallNumber = Config.MaxWallNumber + 2;
		
		leftWalls = new ArrayList<Wall>();
		rightWalls = new ArrayList<Wall>();
		
		player = null;
		
		reset();
	}
	
	public void setPlayer(Player player) 
	{
		this.player = player;
	}
	
	public void reset()
	{
		leftWalls.clear();
		rightWalls.clear();
		
		for (int i = 0; i < realWallNumber + 1; i++) {
			leftWalls.add(new Wall(i, true, leftWalls, rightWalls, gameData));
			rightWalls.add(new Wall(i, false, leftWalls, rightWalls, gameData));
		}
	}
	
	@Override
	public void Update(float delta) {
		for (int i = 0; i < realWallNumber + 1; i++) {
			leftWalls.get(i).Update(delta);
			rightWalls.get(i).Update(delta);
			
			// Intersects ?
			if (player != null && player.isAlive)
			{
				if (leftWalls.get(i).Intersects(player.getBoundingBox()) ||
					rightWalls.get(i).Intersects(player.getBoundingBox()))
					player.Kill();
			}
		}
	}

	@Override
	public void Draw(SpriteBatch spriteBatch, float delta) {
		for (int i = realWallNumber; i >= 0; i--) {
			leftWalls.get(i).Draw(spriteBatch, delta);
			rightWalls.get(i).Draw(spriteBatch, delta);
		}
	}

}
