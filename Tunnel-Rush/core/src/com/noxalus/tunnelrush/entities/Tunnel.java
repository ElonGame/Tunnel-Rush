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
	private ArrayList<LeftWall> leftWalls;
	private ArrayList<RightWall> rightWalls;
	
	public Tunnel(GameData gameData)
	{
		this.gameData = gameData;
		
		this.realWallNumber = Config.MaxWallNumber + 2;
		
		leftWalls = new ArrayList<LeftWall>();
		rightWalls = new ArrayList<RightWall>();
		
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
		
		for (int i = 0; i < realWallNumber + 1; i++)
        {
            LeftWall previousLeftWall = (i == 0) ? null : leftWalls.get(i - 1);
            RightWall previousRightWall = (i == 0) ? null : rightWalls.get(i - 1);

            LeftWall leftWall = new LeftWall(i, gameData, previousLeftWall, null);
            RightWall rightWall = new RightWall(i, gameData, leftWall, previousRightWall, null);

            if (previousLeftWall != null)
                previousLeftWall.setNextLeftWall(leftWall);
            if (previousRightWall != null)
                previousRightWall.setNextRightWall(rightWall);

            leftWalls.add(leftWall);
			rightWalls.add(rightWall);
		}

        leftWalls.get(0).setPreviousLeftWall(leftWalls.get(realWallNumber));
        rightWalls.get(0).setPreviousRightWall(rightWalls.get(realWallNumber));

        leftWalls.get(realWallNumber).setNextLeftWall(leftWalls.get(0));
        rightWalls.get(realWallNumber).setNextRightWall(rightWalls.get(0));
	}
	
	@Override
	public void Update(float delta) {
		for (int i = 0; i < realWallNumber + 1; i++) {
			leftWalls.get(i).Update(delta);
			rightWalls.get(i).Update(delta);
			
			// Intersects ?
			if (player != null && player.isAlive && !gameData.godMode)
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
