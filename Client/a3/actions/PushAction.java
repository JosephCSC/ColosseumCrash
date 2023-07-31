package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class PushAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient p;

	public PushAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = g.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{
		Vector3f tempLocation = av.getLocalLocation();
		fwdDirection =  av.getWorldForwardVector();
		
		System.out.println(fwdDirection);
		
		//if(g.rock.getLocalLocation == av.getLocalLocation + fwdDirection){}
		
		game.rock.getPhysicsObject().applyForce(100f, 0f, 0f, 0f, 0f, 0f);
	}
}