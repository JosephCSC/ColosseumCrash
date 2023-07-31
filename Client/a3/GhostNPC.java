package a3;

import tage.*;
import org.joml.*;
import tage.shapes.*;

public class GhostNPC extends GameObject
{
	private int id;
	private MyGame game;
	private AnimatedShape slimeS;
	
	public GhostNPC(int id, AnimatedShape s, TextureImage t, Vector3f p, MyGame g) 
	{	
		super(GameObject.root(), s, t);
		this.id = id;
		setPosition(p);
		this.game = g;
	}
	
	public void setSize(boolean big)
	{	if (!big) { this.setLocalScale((new Matrix4f()).scaling(0.5f)); }
		else { this.setLocalScale((new Matrix4f()).scaling(1.0f)); }
	}
	
	public int getID() { return id; }
	public void setPosition(Vector3f p) { 
		this.setLocalLocation(p);
	}
	public Vector3f getPosition() { return this.getWorldLocation(); }
}
