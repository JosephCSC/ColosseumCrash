import java.util.Random;
import java.util.UUID;
import tage.ai.behaviortrees.*;
import java.util.ArrayList;

public class NPCcontroller
{
	private NPC npc;
	Random rn = new Random();
	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	boolean nearFlag = false;
	long thinkStartTime, tickStartTime, lastThinkUpdateTime, lastTickUpdateTime;
	GameServerUDP server;
	double criteria = 2.0;

	

	double followDist = 4;
	double attackDist = .5;
	double avLocX, avLocY, avLocZ;
	boolean joined = false;
	
	ArrayList<NPC> nList = new ArrayList<NPC>();
	boolean loopHasStared = false;
	boolean isAlive = true;
	

	public void updateNPCs()
	{	
		for(NPC n: nList){
			n.updateLocation();
		}
	}

	public void start(GameServerUDP s)
	{	thinkStartTime = System.nanoTime();
		tickStartTime = System.nanoTime();
		lastThinkUpdateTime = thinkStartTime;
		lastTickUpdateTime = tickStartTime;
		server = s;
		setupNPCs();
		setupBehaviorTree();
		npcLoop();
	}

	public NPC getNPC(){return npc;}
	public void setupNPCs() 
	{	npc = new NPC();
	}
	
	
	public void setNearFlag(boolean b) { nearFlag=b; }
	public boolean getNearFlag() { return nearFlag; }
	public double getCriteria() { return criteria; }
	
	public void setAvX(double x) {avLocX = x;}
	public void setAvY(double y) {avLocY = y;}
	public void setAvZ(double z) {avLocZ = z;}
	public double getAvX() { return avLocX;}
	public double getAvY() { return avLocY;}
	public double getAvZ() { return avLocZ;}
	
	public void setJoined(boolean b) {joined = b;}
	public boolean getJoined() {return joined;}
	
	public void setDead() {isAlive = false;}



	public void addNPC(){
	}

	public void npcLoop()
	{	while (isAlive)
		{	long currentTime = System.nanoTime();
			float elapsedThinkMilliSecs = (currentTime-lastThinkUpdateTime)/(1000000.0f);
			float elapsedTickMilliSecs = (currentTime-lastTickUpdateTime)/(1000000.0f);

			if (elapsedTickMilliSecs >= 25.0f)
			{	lastTickUpdateTime = currentTime;
				npc.updateLocation();
				server.sendNPCinfo();
				//System.out.println("tick");
			}
	  
			if (elapsedThinkMilliSecs >= 100.0f)
			{	lastThinkUpdateTime = currentTime;
				bt.update(elapsedThinkMilliSecs);
				//System.out.println("----------- THINK ------------");
			}
			Thread.yield();
		}
	}	

	public void setupBehaviorTree()
	{	
	
		bt.insertAtRoot(new BTSequence(10));
		bt.insert(10, new Attacked(server, this, npc, false));
		bt.insert(10, new CanSeeAv(server, this, npc, false));
		bt.insert(10, new FollowAvatar(server, this, npc));
		
		
		/*
		bt.insertAtRoot(new BTSequence(20));
		bt.insert(10, new OneSecPassed(this,npc,false));
		bt.insert(10, new GetSmall(npc));
		bt.insert(20, new AvatarNear(server,this,npc,false));
		bt.insert(20, new GetBig(npc));
		*/
	}
}
