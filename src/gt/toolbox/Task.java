package gt.toolbox;

public abstract class Task implements Runnable {
	private String newPackage;
	private String oldPackage;

	public void setPackage(String newPackage, String oldPackage) {
		this.newPackage = newPackage;
		this.oldPackage = oldPackage;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
