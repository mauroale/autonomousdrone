import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;

public class PID {
	
	private IARDrone drone;
	private double   distanciaAlvo = 1; // 1 metro de distancia
	static CommandManager cmd = null;
	private int pi;
	private int pk;
	private int pd;
	private int SPEED = 10;
	private final static int SLEEP = 500;
	
	public PID ( IARDrone drone2 ) {
		this.drone = drone;
		cmd = drone.getCommandManager();
	}
	
	public void action (double distance ) {
		
		if ( distance > distanciaAlvo ) {
			aproximar();
		} else if ( distance < distanciaAlvo ) {
			distanciar();
			
		}
		
	}
	
	public void aproximar () {
		System.out.println("Chegando perto do alvo");
		cmd.forward( SPEED );
	}
	
	public void distanciar () {
		System.out.println("Afastando do alvo");
		cmd.backward( SPEED );
		cmd.hover();
	}
	
	
	public void centralizar () {
		System.out.println("Centralizando o alvo");
		cmd.hover();
	}
	
	public void estacionar () {
		System.out.println("ESTACIONANDO");
		cmd.hover().doFor( SLEEP );
	}
	
	
}
