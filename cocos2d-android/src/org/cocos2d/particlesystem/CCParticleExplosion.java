package org.cocos2d.particlesystem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccBlendFunc;
import org.cocos2d.types.ccColor4F;
import org.cocos2d.utils.Base64;
import org.cocos2d.utils.PlistParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CCParticleExplosion extends CCQuadParticleSystem {

    public static CCParticleSystem node() {
        return new CCParticleExplosion();
    }
    
    public static CCParticleSystem particleWithFile(String plistFile) {
    	HashMap<String,Object> dictionary = PlistParser.parse(plistFile);
    	assert (dictionary != null) : "Particle file not found.";

    	int numParticles = ((Number)dictionary.get("maxParticles")).intValue();
    	CCParticleSystem particle = new CCParticleExplosion(numParticles); 

    	    	// angle
    	particle.setAngle(((Number)dictionary.get("angle")).floatValue());
    	particle.setAngleVar(((Number)dictionary.get("angleVariance")).floatValue());

    	// duration
    	particle.setDuration(((Number)dictionary.get("duration")).floatValue());

    	// blend function 
    	particle.setBlendFunc(new ccBlendFunc(((Number)dictionary.get("blendFuncSource")).intValue(), 
    			((Number)dictionary.get("blendFuncDestination")).intValue()));

    	// color
    	float r,g,b,a;

    	r = ((Number)dictionary.get("startColorRed")).floatValue();
    	g = ((Number)dictionary.get("startColorGreen")).floatValue();
    	b = ((Number)dictionary.get("startColorBlue")).floatValue();
    	a = ((Number)dictionary.get("startColorAlpha")).floatValue();
    	particle.setStartColor(new ccColor4F(r,g,b,a));

    	r = ((Number)dictionary.get("startColorVarianceRed")).floatValue();
    	g = ((Number)dictionary.get("startColorVarianceGreen")).floatValue();
    	b = ((Number)dictionary.get("startColorVarianceBlue")).floatValue();
    	a = ((Number)dictionary.get("startColorVarianceAlpha")).floatValue();
    	particle.setStartColorVar(new ccColor4F(r,g,b,a));

    	r = ((Number)dictionary.get("finishColorRed")).floatValue();
    	g = ((Number)dictionary.get("finishColorGreen")).floatValue();
    	b = ((Number)dictionary.get("finishColorBlue")).floatValue();
    	a = ((Number)dictionary.get("finishColorAlpha")).floatValue();
    	particle.setEndColor(new ccColor4F(r,g,b,a));

    	r = ((Number)dictionary.get("finishColorVarianceRed")).floatValue();
    	g = ((Number)dictionary.get("finishColorVarianceGreen")).floatValue();
    	b = ((Number)dictionary.get("finishColorVarianceBlue")).floatValue();
    	a = ((Number)dictionary.get("finishColorVarianceAlpha")).floatValue();
    	particle.setEndColorVar(new ccColor4F(r,g,b,a));

    	// particle size
    	particle.setStartSize(((Number)dictionary.get("startParticleSize")).floatValue());
    	particle.setStartSizeVar(((Number)dictionary.get("startParticleSizeVariance")).floatValue());
    	particle.setEndSize(((Number)dictionary.get("finishParticleSize")).floatValue());
    	particle.setEndSizeVar(((Number)dictionary.get("finishParticleSizeVariance")).floatValue());

    	// position
    	float x = ((Number)dictionary.get("sourcePositionx")).floatValue();
    	float y = ((Number)dictionary.get("sourcePositiony")).floatValue();
    	particle.setPosition(CGPoint.ccp(x,y));
    	particle.setPosVar(CGPoint.ccp(((Number)dictionary.get("sourcePositionVariancex")).floatValue(),
    			((Number)dictionary.get("sourcePositionVariancey")).floatValue()));

    	particle.setEmitterMode(((Number)dictionary.get("emitterType")).intValue());

    	if(particle.emitterMode == kCCParticleModeGravity) {
    		// Mode A: Gravity + tangential accel + radial accel
    		// gravity
    		particle.setGravity(CGPoint.ccp(((Number)dictionary.get("gravityx")).floatValue(),
    				((Number)dictionary.get("gravityy")).floatValue()));

    		//
    		// speed
    		particle.setSpeed(((Number)dictionary.get("speed")).floatValue());
    		particle.setSpeedVar(((Number)dictionary.get("speedVariance")).floatValue());

    		// radial acceleration
    		particle.setRadialAccel(((Number)dictionary.get("radialAcceleration")).floatValue());
    		particle.setRadialAccelVar(((Number)dictionary.get("radialAccelVariance")).floatValue());

    		// tangential acceleration
    		particle.setTangentialAccel(((Number)dictionary.get("tangentialAcceleration")).floatValue());
    		particle.setTangentialAccelVar(((Number)dictionary.get("tangentialAccelVariance")).floatValue());
    	}
    	else {
    		float maxRadius    = ((Number)dictionary.get("maxRadius")).floatValue();
    		float maxRadiusVar = ((Number)dictionary.get("maxRadiusVariance")).floatValue();
    		float minRadius    = ((Number)dictionary.get("minRadius")).floatValue();

    		particle.setStartRadius(maxRadius);
    		particle.setStartRadiusVar(maxRadiusVar);
    		particle.setEndRadius(minRadius);
    		particle.setEndRadiusVar(0);
    		particle.setRotatePerSecond(((Number)dictionary.get("rotatePerSecond")).floatValue());
    		particle.setRotatePerSecondVar(((Number)dictionary.get("rotatePerSecondVariance")).floatValue());
    	}
    	// life span
    	particle.setLife(((Number)dictionary.get("particleLifespan")).floatValue());
    	particle.setLifeVar(((Number)dictionary.get("particleLifespanVariance")).floatValue());				

    	// emission Rate
    	particle.setEmissionRate(particle.getTotalParticles()/particle.getLife());

    	// texture		
    	// Try to get the texture from the cache
    	String textureName = (String)dictionary.get("textureFileName");
    	String textureData = (String)dictionary.get("textureImageData");

    	if(new File(textureName).exists()) {
    		try {
    			CCTexture2D tex = CCTextureCache.sharedTextureCache().addImage(textureName);
    			particle.setTexture(tex);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	else if (textureData != null) {
    		// if it fails, try to get it from the base64-gzipped data			
    		byte[] buffer = null;

    		try {
    			buffer = Base64.decode(textureData);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    		byte[] deflated = new byte[buffer.length];
    		Inflater decompresser = new Inflater(false);

    		int deflatedLen = 0;

    		try {
    			deflatedLen = decompresser.inflate(deflated);
    		} catch (DataFormatException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    		Bitmap bmp = BitmapFactory.decodeByteArray(deflated, 0, deflatedLen);

    		if(bmp != null) {
    			particle.setTexture(CCTextureCache.sharedTextureCache().addImage(bmp, textureName));
    		}
    	}
    	
    	return particle;
    }
    
    protected CCParticleExplosion() {
        this(700);
    }

    protected CCParticleExplosion(int p) {
        super(p);

    	
		// duration
		duration = 0.1f;
		
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(0,0));
		
		// Gravity Mode: speed of particles
		setSpeed(70);
		setSpeedVar(40);
		
		// Gravity Mode: radial
		setRadialAccel( 0 );
		setRadialAccelVar( 0 );
		
		// Gravity Mode: tagential
		setTangentialAccel( 0 );
		setTangentialAccelVar( 0 );
		
		// angle
		angle = 90;
		angleVar = 360;
				
		// emitter position
		CGSize winSize = CCDirector.sharedDirector().winSize();
		
		this.setPosition(CGPoint.ccp(winSize.width/2, winSize.height/2));
		posVar = CGPoint.zero();
		
		// life of particles
		life = 5.0f;
		lifeVar = 2;
		
		// size, in pixels
		startSize = 15.0f;
		startSizeVar = 10.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per second
		emissionRate = totalParticles/duration;
		
		// color of particles
		startColor.r = 0.7f;
		startColor.g = 0.1f;
		startColor.b = 0.2f;
		startColor.a = 1.0f;
		startColorVar.r = 0.5f;
		startColorVar.g = 0.5f;
		startColorVar.b = 0.5f;
		startColorVar.a = 0.0f;
		endColor.r = 0.5f;
		endColor.g = 0.5f;
		endColor.b = 0.5f;
		endColor.a = 0.0f;
		endColorVar.r = 0.5f;
		endColorVar.g = 0.5f;
		endColorVar.b = 0.5f;
		endColorVar.a = 0.0f;
		
		setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));

        // additive
        setBlendAdditive(false);
    }

	@Override
	public ccBlendFunc getBlendFunc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBlendFunc(ccBlendFunc blendFunc) {
		// TODO Auto-generated method stub
		
	}
}
