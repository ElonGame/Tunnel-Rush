package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.GameData.CameraRotationType;

public class ChangeCameraRotationTask extends Task
{
	private GameData gameData;
	
	public ChangeCameraRotationTask(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	@Override
	public void run() {
		if (!gameData.cameraRotationEnabled || Config.random.nextFloat() > 0.5f)
			return;
					
		float randomValue = Config.random.nextFloat();
		CameraRotationType newCameraRotationType;
		
		if (randomValue < 0.33f)
		{
			newCameraRotationType = CameraRotationType.NONE;
		}
		else if (randomValue < 0.66f)
		{
			newCameraRotationType = CameraRotationType.CLOCKWISE;
		}
		else
		{
			newCameraRotationType = CameraRotationType.COUNTERCLOCKWISE;
		}
		
		// Change rotation
		gameData.cameraRotationType = newCameraRotationType;
	}	
}
