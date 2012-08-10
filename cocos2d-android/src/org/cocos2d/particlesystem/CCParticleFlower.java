package org.cocos2d.particlesystem;

import java.util.HashMap;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccBlendFunc;
import org.cocos2d.utils.PlistParser;

public class CCParticleFlower extends CCQuadParticleSystem {

    public static CCParticleFlower node() {
        return new CCParticleFlower();
    }
    
    public static CCParticleFlower node(int p) {
        return new CCParticleFlower(p);
    }
    
    public static CCParticleFlower particleWithFile(String plistFile) {
    	HashMap<String,Object> dictionary = PlistParser.parse(plistFile);
    	assert (dictionary != null) : "Particle file not found.";

    	int numParticles = ((Number)dictionary.get("maxParticles")).intValue();
    	CCParticleFlower particle = new CCParticleFlower(numParticles); 

    	particle.loadParticleFile(dictionary);
    	
    	return particle;
    }

    protected CCParticleFlower() {
        this(250);
    }

    protected CCParticleFlower(int p) {
        super(p);

		// duration
		duration = kCCParticleDurationInfinity;

		// Gravity Mode
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(0,0));
		
		// Gravity Mode: speed of particles
		setSpeed( 80 );
		setSpeedVar( 10 );
		
		// Gravity Mode: radial
		setRadialAccel( -60 );
		setRadialAccelVar( 0 );
		
		// Gravity Mode: tagential
		setTangentialAccel( 15 );
		setTangentialAccelVar( 0 );

		// angle
		angle = 90;
		angleVar = 360;
		
		// emitter position
		CGSize winSize = CCDirector.sharedDirector().winSize();
		this.setPosition(CGPoint.ccp(winSize.width/2, winSize.height/2));
		posVar = CGPoint.zero();
		
		// life of particles
		life = 4;
		lifeVar = 1;
		
		// size, in pixels
		startSize = 30.0f;
		startSizeVar = 10.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per second
		emissionRate = totalParticles/life;
		
		// color of particles
		startColor.r = 0.50f;
		startColor.g = 0.50f;
		startColor.b = 0.50f;
		startColor.a = 1.0f;
		startColorVar.r = 0.5f;
		startColorVar.g = 0.5f;
		startColorVar.b = 0.5f;
		startColorVar.a = 0.5f;
		endColor.r = 0.0f;
		endColor.g = 0.0f;
		endColor.b = 0.0f;
		endColor.a = 1.0f;
		endColorVar.r = 0.0f;
		endColorVar.g = 0.0f;
		endColorVar.b = 0.0f;
		endColorVar.a = 0.0f;
		
		setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));

        // additive
		setBlendAdditive(true);
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

