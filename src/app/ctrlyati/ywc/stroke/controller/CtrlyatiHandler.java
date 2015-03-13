package app.ctrlyati.ywc.stroke.controller;

import java.util.ArrayList;

public class CtrlyatiHandler implements Runnable{

	private ArrayList<Runnable> mRunnables = new ArrayList<Runnable>();
	private Thread mThread = new Thread(this);
	
	private HandlerState mState;
	
	public void register(Runnable r){
		mRunnables.add(r);
	}
	
	private static enum  HandlerState{
		START, STOP
	}
	
	@Override
	public void run() {
		while(mState == HandlerState.START){
			for(Runnable runnable: mRunnables){
				runnable.run();
			}
			

			try{
				Thread.sleep(16);
			}catch(Exception e){}
		}
	}
	
	public void start(){
		mState = HandlerState.START;
		if(!mThread.isAlive()){
			mThread.setPriority(Thread.MAX_PRIORITY);
			mThread.start();
		}
	}
	
	public void stop(){
		mState = HandlerState.STOP;
		
	}

}
