package com.noxalus.tunnelrush.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface DrawableGameEntity {
	public void Update(float delta);
	public void Draw(SpriteBatch spriteBatch);
}
