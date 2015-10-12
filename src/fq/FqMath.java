package fq;

public class FqMath {
	public static int max( int a , int b ){
		return	a > b ? a : b;
	}
	
	public static int max( int a , int b , int c ){
		int temp	=	max( a , b );
		return	temp > c ? temp : c;
	}
	
	public static double max( double a , double b ){
		return	a > b ? a : b;
	}
	
	public static int min( int a , int b ){
		return	a < b ? a : b;
	}
	
	public static int min( int a , int b , int c ){
		int temp	=	min( a , b );
		return	temp < c ? temp : c;
	}

}
