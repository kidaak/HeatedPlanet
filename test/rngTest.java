import common.CrossPlatformSimpleRng;


public class rngTest {
	rngTest() {
		CrossPlatformSimpleRng rng = new CrossPlatformSimpleRng("42");
		float[] data = rng.getVal(10000);
		double avg = 0;
		for(float f : data) {
			avg += f;
			System.out.println(f);
		}
		System.out.printf("avg: %f\n", avg/data.length);
	}

	public static void main(String[] args) {
		new rngTest();
	}
}
