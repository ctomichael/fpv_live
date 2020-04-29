package dji.publics.utils;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

public class CutoutDisplayOrientationManager {
    private static final String TAG = CutoutDisplayOrientationManager.class.getSimpleName();
    /* access modifiers changed from: private */
    public List<AdaptiveViewMeta> mAdaptiveViewMetas;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentDisplayRotation = -1;
    /* access modifiers changed from: private */
    public LandscapeChangeListener mLandscapeChangeListener;
    private OrientationEventListener mOrientationEventListener;
    /* access modifiers changed from: private */
    public PortraitChangeListener mPortraitChangeListener;
    /* access modifiers changed from: private */
    public Window mWindow;
    private WindowManager mWindowManager;

    public interface LandscapeChangeListener {
        void onLandscape(View view);

        void onReverseLandscape(View view);
    }

    public interface PortraitChangeListener {
        void onPortrait(View view);

        void onReversePortrait(View view);
    }

    public CutoutDisplayOrientationManager(Window window) {
        this.mWindowManager = (WindowManager) window.getContext().getSystemService("window");
        this.mContext = window.getContext();
        this.mWindow = window;
        this.mAdaptiveViewMetas = new ArrayList();
    }

    public boolean register(LandscapeChangeListener landscapeChangeListener, PortraitChangeListener portraitChangeListener) {
        this.mLandscapeChangeListener = landscapeChangeListener;
        this.mPortraitChangeListener = portraitChangeListener;
        this.mOrientationEventListener = new OrientationEventListener(this.mContext, 3) {
            /* class dji.publics.utils.CutoutDisplayOrientationManager.AnonymousClass1 */

            public void onOrientationChanged(int orientation) {
                int rotation = CutoutDisplayOrientationManager.this.getRotation();
                if (CutoutDisplayOrientationManager.this.mCurrentDisplayRotation != rotation) {
                    int unused = CutoutDisplayOrientationManager.this.mCurrentDisplayRotation = rotation;
                    int notchHeight = DisplayCutoutHelper.getNotchHeight(CutoutDisplayOrientationManager.this.mWindow);
                    if (CutoutDisplayOrientationManager.this.mCurrentDisplayRotation == 1) {
                        for (AdaptiveViewMeta viewMeta : CutoutDisplayOrientationManager.this.mAdaptiveViewMetas) {
                            if (viewMeta.adaptLandscape) {
                                viewMeta.adaptViewLeft(notchHeight);
                                if (CutoutDisplayOrientationManager.this.mLandscapeChangeListener != null) {
                                    CutoutDisplayOrientationManager.this.mLandscapeChangeListener.onLandscape(viewMeta.view);
                                }
                            } else {
                                viewMeta.reset();
                            }
                        }
                    } else if (CutoutDisplayOrientationManager.this.mCurrentDisplayRotation == 3) {
                        for (AdaptiveViewMeta viewMeta2 : CutoutDisplayOrientationManager.this.mAdaptiveViewMetas) {
                            if (viewMeta2.adaptReverseLandscape) {
                                viewMeta2.adaptViewRight(notchHeight);
                                if (CutoutDisplayOrientationManager.this.mLandscapeChangeListener != null) {
                                    CutoutDisplayOrientationManager.this.mLandscapeChangeListener.onReverseLandscape(viewMeta2.view);
                                }
                            } else {
                                viewMeta2.reset();
                            }
                        }
                    } else if (CutoutDisplayOrientationManager.this.mCurrentDisplayRotation == 0) {
                        for (AdaptiveViewMeta viewMeta3 : CutoutDisplayOrientationManager.this.mAdaptiveViewMetas) {
                            if (viewMeta3.adaptPortrait) {
                                viewMeta3.adaptViewTop(notchHeight);
                                if (CutoutDisplayOrientationManager.this.mPortraitChangeListener != null) {
                                    CutoutDisplayOrientationManager.this.mPortraitChangeListener.onPortrait(viewMeta3.view);
                                }
                            } else {
                                viewMeta3.reset();
                            }
                        }
                    } else if (CutoutDisplayOrientationManager.this.mCurrentDisplayRotation == 2) {
                        for (AdaptiveViewMeta viewMeta4 : CutoutDisplayOrientationManager.this.mAdaptiveViewMetas) {
                            if (viewMeta4.adaptReversePortrait) {
                                viewMeta4.adaptViewBottom(notchHeight);
                                if (CutoutDisplayOrientationManager.this.mPortraitChangeListener != null) {
                                    CutoutDisplayOrientationManager.this.mPortraitChangeListener.onReversePortrait(viewMeta4.view);
                                }
                            } else {
                                viewMeta4.reset();
                            }
                        }
                    }
                }
            }
        };
        if (this.mOrientationEventListener.canDetectOrientation()) {
            this.mOrientationEventListener.enable();
            return true;
        }
        this.mOrientationEventListener.disable();
        return false;
    }

    /* access modifiers changed from: private */
    public int getRotation() {
        if (this.mWindowManager != null) {
            return this.mWindowManager.getDefaultDisplay().getRotation();
        }
        return 0;
    }

    public void addView(View view) {
        if (view != null) {
            AdaptiveViewMeta viewMeta = new AdaptiveViewMeta(view);
            if (!this.mAdaptiveViewMetas.contains(viewMeta)) {
                this.mAdaptiveViewMetas.add(viewMeta);
                int rotation = getRotation();
                if (this.mCurrentDisplayRotation != rotation) {
                    this.mCurrentDisplayRotation = rotation;
                }
                int notchHeight = DisplayCutoutHelper.getNotchHeight(this.mWindow);
                if (this.mCurrentDisplayRotation == 1) {
                    viewMeta.adaptViewLeft(notchHeight);
                } else if (this.mCurrentDisplayRotation == 3) {
                    viewMeta.adaptViewRight(notchHeight);
                } else if (this.mCurrentDisplayRotation == 0) {
                    viewMeta.adaptViewTop(notchHeight);
                } else if (this.mCurrentDisplayRotation == 2) {
                    viewMeta.adaptViewBottom(notchHeight);
                }
            }
        }
    }

    public boolean addViewForLandscapeAdaption(View view, boolean adaptLandscape, boolean adaptReverseLandscape) {
        if (view == null) {
            return false;
        }
        AdaptiveViewMeta viewMeta = new AdaptiveViewMeta(view, adaptLandscape, adaptReverseLandscape, false, false);
        if (this.mAdaptiveViewMetas.contains(viewMeta)) {
            return false;
        }
        this.mAdaptiveViewMetas.add(viewMeta);
        int rotation = getRotation();
        if (this.mCurrentDisplayRotation != rotation) {
            this.mCurrentDisplayRotation = rotation;
        }
        int notchHeight = DisplayCutoutHelper.getNotchHeight(this.mWindow);
        if (this.mCurrentDisplayRotation == 1 && adaptLandscape) {
            viewMeta.adaptViewLeft(notchHeight);
        } else if (this.mCurrentDisplayRotation == 3 && adaptReverseLandscape) {
            viewMeta.adaptViewRight(notchHeight);
        }
        return true;
    }

    public boolean addViewForPortraitAdaption(View view, boolean adaptPortrait, boolean adaptReversePortrait) {
        if (view == null) {
            return false;
        }
        AdaptiveViewMeta viewMeta = new AdaptiveViewMeta(view, false, false, adaptPortrait, adaptReversePortrait);
        if (this.mAdaptiveViewMetas.contains(viewMeta)) {
            return false;
        }
        this.mAdaptiveViewMetas.add(viewMeta);
        int rotation = getRotation();
        if (this.mCurrentDisplayRotation != rotation) {
            this.mCurrentDisplayRotation = rotation;
        }
        int notchHeight = DisplayCutoutHelper.getNotchHeight(this.mWindow);
        if (this.mCurrentDisplayRotation == 0 && adaptPortrait) {
            viewMeta.adaptViewTop(notchHeight);
        } else if (this.mCurrentDisplayRotation == 1 && adaptReversePortrait) {
            viewMeta.adaptViewBottom(notchHeight);
        }
        return true;
    }

    public void removeView(View view) {
        if (view != null) {
            AdaptiveViewMeta viewMeta = new AdaptiveViewMeta(view);
            for (AdaptiveViewMeta internViewMeta : this.mAdaptiveViewMetas) {
                if (internViewMeta.equals(viewMeta)) {
                    internViewMeta.reset();
                }
            }
        }
    }

    public boolean unregister() {
        this.mLandscapeChangeListener = null;
        this.mOrientationEventListener.disable();
        this.mAdaptiveViewMetas.clear();
        this.mCurrentDisplayRotation = -1;
        return true;
    }

    private class AdaptiveViewMeta {
        boolean adaptLandscape = true;
        boolean adaptPortrait = true;
        boolean adaptReverseLandscape = true;
        boolean adaptReversePortrait = true;
        boolean bottomAdapted;
        boolean leftAdapted;
        int padding;
        boolean rightAdapted;
        boolean topAdapted;
        View view;

        public AdaptiveViewMeta(View view2) {
            this.view = view2;
        }

        public AdaptiveViewMeta(View view2, boolean adaptLandscape2, boolean adaptReverseLandscape2, boolean adaptPortrait2, boolean adaptReversePortrait2) {
            this.view = view2;
            this.adaptLandscape = adaptLandscape2;
            this.adaptReverseLandscape = adaptReverseLandscape2;
            this.adaptPortrait = adaptPortrait2;
            this.adaptReversePortrait = adaptReversePortrait2;
        }

        public void reset() {
            resetLeftAdaptState();
            resetRightAdaptState();
            resetTopAdaptState();
            resetBottomAdaptState();
        }

        public void adaptViewLeft(int notchHeight) {
            if (this.adaptLandscape) {
                this.padding = notchHeight;
                int rightPadding = resetRightAdaptState();
                int topPadding = resetTopAdaptState();
                int bottomPadding = resetBottomAdaptState();
                this.leftAdapted = true;
                this.view.setPadding(this.view.getPaddingLeft() + notchHeight, topPadding, rightPadding, bottomPadding);
            }
        }

        public void adaptViewRight(int notchHeight) {
            if (this.adaptReverseLandscape) {
                this.padding = notchHeight;
                int leftPadding = resetLeftAdaptState();
                int topPadding = resetTopAdaptState();
                int bottomPadding = resetBottomAdaptState();
                this.rightAdapted = true;
                this.view.setPadding(leftPadding, topPadding, this.view.getPaddingRight() + notchHeight, bottomPadding);
            }
        }

        public void adaptViewTop(int notchHeight) {
            if (this.adaptPortrait) {
                this.padding = notchHeight;
                int rightPadding = resetRightAdaptState();
                int leftPadding = resetLeftAdaptState();
                int bottomPadding = resetBottomAdaptState();
                this.topAdapted = true;
                this.view.setPadding(leftPadding, this.view.getPaddingTop() + this.padding, rightPadding, bottomPadding);
            }
        }

        public void adaptViewBottom(int notchHeight) {
            if (this.adaptReversePortrait) {
                this.padding = notchHeight;
                int rightPadding = resetRightAdaptState();
                int topPadding = resetTopAdaptState();
                int leftPadding = resetLeftAdaptState();
                this.bottomAdapted = true;
                this.view.setPadding(leftPadding, topPadding, rightPadding, this.view.getPaddingBottom() + this.padding);
            }
        }

        public int resetLeftAdaptState() {
            int leftPadding;
            if (!this.leftAdapted) {
                leftPadding = this.view.getPaddingLeft();
            } else {
                leftPadding = this.view.getPaddingLeft() - this.padding;
                this.leftAdapted = false;
            }
            this.view.setPadding(leftPadding, this.view.getPaddingTop(), this.view.getPaddingRight(), this.view.getPaddingBottom());
            return leftPadding;
        }

        public int resetRightAdaptState() {
            int rightPadding;
            if (!this.rightAdapted) {
                rightPadding = this.view.getPaddingRight();
            } else {
                rightPadding = this.view.getPaddingRight() - this.padding;
                this.rightAdapted = false;
            }
            this.view.setPadding(this.view.getPaddingLeft(), this.view.getPaddingTop(), rightPadding, this.view.getPaddingBottom());
            return rightPadding;
        }

        public int resetTopAdaptState() {
            int topPadding;
            if (!this.topAdapted) {
                topPadding = this.view.getPaddingTop();
            } else {
                topPadding = this.view.getPaddingTop() - this.padding;
                this.topAdapted = false;
            }
            this.view.setPadding(this.view.getPaddingLeft(), topPadding, this.view.getPaddingRight(), this.view.getPaddingBottom());
            return topPadding;
        }

        public int resetBottomAdaptState() {
            int bottomPadding;
            if (!this.bottomAdapted) {
                bottomPadding = this.view.getPaddingBottom();
            } else {
                bottomPadding = this.view.getPaddingBottom() - this.padding;
                this.bottomAdapted = false;
            }
            this.view.setPadding(this.view.getPaddingLeft(), this.view.getPaddingTop(), this.view.getPaddingRight(), bottomPadding);
            return bottomPadding;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (!(o instanceof AdaptiveViewMeta)) {
                return false;
            }
            if (!((AdaptiveViewMeta) o).view.equals(this.view)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.view.hashCode();
        }
    }
}
