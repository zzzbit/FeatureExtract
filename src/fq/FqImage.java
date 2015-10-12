package fq;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import fq.Point;

public class FqImage {

	public static final double SOBELDOOR = 80.00;


	public BufferedImage	imh;
	public int 				height;
	public int 				width;
	public Point[][]		points;
	public double[][]		sobels;
	public int[][]			edge;
	public double[]			colorJuH	=	new double[4];
	public double[]			colorJuS	=	new double[4];
	public double[]			colorJuV	=	new double[4];
	public double[]			huJu		=	new double[8];
	
	

	public double getAbsColorDistance( FqImage other ){
		int width	=	FqMath.min(this.width, other.width);
		int	height	=	FqMath.min(this.height, other.height);
		
		int	sum	=	0;
		for( int i = 0 ; i < width ; i++ ){
			for( int j = 0 ; j < height ; j++ ){
				sum += this.points[i][j].pointDistance(other.points[i][j]);
			}
		}
		return	sum;
	}
	
	public double f( int i , int j ){
		double	temp	=	this.points[i][j].getGray();
		return	temp;
	}
	
	public void toSobel(){
		double	fx , fy;
		double	sobel;
		int		i , j;
		for( i = 1 ; i < this.width - 1 ; i++ ){
			for( j = 1 ; j < this.height - 1 ; j++ ){
				fx	=	(	f(i-1,j-1) + 2*f(i-1,j) + f(i-1,j+1)	)	-	(	f(i+1,j-1) + 2*f(i+1,j) + f(i+1,j+1)	);
				fy	=	(	f(i-1,j-1) + 2*f(i,j-1) + f(i+1,j-1)	)	-	(	f(i-1,j+1) + 2*f(i,j+1) + f(i+1,j+1)	);
				this.sobels[i][j]	=	FqMath.max( Math.abs(fx), Math.abs(fy) );			
			}
		}
	}

	public void toEdge(){
		int	i , j;
		for( i = 0 ; i < this.width ; i++ ){
			for( j = 0 ; j < this.height ; j++ ){
				if( this.sobels[i][j] > SOBELDOOR )
					this.edge[i][j] = 1;
				else
					this.edge[i][j] = 0;
			}
		}
	}
		
	public FqImage cutSmallest(){
		final double EDGEDOOR = 4;
		
		int	i , j;
		int left	=	0;
		int	right	=	0;
		int	top		=	0;
		int	bottom	=	0;
		int	count	=	0;
		
		
		for( i = 0 ; i < this.width ; i++ ){
			count = 0;
			for( j = 0 ; j < this.height ; j++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				left = i;
				break;
			}
		}
		
		for( i = this.width - 1 ; i >= 0 ; i-- ){
			count = 0;
			for( j = 0 ; j < this.height ; j++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				right = i;
				break;
			}
		}
		
		for( j = 0 ; j < this.height ; j++ ){
			count = 0;
			for( i = 0 ; i < this.width ; i++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				top = j;
				break;
			}
		}
		
		for( j = this.height - 1 ; j >= 0 ; j-- ){
			count = 0;
			for( i = 0 ; i < this.width ; i++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				bottom = j;
				break;
			}
		}
		
		BufferedImage subImh	=	this.imh.getSubimage(left, top, right-left , bottom-top);
		return new FqImage(subImh);
	
	
	}
	
	public void setColorJu(){
		int		i , j;
		int 	k;
		double	sumh , sums , sumv;
		
		this.colorJuH[1]	=	0.0;
		this.colorJuS[1]	=	0.0;
		this.colorJuV[1]	=	0.0;
		
//		System.out.println(this.width * this.height);
		for( k = 1 ; k <= 3 ; k++ ){
			sumh = 0.0;
			sums = 0.0;
			sumv = 0.0;
			for( i = 0 ; i < this.width ; i++ ){
				for( j = 0 ; j < this.height ; j++ ){
//					double	temp = this.points[i][j].getHHSV();
//					temp -= this.colorJuH[1];
//					temp = Math.pow(temp, k);
//					sumh += temp;
					sumh += Math.pow(	( this.points[i][j].getHHSV() - this.colorJuH[1] )	,	k	);
					sums += Math.pow(	( this.points[i][j].getSHSV() - this.colorJuS[1] )	,	k	);
					sumv += Math.pow(	( this.points[i][j].getVHSV() - this.colorJuV[1] )	,	k	);
				}
			}
			
			
//			System.out.println(k+":");
			
			this.colorJuH[k] = sumh / ( this.width * this.height );				
			if( k == 2 )
				this.colorJuH[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuH[k]	=	Math.cbrt(this.colorJuH[k]);
			
			
//			System.out.println(colorJuH[k]);
			
			this.colorJuS[k] = sums / ( this.width * this.height );
			if( k == 2 )
				this.colorJuS[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuS[k]	=	Math.cbrt(this.colorJuH[k]);
			
			this.colorJuV[k] = sumv / ( this.width * this.height );
			if( k == 2 )
				this.colorJuV[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuV[k]	=	Math.cbrt(this.colorJuH[k]);
			
//			System.out.println("sumh"+sumh+","+"sums"+sums+","+"sumv"+sumv);
		}
	}
	
	public double dColorJu( FqImage imh ){
		double	temph	=	0.0;
		double 	temps 	= 	0.0;
		double 	tempv 	= 	0.0;
		int		i;
		double	wh		=	5.0;
		double	ws		=	0.01;
		double	wv		=	0.01;
		for( i = 1 ; i <= 3 ; i++ ){
			temph += (	( this.colorJuH[i] - imh.colorJuH[i] ) * ( this.colorJuH[i] - imh.colorJuH[i] )	);
			temps += (	( this.colorJuS[i] - imh.colorJuS[i] ) * ( this.colorJuS[i] - imh.colorJuS[i] )	);
			tempv += (	( this.colorJuV[i] - imh.colorJuV[i] ) * ( this.colorJuV[i] - imh.colorJuV[i] ) );
		}
		return	Math.sqrt( temph * wh + temps * ws + tempv * wv );
	}
	
	public double m( int p , int q ){
		int 	x , y;
		double 	sum = 0;
		for( x = 0 ; x < this.width ; x++ )
			for( y = 0 ; y < this.height ; y++ )
				sum += Math.pow(x,p) * Math.pow(y,q) * this.edge[x][y];
		return sum;
	}
	
	public double miu( int p , int q ){
		int		x , y;
		double 	x0 , y0;
		double	sum = 0;
		x0 = m(1,0) / m(0,0);
		y0 = m(0,1) / m(0,0);		
		for( x = 0 ; x < this.width ; x++ )
			for( y = 0 ; y < this.height ; y++ )
				sum += Math.pow((x-x0), p) * Math.pow((y-y0), q) * this.edge[x][y];
		return sum;
	}
	
	public double eda( int p , int q ){
		int g = p+q+2;
		return miu(p,q) / Math.sqrt( Math.pow( miu(0,0), g ) );
	}
		
	
	public void toHuJu(){
		double	eda20 = eda(2,0);
		double	eda02 = eda(0,2);
		double 	eda22 = eda(2,2);
		double 	eda11 = eda(1,1);
		double	eda30 = eda(3,0);
		double	eda03 = eda(0,3);
		double	eda21 = eda(2,1);
		double	eda12 = eda(1,2);
		
		this.huJu[1]	=	eda20 + eda02;
		this.huJu[2]	=	(eda20-eda02) * (eda20-eda02)	+	4 * eda11 * eda11;
		this.huJu[3]	=	(eda30-3*eda12) * (eda30-3*eda12)	+	(eda03-3*eda21) * (eda03-3*eda21);
		this.huJu[4]	=	(eda30+eda12) * (eda30+eda12)	+	(eda21+eda03) * (eda21+eda03);
		this.huJu[5]	= 	(eda30-3*eda12) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)
							+	(3*eda21-eda03) * (eda21+eda03) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);
		this.huJu[6]	=	(eda20-eda02) * (	(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	)	+
							4 * eda11 * (eda30+eda12) * (eda21+eda03);
		this.huJu[7]	=	(3*eda21-eda03) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)	+
							(3*eda12-eda30) * (eda03+eda21) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);							
	}
	
	public double dHuJu( FqImage imh ){
		int			i;
		double[]	wHu	=	new double[8];
		wHu[1]		=	1.0;
		wHu[2]		=	1.0;
		wHu[3]		=	1.0;
		wHu[4]		=	1.0;
		wHu[5]		=	1.0;
		wHu[5]		=	1.0;
		wHu[6]		=	1.0;
		wHu[7]		=	1.0;
		double	sum	=	0.0;
		for( i = 1 ; i <= 7 ; i++ )
			sum += wHu[i]	*	( this.huJu[i] - imh.huJu[i] ) * ( this.huJu[i] - imh.huJu[i] );
		return	Math.sqrt(sum) * 10000;
	}
	
	public FqImage( BufferedImage imh ){
		this.imh	=	imh;
		this.height	=	imh.getHeight(null);
		this.width	=	imh.getWidth(null);
		this.points	=	new Point[width][height];
		this.sobels	=	new double[width][height];
		this.edge	=	new int[width][height];

//			ColorModel 		colorModel	=	this.imh.getColorModel(); 
//			WritableRaster 	raster		=	this.imh.getRaster();
			
		int	i,j;
		for( i = 0 ; i < this.width ; i++ ){
			for( j = 0 ; j < this.height ; j++ ){
				this.points[i][j]	=	new Point();
				this.points[i][j].setRGB( imh.getRGB(i, j) );
				this.points[i][j].toRed();
				this.points[i][j].toGreen();
				this.points[i][j].toBlue();
//					this.points[i][j].setRed(	colorModel.getRed(raster.getDataElements(i, j, null))	);
//					this.points[i][j].setGreen(	colorModel.getGreen(raster.getDataElements(i, j, null))	);
//					this.points[i][j].setBlue(	colorModel.getBlue(raster.getDataElements(i, j, null))	);
				this.points[i][j].toGray();
				this.points[i][j].toHSV();
			}
		}
		this.toSobel();
		this.toEdge();
		this.setColorJu();
		this.toHuJu();
	}
	
//	public FqImage( String file ){
//		BufferedImage imh;
//		try{
//			imh	=	ImageIO.read(new File(file));
//			new FqImage( imh );
//		}catch( Exception e ){
//			e.printStackTrace();
//		}
////		try{
////			this.imh	=	ImageIO.read(new File(file));
////			this.height	=	imh.getHeight(null);
////			this.width	=	imh.getWidth(null);
////			this.points	=	new Point[width][height];
////			this.sobels	=	new double[width][height];
////			this.edge	=	new int[width][height];
////
//////			ColorModel 		colorModel	=	this.imh.getColorModel(); 
//////			WritableRaster 	raster		=	this.imh.getRaster();
////			
////			int	i,j;
////			for( i = 0 ; i < this.width ; i++ ){
////				for( j = 0 ; j < this.height ; j++ ){
////					this.points[i][j]	=	new Point();
////					this.points[i][j].setRGB( imh.getRGB(i, j) );
////					this.points[i][j].toRed();
////					this.points[i][j].toGreen();
////					this.points[i][j].toBlue();
//////					this.points[i][j].setRed(	colorModel.getRed(raster.getDataElements(i, j, null))	);
//////					this.points[i][j].setGreen(	colorModel.getGreen(raster.getDataElements(i, j, null))	);
//////					this.points[i][j].setBlue(	colorModel.getBlue(raster.getDataElements(i, j, null))	);
////					this.points[i][j].toGray();
////					this.points[i][j].toHSV();
////				}
////			}
////			this.toSobel();
////			this.toEdge();
////		}catch (Exception e) {
////			e.printStackTrace();
////		}
//	}

	
}
