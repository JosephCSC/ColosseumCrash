import tage.ai.behaviortrees.*;

public class FollowAvatar extends BTAction
{
	NPC npc;
	NPCcontroller npcc;
	GameServerUDP server;
	
	double avLocX, avLocY, avLocZ;
  
	public FollowAvatar(GameServerUDP s, NPCcontroller c, NPC n)
	{	server = s;
		npcc = c;
		npc = n;
	}

	protected BTStatus update(float elapsedTime)
	{	
		server.sendGetAvatarLoc();
		avLocX  = npcc.getAvX();
		avLocY  = npcc.getAvY();
		avLocZ  = npcc.getAvZ();

		npc.followAvatar(avLocX, avLocY, avLocZ, npcc.getJoined());
		
		return BTStatus.BH_SUCCESS;
	}
}
