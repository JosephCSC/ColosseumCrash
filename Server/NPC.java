import java.util.Random;
import java.util.UUID;

public class NPC
{
	double locationX, locationY, locationZ;
	double dir = 0.1;
	double size = 1.0;
	Random rn = new Random();

	boolean attacked = true;
	int health;

	public NPC()
	{	locationX= rn.nextDouble();
		locationY= 2;
		locationZ= rn.nextDouble();
	}

	public double getX() { return locationX; }
	public double getY() { return locationY; }
	public double getZ() { return locationZ; }
	public boolean getIsAttacked() { return attacked; }

	public void getBig() { size=2.0; }
	public void getSmall() { size=1.0; }
	public double getSize() { return size; }

	public void followAvatar( double x, double y, double z, boolean b){
		

		if(b){
			double distance = Math.sqrt(Math.pow((locationX -x), 2) + Math.pow((locationZ - z), 2));
			double k = .1/distance;
			locationX = k*x+ (1-k)*locationX;
			locationZ = k*z+ (1-k)*locationZ;
		}
		
		System.out.println(locationX+ "   " + locationZ);
		
	}
	
	public boolean getCanSee(double x, double y, double z){
		double distance = Math.sqrt(Math.pow((locationX -x), 2) + Math.pow((locationZ - z), 2));
		if(distance < 5){return true;}
		else{return false;}
		
	}

	public void updateLocation() 
	{		
		//System.out.println("x: "+ locationX +"  y: "+ locationY+ "  z: " + locationZ);
	}
}
