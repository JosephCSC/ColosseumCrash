package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class BwdAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient protoClient;

	public BwdAction(MyGame g, ProtocolClient p)
	{	game = g;
		protoClient = p;
		av = g.getAvatar();
	}

	@Override
	public void performAction(float time, Event e)
	{
		float keyValue = e.getValue();
		if(keyValue<.2 && keyValue>-.2){return;}
		
		fwdDirection = av.getWorldForwardVector();
		oldPosition = av.getWorldLocation();
		newPosition = oldPosition.add(fwdDirection.mul(-game.speed));
        	av.setLocalLocation(newPosition);
		protoClient.sendMoveMessage(newPosition);
	}
}
