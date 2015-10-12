package fq;

public class FeatureToSign {
	private void print(long a){
		for (int i = 0; i < 64; i++){
			if ((a & (1 << i)) == 0 ){
				System.out.print("0");
			}
			else {
				System.out.print("1");
			}
		}
		System.out.println();
	}
	public long ToSign(double[] feature) {
		long[] v = new long[feature.length];
		long a = 0;
		int count;
		for (int j = 0;j < feature.length; j++){
			v[j] = Double.doubleToLongBits(feature[j]);
		}
		for (int i = 0; i < 64;i++){
			count = 0;
			for (int j = 0; j < feature.length;j++){
				if ((v[j] & (1 << i)) == 0 ){
					count--;
				}
				else {
					count++;
				}
			}
			if (count>=0){
				a |= 1 << i;
			}
		}
		return a;
	}
	public static void main(String[] args) {
		FeatureToSign featureToSign = new FeatureToSign();
		double[] feature ={1.2,2.3,4.6};
		System.out.println(featureToSign.ToSign(feature));
	}
}
