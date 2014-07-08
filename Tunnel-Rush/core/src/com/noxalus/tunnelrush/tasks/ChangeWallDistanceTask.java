package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.screens.GameScreen;
import com.noxalus.tunnelrush.screens.GameScreen.WallDistanceType;

public class ChangeWallDistanceTask extends Task
{
	private GameScreen gameScreen;
	
	public ChangeWallDistanceTask(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
	}
	
	@Override
	public void run() {
		
		if (gameScreen.score <= 0 || Config.random.nextFloat() > 0.5f)
			return;
					
		float randomValue = Config.random.nextFloat();
		WallDistanceType newWallDistanceType;
		
		if (randomValue < 0.33f)
		{
			newWallDistanceType = WallDistanceType.NONE;
		}
		else if (randomValue < 0.66f)
		{
			newWallDistanceType = WallDistanceType.DECREASE;
		}
		else
		{
			newWallDistanceType = WallDistanceType.INCREASE;
		}
		
		// Change wall distance type
		gameScreen.wallDistanceType = newWallDistanceType;
	}
}