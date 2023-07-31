package tage.nodeControllers;
import tage.*;
import org.joml.*;


public class FloatController extends NodeController
{
	private float movRate = .0003f;
	private float cycleTime = 2000.0f;
	private float totalTime = 0.0f;
	private float direction = 1.0f;
	private Vector3f upDirection, oldPosition, newPosition;
	private Engine engine;

	public FloatController(Engine e, float ctime)
	{	super();
		cycleTime = ctime;
		engine = e;
	}

	@Override
	public void apply(GameObject go)
	{	float elapsedTime = super.getElapsedTime();
		totalTime += elapsedTime/1000.0f;

		if (totalTime > cycleTime)
		{	direction = -direction;
			totalTime = 0.0f;
		}
		
		float movAmt = direction * movRate * elapsedTime;

		upDirection = new Vector3f(0,1,0);
		oldPosition = go.getLocalLocation();
		newPosition = oldPosition.add(upDirection.mul(movAmt));
        	go.setLocalLocation(newPosition);
	}
}