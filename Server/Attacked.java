import tage.ai.behaviortrees.BTCondition;

public class Attacked extends BTCondition
{
	NPC npc;
	NPCcontroller npcc;
	GameServerUDP server;
  
	public Attacked(GameServerUDP s, NPCcontroller c, NPC n, boolean toNegate)
	{	super(toNegate);
		server = s;
		npcc = c;
		npc = n;
	}

	protected boolean check()
	{
		return  npc.getIsAttacked();
	}
}
