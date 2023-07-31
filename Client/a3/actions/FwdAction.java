package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class FwdAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient p;

	public FwdAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = g.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{
		game.avatarWalk();
		fwdDirection = av.getWorldForwardVector();
		oldPosition = av.getWorldLocation();
		newPosition = oldPosition.add(fwdDirection.mul(game.speed));
        	av.setLocalLocation(newPosition);
		p.sendMoveMessage(newPosition);
		game.isWalking = true;
		game.idle = false;
	}
}

