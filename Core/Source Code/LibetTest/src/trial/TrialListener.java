package trial;

public interface TrialListener {
	
	public void libetTestStarted();
	
	public void libetAcceptingActions();
	
	public void libetActionReceived();
	
	public void libetNotAcceptingActions();
	
	public void libetTestFinished();
	
	public void libetTrialModeChanged();
	
}
