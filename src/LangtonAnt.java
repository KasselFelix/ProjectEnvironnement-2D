
public class LangtonAnt extends Agent {
	boolean _alive;
 	public LangtonAnt( int __x, int __y,int __z, World __w )
	{
		super(__x,__y,__z,__w);
		
		_redValue = (128*_world.getNivSOL(_x,_y))/(_world.dz/2);
		_greenValue = (128*_world.getNivSOL(_x,_y))/(_world.dz/2);
		_blueValue = (128*_world.getNivSOL(_x,_y))/(_world.dz/2);
		
		_alive = true;
	}
	
	public void step( )
	{
		// met a jour: (1) la couleur du sol (2) l'orientation de l'agent
		
		//int cellColor[] = _world.getCellState(_x, _y);
		//_world.setCellState(_x, _y, cellColor);
		
		// met a jour: la position de l'agent (d�pend de l'orientation)
		 switch ( _orient ) 
		 {
         	case 0: // nord	
         		if(_world.tabFT[_x][_y][0]==0) {
         			_world.tabFT[_x][_y][0]=1;
         			if(_world.tabA[_x][_y][_world.getNivSOL(_x,_y)]==0 && _world.tabE[_x][_y][_world.getNivSOL(_x,_y)]==0)
         				_world.setCellState (_x, _y,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz);
         			_orient=3;
         		}else {
         			_world.tabFT[_x][_y][0]=0;
         			_world.upPixel(_x,_y);
         			_orient=1;
         		}
         		if(_orient == 3) {
         			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
         		}
         		else {
         			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
         		}
         		break;
         	case 1:	// est
         		if(_world.tabFT[_x][_y][0]==0) {
         			_world.tabFT[_x][_y][0]=1;
         			if(_world.tabA[_x][_y][_world.getNivSOL(_x,_y)]==0 && _world.tabE[_x][_y][_world.getNivSOL(_x,_y)]==0)
         				_world.setCellState (_x, _y,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz);
         			_orient=0;
         			
         		}else {
         			_world.tabFT[_x][_y][0]=0;
         			_world.upPixel(_x,_y);
         			_orient=2;
         		}
         		if(_orient==0) {
         			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
         		}
         		else {
         			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
         		}
 				break;
         	case 2:	// sud
         		if(_world.tabFT[_x][_y][0]==0) {
         			_world.tabFT[_x][_y][0]=1;
         			if(_world.tabA[_x][_y][_world.getNivSOL(_x,_y)]==0 && _world.tabE[_x][_y][_world.getNivSOL(_x,_y)]==0)
         				_world.setCellState (_x, _y,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz);
         			_orient=1;
         		}else {
         			_world.tabFT[_x][_y][0]=0;
         			_world.upPixel(_x,_y);
         			_orient=3;
         		}
         		if(_orient==1) {
         			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
         		}
         		else {
         			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
         		}
 				break;
         	case 3:	// ouest
         		if(_world.tabFT[_x][_y][0]==0) {
         			_world.tabFT[_x][_y][0]=1;
         			if(_world.tabA[_x][_y][_world.getNivSOL(_x,_y)]==0 && _world.tabE[_x][_y][_world.getNivSOL(_x,_y)]==0)
         				_world.setCellState (_x, _y,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz,(128*_world.getNivSOL(_x,_y))/_world.dz);
         			_orient=2;
         		}else {
         			_world.tabFT[_x][_y][0]=0;
         			_world.upPixel(_x,_y);
         			_orient=0;
         		}
         		if(_orient==2) {
         			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
         		}
         		else {
         			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
         		}
 				break;
		 }
		 _redValue = (128*_world.getNivSOL(_x,_y))/_world.dz;
		 _greenValue = (128*_world.getNivSOL(_x,_y))/_world.dz;
		 _blueValue = (128*_world.getNivSOL(_x,_y))/_world.dz;
		 
	}
	
	
}






