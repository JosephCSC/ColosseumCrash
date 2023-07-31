package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class PanDownAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f downDirection;

	public PanDownAction(MyGame g)
	{	game = g;
	}

	@Override
	public void performAction(float time, Event e)
	{
		c = (game.getEngine().getRenderSystem()).getViewport("RIGHT").getCamera();
		oldPosition = c.getLocation();
		downDirection = c.getV();
		downDirection.mul(-.01f);
		newPosition = oldPosition.add(downDirection.x(), downDirection.y(), downDirection.z());
		c.setLocation(newPosition);
	}
}

