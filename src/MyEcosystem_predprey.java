

public class MyEcosystem_predprey extends CAtoolbox {


	public static void main(String[] args) {

		// initialisation generale
	    
		int dx = 200;//20
		int dy = 200;//20
		int dz = 40;//10
		
		int displayWidth = dx;  // 
		int displayHeight = dy; // 

		// pick dimension for display
		if ( displayWidth < 200 )
			displayWidth = displayWidth*2;
		else
			if ( displayWidth > 600 )
				displayWidth = 1000;
			else
				if ( displayWidth < 600 )
					displayWidth = 800; 
		if ( displayHeight < 200 )
			displayHeight = displayHeight*2;
		else
			if ( displayHeight > 600 )
				displayHeight = 1000;
			else
				if ( displayHeight < 600 )
					displayHeight = 800;
		
		
		int delai = 100;//100 // -- delay before refreshing display -- program is hold during delay, even if no screen update was requested. USE WITH CARE. 
		int nombreDePasMaximum = Integer.MAX_VALUE;
		int it = 0;
		int displaySpeed =1 ;//50; // from 1 to ...
		
		CAImageBuffer image = new CAImageBuffer(dx,dy);
	    ImageFrame imageFrame =	ImageFrame.makeFrame( "My Ecosystem", image, delai, displayWidth, displayHeight );
	    
	    // initialise l'ecosysteme
	    
		World world = new World(dx,dy,dz,true,true);
		
		int px=0;
		int py=0;
		for ( int i = 0 ; i != world.nbproies; i++ ){
			px=(int)(Math.random()*dx);
			py=(int)(Math.random()*dy);
			world.add(new PreyAgent(px,py,world.getNivSOL(px, py)+1,world));
		}
		for ( int i = 0 ; i != world.nbpredateur; i++ ){
			px=(int)(Math.random()*dx);
			py=(int)(Math.random()*dy);
			world.add(new PredatorAgent(px,py,world.getNivSOL(px, py)+1,world));
		}
		for ( int i = 0 ; i != world.nbfourmis; i++ ){
			px=(int)(Math.random()*dx);
			py=(int)(Math.random()*dy);
			world.add(new LangtonAnt(px,py,world.getNivSOL(px, py),world));
		}
			
		
	    // mise a jour de l'etat du monde
		System.out.println("# it,nbproies,nbpredateur");
		//System.out.println("# it,nbproies");
		//System.out.println("# it,nbpredateur");
		while ( it != nombreDePasMaximum )
		{
			/*
			if(world.nbproies == 0 || world.nbpredateur==0) {
				it=nombreDePasMaximum-1;
			}*/
			
			// 1 - display
			if ( it % displaySpeed == 0 )
				world.display(image); 
			
			// 2 - update
			
			
			world.step(it);
			
			// 3 - iterate
			System.out.println(it+","+world.nbproies+","+world.nbpredateur);
			//System.out.println(it+","+world.nbpredateur);
			//System.out.println(it+","+world.nbproies);
			
			it++;
			try {
				Thread.sleep(delai);
			} catch (InterruptedException e) 
			{
			}
		}
		
	}

}
