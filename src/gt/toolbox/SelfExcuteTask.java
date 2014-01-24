package gt.toolbox;

import gt.toolbox.TaskWatcherService.ActionType;

// all the changes must be done on the main thread
class SelfExcuteTask extends Task {
	private String newPackage;
	private String oldPackage;

	public void setPackage(String newPackage, String oldPackage) {
		this.newPackage = newPackage;
		this.oldPackage = oldPackage;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TaskWatcherService.getInstance().informListener(ActionType.EXIT,
				oldPackage);
		TaskWatcherService.getInstance().informListener(ActionType.ENTER,
				newPackage);
	}
}