package org.cocos2d.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import android.os.Build;

public class BufferUtils {
	/**
	 * Copy with JNI for devices prior GINGERBREAD(api 9), put for newer versions(HTC Sensation has faster put(float[]) )  
	 */
	public static void copyFloats(float[] src, int srcOffset, FloatBuffer dst, int numElements) {
		if(Build.VERSION.SDK_INT >= 9) {
			dst.put(src, srcOffset, numElements);
		} else {
			com.badlogic.gdx.utils.BufferUtils.copy(src, srcOffset, dst, numElements);
		}
	}
	
	// BRIGOSX 26JUL2012 - New copy method for byte buffers - 
	public static void copyBytes(byte[] src, int srcOffset, ByteBuffer dst, int numElements) {
		if(Build.VERSION.SDK_INT >= 9) {
			dst.put(src, srcOffset, numElements);
		} else {
			com.badlogic.gdx.utils.BufferUtils.copy(src, srcOffset, dst, numElements);
		}
	}
}
