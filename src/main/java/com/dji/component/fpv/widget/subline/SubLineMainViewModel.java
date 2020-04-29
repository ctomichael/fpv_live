package com.dji.component.fpv.widget.subline;

import android.content.Context;
import com.dji.component.fpv.base.AbstractViewModel;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.component.fpv.base.PersistenceObservable;
import com.dji.component.fpv.widget.preview.R;
import com.dji.video.framing.utils.DJIVideoUtil;
import dji.utils.Optional;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;

public class SubLineMainViewModel extends AbstractViewModel {
    /* access modifiers changed from: private */
    public int mHalfCenterLineLength = 0;
    private PersistenceObservable mPersistenceObservable = new PersistenceObservable(DJIVideoUtil.KEY_SUB_LINE_DIAGONAL_KEY, Boolean.class, false);
    private PersistenceObservable mPersistenceObservable2 = new PersistenceObservable(DJIVideoUtil.KEY_SUB_LINE_GRID_KEY, Boolean.class, false);
    private PersistenceObservable mPersistenceObservable3 = new PersistenceObservable(DJIVideoUtil.KEY_SUB_LINE_CENTER_KEY, Boolean.class, false);
    private List<SubLineTypeInfo> mSubLineTypeInfoList = new ArrayList();

    public SubLineMainViewModel(BulletinBoardProvider bridge, Context context) {
        super(bridge);
        this.mHalfCenterLineLength = context.getResources().getDimensionPixelOffset(R.dimen.s_56_dp) / 2;
        initSubLineLists();
    }

    private void initSubLineLists() {
        this.mSubLineTypeInfoList.add(new SubLineTypeInfo(DJIVideoUtil.KEY_SUB_LINE_DIAGONAL_KEY) {
            /* class com.dji.component.fpv.widget.subline.SubLineMainViewModel.AnonymousClass1 */

            /* access modifiers changed from: package-private */
            public float[] calculatePoint(int width, int height) {
                return new float[]{0.0f, 0.0f, (float) width, (float) height, (float) width, 0.0f, 0.0f, (float) height};
            }
        });
        this.mSubLineTypeInfoList.add(new SubLineTypeInfo(DJIVideoUtil.KEY_SUB_LINE_GRID_KEY) {
            /* class com.dji.component.fpv.widget.subline.SubLineMainViewModel.AnonymousClass2 */

            /* access modifiers changed from: package-private */
            public float[] calculatePoint(int width, int height) {
                return new float[]{0.0f, ((float) height) / 3.0f, (float) width, ((float) height) / 3.0f, 0.0f, ((float) (height * 2)) / 3.0f, (float) width, ((float) (height * 2)) / 3.0f, ((float) width) / 3.0f, 0.0f, ((float) width) / 3.0f, (float) height, ((float) (width * 2)) / 3.0f, 0.0f, ((float) (width * 2)) / 3.0f, (float) height};
            }
        });
        this.mSubLineTypeInfoList.add(new SubLineTypeInfo(DJIVideoUtil.KEY_SUB_LINE_CENTER_KEY) {
            /* class com.dji.component.fpv.widget.subline.SubLineMainViewModel.AnonymousClass3 */

            /* access modifiers changed from: package-private */
            public float[] calculatePoint(int width, int height) {
                return new float[]{(((float) width) / 2.0f) - ((float) SubLineMainViewModel.this.mHalfCenterLineLength), ((float) height) / 2.0f, (((float) width) / 2.0f) + ((float) SubLineMainViewModel.this.mHalfCenterLineLength), ((float) height) / 2.0f, ((float) width) / 2.0f, (((float) height) / 2.0f) - ((float) SubLineMainViewModel.this.mHalfCenterLineLength), ((float) width) / 2.0f, (((float) height) / 2.0f) + ((float) SubLineMainViewModel.this.mHalfCenterLineLength)};
            }
        });
    }

    public void onCreateView() {
    }

    public void onDestroyView() {
    }

    public Observable<List<SubLineTypeInfo>> getObservable() {
        return Observable.combineLatest(this.mPersistenceObservable, this.mPersistenceObservable2, this.mPersistenceObservable3, new SubLineMainViewModel$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ List lambda$getObservable$0$SubLineMainViewModel(Optional v1, Optional v2, Optional v3) throws Exception {
        for (int i = 0; i < this.mSubLineTypeInfoList.size(); i++) {
            if (this.mPersistenceObservable.getKey().equals(this.mSubLineTypeInfoList.get(i).getKey())) {
                this.mSubLineTypeInfoList.get(i).setCanDraw(((Boolean) v1.get()).booleanValue());
            }
            if (this.mPersistenceObservable2.getKey().equals(this.mSubLineTypeInfoList.get(i).getKey())) {
                this.mSubLineTypeInfoList.get(i).setCanDraw(((Boolean) v2.get()).booleanValue());
            }
            if (this.mPersistenceObservable3.getKey().equals(this.mSubLineTypeInfoList.get(i).getKey())) {
                this.mSubLineTypeInfoList.get(i).setCanDraw(((Boolean) v3.get()).booleanValue());
            }
        }
        return this.mSubLineTypeInfoList;
    }
}
