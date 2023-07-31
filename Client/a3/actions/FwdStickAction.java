package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;
import java.lang.Math; 

public class FwdStickAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient p;

	public FwdStickAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = game.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{
		game.avatarWalk();
		//create a deadzone
		float keyValue = e.getValue();
		if (keyValue > -.2 && keyValue< .2) return;
		
		fwdDirection = av.getWorldForwardVector();
		oldPosition = av.getWorldLocation();
		if(keyValue < 0) {newPosition = oldPosition.add(fwdDirection.mul(game.speed* Math.abs(keyValue)));}
		else {newPosition = oldPosition.add(fwdDirection.mul(-.01f * Math.abs(keyValue)));}
		av.setLocalLocation(newPosition);
		p.sendMoveMessage(newPosition);
		game.isWalking = true;
		game.idle = false;
	}
}

