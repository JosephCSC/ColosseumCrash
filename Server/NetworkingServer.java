import java.io.IOException;
import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer 
{
	private GameServerUDP UDPServer;
	private NPCcontroller npcCtrl;

	public NetworkingServer(int serverPort, String protocol) 
	{	
		npcCtrl = new NPCcontroller();
		try 
		{	UDPServer = new GameServerUDP(serverPort, npcCtrl);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
		}
		npcCtrl.start(UDPServer);
	}

	public static void main(String[] args) 
	{	if(args.length > 1)
		{	NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		}
	}

}
