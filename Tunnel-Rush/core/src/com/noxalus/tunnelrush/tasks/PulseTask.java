package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer.Task;

public class PulseTask extends Task
{
	private Rectangle rectangle;
	private float originY;
	private float y;
	private float timer;
	
	public PulseTask(Rectangle rectangle)
	{
		this.rectangle = rectangle;
		this.originY = rectangle.getY();
		this.y = 0;
		this.timer = 0;
	}
	
	@Override
	public void run() {
		timer += 1;
		y = (float) Math.sin(timer) * 30;
		rectangle.setY(this.originY + y);
	}	
}
