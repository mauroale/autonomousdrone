
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

import de.yadrone.base.video.ImageListener;
import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.* ;

public class MarkerPanel implements ImageListener {

	private BufferedImage image = null;
	PImage img;
	MultiMarker nya;
	public long markerFoundTime ;
	public boolean markerFound = false;
	public double markerDistance = 0;
	public double markerX ;


	public MarkerPanel()
	{
		setup();
	}

	private long imageCount = 0;
	void settings () {
	}
	
	public void setup ( ) {
		PApplet papplet = new PApplet();
		/* MARCO */ 
			papplet.sketchPath("/Users/marco/Documents/Processing/libraries");
			nya = new MultiMarker(papplet, 640, 360, "/Users/marco/Documents/Faculdade/TCC/tccdrone/data/28-10-2.dat", NyAR4PsgConfig.CONFIG_PSG);
			nya.addARMarker("/Users/marco/Documents/Faculdade/TCC/tccdrone/data/patt.kanji", 80);
			nya.setLostDelay(0);
			

		/* MAURO */

//		papplet.sketchPath("/Users/skavurzka/Documents/Processing/libraries");				
//		nya = new MultiMarker(papplet,640,360,"C:\\Users\\Mauro\\Documents\\JavaProjects\\tccdrone\\data\\28-10-2.dat", NyAR4PsgConfig.CONFIG_PSG);
//		//nya.addARMarker("/Users/skavurzka/Documents/EclipseWorkspace/TCC/data/patt.hiro", 16,25,80);
//		nya.addARMarker("C:\\Users\\Mauro\\Documents\\JavaProjects\\tccdrone\\data\\patt.kanji",80);
//		
//		nya.setLostDelay(0);
			

//		papplet.sketchPath("/Users/skavurzka/Documents/Processing/libraries");				
//		nya = new MultiMarker(papplet,640,360,"/Users/skavurzka/Documents/EclipseWorkspace/TCC/data/28-10-2.dat", NyAR4PsgConfig.CONFIG_PSG);
//		nya.addARMarker("/Users/skavurzka/Documents/EclipseWorkspace/TCC/data/patt.hiro", 16,25,80);
//		nya.addARMarker("/Users/skavurzka/Documents/EclipseWorkspace/TCC/data/patt.kanji",80);
//		
//		nya.setLostDelay(0);
//			

	}
	
	private PImage getAsImage(BufferedImage bimg) {
		try {
			PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
			bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
			img.updatePixels();
			return img;
		} catch (Exception e) {
			System.err.println("Can't create image from buffer");
			e.printStackTrace();
		}
		return null;
	}

	private void setImage(final BufferedImage image) {
		this.image = image;

		this.img = getAsImage(this.image);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				repaint();
			}
		});
	}
	
	public ImageListener imageListener = new ImageListener() {
		public void imageUpdated(BufferedImage image) {
			setImage(image);
			try {
				if (img == null) {
					System.out.println("Imagem Nula");
				} else {
					detectMarker();
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	
	private PVector[] detectMarker() {
		img = getAsImage(image);
		nya.detect(img);
		if(nya.isExist(0)) {
			PVector[] vect = nya.getMarkerVertex2D(0);
			PMatrix3D m = nya.getMatrix(0);	
			int horizontalInit = 160;
			int horizontalEnd = 480;
			double dist;
			markerX = vect[0].x;
			if(vect[0].x <= horizontalInit) {
				
				System.currentTimeMillis();
				dist =  m.m23 / 3;			
				markerDistance = Math.abs( dist );
				markerFound = true;			
//				System.out.println("Marker localizado a esquerda a " + m.m23/3 + "cm");
			}
			else if(vect[1].x >= horizontalEnd) {
				
				System.currentTimeMillis();
				dist =  m.m23 / 3;			
				markerDistance = Math.abs( dist );
				markerFound = true;			
//				System.out.println("Marker localizado a direita a " + m.m23/3 + "cm");
			}
			else {
				
				System.currentTimeMillis();
				dist =  m.m23 / 3;			
				markerDistance = Math.abs( dist );
				markerFound = true;			
//				System.out.println("Marker localizado no centro a " + m.m23/3 + "cm");
			}
			return vect;
		}
		else {
			return null;
		}
		
	}

	

	@Override
	public void imageUpdated(BufferedImage image) {
		// TODO Auto-generated method stub
		if ((++imageCount % 2) == 0)
			return;
		
	}

	
}