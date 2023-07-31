package a3;

import a3.actions.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import javax.swing.*;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

import org.joml.*;

import tage.*;
import tage.input.*;
import tage.input.action.*;
import tage.networking.IGameConnection.ProtocolType;
import tage.nodeControllers.*;
import tage.shapes.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Invocable;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

import tage.audio.*;
import com.jogamp.openal.ALFactory;


public class MyGame extends VariableFrameRateGame
{
	private Random rd = new Random();
	private InputManager im;
	private GhostManager gm;
	private CameraOrbit3D orbitController;
	
	private static Engine engine;
	private RenderStates renderStates = new RenderStates();

	private double 		 startTime, prevTime, elapsedTime, amt;
	private GameObject   player ,avatar, dropper,
								x, y, z, terr, rock, mageHat, sword, wand, plane, store, store1;
	
	private ObjShape     avatarS, prizeS, linxS, linyS, linzS, terrS, dropperS,
								rockS, ghostS, mageHatS, swordS, wandS, planeS, storeS;
	
	private TextureImage avatartx, prizeColtx, bricktx, hills, droppperTx,
								rocktx, grass, ghosttx, mageHattx, swordtx, storeTx,
								wandtx;
	private Light light1, light2;
	private NodeController rc, fc;
	
	private Viewport leftVp, rightVp;
	private int vpBottom, vpLeft;
	
	private File scriptFile1;
	private long fileLastModifiedTime = 0;
	ScriptEngine jsEngine;

	private int skyBox1;
	
	private String serverAddress;      //Network 
	private int serverPort;
	private ProtocolType serverProtocol = ProtocolType.UDP;
	private ProtocolClient protClient;
	private boolean isClientConnected = false;
	
	//Physics 
	private PhysicsEngine physicsEngine;
	private PhysicsObject ball1p, planeP, avatarP;
	private boolean running = false;
	private float vals[] = new float[16];
	
	//Param
	public float speed = 0;
	public float turnSpeed = 0;
	public float walkingHeight = 0;
	public float mass1 = 1.0f;
	public Vector3f pfLocation = new Vector3f(0f, 0f, 0f);
	public float bBounce = 0;
	public float pBounce = 0;
	
	//enemy
	
	private GameObject enemy1;
	private ObjShape   enemyS;
	public ArrayList<GameObject> enemies = new ArrayList<GameObject>();
	
	//colos
	private GameObject col, cameraL;;
	private ObjShape colS;
	private TextureImage groundtx, marbletx;
	
	//NPC
	private ObjShape npcS;
	private TextureImage npcTx;
	private int cycle;
	
	//sound
	private IAudioManager audioMgr;
	public Sound testSound, backSound;
	
	//animation
	public AnimatedShape avS;
	
	//enemy test
	private GameObject slime;
	private AnimatedShape slimeS;
	private TextureImage slimeTx;
	
	private GameObject skeleton;
	private AnimatedShape skelS;
	private TextureImage skelTx;
	
	public boolean isWalking = false;
	public boolean idle = false;
	
	public int slimeHealth = 100;
	
	
	public MyGame(String serverAddress, int serverPort)
	{	super();
		gm = new GhostManager(this);
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	public static void main(String[] args)
	{	MyGame game = new MyGame(args[0], Integer.parseInt(args[1]));
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	
		//WorldObj
		avatarS = new ImportedModel("character.obj");
		
		
		avS = new AnimatedShape("player1.rkm", "player1.rks");
		avS.loadAnimation("IdleA", "idle.rka");
		avS.loadAnimation("WIN", "happy.rka");
		avS.loadAnimation("WalkA", "walk.rka");
		avS.loadAnimation("AttackA", "attack.rka");
		
		
		ghostS =  new ImportedModel("character.obj");
		rockS = new ImportedModel("rock.obj");
		
		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(5f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,5f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-5f));
		
		terrS = new TerrainPlane(1000);
		planeS = new Plane();
		
		//dropper
		dropperS = new ImportedModel("dropper.obj");
		
		//character obj
		swordS = new ImportedModel("sword.obj");
		wandS = new ImportedModel("wand.obj");
		
		mageHatS = new ImportedModel("mageHat2.obj");
		
		//enemies
		enemyS = new Cube();
		colS = new ImportedModel("col1.obj");
		npcS = new Cube();
		
		//slime
		slimeS	= new AnimatedShape("Slime/Slime.rkm", "Slime/Slime.rks");
		slimeS.loadAnimation("AttackSl", "Slime/SlimeAttack.rka");
		slimeS.loadAnimation("IdleSl", "Slime/SlimeIdle.rka");
		slimeS.loadAnimation("DeathSl", "Slime/SlimeDeath.rka");
		slimeS.loadAnimation("WalkSl", "Slime/SlimeWalk.rka");
		
		//Skeleton 
		skelS = new AnimatedShape("Skeleton/Skeleton.rkm", "Skeleton/Skeleton.rks");
		skelS.loadAnimation("AttackSk", "Skeleton/SkeletonAttack.rka");
		skelS.loadAnimation("IdleSk", "Skeleton/SkeletonIdle.rka");
		skelS.loadAnimation("DeathSk",  "Skeleton/SkeletonDeath.rka");
		skelS.loadAnimation("WalkSk", "Skeleton/SkeletonRun.rka");
		
		storeS = new ImportedModel("store.obj");
	}
	

	@Override
	public void loadTextures()
	{	avatartx = new TextureImage("characterB.png");
		bricktx = new TextureImage("Brick_01.png");
		hills = new TextureImage("hills.jpg");
		ghosttx = new TextureImage("art.jpg");
		//not finished textures.
		rocktx = new TextureImage("rock.jpg");
		mageHattx = new TextureImage("mageHat.png");
		swordtx =   new TextureImage("art.jpg");
		wandtx  =   new TextureImage("art.jpg");
		marbletx = new TextureImage("marble.jpg");
		groundtx = new TextureImage("ground.jpg");
		
		npcTx = new TextureImage("art.jpg");
		slimeTx = new TextureImage("SlimeRed.png");
		droppperTx = new TextureImage("metal.jpg");
		storeTx = new TextureImage("wood1.jpg");
	}

	@Override
	public void buildObjects()
	{	Matrix4f initialTranslation, initialScale, initialRotation;
	
		// build avatarphin avatar
		avatar = new GameObject(GameObject.root(), avS, avatartx);
		initialTranslation = (new Matrix4f()).translation(-0f,.2f,-10f);
		avatar.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(.1f);
		avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180f));
		avatar.setLocalRotation(initialRotation);
		avatar.getRenderStates().hasLighting(true);

		
		
		//build terrain
		
		terr = new GameObject(GameObject.root(), terrS, groundtx);
		initialTranslation = (new Matrix4f()).translation(0, 0, 0);
		terr.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(40f, 5f, 40f);
		terr.setLocalScale(initialScale);
		terr.setHeightMap(hills);
	
		//build the x y and z axis
		/*
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
		*/
		
		
		//build rock
		rock = new GameObject(GameObject.root(), rockS, rocktx);
		initialTranslation = (new Matrix4f()).translation(0, 5f, 0);
		initialScale = (new Matrix4f()).scaling(.22f);
		rock.setLocalScale(initialScale);
		rock.setLocalTranslation(initialTranslation);
		
		//mageHat
		mageHat = new GameObject(GameObject.root(), mageHatS, mageHattx);
		initialTranslation = (new Matrix4f()).translation(0f,.5f, 0);
		initialScale = (new Matrix4f()).scaling(1.45f);
		mageHat.setLocalScale(initialScale);
		mageHat.setLocalTranslation(initialTranslation);
		mageHat.getRenderStates().setRenderHiddenFaces(true);
		mageHat.setParent(avatar);
		
		//sword
		sword = new GameObject(GameObject.root(), swordS, swordtx);
		initialTranslation = (new Matrix4f()).translation(5.4f, 2.2f, -10.4f);
		initialScale = (new Matrix4f()).scaling(.15f);
		sword.setLocalScale(initialScale);
		sword.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationZ((float)java.lang.Math.toRadians(90f));
		sword.setLocalRotation(initialRotation);
		sword.getRenderStates().setRenderHiddenFaces(true);
		//wand
		wand = new GameObject(GameObject.root(), wandS, wandtx);
		initialTranslation = (new Matrix4f()).translation(-5f, 2.2f, -10.4f);
		initialScale = (new Matrix4f()).scaling(.20f);
		wand.setLocalScale(initialScale);
		wand.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationZ((float)java.lang.Math.toRadians(-90f));
		wand.setLocalRotation(initialRotation);
		wand.getRenderStates().setRenderHiddenFaces(true);
		
		//enemy
		
		//physics plane
		plane = new GameObject(GameObject.root(), planeS);
		plane.setLocalTranslation((new Matrix4f()).translation(0.0f, 1.6f, 0.0f));
		plane.setLocalScale((new Matrix4f()).scaling(2.0f));
		plane.getRenderStates().disableRendering();
		
		//colo
		col = new GameObject(GameObject.root(), colS, marbletx);
		col.setLocalTranslation((new Matrix4f()).translation(0.0f, 1.0f, 0.0f));
		col.setLocalScale((new Matrix4f()).scaling(.75f));
		
		//camera Loot at
		cameraL = new GameObject(avatar);
		cameraL.setLocalTranslation((new Matrix4f()).translation(0.0f, .5f, 0.0f));
		
		//dropper
		
		dropper = new GameObject(GameObject.root(), dropperS, droppperTx);
		dropper.setLocalTranslation((new Matrix4f()).translation(3f, 2f, 6.5f));
		dropper.setLocalScale((new Matrix4f()).scaling(.15f));
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(90f));
		dropper.setLocalRotation(initialRotation);
		
		//store
		store = new GameObject(GameObject.root(), storeS, storeTx);
		store.setLocalTranslation((new Matrix4f()).translation(-5f, 1.8f, -10f));
		store.setLocalScale((new Matrix4f()).scaling(.5f));
		
		store1 = new GameObject(GameObject.root(), storeS, storeTx);
		store1.setLocalTranslation((new Matrix4f()).translation(5f, 1.8f, -10f));
		store1.setLocalScale((new Matrix4f()).scaling(.5f));
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180f));
		store1.setLocalRotation(initialRotation);
		
		
		
		
		setupNetworking();
	}

	@Override 
	public void createViewports() { 
		(engine.getRenderSystem()).addViewport("RIGHT",.75f,0,.25f,.25f); 
		(engine.getRenderSystem()).addViewport("LEFT",0,0,1f,1f); 
		
		leftVp = (engine.getRenderSystem()).getViewport("LEFT"); 
		rightVp = (engine.getRenderSystem()).getViewport("RIGHT"); 
		
		Camera mainCamera = leftVp.getCamera(); 
		Camera overCamera = rightVp.getCamera(); 
		
		rightVp.setHasBorder(true); 
		rightVp.setBorderWidth(2); 
		rightVp.setBorderColor(0.0f, 1.0f, .8f); 
 
		mainCamera.setLocation(new Vector3f(-2,0,2)); 
		mainCamera.setU(new Vector3f(1,0,0)); 
		mainCamera.setV(new Vector3f(0,1,0)); 
		mainCamera.setN(new Vector3f(0,0,-1)); 
 
		overCamera.setLocation(new Vector3f(0,10,0)); 
		overCamera.setU(new Vector3f(1,0,0)); 
		overCamera.setV(new Vector3f(0,0,-1)); 
		overCamera.setN(new Vector3f(0,-1,0)); 
	}
	
	@Override
	public void loadSkyBoxes(){
		skyBox1 = (engine.getSceneGraph()).loadCubeMap("fluffyClouds");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(skyBox1);
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}
	
	@Override
	public void initializeGame()
	{	
		startTime = System.currentTimeMillis();
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);
		
		//----------------- adding lights -----------------
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);

		light1 = new Light();
		light1.setLocation(new Vector3f(0.0f, 6.0f, 0.0f));
		(engine.getSceneGraph()).addLight(light1);
		
		light2 = new Light();
		light2.setLocation(new Vector3f(5.6f, 6.0f, -10.4f));
		light2.setDiffuse(.5f, .4f,.3f);
		(engine.getSceneGraph()).addLight(light2);
		
		initializeScript();
		
		//Inputs
		setInputs();
		
		//------------Physics-----------------
		initPhysics();

		// -------------- adding node controllers -----------
		rc = new RotationController(engine, new Vector3f(1,1,0), 0.001f);
		(engine.getSceneGraph()).addNodeController(rc);
		rc.toggle();
		
		fc = new FloatController(engine, 2f);
		(engine.getSceneGraph()).addNodeController(fc);
		fc.toggle();
		
		//initialize game
		initAudio();
		
	}
	
	public void initAudio(){
		AudioResource resource1;
		AudioResource resource2;
		
		audioMgr = AudioManagerFactory.createAudioManager("tage.audio.joal.JOALAudioManager");
		
		if (!audioMgr.initialize())
		{	System.out.println("Audio Manager failed to initialize!");
			return;
		}
		
		resource1 = audioMgr.createAudioResource("assets/sounds/impact1.wav", AudioResourceType.AUDIO_SAMPLE);
		testSound = new Sound(resource1, SoundType.SOUND_EFFECT, 80, false);
		testSound.initialize(audioMgr);
		testSound.setMaxDistance(2.0f);
		testSound.setMinDistance(0.5f);
		testSound.setRollOff(5.0f);
		
		resource2 = audioMgr.createAudioResource("assets/sounds/background.wav",  AudioResourceType.AUDIO_STREAM);
		backSound = new Sound(resource2, SoundType.SOUND_MUSIC, 80, true);
		backSound.initialize(audioMgr);
		backSound.setMaxDistance(2.0f);
		backSound.setMinDistance(0.5f);
		backSound.setRollOff(5.0f);
		
		testSound.setLocation(avatar.getLocalLocation());
		backSound.setLocation(avatar.getLocalLocation());
		setEarParameters();
		
		backSound.play();
	}
	
	public void setEarParameters()
	{	
		Camera camera = (engine.getRenderSystem()).getViewport("LEFT").getCamera();
		audioMgr.getEar().setLocation(avatar.getWorldLocation());
		audioMgr.getEar().setOrientation(camera.getN(), new Vector3f(0.0f, 1.0f, 0.0f));
	}
	
	
	
	public void initializeScript(){
		//utilize script
		ScriptEngineManager factory = new ScriptEngineManager();
		java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
		jsEngine = factory.getEngineByName("js");
		
		scriptFile1 = new File("assets/scripts/InitParams.js");
		this.runScript(scriptFile1);
		speed = ((Double)(jsEngine.get("avatarSpeed"))).floatValue();
		turnSpeed = ((Double)(jsEngine.get("avTurnSpeed"))).floatValue();
		walkingHeight = ((Double)(jsEngine.get("walkingHeight"))).floatValue();
		
		bBounce = ((Double)(jsEngine.get("bBounce"))).floatValue();
		pBounce = ((Double)(jsEngine.get("bBounce"))).floatValue();
	}
	
	public void initPhysics(){
		String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = {0f, -3f, 0f};
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		physicsEngine.setGravity(gravity);
		
		//create physics world
		float up[] = {0, 1, 0};
		double[] tempTransform;
		
		Matrix4f translation = new Matrix4f(rock.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		ball1p = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass1, tempTransform, .22f);
		ball1p.setBounciness(bBounce);
		rock.setPhysicsObject(ball1p);
		
		Vector3f temptrans = new Vector3f(0,0,0);
		translation = new Matrix4f(plane.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), tempTransform, up, 0.0f);
		planeP.setBounciness(pBounce);
		plane.setPhysicsObject(planeP);
	}
	
	public GameObject getAvatar() { return avatar; }
	
	public float rdFloat(float min, float max){
		return min + rd.nextFloat() * (max - min);
	}

	@Override
	public void update()
	{	
		elapsedTime = System.currentTimeMillis() - prevTime;
		prevTime = System.currentTimeMillis();
		amt = elapsedTime * 0.03;
		vpLeft = (int) rightVp.getActualLeft() - (int)leftVp.getActualLeft();	
		
		// build and set HUD
		int elapsTimeSec = Math.round((float)(System.currentTimeMillis()-startTime)/1000.0f);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String prizeCounterStr = Integer.toString(0);
		String dispStr1 = "Time = " + elapsTimeStr;
		String dispStr2 = "Slime Health = " + slimeHealth;
		Vector3f hud1Color = new Vector3f(1,0,0);
		Vector3f hud2Color = new Vector3f(0,0,1);
		//(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
		(engine.getHUDmanager()).setHUD1(dispStr2, hud2Color, 500, 15);
		
			//Hud for the second vieport
		String dispStr3 = "Avatar position = "
			+ (avatar.getWorldLocation()).x()
			+ ", " +  (avatar.getWorldLocation()).y()
			+ ", " +  (avatar.getWorldLocation()).z();
		Vector3f hud3Color = new Vector3f(1,1,1);
		(engine.getHUDmanager()).setHUD2(dispStr3, hud3Color, vpLeft, 15);
		
		//Update script again if it has been changed
		long modTime = scriptFile1.lastModified();
		if (modTime > fileLastModifiedTime){
			fileLastModifiedTime = modTime;
			updateParams();
		}
	
		// return avatar if next position is too High
		onTerrain(avatar, .05f);
		Vector3f loc = avatar.getLocalLocation();
		im.update((float)elapsedTime);		//updates x and y position.
		
		if(avatar.getLocalLocation().equals(loc)&& !idle){
			System.out.println("the Avatar is not moving");
			avS.stopAnimation();
			avS.playAnimation("IdleA", 0.5f, AnimatedShape.EndType.LOOP, 0);
			idle = true;
			isWalking = false;
		}
		onTerrain(avatar, .05f);  					//Set the avatar to terrain
		
		if(!isScalable(loc.y(), avatar.getLocalLocation().y())){
			avatar.setLocalLocation(loc);
		}
		
		
		
		orbitController.updateCameraPosition();	
		processNetworking((float)elapsedTime);
		updateAnimations();
		updateSound();
		
		//update physics
		if (running)
		{	Matrix4f mat = new Matrix4f();
			Matrix4f mat2 = new Matrix4f().identity();
			checkForCollisions();
			physicsEngine.update((float)elapsedTime);
			for (GameObject go:engine.getSceneGraph().getGameObjects())
			{	if (go.getPhysicsObject() != null)
				{	mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
					mat2.set(3,0,mat.m30()); mat2.set(3,1,mat.m31()); mat2.set(3,2,mat.m32());
					go.setLocalTranslation(mat2);
				}
			}
		}
	}
	
	
	
	public void updateAnimations(){
		avS.updateAnimation();
		slimeS.updateAnimation();
		skelS.updateAnimation();
	}
	public void updateSound(){
		testSound.setLocation(avatar.getLocalLocation());
		setEarParameters();
	}
	
	public void updateParams(){
		this.runScript(scriptFile1);
		speed = ((Double)(jsEngine.get("avatarSpeed"))).floatValue();
		turnSpeed = ((Double)(jsEngine.get("avTurnSpeed"))).floatValue();
		walkingHeight = ((Double)(jsEngine.get("walkingHeight"))).floatValue();
		bBounce = ((Double)(jsEngine.get("bBounce"))).floatValue();
		pBounce = ((Double)(jsEngine.get("bBounce"))).floatValue();
	}
	
	public void onTerrain(GameObject obj, float hOffset){
		Vector3f loc = obj.getWorldLocation();
		float height = terr.getHeight(loc.x(), loc.z());
		obj.setLocalLocation(new Vector3f(loc.x(), height + hOffset, loc.z()));
	}
	
	
	private boolean isScalable(float h1, float h2){
		float heightDiff = h2 - h1;
		if(heightDiff < walkingHeight){return true;}
		return false;
	}
	
	private void setInputs(){
		im = engine.getInputManager();
		ArrayList<Controller> controllers = im.getControllers();
		
		FwdAction fwdAction = new FwdAction(this, protClient);
		BwdAction bwdAction = new BwdAction(this, protClient);
		FwdStickAction fwdStickAction = new FwdStickAction(this, protClient);
		TurnRAction turnRAction = new TurnRAction(this, protClient);
		TurnLAction turnLAction = new TurnLAction(this, protClient);
		TurnStickAction turnStickAction = new TurnStickAction(this, protClient);
		PanUpAction panUpAction = new PanUpAction(this);
		PanDownAction panDownAction = new PanDownAction(this);
		PanLeftAction panLeftAction = new PanLeftAction(this);
		PanRightAction panRightAction = new PanRightAction(this);
		ZoomAction zoomAction = new  ZoomAction(this);
		AttackAction attackAction = new AttackAction(this, protClient);
		DropAction dropAction = new DropAction(this, protClient);
		
	
		for (Controller c : controllers){
			if(c.getType() == Controller.Type.KEYBOARD){
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.W, 
									fwdAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.S, 
									bwdAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.D, 
									turnRAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.A, 
									turnLAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.UP, 
									panUpAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.DOWN, 
									panDownAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.LEFT, 
									panLeftAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.RIGHT, 
									panRightAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.Q, 
									zoomAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Key.E, 
									zoomAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);

				
				
				setCamera(c);
				
			}
			else if ( c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK){
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Axis.Y, 
									fwdStickAction,
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c,
									net.java.games.input.Component.Identifier.Axis.X, 
									turnStickAction, 
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);
				im.associateAction(
									c,
									net.java.games.input.Component.Identifier.Axis.Z, 
									zoomAction, 
									InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
									);	
				im.associateAction(
									c,
									net.java.games.input.Component.Identifier.Button._2, 
									attackAction, 
									InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY
									);
				im.associateAction(
									c,
									net.java.games.input.Component.Identifier.Button._0, 
									dropAction, 
									InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY
									);	
				setCamera(c);
			}
			else if ( c.getType() == Controller.Type.MOUSE){
				im.associateAction(
									c, 
									net.java.games.input.Component.Identifier.Button.LEFT, 
									attackAction,
									InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY
									);
			}
		}
		
	}
	
	private void setCamera(Controller controller){
		Camera c = (engine.getRenderSystem()).getViewport("LEFT").getCamera();
		orbitController = new CameraOrbit3D(c, cameraL, controller, engine);
	}
	
	// ---------- NETWORKING SECTION ----------------

	public ObjShape getGhostShape() { return ghostS; }
	public TextureImage getGhostTexture() { return ghosttx; }
	public GhostManager getGhostManager() { return gm; }
	public Engine getEngine() { return engine; }
	
	private void setupNetworking()
	{	isClientConnected = false;	
		try 
		{	protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} 	catch (UnknownHostException e) 
		{	e.printStackTrace();
		}	catch (IOException e) 
		{	e.printStackTrace();
		}
		if (protClient == null)
		{	System.out.println("missing protocol host");
		}
		else
		{	// Send the initial join message with a unique identifier for this client
			System.out.println("sending join message to protocol host");
			protClient.sendJoinMessage();
		}
	}
	
	protected void processNetworking(float elapsTime)
	{	// Process packets received by the client from the server
		if (protClient != null)
			protClient.processPackets();
	}

	public Vector3f getPlayerPosition() { return avatar.getLocalLocation(); }

	public void setIsConnected(boolean value) { this.isClientConnected = value; }
	
	private class SendCloseConnectionPacketAction extends AbstractInputAction
	{	@Override
		public void performAction(float time, net.java.games.input.Event evt) 
		{	if(protClient != null && isClientConnected == true)
			{	protClient.sendByeMessage();
			}
		}
	}
	
	
	//toggle Axis
	@Override
	public void keyPressed(KeyEvent e)
	{	switch (e.getKeyCode())
		{	case KeyEvent.VK_V:
				RenderStates xRender = x.getRenderStates();
				RenderStates yRender = y.getRenderStates();
				RenderStates zRender = z.getRenderStates();
				
				if(xRender.renderingEnabled()){
					xRender.disableRendering();
					yRender.disableRendering();
					zRender.disableRendering();
				}
				else{
					xRender.enableRendering();
					yRender.enableRendering();
					zRender.enableRendering();
				}
				break;
		}
		super.keyPressed(e);
	}
	
	private void runScript(File scriptFile)
	{	try
		{	FileReader fileReader = new FileReader(scriptFile);
			jsEngine.eval(fileReader);
			fileReader.close();
		}
		catch (FileNotFoundException e1)
		{	System.out.println(scriptFile + " not found " + e1); }
		catch (IOException e2)
		{	System.out.println("IO problem with " + scriptFile + e2); }
		catch (ScriptException e3) 
		{	System.out.println("ScriptException in " + scriptFile + e3); }
		catch (NullPointerException e4)
		{	System.out.println ("Null ptr exception reading " + scriptFile + e4);
		}
	}
	
	private void checkForCollisions()
	{	com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
		com.bulletphysics.collision.broadphase.Dispatcher dispatcher;
		com.bulletphysics.collision.narrowphase.PersistentManifold manifold;
		com.bulletphysics.dynamics.RigidBody object1, object2;
		com.bulletphysics.collision.narrowphase.ManifoldPoint contactPoint;
		
		boolean hit = false;
		dynamicsWorld = ((JBulletPhysicsEngine)physicsEngine).getDynamicsWorld();
		dispatcher = dynamicsWorld.getDispatcher();
		int manifoldCount = dispatcher.getNumManifolds();
		for (int i=0; i<manifoldCount; i++)
		{	manifold = dispatcher.getManifoldByIndexInternal(i);
			object1 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody0();
			object2 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody1();
			JBulletPhysicsObject obj1 = JBulletPhysicsObject.getJBulletPhysicsObject(object1);
			JBulletPhysicsObject obj2 = JBulletPhysicsObject.getJBulletPhysicsObject(object2);
			for (int j = 0; j < manifold.getNumContacts(); j++)
			{	contactPoint = manifold.getContactPoint(j);
				if (contactPoint.getDistance() < 0.0f)
				{	
					if(!hit) {rockHit(hit);}
					break;
				}
			}
		}
	}
	
	private void rockHit(boolean h){
		h = true;
		System.out.println("Rock Hit");
	}
	
	private double[] toDoubleArray(float[] arr)
	{	if (arr == null) return null;
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++)
		{	ret[i] = (double)arr[i];
		}
		return ret;
	}
	
	private float[] toFloatArray(double[] arr)
	{	if (arr == null) return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++)
		{	ret[i] = (float)arr[i];
		}
		return ret;
	}
	
	//------------------------------NPC/AI Section-------
	public AnimatedShape getNPCshape() { return slimeS; }
	public TextureImage getNPCtexture() { return slimeTx; }
	
	//Animations
	public void avatarWalk(){
		if(!isWalking){
			avS.stopAnimation();
			avS.playAnimation("WalkA", 0.5f, AnimatedShape.EndType.LOOP, 0);
		}
	}
	
	public void playAttack(){
		avS.stopAnimation();
		avS.playAnimation("AttackA", .5f, AnimatedShape.EndType.NONE, 0);
	}
	
	public void attackAnim(){
		slimeS.playAnimation("WalkSl", 0.2f, AnimatedShape.EndType.LOOP, 0);
	}
	
	public void playNPCDeath(){
		slimeS.playAnimation("DeathSl", 0.2f, AnimatedShape.EndType.PAUSE, 0);
	}
	
	public void enablePhysics(){
		running = true;
	}
	
	public void NPCHit(){
		if(slimeHealth > 0){slimeHealth -= 10;}
	}
 
}