package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;
import java.lang.Math;

public class TurnStickAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f upVector, rightVector, fwdVector;
	private ProtocolClient p;

	public TurnStickAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = game.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{	float keyValue = e.getValue();
		//return if the input is no Sufficient
		if(keyValue<.2 && keyValue>-.2){return;}
		
		
		if(keyValue<0){av.yaw(game.turnSpeed * Math.abs(keyValue));}
		else {av.yaw(-game.turnSpeed * Math.abs(keyValue));};
		p.sendRotateMessage(av.getWorldRotation());
	}
}