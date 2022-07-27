import java.util.ArrayList;
import java.util.Collections;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;



public class World {

	int _dx;
	int _dy;
	int dz;

	int Buffer0[][][];
	int Buffer1[][][];

	boolean buffering;
	boolean cloneBuffer; // if buffering, clone buffer after swith

	int activeIndex;
	double pherbe= 0.003;//0.03// probabiliter d'apparition de l'herbe
	double pF=0.0000;//probabilite de prendre feu pour les arbres
	double pA=0.00006;// 0.00006// probabilite d'apparition des arbres
	double pinondation=0.;//0.0002// probabilite d'une inondation
	double perruption=0.02;//probabilite d'une erruption
	double dherbe=0.8;//0.5 // densite herbe
	double darbre = 0.8; //0.55; // densite arbre
	double hautSOL=0.5;// hauteur du sol 1 exclu
	double nivE=0.4;//0.4 // hauteur de l'eau
	double nivNG=0.85;// seuil d'apparition neige//0.85
	int tmpSoLave=150;//iteration avant solidification de la lave 
	int nbpredateur = 10;//20
	int nbproies =30;//45//20
	int nbfourmis=1;
	int vLave=0;//vitesse d'etalement lave
	int nivEdeb;
	int NbArbreSaint=0,NbArbreFeu=0;
	int tabZ[][];
	int tabD[][][];
	int tabA[][][];
	int tabFT[][][];
	int tabE[][][];
	int stone[][][];
	int lave[][][];
	int solid[][][];
	//int source[][][];// plusieur sources ?
	int sourceX;
	int sourceY;
	int sourceZ;
	ArrayList<Agent> agents;
	ArrayList<Integer> list;

	int bE=0;//1 si monter des eaux -1 si descente
	int bErupt=0;// 1 si erruption
	int rVolcan;//rayon delimite perimetre coulee de lave  

	public static final String IMG = "landscape_default-128.png";


	public World ( int __dx , int __dy, int __dz, boolean __buffering, boolean __cloneBuffer )
	{
		_dx = __dx;
		_dy = __dy;
		dz = __dz;
		//conversion double en int 0<=int<dz
		hautSOL=hautSOL*dz;
		nivE=nivE*hautSOL;
		nivEdeb=(int) nivE;
		nivNG=nivNG*hautSOL;
		rVolcan=(int) (Math.sqrt(((_dx*_dy)/2)/(2*Math.PI)));

		buffering = __buffering;
		cloneBuffer = __cloneBuffer;

		Buffer0 = new int[_dx][_dy][3];
		Buffer1 = new int[_dx][_dy][3];
		activeIndex = 0;

		agents = new ArrayList<Agent>();
		list=new ArrayList<Integer>();

		tabZ=new int[_dx][_dy];
		tabD=new int[_dx][_dy][dz];
		tabA=new int[_dx][_dy][dz];
		tabFT=new int[_dx][_dy][dz];
		tabE=new int[_dx][_dy][dz];
		lave=new int[_dx][_dy][dz];
		stone=new int[_dx][_dy][dz];
		solid=new int[_dx][_dy][dz];
		//source=new int[_dx][_dy][dz];
		//sourceX=new int[1];
		//sourceY=new int[1];
		//sourceZ=new int[1];
		readIMG(tabZ);
		int s=0;
		int cpt=0;

		for ( int x = 0 ; x != _dx ; x++ ){
			for ( int y = 0 ; y != _dy ; y++ )
			{
				int xe=(x+1+_dx)%_dx;
				int xo=(x-1+_dx)%_dx;
				int yn=(y-1+_dy)%_dy;
				int ys=(y+1+_dy)%_dy;

				Buffer0[x][y][0]=255;
				Buffer0[x][y][1]=255;
				Buffer0[x][y][2]=255;
				Buffer1[x][y][0]=255;
				Buffer1[x][y][1]=255;
				Buffer1[x][y][2]=255;

				if(getNivSOL(x,y)< (int)nivE){
					for(int z=getNivSOL(x,y)+1;z<=(int)nivE;z++){
						tabE[x][y][z]=1; //	Eau
						drawE(x,y);
					}
				}

				for(int z=0;z<getNivSOL(x,y);z++){
					tabD[x][y][z]=1; //	Terre
					solid[x][y][z]=1;
				}

				if (Math.random()<dherbe 
						&& getNivSOL(x,y) > (int)nivE
						&& getNivSOL(x,y) <= (int)nivNG){
					tabD[x][y][getNivSOL(x,y)]=2; //herbe
					solid[x][y][getNivSOL(x,y)]=1;
					drawD(x,y);
				}else{
					tabD[x][y][getNivSOL(x,y)]=1;//terre
					solid[x][y][getNivSOL(x,y)]=1;
					drawD(x,y);
				}
				if(getNivSOL(x,y)==(int)nivE){
					tabD[x][y][getNivSOL(x,y)]=3;//sable
					solid[x][y][getNivSOL(x,y)]=1;
					drawD(x,y);
				}
				if(getNivSOL(x,y)>(int)nivNG){
					tabD[x][y][getNivSOL(x,y)]=4;//neige
					solid[x][y][getNivSOL(x,y)]=1;
					drawD(x,y);
				}


				//volcan
				if (getNivSOL(x,y)>=(hautSOL-2) && s==0){
					//System.out.println(x+":"+y);
					//source[x][y][getNivSOL(x,y)+1]=0;
					sourceX=x;//75
					sourceY=y;//40
					sourceZ=getNivSOL(sourceX,sourceY);
					s=1;
					for ( int i =(sourceX-11+_dx)%_dx  ;((sourceX-11+_dx)%_dx <= i && i <=(sourceX+11+_dx)%_dx) || (_dx-12<=i || i <=13); i=(i+1+_dx)%_dx ){
						for ( int j =(sourceY-11+_dy)%_dy ;(( sourceY-11+_dy)%_dy <= j && j <=(sourceY+11+_dy)%_dy) || (_dy-12<= j || j <=13);j=(j+1+_dy)%_dy){
							//pose colonne de pierre
							if((int)(distance(i,j,sourceX,sourceY))<=10){
								for(int z=getNivSOL(i,j)+1;z<=sourceZ;z++){
									if(tabD[i][j][z]!=0){
										tabD[i][j][z]=0;
									}
									stone[i][j][z]=1;
									solid[i][j][z]=1;
									if(z==getNivSOL(i,j)+1)setNivSOL(i,j,1);
								}
							}
							//creuse un cratere*/
							if(distance(i,j,sourceX,sourceY)<10 && getNivSOL(i,j)==sourceZ /*&& stone[i][j][sourceZ]==0*/){
								tabD[i][j][getNivSOL(i,j)]=0;
								stone[i][j][getNivSOL(i,j)]=0;
								solid[i][j][getNivSOL(i,j)]=0;
								lave[i][j][getNivSOL(i,j)]=1;
								setNivSOL(i,j,-1);
							}
						}
					}
				}

				if ( darbre >= Math.random() 
						&& getNivSOL(x,y)+1!=dz
						&& lave[x][y][getNivSOL(x,y)+1]==0
						&& getNivSOL(x,y)+1 > (int)nivE
						&& getNivSOL(x,y) <= (int)nivNG){
					tabA[x][y][getNivSOL(x,y)+1]=1; // Arbre
					solid[x][y][getNivSOL(x,y)+1]=1;
					drawA(x,y);
					NbArbreSaint+=1;
				}


				/*
	    				 	tabE[(x-cpt+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x-cpt+_dx)%_dx,(y+_dy)%_dy)+1]=1;
							tabE[(x+cpt+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x+cpt+_dx)%_dx,(y+_dy)%_dy)+1]=1;
							tabE[(x+_dx)%_dx][(y-cpt+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y-cpt+_dy)%_dy)+1]=1;
							tabE[(x+_dx)%_dx][(y+cpt+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y+cpt+_dy)%_dy)+1]=1;
							tabE[(x+cpt+_dx)%_dx][(y+cpt+_dy)%_dy][getNivSOL((x+cpt+_dx)%_dx,(y+cpt+_dy)%_dy)+1]=1;
							tabE[(x-cpt+_dx)%_dx][(y-cpt+_dy)%_dy][getNivSOL((x-cpt+_dx)%_dx,(y-cpt+_dy)%_dy)+1]=1;
							tabE[(x+cpt+_dx)%_dx][(y-cpt+_dy)%_dy][getNivSOL((x+cpt+_dx)%_dx,(y-cpt+_dy)%_dy)+1]=1;
							tabE[(x-cpt+_dx)%_dx][(y+cpt+_dy)%_dy][getNivSOL((x-cpt+_dx)%_dx,(y+cpt+_dy)%_dy)+1]=1;
				 */

				list.add(cpt);
				cpt++;
			}
		}
	}

	private static int[] getPixelData(BufferedImage img, int x, int y) { 
		int [] rgb = new int[3];
		int rawvalue = img.getRGB(x, y);
		rgb[0] = ( rawvalue & 0x00FF0000 ) / (int)Math.pow(256,2); // red
		rgb[1] = ( rawvalue & 0x0000FF00 ) / 256; // green
		rgb[2] = ( rawvalue & 0x000000FF ); // blue
		//System.out.println("rgb: " + rgb[0] + " " + rgb[1] + " " + rgb[2]);
		return rgb;
	}

	public void readIMG(int tab[][]){
		BufferedImage img;
		try {
			img = ImageIO.read(new File(IMG));
			int[] rgb;

			for(int i = 0; i < img.getHeight(); i++){
				for(int j = 0; j < img.getWidth(); j++){
					rgb = getPixelData(img, i, j);
					for(int x=(int)(i*_dx)/img.getHeight();x<(int)((i+1)*_dx)/img.getHeight();x++){
						for(int y=(int)(j*_dy)/img.getHeight();y<(int)((j+1)*_dy)/img.getHeight();y++){
							tab[x][y]=(int)(rgb[0]*(dz-1))/255;
						} 
					}
				}
			}
			/*
	        for ( int x = 0 ; x != _dx ; x++ ){
		    	for ( int y = 0 ; y != _dy ; y++ )
		    	{
		    		//System.out.print(getNivSOL(x,y)+" ");
		    		System.out.print(tabZ[x][y]+" ");
		    	}
		    	System.out.println();
	        }
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkBounds( int __x , int __y )
	{
		if ( __x < 0 || __x > _dx || __y < 0 || __y > _dy )
		{
			System.err.println("[error] out of bounds ("+__x+","+__y+")");
			System.exit(-1);
		}
	}

	public int getNivSOL(int x, int y){
		checkBounds (x,y);
		return (int)(tabZ[x][y]*(hautSOL/dz));
	}

	public void setNivSOL(int x, int y,int o){
		checkBounds (x,y);
		tabZ[x][y]=(int)(tabZ[x][y]+(dz/(hautSOL)*o));
	}

	public void drawA(int x,int y){
		checkBounds(x,y);
		int state=tabA[x][y][getNivSOL(x,y)+1];
		switch(state){
		case 0://vide
			break;
		case 1://arbre
			setCellState(x,y,0,(int)( ((128/2)*getNivSOL(x,y) +1) / (hautSOL) + 128/2 ),0);
			//setCellState (x,y,0,(128*getNivSOL(x,y))/dz,0);
			//setCellState (x,y,0,(120*dz)/(getNivSOL(x,y)),0);
			break;
		case 2://arbre en feu
			setCellState(x,y,255,200,0);
			break;
		case 3://arbre en cendre
			setCellState(x,y,0,0,0);
		}
	}

	public void drawD(int x,int y){
		checkBounds(x,y);
		int state=tabD[x][y][getNivSOL(x,y)];
		switch(state){
		case 0://vide
			break;
		case 1://terre
			//setCellState (x,y,0,(int)((128/2)*getNivSOL(x,y))/dz+128/2,0);
			//setCellState (x,y,(128*getNivSOL(x,y))/dz,(128*getNivSOL(x,y))/dz,(128*getNivSOL(x,y))/dz );
			setCellState (x,y,(int)((205*getNivSOL(x,y))/(hautSOL)),(int)((133*getNivSOL(x,y))/(hautSOL)),(int)((38*getNivSOL(x,y))/(hautSOL)) );
			break;
		case 2://terre+herbe
			setCellState (x,y,0,(int)( ((221/2)*getNivSOL(x,y)) / (hautSOL) + 221/2),0);
			//setCellState (x,y,0,(221*getNivSOL(x,y))/dz,0);
			//setCell}else setCellState (x,y,207,16,32);State (x,y,0,(221*dz)/(getNivSOL(x,y)),0);
			break;
		case 3://sable
			setCellState (x,y,194,178,128);
			break;
		case 4://terre+neige
			setCellState (x,y,(int)((255*getNivSOL(x,y))/(hautSOL)),(int)((255*getNivSOL(x,y))/(hautSOL)),(int)((255*getNivSOL(x,y))/(hautSOL)));
			break;
		}
	}

	public void drawE(int x,int y){
		checkBounds(x,y);
		int state=tabE[x][y][getNivSOL(x,y)+1];
		//int state=tabE[x][y][(int)nivE];
		switch(state){
		case 0://vide
			break;
		case 1://eau
			//setCellState (x,y,(int)((26*getNivSOL(x,y))/(dz/2)),(int)((84*getNivSOL(x,y))/(dz/2)),(int)((93*getNivSOL(x,y))/(dz/2)));
			//setCellState (x,y,(int)((26*getNivSOL(x,y))/nivE),(int)((84*getNivSOL(x,y))/nivE),(int)((93*getNivSOL(x,y))/nivE));
			setCellState (x,y,(int)(((26/2)*getNivSOL(x,y))/nivE+26/2),(int)(((84/2)*getNivSOL(x,y))/nivE+84/2),(int)(((93/2)*getNivSOL(x,y))/nivE+93/2));
			//setCellState (x,y,(int)(((67/2)*getNivSOL(x,y))/nivE+67/2),(int)(((214/2)*getNivSOL(x,y))/nivE+214/2),(int)(((237/2)*getNivSOL(x,y))/nivE+237/2));
		}
	}

	public void drawStone(int x,int y,int z){
		checkBounds(x,y);
		int state=stone[x][y][z];
		switch(state){
		case 0://vide
			break;
		case 1://pierre
			setCellState (x,y,(int)((172*getNivSOL(x,y))/(dz)),(int)((177*getNivSOL(x,y))/(dz)),(int)((181*getNivSOL(x,y))/(dz)) );
			//setCellState (x,y,(int)(((172/2)*getNivSOL(x,y))/dz+172/2),(int)(((177/2)*getNivSOL(x,y))/dz+177/2),(int)(((181/2)*getNivSOL(x,y))/dz+181/2));
			break;
		case 2://obsidienne
			setCellState (x,y,(int)( ((49/2)*z) / (hautSOL) + 49/2 ),0,0);
			//setCellState (x,y,(int)(((49/2)*z)/nivE+49/2),0,0);
		}
	}

	public void upPixel(int x,int y){
		checkBounds (x,y);
		int up=0;
		int upZ=dz/2;
		int upS=(int)nivE;
		//a verifier
		for(int z=getNivSOL(x,y);z<dz;z++){
			if(lave[x][y][z]!=0){
				up=2;
				upS=z;
				upZ=z;
			}
			else if( tabE[x][y][z]!=0){
				up=1;
			}else if(stone[x][y][z]!=0){
				up=3;
				upS=z;
				upZ=z;
			}
			else if(tabA[x][y][z]!=0){
				up=4;
			}else if (tabD[x][y][z]!=0){
				up=5;
				upS=z;
			}
		}
		int r,g,b;
		switch(up){
		case 0://vide
			break;
		case 1://eau
			setCellState (x,y,(int)(((26/2)*upS)/nivE+26/2),(int)(((84/2)*upS)/nivE+84/2),(int)(((93/2)*upS)/nivE+93/2));
			break;
		case 2://lave
			//setCellState (x,y,(int)( ((255/2)*getNivSOL(x,y)) / (hautSOL) + 255/2),(int)( distance( x,y,sourceX,sourceY)*50),0);
			if(lave[x][y][upZ]<=50){
				setCellState (x,y,(int)((55*upZ)/(dz)+200),(int)((150*distance( x,y,sourceX,sourceY))/rVolcan),0);
			}else{
				int vr=(int)((55*upZ)/(dz)+200);
				int vg=(int)((150*distance( x,y,sourceX,sourceY))/rVolcan);
				int vb=0;
				int pr=(int)((172*upZ)/(dz));
				int pg=(int)((177*upZ)/(dz));
				int pb=(int)((181*upZ)/(dz));
				r=vr+((pr-vr)*(lave[x][y][upZ]-50))/(tmpSoLave-50);
				g=vg-((vg-pg)*(lave[x][y][upZ]-50))/(tmpSoLave-50);
				b=vb+((pb-vb)*(lave[x][y][upZ]-50))/(tmpSoLave-50);
				setCellState (x,y,r,g,b);
			}
			break;
		case 3://pierre
			drawStone(x,y,upZ);
			break;
		case 4://arbre
			drawA(x,y);
			break;
		case 5://terre
			drawD(x,y);
			break;
		}
	}

	//calcul distance dans un monde torique
	public double distance( int ib,int jb,int ia,int ja){
		checkBounds (ib,jb);
		checkBounds (ia,ja);
		/*
		int ib1=ib-_dx,jb1=jb-_dy;int ib2=ib,jb2=jb-_dy;int ib3=ib+_dx,jb3=jb-_dy;
		int ib4=ib-_dx,jb4=jb;							int ib6=ib+_dx,jb6=jb;
		int ib7=ib-_dx,jb7=jb+_dy;int ib8=ib,jb8=jb+_dy;int ib9=ib+_dx,jb6=jb+_dy;
		 */
		double tmp=Double.MAX_VALUE;
		for(int i=-1;i<2;i++){
			ib=ib+i*_dx;
			for(int j=-1;j<2;j++){
				jb=jb+j*_dy;
				tmp=Math.min(tmp,Math.abs(Math.sqrt((jb-ja)*(jb-ja)+(ib-ia)*(ib-ia))));
				jb=jb-j*_dy;
			}
			ib=ib-i*_dx;
		}
		return tmp;
	}

	public void updateDirt(int i,int j){
		if( getNivSOL(i,j)+1!=dz){
			if(tabE[i][j][getNivSOL(i,j)+1]==0
					&& lave[i][j][getNivSOL(i,j)+1]==0
					&& tabD[i][j][getNivSOL(i,j)]==1 
					&& Math.random()<pherbe
					&& getNivSOL(i, j) <= (int)nivNG
					&& getNivSOL(i,j) > (int)nivE){
				tabD[i][j][getNivSOL(i,j)]=2;//herbe
			}
		}
	}

	public void updateForest(int x,int y){

		//PROBLEME SI X DIF DE Y !!!
		//System.out.println("x:"+x+",y:"+y);
		//verifier le type de sol !!
		if(getNivSOL(x,y)+1!=dz){// verifie l'accesibiliter a la coordonner
			if( tabE[x][y][getNivSOL(x,y)+1]==0 
					&& tabA[x][y][getNivSOL(x,y)+1]==0
					&& lave[x][y][getNivSOL(x,y)+1]==0){ // Pour une case sans arbre
				if(Math.random()<pA){
					tabA[x][y][getNivSOL(x,y)+1]=1;
					solid[x][y][getNivSOL(x,y)+1]=1;
					NbArbreSaint+=1;
				}
			}	
			else if(tabA[x][y][getNivSOL(x,y)+1]==2){  // Pour un arbre en feu
				tabA[x][y][getNivSOL(x,y)+1]=3;
				NbArbreFeu-=1;
			}
			else if(tabE[x][y][getNivSOL(x,y)+1]==0 && tabA[x][y][getNivSOL(x,y)+1]==1){ // Pour un arbre
				if(lave[x][y][getNivSOL(x,y)+1]!=0){
					tabA[x][y][getNivSOL(x,y)+1]=0;
					solid[x][y][getNivSOL(x,y)+1]=0;
				}else{
					if(Math.random()<pF){
						tabA[x][y][getNivSOL(x,y)+1]=2;
						NbArbreSaint-=1;
						NbArbreFeu+=1;
					}
					else{
						if(tabE[x][y][getNivSOL(x,y)+1] == 0) {
							if(	(tabA[(x-1+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x-1+_dx)%_dx,(y+_dy)%_dy)+1]==2 
									|| lave[(x-1+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x-1+_dx)%_dx,(y+_dy)%_dy)+1]!=0)
									&& getNivSOL((x-1+_dx)%_dx,(y+_dy)%_dy)+1< dz-1
									&& getNivSOL((x-1+_dx)%_dx,(y+_dy)%_dy)+1>= 0){
								tabA[x][y][getNivSOL(x,y)+1]=2;
								NbArbreSaint-=1;
								NbArbreFeu+=1;
							}else if((tabA[(x+_dx)%_dx][(y-1+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y-1+_dy)%_dy)+1]==2
									|| lave[(x+_dx)%_dx][(y-1+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y-1+_dy)%_dy)+1]!=0)
									&& getNivSOL((x+_dx)%_dx,(y-1+_dy)%_dy)+1< dz-1
									&& getNivSOL((x+_dx)%_dx,(y-1+_dy)%_dy)+1>= 0){
								tabA[x][y][getNivSOL(x,y)+1]=2;
								NbArbreSaint-=1;
								NbArbreFeu+=1;

							}else if((tabA[(x+1+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x+1+_dx)%_dx,(y+_dy)%_dy)+1]==2 
									|| lave[(x+1+_dx)%_dx][(y+_dy)%_dy][getNivSOL((x+1+_dx)%_dx,(y+_dy)%_dy)+1]!=0)
									&& getNivSOL((x+1+_dx)%_dx,(y+_dy)%_dy)+1< dz-1
									&& getNivSOL((x+1+_dx)%_dx,(y+_dy)%_dy)+1>= 0){ 
								tabA[x][y][getNivSOL(x,y)+1]=2;
								NbArbreSaint-=1;
								NbArbreFeu+=1;
							}else if((tabA[(x+_dx)%_dx][(y+1+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y+1+_dy)%_dy)+1]==2
									|| lave[(x+_dx)%_dx][(y+1+_dy)%_dy][getNivSOL((x+_dx)%_dx,(y+1+_dy)%_dy)+1]!=0)
									&& getNivSOL((x+_dx)%_dx,(y+1+_dy)%_dy)+1< dz-1
									&& getNivSOL((x+_dx)%_dx,(y+1+_dy)%_dy)+1>= 0){
								tabA[x][y][getNivSOL(x,y)+1]=2;
								NbArbreSaint-=1;
								NbArbreFeu+=1;
							}
						}
					}
				}
			} else if(tabA[x][y][getNivSOL(x,y)+1]==3){  // Pour un arbre en cendre
				tabA[x][y][getNivSOL(x,y)+1]=0;
				solid[x][y][getNivSOL(x,y)+1]=0;
			}
		}
	}

	//note: gerer l'ecoulement
	public void updateEau(int x,int y,int inond){
		if(getNivSOL(x,y)< (int)nivE){
			for(int z=getNivSOL(x,y)+1;(z<=(int)nivE) && (z<dz);z++){
				if(stone [x][y][z]==0 && tabD [x][y][z]==0){
					tabE[x][y][z]=1; //	Eau
					if(lave[x][y][z]>=tmpSoLave && tabE[x][y][z]!=0){
						tabE[x][y][z]=0;
						lave[x][y][z]=0;
						stone [x][y][z]=2;
						solid [x][y][z]=1;
						if(z==getNivSOL(x,y)+1){
							setNivSOL(x,y,1);
						}
					}
				}
			}
		}else{
			for(int z=(int)nivE+1;z<dz;z++){
				tabE[x][y][z]=0; //	Eau
			}
		}
		if(inond==1){/*&& be==-1*/
			if(tabA[x][y][getNivSOL(x,y)+1]==1 && getNivSOL(x,y)+1==(int)nivE){
				tabA[x][y][getNivSOL(x,y)+1]=0;//retire arbre
			}
			if(tabD[x][y][getNivSOL(x,y)]==2 && getNivSOL(x,y)+1==(int)nivE){
				tabD[x][y][getNivSOL(x,y)]=1;//retire herbe
			}
		}	
	}

	public void updateLave(int x,int y){
		int xe=(x+1+_dx)%_dx;
		int xo=(x-1+_dx)%_dx;
		int yn=(y-1+_dy)%_dy;
		int ys=(y+1+_dy)%_dy;
		if(getNivSOL(x,y)+1!=dz){
			if(lave[x][y][getNivSOL(x,y)+1]!=0 )
			{	//solidification
				if(tabE [x][y][getNivSOL(x,y)+1]!=0)lave[x][y][getNivSOL(x,y)+1]+=2;
				else lave[x][y][getNivSOL(x,y)+1]++;
				if(lave[x][y][getNivSOL(x,y)+1]>=tmpSoLave)
				{
					lave[x][y][getNivSOL(x,y)+1]=0;
					if(tabE [x][y][getNivSOL(x,y)+1]!=0){
						tabE [x][y][getNivSOL(x,y)+1]=0;
						stone[x][y][getNivSOL(x,y)+1]=2;
					}
					else stone[x][y][getNivSOL(x,y)+1]=1;
					solid[x][y][getNivSOL(x,y)+1]=1;
					setNivSOL(x,y,1);
				}else{//parametre coulee de lave : vitesse/limite temp/limite distance
					if(vLave<100 && lave[x][y][getNivSOL(x,y)+1]<=50 && distance(x,y,sourceX,sourceY)<rVolcan)
					{
						//if(vLave<100 && x<(sourceX+rVolcan)-(y-sourceY) && x>(sourceX-rVolcan)+(sourceY-y)  &&  y>(sourceY-rVolcan)-(sourceX-x) && y<(sourceY+rVolcan)+(x-sourceX) ){

						//note ://mettre la lave pour tou z switch case coulee randomiser
						if( getNivSOL(x,yn)+1 <= getNivSOL(x,y)+1
								&& tabD[x][yn][getNivSOL(x,yn)+1]==0
								&& stone[x][yn][getNivSOL(x,yn)+1]==0
								&& lave[x][yn][getNivSOL(x,yn)+1]==0)
						{
							lave[x][yn][getNivSOL(x,yn)+1]=lave[x][y][getNivSOL(x,y)+1];
							vLave++;
						}
						else if(getNivSOL(x,ys)+1 <= getNivSOL(x,y)+1
								&& tabD[x][ys][getNivSOL(x,ys)+1]==0
								&& stone[x][ys][getNivSOL(x,ys)+1]==0
								&& lave[x][ys][getNivSOL(x,ys)+1]==0)
						{
							lave[x][ys][getNivSOL(x,ys)+1]=lave[x][y][getNivSOL(x,y)+1];
							vLave++;
						}
						else if(getNivSOL(xe,y)+1 <= getNivSOL(x,y)+1
								&& tabD[xe][y][getNivSOL(xe,y)+1]==0
								&& stone[xe][y][getNivSOL(xe,y)+1]==0
								&& lave[xe][y][getNivSOL(xe,y)+1]==0)
						{
							lave[xe][y][getNivSOL(xe,y)+1]=lave[x][y][getNivSOL(x,y)+1];
							vLave++;
						}
						else if( getNivSOL(xo,y)+1 <= getNivSOL(x,y)+1
								&& tabD[xo][y][getNivSOL(xo,y)+1]==0
								&& stone[xo][y][getNivSOL(xo,y)+1]==0
								&& lave[xo][y][getNivSOL(xo,y)+1]==0)
						{
							lave[xo][y][getNivSOL(xo,y)+1]=lave[x][y][getNivSOL(x,y)+1];
							vLave++;
						}
					}
				}
			}
		}
	}


	public void inondation(int it,int hautINO){
		if(it%10==0 ){//24
			if (nivE<hautINO && bE==1){
				nivE++;
			}
			if ((int)nivE==hautINO){
				bE=-1;
			}	
			if(nivE>nivEdeb && bE==-1){
				nivE--;
			}
			else if ((int)nivE==nivEdeb){
				bE=0;
			}
		}
	}


	public void stepWorld(int it) // world THEN agents
	{

		// inondation
		int inond=0;
		if(Math.random()<pinondation && bE==0)bE=1;
		if(bE!=0){
			inondation(it,(int)(hautSOL*0.7));
			inond=1;
		}

		//erruption
		if(bErupt==500)bErupt=0;
		if(Math.random()<perruption && bErupt==0)bErupt=1;

		// mise a jour asynchrone  
		Collections.shuffle(list);
		for(int i=0;i<list.size();i++){
			int x=list.get(i)%_dx;
			int y=list.get(i)/_dy;
			checkBounds (x,y);
			if(distance(x,y,sourceX,sourceY)<=10 && getNivSOL(x,y)+1==sourceZ && solid[x][y][sourceZ]==0 && getNivSOL(x,y)+1!=dz){
				lave[x][y][sourceZ]=1;//source lave
			}
			if(bErupt==1 && (int)(distance(x,y,sourceX,sourceY))==10 && getNivSOL(x,y)+1!=dz){
				if(tabD[x][y][sourceZ+1]==0)lave[x][y][getNivSOL(x,y)+1]=1;//fait deborder la source
			}
			updateForest(x,y);//arbre
			updateLave(x,y);//lave du volcan
			if(bE!=0)updateEau(x,y,inond);//mise ajour de l'eau// a refaire !!
			updateDirt(x,y);// mise ajour de la terre
			upPixel(x,y);//mise a jour pixel world
		}
		if(bErupt!=0)bErupt++;
		vLave=0;
	}

	public void stepAgents(int it) // world THEN agents
	{
		for ( int i = 0 ; i < agents.size() ; i++ )
		{
			if(agents.get(i) instanceof PreyAgent ) {
				PreyAgent pa = (PreyAgent)agents.get(i);
				if(pa._alive == false) {
					agents.remove(i);
					nbproies--;
				}
			}else if(agents.get(i) instanceof LangtonAnt ) {
				LangtonAnt la = (LangtonAnt) agents.get(i);
				if(la._alive == false) {
					agents.remove(i);
					nbproies--;
				}
			}else{
				PredatorAgent pa = (PredatorAgent)agents.get(i);
				if(pa._predator == false) {
					agents.remove(i);
					nbpredateur--;
				}
			}
		}

		for ( int i = 0 ; i != agents.size() ; i++ )
		{
			synchronized ( Buffer0 ) {
				agents.get(i).step();
			}
		}

	}

	/**
	 * Update the world state and return an array for the current world state (may be used for display)
	 * @return
	 */
	public void step (int it)
	{
		stepWorld(it);
		stepAgents(it);

		if ( buffering && cloneBuffer )
		{
			if ( activeIndex == 0 )
				for ( int x = 0 ; x != _dx ; x++ )
					for ( int y = 0 ; y != _dy ; y++ )
					{
						Buffer1[x][y][0] = Buffer0[x][y][0];
						Buffer1[x][y][1] = Buffer0[x][y][1];
						Buffer1[x][y][2] = Buffer0[x][y][2];
					}
			else
				for ( int x = 0 ; x != _dx ; x++ )
					for ( int y = 0 ; y != _dy ; y++ )
					{
						Buffer0[x][y][0] = Buffer1[x][y][0];
						Buffer0[x][y][1] = Buffer1[x][y][1];
						Buffer0[x][y][2] = Buffer1[x][y][2];
					}

			activeIndex = (activeIndex + 1 ) % 2; // switch buffer index
		}

	}

	public void add (Agent agent)
	{
		agents.add(agent);
	}

	public int[] getCellState ( int __x, int __y )
	{
		checkBounds (__x,__y);

		int color[] = new int[3];

		if ( buffering == false )
		{
			color[0] = Buffer0[__x][__y][0];
			color[1] = Buffer0[__x][__y][1];
			color[2] = Buffer0[__x][__y][2];
		}
		else
		{
			if ( activeIndex == 1 ) // read old buffer
			{
				color[0] = Buffer0[__x][__y][0];
				color[1] = Buffer0[__x][__y][1];
				color[2] = Buffer0[__x][__y][2];
			}
			else
			{
				color[0] = Buffer1[__x][__y][0];
				color[1] = Buffer1[__x][__y][1];
				color[2] = Buffer1[__x][__y][2];
			}
		}

		return color;
	}

	public void setCellState ( int __x, int __y, int __r, int __g, int __b )
	{
		checkBounds (__x,__y);

		if ( buffering == false )
		{
			Buffer0[__x][__y][0] = __r;
			Buffer0[__x][__y][1] = __g;
			Buffer0[__x][__y][2] = __b;
		}
		else
		{
			if ( activeIndex == 0 ) // write new buffer
			{
				Buffer0[__x][__y][0] = __r;
				Buffer0[__x][__y][1] = __g;
				Buffer0[__x][__y][2] = __b;
			}
			else
			{
				Buffer1[__x][__y][0] = __r;
				Buffer1[__x][__y][1] = __g;
				Buffer1[__x][__y][2] = __b;
			}
		}
	}

	public void setCellState ( int __x, int __y, int __color[] )
	{
		checkBounds (__x,__y);

		if ( buffering == false )
		{
			Buffer0[__x][__y][0] = __color[0];
			Buffer0[__x][__y][1] = __color[1];
			Buffer0[__x][__y][2] = __color[2];
		}
		else
		{
			if ( activeIndex == 0 )
			{
				Buffer0[__x][__y][0] = __color[0];
				Buffer0[__x][__y][1] = __color[1];
				Buffer0[__x][__y][2] = __color[2];
			}
			else
			{
				Buffer1[__x][__y][0] = __color[0];
				Buffer1[__x][__y][1] = __color[1];
				Buffer1[__x][__y][2] = __color[2];
			}	
		}
	}

	public int[][][] getCurrentBuffer()
	{
		if ( activeIndex == 0 || buffering == false ) 
			return Buffer0;
		else
			return Buffer1;		
	}

	public int getWidth()
	{
		return _dx;
	}

	public int getHeight()
	{
		return _dy;
	}


	public void display( CAImageBuffer image )
	{
		image.update(this.getCurrentBuffer());

		for ( int i = 0 ; i != agents.size() ; i++ )
			image.setPixel(agents.get(i)._x, agents.get(i)._y, agents.get(i)._redValue, agents.get(i)._greenValue, agents.get(i)._blueValue);
	}

}
