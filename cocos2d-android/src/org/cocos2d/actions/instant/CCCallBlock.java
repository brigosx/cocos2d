package org.cocos2d.actions.instant;

import org.cocos2d.nodes.CCNode;

public class CCCallBlock extends CCInstantAction implements ICCCallBlock {
	
	private final ICCCallBlock blk;
	
		protected CCCallBlock(ICCCallBlock block_) {
		// TODO Auto-generated constructor stub
		this.blk = block_;
	}
		
	public static CCCallBlock action(ICCCallBlock iBlock) {
		return new CCCallBlock(iBlock);
	}
	
	@Override
	public CCInstantAction copy() {
		return new CCInstantAction();
	}
	
	@Override
	public CCInstantAction reverse() {
		return copy();
	}
	
	@Override
    public void start(CCNode aTarget) {
        super.start(aTarget);
        execute();
    } 
	
	public void execute() {
		try {
			this.blk.callBlock();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	@Override
	public void callBlock() {
		// TODO Auto-generated method stub
		
	}

}
