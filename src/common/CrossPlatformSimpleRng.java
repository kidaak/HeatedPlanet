package common;
// The goal of this class is to have a consistent set of random values generated
// identically across multiple platforms given a seed.
//
// It does this by using the MD5 hash function and keeps rehashing the prior
// value to generate a new one.  This is therefore not a super fast rng, but
// it's quick to write and does the job.
//
// Of course don't use this where statistically random numbers are important.
//
// Jeff Copeland

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.*;

public class CrossPlatformSimpleRng {
	private byte[] prevBytesOfMessage;
	MessageDigest md;
	
	public CrossPlatformSimpleRng(String seed) {
		try {
			prevBytesOfMessage = seed.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public float getVal() {
		// returns uniform random between 0 and 1
		byte[] thedigest = md.digest(prevBytesOfMessage);
		// get val from first 4 bytes of digest and normalize
		int outval = ByteBuffer.wrap(thedigest).order(ByteOrder.LITTLE_ENDIAN).getInt();
		prevBytesOfMessage = thedigest;
		return ((float)Math.abs(outval) / Integer.MAX_VALUE);
	}

	public float[] getVal(int num) {
		// returns an array of num uniform random numbers between 0-1.0
		float[] ret = new float[num];
		for(int i=0; i < num; i++) {
			ret[i] = getVal();
		}
		return ret;
	}
}
