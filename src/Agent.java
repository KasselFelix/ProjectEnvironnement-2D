
public abstract class Agent {

	World _world;
	
	static int redId = 0;
	static int greenId = 1;
	static int blueId  = 2;
	
	int 	_x;
	int 	_y;
	int		_z;
	int 	_orient;
	int 	_etat;
	
	int 	_redValue;
	int 	_greenValue;
	int 	_blueValue;
	int 	_grayValue;
	int 	_whiteValue;
	
	public Agent( int __x, int __y,int __z, World __w )
	{
		_x = __x;
		_y = __y;
		_z= __z;
		_world = __w;
		
		_redValue =(int) ((255*_world.getNivSOL(_x,_y))/(_world.hautSOL));
		_greenValue = 0;
		_blueValue = 0;
		
		_orient = 0;
	}
	
	abstract public void step( );
	
}
