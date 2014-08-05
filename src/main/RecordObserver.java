package main;

public interface RecordObserver {

	RecordState getState();
	
	void setState(RecordState state);
}
