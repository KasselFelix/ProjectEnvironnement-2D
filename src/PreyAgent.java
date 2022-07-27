
public class PreyAgent extends Agent {

	boolean _alive;
	double PreproD=0.02;//0.06
	double Prepro=PreproD;
	int energieMAX=100;//20
	int energie=energieMAX;//10
	public PreyAgent( int __x, int __y,int __z, World __w )
	{
		super(__x,__y,__z,__w);
		
		_redValue = 0;
		_greenValue = (int)((128*_world.getNivSOL(_x,_y)+1)/(_world.hautSOL));
		_blueValue = (int)((255*_world.getNivSOL(_x,_y)+1)/(_world.hautSOL));
		
		_alive = true;
	}
	
	public void step( )
	{
		// met a jour l'agent
		
		int b=0;
		int xn=_x;
		int xs=_x;
		int xe=(_x+1+_world._dx)%_world._dx;
		int xo=(_x-1+_world._dx)%_world._dx;
		int yn=(_y-1+_world._dy)%_world._dy;
		int ys=(_y+1+_world._dy)%_world._dy;
		int ye=_y;
		int yo=_y;
		//System.out.println(_z+","+_x+","+_y+"|"+_world.tabD[_x][_y][_z]+"|"+energie);
		
		
		//fuite si predateur autour
		
		for(Agent a : _world.agents) {
			if(a instanceof PredatorAgent){
					if((_x-1+_world._dx)%_world._dx == a._x && (_y+_world._dy)%_world._dy == a._y ){
						_orient=1;
						b=1;
						//System.out.println("fuite");
						break;
					}
					if( (_x+_world._dx)%_world._dx == a._x && (_y-1+_world._dy)%_world._dy == a._y ){
						_orient=2;
						b=1;
						//System.out.println("fuite");
						break;
					}
					if( (_x+1+_world._dx)%_world._dx == a._x && (_y+_world._dy)% _world._dy == a._y){
						_orient=3;
						b=1;
						//System.out.println("fuite");
						break;
						
					}
					if( (_x+_world._dx)%_world._dx == a._x && (_y+1+_world._dy)% _world._dy == a._y){
						_orient=0;
						b=1;
						//System.out.println("fuite");
						break;
					}
			}
		}
		if(b==0){
			if (Math.random() > 0.5) //deplacement au hasard
				_orient = (_orient + 1) % 4;
			else
				_orient = (_orient - 1 + 4) % 4;
		}
		
		//Broute
		int m=0;
		if(energie<(energieMAX/2) && b==0 && _world.tabE[_x][_y][_z-1]==0) {
			if(_world.tabD[_x][_y][_z-1]==2){
				_world.tabD[_x][_y][_z-1]=1;
				_world.upPixel(_x,_y);
				energie=energieMAX;//10
				m=1;
				//System.out.println("broute");
			}
		}
		
		
		// met a jour: la position de l'agent (depend de l'orientation)
		if(energie>2) {
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
		}
		
		
		//Broute 2 
		if(energie<(energieMAX/2) && b==0 && m==0 && _world.tabE[_x][_y][_z-1]==0) {
			if(_world.tabD[_x][_y][_z-1]==2){
				_world.tabD[_x][_y][_z-1]=1;
				_world.upPixel(_x,_y);
				energie=energieMAX;//10
				m=1;
				//System.out.println("broute 2");
			}
		}
		
		//reproduction
		if(Math.random()<Prepro) {
			PreyAgent prea=new PreyAgent(_x,_y,_z,_world);
			Agent delta= (Agent)prea;
			_world.agents.add(delta);
			_world.nbproies++;
		}
		
		// si rencontre feu //gagne etat feu arraylist//va chercher de l'eau
		if( (_world.tabA[xe][ye][_world.getNivSOL(xe,ye)+1]==2 ) 
				|| (_world.tabA[xn][yn][_world.getNivSOL(xn,yn)+1]==2 ) 
				|| (_world.tabA[xo][yo][_world.getNivSOL(xo,yo)+1]==2 ) 
				|| (_world.tabA[xs][ys][_world.getNivSOL(xs,ys)+1]==2 )
				|| (_world.tabA[(_x+_world._dx)%_world._dx][(_y+_world._dy)%_world._dy][_z]==2 )){
					_redValue = 255;
					_greenValue = 200;
					_blueValue = 0;
					_alive= false;
		}
		
		// si dans l'eau
		if(_world.tabE[_x][_y][_z]==1 ) {
			_redValue = 0;
			_greenValue = 0;
			_blueValue = 0;
			energie-=5;
		}
		
		//mise a jour energie
		if(m==0){
			if(energie>0){
				energie--;
			}else{_alive = false;}
			
			if(energie<3){
				_redValue = 205;
				_greenValue = 200;
				_blueValue = 255;
			}else{
				if(_world.tabE[_x][_y][_z]==1 ) {
	  				_redValue = 0;
					_greenValue = 0;
					_blueValue = 0;
					energie-=5;
	  			}else{
				_redValue = 0;
				_greenValue =(int) ((128*_world.getNivSOL(_x,_y))/(_world.hautSOL));
				_blueValue = (int) ((255*_world.getNivSOL(_x,_y))/(_world.hautSOL));
	  			}
			}
		}
		
		// si dans lave//
		if(_world.lave[_x][_y][_z]!=0 ) {
			_redValue = 255;
			_greenValue = 200;
			_blueValue = 0;
			_alive=false;
		}
		
		// limitation reproduction
		if(_world.nbproies<10){Prepro=PreproD;}
		else if(_world.nbproies > 20){Prepro=0;}
		else Prepro = PreproD;
	}
	
}
