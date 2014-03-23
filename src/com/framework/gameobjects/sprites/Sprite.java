package com.framework.gameobjects.sprites;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;

import junit.framework.Assert;
import android.content.Context;
import android.opengl.GLES20;

import com.framework.RenderUtilities;
import com.framework.Utilities;
import com.framework.gameobjects.GameObject;
import com.framework.graphics.Texture;

public class Sprite extends GameObject {
	final private static String TAG = "SPRITE";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	public enum Transparency {SOLID, ADDITIVE, TRANSPARENT};
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/

	private static String mVertexShader; 
	private static String mFragmentShader;
	private static int mProgram;
	
	private String mFileName = null;
	private Texture mTexture = null;
	
	private float mTopLeftCornerNDC[];
	private float mWidthNDC;
	private float mHeightNDC;
	private float mSquareVertices[] = new float[8];
	private float mTextureCoordinates[] = {
		0.0f, 1.0f,
		1.0f, 1.0f,
		0.0f,  0.0f,
		1.0f,  0.0f
	};
	
	private Transparency mTransparency;
		
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	/**
	 * This function creates the requested sprite
	 * 
	 * @param topLeftCornerNDC
	 * @param widthNDC
	 * @param heightNDC
	 * @param texture
	 * @param transparency
	 */
	public Sprite(float[] topLeftCornerNDC, float widthNDC, float heightNDC, String fileName, Transparency transparency) {
		Assert.assertNotNull("Expected an existing fileName!", fileName);
		Assert.assertTrue("The top left corner value does not hold NDC coordinates", topLeftCornerNDC[0] >= -1 && topLeftCornerNDC[0] <= 1);
		Assert.assertTrue("The top left corner value does not hold NDC coordinates", topLeftCornerNDC[1] >= -1 && topLeftCornerNDC[1] <= 1);
		Assert.assertTrue("The top left corner value should hold 2 coordinates", topLeftCornerNDC.length == 2); 
		Assert.assertTrue("Width should be positive and should not be bigger then 2", widthNDC >= 0 && widthNDC <= 2);
		Assert.assertTrue("Heigt should be positive and should not be bigger then 2", heightNDC >= 0 && heightNDC <= 2);
		
		mWidthNDC = widthNDC;
		mHeightNDC = heightNDC;
		
		//Bottom left
		mSquareVertices[0] = topLeftCornerNDC[0];
		mSquareVertices[1] = topLeftCornerNDC[1] - mHeightNDC;
		//Bottom right
		mSquareVertices[2] = topLeftCornerNDC[0] + mWidthNDC;
		mSquareVertices[3] = topLeftCornerNDC[1] - mHeightNDC;
		//Top left
		mSquareVertices[4] = topLeftCornerNDC[0];
		mSquareVertices[5] = topLeftCornerNDC[1];
		//Top right
		mSquareVertices[6] = topLeftCornerNDC[0] + mWidthNDC;
		mSquareVertices[7] = topLeftCornerNDC[1];
		
		mFileName = fileName;
		
		mTransparency = transparency;
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/

	@Override
	public void initialize(Context context) {
		mTexture = new Texture(mFileName);
		mTexture.initialize(context);
		
		mVertexShader = Utilities.readStringFromAsset(context, "shaders/sprite.vs", Charset.defaultCharset());
		mFragmentShader = Utilities.readStringFromAsset(context, "shaders/sprite.fs", Charset.defaultCharset());
		
		int vertexShader = RenderUtilities.loadShader(GLES20.GL_VERTEX_SHADER, mVertexShader);
		int fragmentShader = RenderUtilities.loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader);
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
	}

	@Override
	public void update(Context context, float deltaTS) {
		// Nothing to do...	
	}
	
	@Override
	public void render() {
		mTexture.bind();
		
		GLES20.glUseProgram(mProgram);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		ByteBuffer bb = ByteBuffer.allocateDirect(mSquareVertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer tmp = bb.asFloatBuffer();
		tmp.put(mSquareVertices);
		tmp.position(0);
	    int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, tmp);
		GLES20.glEnableVertexAttribArray(positionHandle);
		
		ByteBuffer bb2 = ByteBuffer.allocateDirect(mTextureCoordinates.length * 4);
		bb2.order(ByteOrder.nativeOrder());
		FloatBuffer tmp2 = bb2.asFloatBuffer();
		tmp2.put(mTextureCoordinates);
		tmp2.position(0);
		int vertexHandle = GLES20.glGetAttribLocation(mProgram, "vTextureCoordinate");
		GLES20.glVertexAttribPointer(vertexHandle, 2, GLES20.GL_FLOAT, false, 0, tmp2);
		GLES20.glEnableVertexAttribArray(vertexHandle);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
	    GLES20.glDisableVertexAttribArray(positionHandle);
	    GLES20.glDisableVertexAttribArray(vertexHandle);
	    
	    GLES20.glDisable(GLES20.GL_BLEND);
	}
}
