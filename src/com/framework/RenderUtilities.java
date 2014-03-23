package com.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

public class RenderUtilities {
	final static String TAG = "RenderUtilities";
	
	public enum Color {RED, ORANGE, MAGENTA, BLUE, LIME, OLIVE, CYAN};
	
	final private static int mScreenWidth = 320;
	final private static int mScreenHeight = 480;
	
	public static int loadShader(int shaderType, final String shaderCode){
	    int shaderHandle = GLES20.glCreateShader(shaderType);
	    assert GLES20.glGetError() == GLES20.GL_NO_ERROR;
	    assert GLES20.glGetError() != GLES20.GL_INVALID_ENUM;
	    
	    if (shaderHandle != 0) {
		    GLES20.glShaderSource(shaderHandle, shaderCode);
		    assert GLES20.glGetError() == GLES20.GL_NO_ERROR;
		    
		    GLES20.glCompileShader(shaderHandle);
		    assert GLES20.glGetError() == GLES20.GL_NO_ERROR;

		    int[] compiled = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if(compiled[0] == GLES20.GL_FALSE) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shaderType));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
		    
	    } else if (shaderHandle == 0) {
	    	throw new RuntimeException("Error creating the shader");
	    }

	    return shaderHandle;
	}
	
	public static Bitmap loadImage(Context context, int resourceID) {		
		Bitmap bitmap = null;
		Resources res = context.getResources();
		try {
			//BitmapFactory.Options options = new BitmapFactory.Options();
			//options.inDensity = 0;
            bitmap = BitmapFactory.decodeResource(res, resourceID);
	    } catch ( Throwable e ) {
	    	e.printStackTrace();
	    	Log.v("test", e.getMessage());
	    } finally {
	    }
		return bitmap;
	}
	
	public static class Square {
		private static final int mSize = 16;
		private static FloatBuffer mVertexBuffer;
		private static float[] mVertexCoords = new float[8];
		private static ShortBuffer mDrawListBuffer;
		private static final int COORDS_PER_VERTEX = 2;
		private static final float mSquareCoords[] = {
			0.0f, 							0.0f, 							//top left
			0.0f, 							(float) -mSize * 2.0f/mScreenHeight, 	//bottom left
			(float) mSize * 2.0f/mScreenWidth, 	(float) -mSize * 2.0f/mScreenHeight,	//bottom right
			(float) mSize * 2.0f/mScreenWidth, 	0.0f							//top right
		};
		private static short mDrawOrder[] = {0, 1, 2, 0, 2, 3};
		
		private static final String vertexShaderCode =
		    "attribute vec4 vPosition;" +
		    "void main() {" +
		    "  gl_Position = vPosition;" +
		    "}";
		private static final String fragmentShaderCode =
		    "precision mediump float;" +
		    "uniform vec4 vColor;" +
		    "void main() {" +
		    "  gl_FragColor = vColor;" +
		    "}";
		private static int mProgram;
		
		static {			
			ByteBuffer dlb = ByteBuffer.allocateDirect(mDrawOrder.length * 2);
			dlb.order(ByteOrder.nativeOrder());
			mDrawListBuffer = dlb.asShortBuffer();
			mDrawListBuffer.put(mDrawOrder);
			mDrawListBuffer.position(0);
		}
		
		public static void initialize(Context context) {			
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
			mProgram = GLES20.glCreateProgram();
			GLES20.glAttachShader(mProgram, vertexShader);
			GLES20.glAttachShader(mProgram, fragmentShader);
			GLES20.glLinkProgram(mProgram);	
		}
		
		public static void renderSquare(float posX, float posY, Color c) {
			mVertexCoords[0] = mSquareCoords[0] + posX;
			mVertexCoords[1] = mSquareCoords[1] + posY;
			mVertexCoords[2] = mSquareCoords[2] + posX;
			mVertexCoords[3] = mSquareCoords[3] + posY;
			mVertexCoords[4] = mSquareCoords[4] + posX;
			mVertexCoords[5] = mSquareCoords[5] + posY;
			mVertexCoords[6] = mSquareCoords[6] + posX;
			mVertexCoords[7] = mSquareCoords[7] + posY;
			ByteBuffer bb = ByteBuffer.allocateDirect(mVertexCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			mVertexBuffer = bb.asFloatBuffer();
			mVertexBuffer.position(0);
			mVertexBuffer.put(mVertexCoords);
			mVertexBuffer.position(0);

			GLES20.glUseProgram(mProgram);
		    int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		    GLES20.glEnableVertexAttribArray(positionHandle);
		    GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
		                                 GLES20.GL_FLOAT, false,
		                                 0, mVertexBuffer);

		    int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		    switch(c) {
		    //public enum Color {RED, ORANGE, MAGENTA, BLUE, LIME, OLIVE, CYAN};
		    case RED:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{1.0f, 0.0f, 0.0f, 1.0f}, 0);
		    	break;
		    case ORANGE:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{1.0f, 0.647f, 0.0f, 1.0f}, 0);
		    	break;
		    case MAGENTA:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{1.0f, 0.0f, 1.0f, 1.0f}, 0);
		    	break;
		    case BLUE:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{0.0f, 0.0f, 1.0f, 1.0f}, 0);
		    	break;
		    case LIME:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
		    	break;
		    case OLIVE:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{0.502f, 0.502f, 0.0f, 1.0f}, 0);
		    	break;
		    case CYAN:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{0.0f, 1.0f, 1.0f, 1.0f}, 0);
		    	break;
		    default:
		    	GLES20.glUniform4fv(colorHandle, 1, new float[]{1.0f, 0.0f, 0.0f, 1.0f}, 0);
		    	break;
		    }
		    
		    GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawOrder.length,
                    GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

		    // Disable vertex array
		    GLES20.glDisableVertexAttribArray(positionHandle);		
		}
		
		public static void renderSquare(float posX, float posY) {
			renderSquare(posX, posY, Color.RED);
		}
	}
}
