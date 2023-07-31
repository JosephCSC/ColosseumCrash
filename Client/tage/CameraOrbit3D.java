package tage;

import tage.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;
import java.lang.Math;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

/** 
*CameraOrbit3D creates a camera that Orbits around a given Object.
*<br>
* There is the camera Azimuth that controls rotation around Target Y axis. 
* The cameraElevation that controls the elevation above the target, and the 
* cameraRaius that controls the distance from the camera to the target.
*/

public class CameraOrbit3D
{	private Engine engine;
	private Camera camera;		//the camera being controlled
	private GameObject avatar;	//the target avatar the camera looks at
	private float cameraAzimuth;	//rotation of camera around target Y axis
	private float cameraElevation;	//elevation of camera above target
	private float cameraRadius;	//distance between camera and target
	
	//------------------ CONSTRUCTORS -----------------
	
	/** Creates a Camera Orbit Controller with specified camera, GameObject, controller, engine. */
	public CameraOrbit3D(Camera cam, GameObject av, Controller controller, Engine e)
	{	engine = e;
		camera = cam;
		avatar = av;
		cameraAzimuth = 0.0f;		// start from BEHIND and ABOVE the target
		cameraElevation = 20f;	// elevation is in degrees
		cameraRadius = 1.2f;		// distance from camera to avatar
		setupInputs(controller);
		updateCameraPosition();
	}

	private void setupInputs(Controller c)
	{	OrbitAzimuthAction azmAction = new OrbitAzimuthAction();
		OrbitRadiusAction rdAction = new OrbitRadiusAction();
		OrbitElevationAction elAction = new OrbitElevationAction();
		
		InputManager im = engine.getInputManager();
		im.associateAction( c,
							net.java.games.input.Component.Identifier.Axis.RX,
							azmAction, 
							InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateAction( c,
							net.java.games.input.Component.Identifier.Axis.POV,
							rdAction, 
							InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction( c,
							net.java.games.input.Component.Identifier.Axis.RY,
							elAction, 
							InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
	}

	// Updates the camera position by computing its azimuth, elevation, and distance 
	// relative to the target in spherical coordinates, then converting those spherical 
	// coords to world Cartesian coordinates and setting the camera position from that.

	// ---------------------------------------------------
	
	/** Updates the camera position by computing its azimuth, elevation, and distance 
	* relative to the target in spherical coordinates, then converting those spherical 
	* coords to world Cartesian coordinates and setting the camera position from that.*/
	public void updateCameraPosition()
	{	Vector3f avatarRot = avatar.getWorldForwardVector();
		double avatarAngle = Math.toDegrees((double)avatarRot.angleSigned(new Vector3f(0,0,-1), new Vector3f(0,1,0)));
		//float totalAz = cameraAzimuth - (float)avatarAngle;
		float totalAz = cameraAzimuth;
		double theta = Math.toRadians(totalAz);	// rotation around target
		double phi = Math.toRadians(cameraElevation);	// altitude angle
		

		float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
		float y = cameraRadius * (float)(Math.sin(phi));
		float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
		camera.setLocation(new Vector3f(x,y,z).add(avatar.getWorldLocation()));
		camera.lookAt(avatar);
		camera.pitch(.5f);
	}

	private class OrbitAzimuthAction extends AbstractInputAction
	{	public void performAction(float time, Event event)
		{	float rotAmount;
			if (event.getValue() < -0.2)
			{	rotAmount = 0.4f;
			}
			else
			{	if (event.getValue() > 0.2)
				{	rotAmount = -0.4f;
				}
				else
				{	rotAmount=0.0f;
				}
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}
  
	// private class OrbitRadiusAction extends AbstractInputAction
	private class OrbitRadiusAction extends AbstractInputAction
	{   public void performAction(float time, Event event)
		{	float radAmount;
			if(event.getValue() == .25 && cameraRadius < 5){ radAmount = .03f;}
			else if (event.getValue() ==.75 && cameraRadius > 1) {radAmount = -.03f;}
			else {radAmount = 0;}
			
			cameraRadius += radAmount;
			updateCameraPosition();
		}
	}
	// private class OrbitElevationAction extends AbstractInputAction
	private class OrbitElevationAction extends AbstractInputAction
	{
		public void performAction(float time, Event event){
			float rotAmount;
			if (event.getValue() < -0.2 && cameraElevation < 85)
			{	rotAmount = 0.4f;
			}
			else
			{	if (event.getValue() > 0.2 && cameraElevation > 0)
				{	rotAmount =- 0.4f;
				}
				else
				{	rotAmount=0.0f;
				}
			}
			cameraElevation += rotAmount;
			cameraElevation = cameraElevation % 360;
			updateCameraPosition();
		}
	}		
}