



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.yadrone.apps.paperchase.controller.PaperChaseAbstractController;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;



public class AutoController extends PaperChaseAbstractController
{
	private final static int SPEED = 1;
	private final static int YAW_SPEED = 1;
//	private final static int SLEEP = 300;
	
	private ScheduledExecutorService serviceSearch;
//	private ScheduledExecutorService serviceFollow;
	private IARDrone drone;
	static CommandManager cmd = null;
	private static boolean markerFound = false;
	private static int timeSearch = 1000;  	
	private static int timeoutFollow = 10000; // 2 minutos
	
	int x =0;
	MarkerPanel scanner ;
	
	public AutoController(IARDrone drone)
	{
		super(drone);
	}
	
	public void run () {
		
	}
	
	public void run( IARDrone drone , GUI gui) throws InterruptedException
	{
		this.drone = drone;
		cmd = drone.getCommandManager();
		int horizontalInit = 200;
		int horizontalEnd = 400;
		
		
		System.out.println("Entrou AUTO NO CONTROLER");
		scanMarker();
		//Runnable lostMarker;
		//lostMarker = () -> {
			
			
			//if( scanner.markerFound ) {
				
				//if ( scanner.markerFoundTime > timeLost ) {
					//gui.alvo = "PERDEU";
					//scanner.markerFound = false;
			//	}
			//}
			
				
			// perdeu o marcador por mais de 5 segundos
//		};
		

		//updateMarkerStatus = Executors.newSingleThreadScheduledExecutor();
		//updateMarkerStatus.scheduleAtFixedRate(lostMarker, 0, 500, TimeUnit.MILLISECONDS );			
		
		
		
		System.currentTimeMillis();
		Runnable searching;
		searching = () -> {
			System.currentTimeMillis();
//				System.out.println("Entrou aqui: " + x );
				x ++;
				
//				try {
//					spin3();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
				if (scanner.markerFound ) {
					gui.alvo = "SIM";
					System.out.println("                    MARCADOR ENCONTRADO           " );
					/*
					long timeLost = scanner.markerFoundTime + timeLostMarker;
					
					if ( scanner.markerFound ) {		
						
						if ( System.currentTimeMillis() > timeLost    ) {
							
							gui.alvo = "PERDI";
							scanner.markerFound = false;
							
						} else {
							
							
						}
					
					} 
					*/
//					System.out.println("Agora SEGUINDO: " + x );
//					System.out.println("RAW DISTANCE = " + scanner.markerX );
					
					
					String distancia = Double.toString (scanner.markerDistance) ;
					gui.distancia =  distancia.substring(0,5) + " cm";
					
					if(scanner.markerX <= horizontalInit) {
						markerFound = true;			
						gui.posicaoAlvo = "ESQ";
						System.out.println("Marker localizado a esquerda a " + scanner.markerDistance + "cm");
						for (int i = 20; i >= 0; i=i-10) {
							System.out.println("Speed: " + i);
							cmd.spinLeft(i+1).doFor(5);
						}
						cmd.freeze().doFor(5);
					}
					else if( scanner.markerX >= horizontalEnd) {
						
						//dist =  m.m23 / 3;			
//						markerDistance = Math.abs( dist );
						markerFound = true;			
						gui.posicaoAlvo = "DIR";
						System.out.println("Marker localizado a direita a " + scanner.markerDistance + "cm");
						for (int i = 20; i >= 0; i=i-10) {
							System.out.println("Speed: " + i);
							cmd.spinRight(i+1).doFor(5);
//							cmd.freeze().doFor(3);
						}
//						cmd.spinLeft(1).doFor(5);
						cmd.freeze().doFor(5);
					}
					else if (scanner.markerX < horizontalEnd && scanner.markerX > horizontalInit) {
						
//						dist =  m.m23 / 3;			
//						markerDistance = Math.abs( dist );
						markerFound = true;		
						gui.posicaoAlvo = "CENTRO";
						System.out.println("Marker localizado no centro a " + scanner.markerDistance + "cm");
						cmd.forward(SPEED).doFor(10);
//						follow2();
					}
					else {
						gui.posicaoAlvo = "-------";
					}
	
					/*
					try {
						
						follow2();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					
//					serviceSearch.shutdown();
					scanner.markerFound = false;
				}
				else {
					System.out.println("Marcador Nao Encontrado");	
				}
		};
			
			serviceSearch = Executors.newSingleThreadScheduledExecutor();
			serviceSearch.scheduleAtFixedRate(searching, 0, 200, TimeUnit.MILLISECONDS );			
			System.out.println("					SAIU DO WHILE" );
			x=0;
		
		
		//spin();
/*
		spin2();
		if (scanner.markerFound) {
			
			System.out.println("!!! MARCADOR ENCONTRADO !!!  FOLLOW !!!" );
			
			try {
				follow (  );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("MARCADOR ENCONTRADO. POUSANDO" );
			//cmd.landing();
		} else {
			
			System.out.println("NAO ACHOU, POUSAR");
			cmd.landing();
		}
*/
		
		
	}
	
	
	public void spin() {
		//Runnable runnable = new Runnable();
		//cmd.schedule(1000, cmd.spinRight(10) );
		//cmd.spinRight( velocidade );
		System.out.println("RODAR!!! DENTRO DO SPIN() " );
		
		new Thread()
		{
		    public void run(  ) {
		        //System.out.println("blah");
		        long t= System.currentTimeMillis();
				long end = t + timeSearch;
				while( System.currentTimeMillis() < end ) {
					//System.out.println("PROCURANDO RODANDO!!!" );
					
					//System.out.println("Distancia do marcador: " +  scanner.markerDistance );
					
					if (scanner.markerFound) {
						
						System.out.println("Marcador encontrado. BREAK " );
						break;
						
						
						/*
						System.out.println("!!! MARCADOR ENCONTRADO !!!  FOLLOW !!!" );
						
						try {
							follow ( scanner.markerDistance );
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						*/

						//System.out.println("MARCADOR ENCONTRADO. POUSANDO" );
						//cmd.landing();
					}
					
					 
				  // do something
				  // pause to avoid churning
				  //Thread.sleep( 1000 );
					  
					/*
					try {
						  System.out.println("PROCURANDO RODANDO!!!" );
						  //cmd.spinRight( velocidade );
						  Thread.currentThread().sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					//System.out.println("DENTRO DO WHILE");
				}
				
				
				
				System.out.println("MANDANDO POUSAR DEPOIS DE 60s");
				
				if( !markerFound ){
					System.out.println("POUSANDO");
					drone.landing();
			    	drone.stop();
			        System.exit(0);
				}
				
		    }
		}.start();
		
		
		
	}
	
	public void spin2 () throws InterruptedException {
		
		System.out.println("ENTROU NO SPIN2 " );
		long t= System.currentTimeMillis();
		long end = t + timeSearch;
		while( System.currentTimeMillis() < end ) {
						
			
			if (!scanner.markerFound) {
				
				System.out.println("GIRANDO ENQUANTO NAO ACHA MARCADOR " );
				//cmd.schedule(1000, cmd.spinRight(10) );
				
				//drone.getCommandManager().goRight(SPEED);
				
				
				//cmd.goRight(velocidade).doFor(1000);
				
				
				//Thread.currentThread().sleep(SLEEP);
				
				
				
			} else {
				System.out.println("Marcador encontrado. BREAK " );
				end = 0;
				//cmd.landing();				
				return;
			}
			
		}
		
		
	}
	
	
	public void spin3 () throws InterruptedException {
			
			System.out.println("ENTROU NO SPIN3 " );
			
			
			if (!scanner.markerFound) {
				
				System.out.println("GIRANDO ENQUANTO NAO ACHA MARCADOR " );
				//cmd.schedule(1000, cmd.spinRight(10) );
				
				//drone.getCommandManager().goRight(SPEED);
				
				
				//cmd.goRight(velocidade).doFor(1000);
				
				
				//Thread.currentThread().sleep(SLEEP);
				
				
				
			} else {
				System.out.println("Marcador encontrado. BREAK " );
				
				//cmd.landing();				
				return;
			}
			
			
			
	}
	
	
	
	public void follow () throws InterruptedException {
		
		double distancia ;
		distancia = scanner.markerDistance;
		
		// mais que 50 cm de distancia, se aproxima
		System.out.println("!!! MARCADOR ENCONTRADO !!! DISTANCIA DE: "  + distancia);
//		drone.landing();
//    	drone.stop();
//		System.exit(0);
				
		long t= System.currentTimeMillis();
		long end = t + timeoutFollow;
		while( System.currentTimeMillis() < end ) {
			
			distancia = scanner.markerDistance;
			//System.out.println("!!! DISTANCIA DE: "  + distancia);
			
			if( distancia < 50 )  {
				System.out.println("!!! MARCADOR MUITO PROXIMO !!!  HOVER " + distancia);
				drone.getCommandManager().hover();
				//Thread.currentThread().sleep(SLEEP);
				
			
			} else if ( distancia > 50 ) {
				System.out.println("!!! CHEGANDO MAIS PROXIMO !!!  FORWARD" + distancia);
				
				drone.getCommandManager().forward(SPEED);
				//Thread.currentThread().sleep(SLEEP);
			}
			
			cmd.freeze();
			Thread.sleep(1000); // slow down while
		}
		
		System.out.println(" SAIU DO FOLLOW ");
		drone.landing();
    		drone.stop();
        System.exit(0);
	}
	
	public void follow2 ()  {
		
		double distancia ;
		distancia = scanner.markerDistance;
		
		// mais que 50 cm de distancia, se aproxima
		System.out.println("!!! MARCADOR ENCONTRADO !!! DISTANCIA DE: "  + distancia);
//		drone.landing();
//    	drone.stop();
//		System.exit(0);
				
		
			
			distancia = scanner.markerDistance;
			//System.out.println("!!! DISTANCIA DE: "  + distancia);
			
			if( distancia < 60 )  {
				System.out.println("!!! MARCADOR MUITO PROXIMO !!!  HOVER " + distancia);
				drone.getCommandManager().hover();
				//Thread.currentThread().sleep(SLEEP);
				
			
			} else if ( distancia > 60 ) {
				System.out.println("!!! CHEGANDO MAIS PROXIMO !!!  FORWARD" + distancia);
				
				drone.getCommandManager().forward(SPEED).doFor(500);
				//Thread.currentThread().sleep(SLEEP);
			}
		/*
		System.out.println(" SAIU DO FOLLOW ");
		drone.landing();
    	drone.stop();
        System.exit(0);
		*/
		
	}
	
	public void scanMarker ( ) {
		this.scanner = new MarkerPanel();
		this.scanner.setup();
		drone.getVideoManager().addImageListener( scanner.imageListener );
	}
}
