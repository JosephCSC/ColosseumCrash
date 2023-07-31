package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.*;
import org.joml.*;

public class ZoomAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f upDirection;

	public ZoomAction(MyGame g)
	{	game = g;
	}

	@Override
	public void performAction(float time, Event e)
	{
		float keyValue = e.getValue();
		if (keyValue > -.2 && keyValue< .2) return;
		
		Component event = e.getComponent();
		String eName = event.getName();
		
		if(event.isAnalog()){
			if(keyValue < 0){zoomOut();}
			else{zoomIn();}
		}
		else if (eName == "Q"){
			zoomIn();
		}
		else if (eName == "E"){
			zoomOut();
		}
		
		
	}
	
	private void zoomOut(){
		c = (game.getEngine().getRenderSystem()).getViewport("RIGHT").getCamera();
		oldPosition = c.getLocation();
		newPosition = oldPosition;
		if(oldPosition.y() < 10) newPosition = oldPosition.add(0f, .01f, 0f);
		c.setLocation(newPosition);
	}
	
	private void zoomIn(){
		
		c = (game.getEngine().getRenderSystem()).getViewport("RIGHT").getCamera();
		oldPosition = c.getLocation();
		newPosition = oldPosition;
		if(oldPosition.y() > 1) newPosition = oldPosition.add(0f, -.01f, 0f);
		c.setLocation(newPosition);
	}
}

