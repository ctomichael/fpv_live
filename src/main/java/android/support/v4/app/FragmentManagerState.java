package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: FragmentManager */
final class FragmentManagerState implements Parcelable {
    public static final Parcelable.Creator<FragmentManagerState> CREATOR = new Parcelable.Creator<FragmentManagerState>() {
        /* class android.support.v4.app.FragmentManagerState.AnonymousClass1 */

        public FragmentManagerState createFromParcel(Parcel in2) {
            return new FragmentManagerState(in2);
        }

        public FragmentManagerState[] newArray(int size) {
            return new FragmentManagerState[size];
        }
    };
    FragmentState[] mActive;
    int[] mAdded;
    BackStackState[] mBackStack;
    int mNextFragmentIndex;
    int mPrimaryNavActiveIndex = -1;

    public FragmentManagerState() {
    }

    public FragmentManagerState(Parcel in2) {
        this.mActive = (FragmentState[]) in2.createTypedArray(FragmentState.CREATOR);
        this.mAdded = in2.createIntArray();
        this.mBackStack = (BackStackState[]) in2.createTypedArray(BackStackState.CREATOR);
        this.mPrimaryNavActiveIndex = in2.readInt();
        this.mNextFragmentIndex = in2.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.mActive, flags);
        dest.writeIntArray(this.mAdded);
        dest.writeTypedArray(this.mBackStack, flags);
        dest.writeInt(this.mPrimaryNavActiveIndex);
        dest.writeInt(this.mNextFragmentIndex);
    }
}
