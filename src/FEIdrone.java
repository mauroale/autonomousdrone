

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.tools.ant.taskdefs.Exit;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.FlyingMode;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.command.VideoBitRateMode;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.utils.ARDroneUtils;

public class FEIdrone {
	
	
	public final static int IMAGE_WIDTH = 640; // 640 or 1280
	public final static int IMAGE_HEIGHT = 360; // 360 or 720
	
	public final static int TOLERANCE = 40;
	
	
	//private static IARDrone drone = null;
	static IARDrone drone = null;
	static CommandManager cmd = null;
	static int velocidade = 20;
	static boolean markerFound = false;
	static int velocidadeDrone = 1; 
	public static  GUI gui;
//	public QRCodeScanner scanner;
	public MarkerPanel scanner;
	private AutoController autoController;
	private boolean autoControl = true; 
	
	public static void main(String[] args) throws InterruptedException
	{
		new FEIdrone();
	}
	
	
	public FEIdrone () throws InterruptedException  {
		
		this.scanner = new MarkerPanel();

		try {
			droneStart();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cmd = drone.getCommandManager();
		cmd.setFlyingMode(FlyingMode.FREE_FLIGHT).doFor(100);
		
		
		System.out.println("STARTING.....");
//		fly();
		ErrorListener();
		beforeSearch();


		
		if ( autoControl ){
			// auto controller is instantiated, but not started
			autoController = new AutoController(drone);
			this.enableAutoControl( autoControl );
		} else {
			drone.landing();
		}
		
		
		/*
		while ( !markerFound ) {
			//spin();
			scan();
		}
		*/

		//bateria();
		
		
		
	}
	
		
	public void droneStart () throws InterruptedException {
				
		//VideoCodec videoCodec = new VideoCodec();
		
		drone = new ARDrone();		
		drone.reset();
		
		
		ConfigurationManager conf =  drone.getConfigurationManager();
		drone.getCommandManager().setEnableCombinedYaw(true).doFor(100);
//		conf.
//		drone.getCommandManager().setVideoBitrate(4000);
//		drone.getCommandManager().setVideoCodecFps(30);
		drone.getCommandManager().setVideoBitrateControl(VideoBitRateMode.DYNAMIC);
       // drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
		drone.getCommandManager().setVideoCodec( VideoCodec.H264_360P );


		
		//drone.getCommandManager().setVideoOnUsb(true); // para gravar o video do drone. Colocar USB
		
		drone.getCommandManager().setOutdoor(false, false);
		drone.getCommandManager().setNavDataDemo(true);
		
		drone.getCommandManager().setMaxYaw(0.7f).doFor(1000);
		
		drone.setHorizontalCamera();

		drone.setMaxAltitude(1);
		//drone.setSpeed(velocidadeDrone);
		//drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
		drone.start();
		System.out.println("start feito");
//		Thread.sleep(3000);
//		System.out.println("sleep 3s");
		
	
		gui = new GUI(drone, this);
		drone.getVideoManager().addImageListener(gui);
		//scanner = new MarkerPanel();
		
		//drone.getVideoManager().addImageListener(scanner.imageListener);
		System.out.println("scanner's image listener added");
		//drone.getCommandManager().setGains(pq_kp, r_kp, r_ki, ea_kp, ea_ki, alt_kp, alt_ki, vz_kp, vz_ki, hovering_kp, hovering_ki, hovering_b_kp, hovering_b_ki)
		
		drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED, 3, 10);
//		drone.getCommandManager().flatTrim().doFor(100);
		TimeUnit.SECONDS.sleep(10);
		bateria();

	}
	
		
	public static void fly() {
		System.out.println("TAKE OFF");
					
		drone.takeOff();
		
		drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_GREEN , 3, 10);
		cmd.hover().doFor(1000);
		
		
		
		//cmd.landing();
	}
	
	public static void beforeSearch() {
		System.out.println("WAIT.....");
//		cmd.hover().doFor(5000);
		drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED  , 3, 10);
//		cmd.hover().doFor(1000);
	}
	
	public static void spin() {
		cmd.spinRight( velocidade );
		//scan();
		
		new Thread()
		{
		    public void run() {
		        System.out.println("blah");
		        long t= System.currentTimeMillis();
				long end = t+30000;
				while(System.currentTimeMillis() < end) {
				  // do something
				  // pause to avoid churning
				  //Thread.sleep( xxx );
					System.out.println("DENTRO DO WHILE");
				}
				System.out.println("POUSANDO DEPOIS DE 30s");
				cmd.landing();
		    }
		}.start();
		
		
		
	}
	
	public static void bateria () {
		
		//System.out.println("PORCENTAGEM DA BATERIA");
		drone.getNavDataManager().addBatteryListener(new BatteryListener() {
			
			public void batteryLevelChanged(int percentage)
			{
				//System.out.println("Battery: " + percentage + " %");
				gui.bateria =  Integer.toString( percentage )  ;
			}
			
			public void voltageChanged(int vbat_raw) { }
		});
			
	}
	
	
	
	public static void ErrorListener () {
		

		
		drone.addExceptionListener(new IExceptionListener() {
		    public void exeptionOccurred(ARDroneException exc)
		    {
		    	System.out.println("BATEU AQUI NO ERRO");
		        exc.printStackTrace();
		        
		        
		        
		        if (!drone.getVideoManager().isConnected() ) {
 					// Try to reconnect to the ARDreon if the socket is currently closed
		        	System.out.println("Tentou reconectar no drone");
		        	//drone.getNavDataManager().connect(ARDroneUtils.NAV_PORT  );
		        	
		        	try {
		        		drone.getVideoManager().close();
		        		Thread.sleep(2000);
						drone.getVideoManager().connect( ARDroneUtils.NAV_PORT  );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//		        	drone.landing();
//		        	drone.stop();
//			        System.exit(0);
		        	catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				}

		        
		     

		    }
		});

		
/*		
		
		drone.getNavDataManager().addAttitudeListener(new AttitudeListener() {
			
		    public void attitudeUpdated(float pitch, float roll, float yaw)
		    {
		        System.out.println("Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
		    }
	
		    public void attitudeUpdated(float pitch, float roll) { }
		    public void windCompensation(float pitch, float roll) { }
		});
*/
	}
	
	
	public static void scan ( ) {
		
		//drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED  , 3, 10);
		QRCodeScanner scanner = new QRCodeScanner();
		
		//scanner.addListener(gui);
		
		drone.getVideoManager().addImageListener(gui);		
		drone.getVideoManager().addImageListener(scanner);
	}
	
	public void pusar () {		
		drone.landing();
    	drone.stop();
    	System.out.println("DRONE STOP");
        System.exit(0);
		
	}
	/*
	public void scanMarker ( ) {
	
		//drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED  , 3, 10);
		MarkerPanel scanner = new MarkerPanel();
		scanner.setup();
		//scanner.addListener(gui);
		
		drone.getVideoManager().addImageListener(gui);		
		drone.getVideoManager().addImageListener(scanner.imageListener);
	}
	*/
	
	public void enableAutoControl(boolean enable) throws InterruptedException
	{
		if (enable)
		{
			//scanner.setup();
			//scanner.addListener(autoController);
			autoController.run( drone , gui );
		}
		else
		{
			autoController.stopController();
			//scanner.removeListener(autoController); // only auto autoController registers as TagListener
		}
	}
	
	
	
	
	
}
