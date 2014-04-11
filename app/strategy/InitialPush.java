package strategy;

public class InitialPush implements NumToPush{
	
	final private int copies;
	
	public InitialPush(){
		copies= 1;
	}
	
	InitialPush(int i){
		copies= i;
	}

	@Override
	public int numToPush(double curTime, int nInfected, int nTotal) {
		if(curTime < 500L)
			return copies;
		return 0;
	}
	
}
