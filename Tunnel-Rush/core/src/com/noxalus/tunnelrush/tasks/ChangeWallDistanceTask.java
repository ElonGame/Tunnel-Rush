package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.GameData.WallDistanceType;

public class ChangeWallDistanceTask extends Task
{
	private GameData gameData;
	
	public ChangeWallDistanceTask(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	@Override
	public void run() {
		
		if (gameData.score <= 0 || Config.random.nextFloat() > 0.5f)
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
		gameData.wallDistanceType = newWallDistanceType;
	}
}