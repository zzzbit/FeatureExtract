package fq;

import fq.FqMath.*;

public class Point {
	private int		alpha;
	private int		red;
	private int		green;
	private int 	blue;
	private int 	rgb;
	private double	hHSV;
	private double	sHSV;
	private double	vHSV;
	private double 	gray;
	
	public Point(){
		this.alpha	=	0;
		this.red	=	0;
		this.green	=	0;
		this.blue	=	0;
		this.rgb	=	0;
		this.hHSV	=	0;
		this.sHSV	=	0;
		this.vHSV	=	0;
		this.gray	=	0;
	}
	
	public void toHSV3( int red , int green , int blue ){
		double 	maxRGB	=	FqMath.max( red , green , blue );
		double	minRGB	=	FqMath.min( red , green , blue );
		double	itemp	=	maxRGB;
		double	temp	=	maxRGB	-	minRGB;
		
		if( maxRGB == minRGB ){
			this.hHSV	=	0;
			this.sHSV	=	0;
			this.vHSV	=	maxRGB / 255;
			return;
		}
		
		double	rtemp	=	( itemp - red )	/ temp;
		double	gtemp	=	( itemp - green ) / temp;
		double	btemp	=	( itemp - blue ) / temp;
		
		
		
		this.vHSV	=	itemp / 255;
		this.sHSV	=	temp / itemp;
		if( red == maxRGB ){
			if( green == minRGB )
				this.hHSV	=	5 + btemp;
			else
				this.hHSV	=	1 - gtemp;
		}
		else if( green == maxRGB ){
			if( blue == minRGB )
				this.hHSV	=	1 + rtemp;
			else
				this.hHSV	=	3 - btemp;
		}
		else if( blue == maxRGB ){
			if( red == minRGB )
				this.hHSV	=	3 + gtemp;
			else
				this.hHSV	=	5 - rtemp;
		}
		this.hHSV	*=	60;
		
	}
	
	public void toHSV( int red , int green , int blue ){
		this.vHSV	=	( red + green + blue ) / 3.0;
		if( red == green && green == blue ){
			this.hHSV	=	0;
			this.sHSV	=	0;
		}
		else if( red > blue && green >= blue ){
			this.hHSV	=	( green - blue ) / ( (this.vHSV - blue) * 3.0 );
			this.sHSV	=	1 - blue / this.vHSV;
		}
		else if( green > red && blue >= red ){
			this.hHSV	=	( blue - red ) / ( (this.vHSV - red) * 3.0 )	+	1;
			this.sHSV	=	1 - red / this.vHSV;
		}
		else if( blue > green && red >= green ){
			this.hHSV	=	( red - green ) / ( (this.vHSV - green) * 3.0 )	+	2;
			this.sHSV	=	1 - green / this.vHSV;
		}
	}
	
	public void toHSV(){
		this.toHSV( this.red , this.green ,this.blue );
	}
	
	public void toGray( int red , int green , int blue ){
		this.gray = red * 0.3 + green * 0.59 + blue * 0.11;
	}
	
	public void toGray(){
		this.toGray( this.red , this.green , this.blue );
	}
	
	public void toAlpha( int rgb ){
		int temp	=	0xff000000;
		
		rgb	=	rgb & temp;
		this.alpha	=	rgb >> 24;
	}
	
	public void toAlpha(){
		this.toAlpha(this.rgb);
	}
	
	public void toRed( int rgb ){
		this.red	=	( rgb >> 16 ) & 0xff;
	}
	
	public void toRed(){
		this.toRed(this.rgb);
	}
	
	public void toGreen( int rgb ){
		this.green	=	( rgb >> 8 ) & 0xff;
	}
	
	public void toGreen(){
		this.toGreen(this.rgb);
	}
	
	public void toBlue( int rgb ){
		this.blue	=	rgb & 0xff;
	}
	
	public void toBlue(){
		this.toBlue(this.rgb);
	}
	
	public double pointDistance( Point other ){
		double h1		=	this.hHSV * Math.PI / 360;
		double h2		=	other.hHSV * Math.PI / 360;
		
		double temp1	=	this.vHSV - other.vHSV; 
		double temp2	=	this.sHSV*Math.sin(h1) - other.sHSV*Math.sin(h2);
		double temp3	=	this.sHSV*Math.cos(h1) - other.sHSV*Math.cos(h2);		
		
		return	Math.sqrt(temp1*temp1 + temp2*temp2 + temp3*temp3);			
	}
	
	public int getAlpha(){
		return	this.alpha;
	}
	
	public void setRed( int red ){
		this.red	=	red;
	}
	
	public int getRed(){
		return	this.red;
	}
	
	public void setGreen( int green ){
		this.green	=	green;
	}
	
	public int getGreen(){
		return	this.green;
	}
	
	public void setBlue( int blue ){
		this.blue	=	blue;
	}
	
	public int getBlue(){
		return	this.blue;
	}
	
	public void setRGB( int rgb ){
		this.rgb	=	rgb;
	}
	
	public int getRGB(){
		return	this.rgb;
	}
	
	public double getGray(){
		return	this.gray;
	}
	
	public double getHHSV(){
		return	this.hHSV;
	}
	
	public double getSHSV(){
		return 	this.sHSV;
	}
	
	public double getVHSV(){
		return	this.vHSV;
	}
	
//	public void setSobel( double sobel ){
//		this.sobel	=	sobel;
//	}
//	
//	public double getSobel(){
//		return	this.sobel;
//	}

}
