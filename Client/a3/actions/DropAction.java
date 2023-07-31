package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class DropAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient p;

	public DropAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = g.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{
		float x = av.getLocalLocation().x();
		float z = av.getLocalLocation().z();

		if((x > 2.7 && x < 3.1) && (z > 6.8 && z < 7)){
			game.enablePhysics();
		}
	}
}