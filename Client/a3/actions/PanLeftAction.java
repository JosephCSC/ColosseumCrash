package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class PanLeftAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f leftDirection;

	public PanLeftAction(MyGame g)
	{	game = g;
	}

	@Override
	public void performAction(float time, Event e)
	{
		c = (game.getEngine().getRenderSystem()).getViewport("RIGHT").getCamera();
		oldPosition = c.getLocation();
		leftDirection = c.getU();
		leftDirection.mul(-.01f);
		newPosition = oldPosition.add(leftDirection.x(), leftDirection.y(), leftDirection.z());
		c.setLocation(newPosition);
	}
}

