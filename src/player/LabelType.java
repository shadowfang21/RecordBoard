package player;

public enum LabelType {
	
	//順序一定要按照這個順序，因為影響到陣列的取index
	IPPON(1), WAZA(2), YUKO(99), SHIDO(4);
	
	private int maxScore;
	
	LabelType(int maxScore) {
		this.maxScore = maxScore;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	
}
