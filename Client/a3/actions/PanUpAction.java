package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class PanUpAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f upDirection;

	public PanUpAction(MyGame g)
	{	game = g;
	}

	@Override
	public void performAction(float time, Event e)
	{
		c = (game.getEngine().getRenderSystem()).getViewport("RIGHT").getCamera();
		oldPosition = c.getLocation();
		upDirection = c.getV();
		upDirection.mul(.05f);
		newPosition = oldPosition.add(upDirection.x(), upDirection.y(), upDirection.z());
		c.setLocation(newPosition);
		
	}
}

