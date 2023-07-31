import tage.ai.behaviortrees.BTCondition;

public class CanSeeAv extends BTCondition
{
	NPC npc;
	NPCcontroller npcc;
	GameServerUDP server;
	double avLocX, avLocY, avLocZ;
  
	public CanSeeAv(GameServerUDP s, NPCcontroller c, NPC n, boolean toNegate)
	{
		super(toNegate);
		server = s;
		npcc = c;
		npc = n;
	}

	protected boolean check()
	{
		server.sendGetAvatarLoc();
		avLocX  = npcc.getAvX();
		avLocY  = npcc.getAvY();
		avLocZ  = npcc.getAvZ();
		
		return npc.getCanSee(avLocX, avLocY, avLocZ);
	}
}
