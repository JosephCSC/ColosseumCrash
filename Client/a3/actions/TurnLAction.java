package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;


public class TurnLAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f upVector, rightVector, fwdVector;
	private ProtocolClient p;

	public TurnLAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = game.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{	
		av.yaw(game.turnSpeed);
		p.sendRotateMessage(av.getWorldRotation());	
	}
}