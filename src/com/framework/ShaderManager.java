package com.framework;

import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;

public class ShaderManager {
	final private AssetManager am;
	Map<String, String> shaderMap;
	
	public ShaderManager(Context context) {
		am = context.getAssets();
		String [] shaders;
		try {
			shaders = am.list("Shaders");
			for(String s : shaders) {
				
				//shaderMap.put(s, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}