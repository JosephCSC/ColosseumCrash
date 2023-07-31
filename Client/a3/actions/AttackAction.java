package a3.actions;

import a3.*;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class AttackAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f oldPosition, newPosition;
	private Vector3f fwdDirection;
	private ProtocolClient p;

	public AttackAction(MyGame g, ProtocolClient p)
	{	game = g;
		av = g.getAvatar();
		this.p = p;
	}

	@Override
	public void performAction(float time, Event e)
	{
		System.out.println("ATTACK");
		game.playAttack();
		
		if(p.isGhost()){
			float gX = p.getGhostLocation().x();
			float gZ = p.getGhostLocation().z();
			float aX = av.getLocalLocation().x();
			float aZ = av.getLocalLocation().z();
			
			double distance =  java.lang.Math.sqrt(java.lang.Math.pow((gX -aX), 2) + java.lang.Math.pow((gZ - aZ), 2));
			System.out.println(distance);
			
			if(distance < 1.8){
				game.testSound.play();
				p.sendNPCHitMessage();
				if(game.slimeHealth < 0){game.slimeHealth = 0;}
				if(game.slimeHealth == 0){
					p.killSlime();
				}
			}
		}
		
	}
}