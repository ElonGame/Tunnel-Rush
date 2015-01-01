package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.GameData.TunnelPatternType;

public class ChangeWallDistanceTask extends Task
{
	private GameData gameData;
	
	public ChangeWallDistanceTask(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	@Override
	public void run() {
/*
		if (gameData.isDemo || Config.random.nextFloat() > 0.5f)
			return;
*/

		float randomValue = Config.random.nextFloat();
        TunnelPatternType newTunnelPattern;
		
/*		if (randomValue < 1.f/6.f)
		else*/ if (randomValue < 2.f/6.f)
            newTunnelPattern = TunnelPatternType.DECREASE;
        else if (randomValue < 3.f/6.f)
            newTunnelPattern = TunnelPatternType.INCREASE;
        else if (randomValue < 4.f/6.f)
            newTunnelPattern = TunnelPatternType.RIGHT;
        else if (randomValue < 5.f/6.f)
            newTunnelPattern = TunnelPatternType.LEFT;
		else
            newTunnelPattern = TunnelPatternType.NONE;

/*
        if (gameData.tunnelPatterns == TunnelPatterns.DECREASE)
            newTunnelPattern = TunnelPatterns.INCREASE;
        else if (gameData.tunnelPatterns == TunnelPatterns.INCREASE)
            newTunnelPattern = TunnelPatterns.DECREASE;
        else
            newTunnelPattern = TunnelPatterns.NONE;
*/
		// Change wall distance type
		gameData.tunnelPattern = newTunnelPattern;
	}
}