
public class PredatorAgent extends Agent {

	boolean _predator;
	double PreproD=0.007;
	double Prepro=PreproD;
	int energieD=1005;//20
	int energie=energieD;
	public PredatorAgent( int __x, int __y,int __z, World __w )
	{
		super(__x,__y,__z,__w);
		
		_predator = true;
	}
	
	public void step( )
	{
		// met a jour l'agent
		
		// A COMPLETER
		

		/*int cellColor[] = _world.getCellState(_x, _y);
		cellColor[redId] = 255;
		cellColor[greenId] = 240;
		cellColor[blueId] = 225;*/

		//_world.setCellState(_x, _y, cellColor);
		
		int xn=_x;
		int xs=_x;
		int xe=(_x+1+_world._dx)%_world._dx;
		int xo=(_x-1+_world._dx)%_world._dx;
		int yn=(_y-1+_world._dy)%_world._dy;
		int ys=(_y+1+_world._dy)%_world._dy;
		int ye=_y;
		int yo=_y;
		int m=0;
		
		//mange
		for(Agent ag : _world.agents) {
				if(ag._z==_z && ag._x==_x && ag._y==_y && ag instanceof PreyAgent ) {
					PreyAgent pa = (PreyAgent)ag;
					pa._alive = false;
					energie=energieD;
					m=1;
					//System.out.println("devore");
					break;
				}
		}
		
		
		
		// met a jour: la position de l'agent (depend de l'orientation)
		switch (_orient) {
		case 0: // nord	
			if(_world.tabA[xn][yn][_world.getNivSOL(xn,yn)+1]==0 && _world.tabE[xn][yn][_world.getNivSOL(xn,yn)+1]==0){
				_y = yn;
				_z=_world.getNivSOL(_x,_y)+1;
			}else{
				int i;
				int j;
				int rx;
				int ry;
				int cpt=20;
				do{
					i=(int)(Math.random()*3)-1;
					j=(int)(Math.random()*3)-1;
					rx=(_x + i + _world.getWidth()) % _world.getWidth();
					ry=(_y + j + _world.getHeight()) % _world.getHeight();
					cpt--;
				}while( (_world.tabA[rx][ry][_world.getNivSOL(rx,ry)+1]==1 || _world.tabE[rx][ry][_world.getNivSOL(rx,ry)+1]==1) && cpt !=0);
				if(cpt!=0){
					_x=(_x + i + _world.getWidth()) % _world.getWidth();
					_y=(_y + j + _world.getHeight()) % _world.getHeight();
					_z=_world.getNivSOL(_x,_y)+1;
				}
			}
			break;
		case 1: // est
			if(_world.tabA[xe][ye][_world.getNivSOL(xe,ye)+1]==0 && _world.tabE[xe][ye][_world.getNivSOL(xe,ye)+1]==0){
				_x = xe;
				_z=_world.getNivSOL(_x,_y)+1;
			}else{
				int i;
				int j;
				int rx;
				int ry;
				int cpt=20;
				do{
					i=(int)(Math.random()*3)-1;
					j=(int)(Math.random()*3)-1;
					rx=(_x + i + _world.getWidth()) % _world.getWidth();
					ry=(_y + j + _world.getHeight()) % _world.getHeight();
					cpt--;
				}while( (_world.tabA[rx][ry][_world.getNivSOL(rx,ry)+1]==1 || _world.tabE[rx][ry][_world.getNivSOL(rx,ry)+1]==1)&& cpt!=0);
				if(cpt!=0){
				_x=(_x + i + _world.getWidth()) % _world.getWidth();
				_y=(_y + j + _world.getHeight()) % _world.getHeight();
				_z=_world.getNivSOL(_x,_y)+1;
				}
			}	
			break;
		case 2: // sud
			if(_world.tabA[xs][ys][_world.getNivSOL(xs,ys)+1]==0 && _world.tabE[xs][ys][_world.getNivSOL(xs,ys)+1]==0){
				_y = ys;
				_z=_world.getNivSOL(_x,_y)+1;
			}else{
				int i;
				int j;
				int rx;
				int ry;
				int cpt=20;
				do{
					i=(int)(Math.random()*3)-1;
					j=(int)(Math.random()*3)-1;
					rx=(_x + i + _world.getWidth()) % _world.getWidth();
					ry=(_y + j + _world.getHeight()) % _world.getHeight();
					cpt--;
				}while( (_world.tabA[rx][ry][_world.getNivSOL(rx,ry)+1]==1 || _world.tabE[rx][ry][_world.getNivSOL(rx,ry)+1]==1) && cpt!=0);
				if(cpt!=0){
				_x=(_x + i + _world.getWidth()) % _world.getWidth();
				_y=(_y + j + _world.getHeight()) % _world.getHeight();
				_z=_world.getNivSOL(_x,_y)+1;
				}
			}
			break;
		case 3: // ouest
			if(_world.tabA[xo][yo][_world.getNivSOL(xo,yo)+1]==0 && _world.tabE[xo][yo][_world.getNivSOL(xo,yo)+1]==0){
				_x = xo;
				_z=_world.getNivSOL(_x,_y)+1;
			}else{
				int i;
				int j;
				int rx;
				int ry;
				int cpt=20;
				do{
					i=(int)(Math.random()*3)-1;
					j=(int)(Math.random()*3)-1;
					rx=(_x + i + _world.getWidth()) % _world.getWidth();
					ry=(_y + j + _world.getHeight()) % _world.getHeight();
					cpt--;
				}while( (_world.tabA[rx][ry][_world.getNivSOL(rx,ry)+1]==1 || _world.tabE[rx][ry][_world.getNivSOL(rx,ry)+1]==1) && cpt!=0);
				//System.out.println(i+","+j);
				if(cpt!=0){
				_x=(_x + i + _world.getWidth()) % _world.getWidth();
				_y=(_y + j + _world.getHeight()) % _world.getHeight();
				_z=_world.getNivSOL(_x,_y)+1;
				}
			}
			break;
		}
		 
		 //poursuite si proie autour
		int b=0;
		for(Agent a : _world.agents) {
				if(a instanceof PreyAgent){
					if((_x-1+_world._dx)%_world._dx == a._x && (_y+_world._dy)%_world._dy == a._y ){
						_orient=3;
						b=1;
						//System.out.println("poursuite");
						break;
					}
					if( (_x+_world._dx)%_world._dx == a._x && (_y-1+_world._dy)%_world._dy == a._y ){
						_orient=0;
						b=1;
						//System.out.println("poursuite");
						break;
					}
					if( (_x+1+_world._dx)%_world._dx == a._x && (_y+_world._dy)% _world._dy == a._y){
						_orient=1;
						b=1;
						//System.out.println("poursuite");
						break;
					}
					if( (_x+_world._dx)%_world._dx == a._x && (_y+1+_world._dy)% _world._dy == a._y) {
						_orient=2;
						b=1;
						//System.out.println("poursuite");
						break;
					}
				}
			}
			if(b==0){
				if( Math.random() > 0.5 ) // au hasard
					_orient = (_orient+1) %4;
			
				else
					_orient = (_orient-1+4) %4;
			}
		 
		 	
		 	
		 	//mange
  			for(Agent ag : _world.agents) {
  					if(ag._z==_z && ag._x==_x && ag._y==_y && ag instanceof PreyAgent ) {
  						PreyAgent pa = (PreyAgent)ag;
  						pa._alive = false;
  						energie=energieD;
  						m=1;
  						//System.out.println("devore");
  						break;
  					}
  			}
  			
  			//reproduction
  			if(Math.random()<Prepro) {
				PredatorAgent prea=new PredatorAgent(_x,_y,_z,_world);
				Agent delta= (Agent)prea;
				_world.agents.add(delta);
				_world.nbpredateur++;
			}
  			
  		
  			
  		// si rencontre feu	//gagne etat feu arraylist////va chercher de l'eau
  			if( (_world.tabA[xe][ye][_world.getNivSOL(xe,ye)+1]==2 ) 
  					|| (_world.tabA[xn][yn][_world.getNivSOL(xn,yn)+1]==2 ) 
  					|| (_world.tabA[xo][yo][_world.getNivSOL(xo,yo)+1]==2 ) 
  					|| (_world.tabA[xs][ys][_world.getNivSOL(xs,ys)+1]==2 )
  					|| (_world.tabA[(_x+_world._dx)%_world._dx][(_y+_world._dy)%_world._dy][_z]==2 )){
  					_redValue = 0;
					_greenValue = 0;
					_blueValue = 0;
					_predator= false;
  			}
  		// si rencontre eau
  			if(_world.tabE[_x][_y][_z]==1 ) {
  				_redValue = 0;
				_greenValue = 0;
				_blueValue = 0;
				energie-=5;
  			}
  			
  		//mise a jour energie
  			if(m!=1){
  				if(energie>0) {
  			 		energie--;
  			 	}
  			 	else {
  			 		_predator=false;
  			 	}
  				
	  			if(energie<3){
	  				_redValue = 255;
	  				_greenValue = 200;
	  				_blueValue = 205;
	  			}else{
	  				if(_world.tabE[_x][_y][_z]==1 ) {
	  	  				_redValue = 0;
	  					_greenValue = 0;
	  					_blueValue = 0;
	  	  			}else{
	  				_redValue =(int) ((255*_world.getNivSOL(_x,_y)+1)/(_world.hautSOL));
	  				_greenValue = 0;
	  				_blueValue = 0;
	  	  			}
	  			}
  			}else{
  				_redValue = 255;
  				_greenValue = 0;
  				_blueValue = 255;
  			}
  		
  		// si dans lave//
  			if(_world.lave[_x][_y][_z]!=0 ) {
  				_redValue = 255;
  				_greenValue = 200;
  				_blueValue = 0;
  				_predator=false;
  			}
  			
  			//limtation reproduction
  			if(_world.nbpredateur <10){Prepro=PreproD*2;}
  			else if(_world.nbpredateur > 20){Prepro=0;}
  			else Prepro = PreproD;
	}
	
}
