package gt.toolbox;

import android.os.Parcel;
import android.os.Parcelable;

public class ExcutionContext implements Parcelable {

	private String newPackage;
	private String oldPackage;

	public ExcutionContext() {

	}

	public ExcutionContext(String newPackage, String oldPackage) {
		this.setNewPackage(newPackage);
		this.setOldPackage(oldPackage);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getNewPackage());
		dest.writeString(getOldPackage());
	}

	public String getNewPackage() {
		return newPackage;
	}

	public void setNewPackage(String newPackage) {
		this.newPackage = newPackage;
	}

	public String getOldPackage() {
		return oldPackage;
	}

	public void setOldPackage(String oldPackage) {
		this.oldPackage = oldPackage;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "exit " + getOldPackage() + " enter " + getNewPackage();
	}

	public static final Parcelable.Creator<ExcutionContext> CREATOR = new Creator<ExcutionContext>() {

		@Override
		public ExcutionContext[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ExcutionContext[size];
		}

		@Override
		public ExcutionContext createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ExcutionContext(source.readString(), source.readString());
		}
	};
}
