package android.support.v7.widget;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ProgressBar;

class AppCompatProgressBarHelper {
    private static final int[] TINT_ATTRS = {16843067, 16843068};
    private Bitmap mSampleTile;
    private final ProgressBar mView;

    AppCompatProgressBarHelper(ProgressBar view) {
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, TINT_ATTRS, defStyleAttr, 0);
        Drawable drawable = a.getDrawableIfKnown(0);
        if (drawable != null) {
            this.mView.setIndeterminateDrawable(tileifyIndeterminate(drawable));
        }
        Drawable drawable2 = a.getDrawableIfKnown(1);
        if (drawable2 != null) {
            this.mView.setProgressDrawable(tileify(drawable2, false));
        }
        a.recycle();
    }

    /* JADX WARN: Type inference failed for: r12v8, types: [android.graphics.drawable.ClipDrawable], assign insn: 0x00a5: CONSTRUCTOR  (r12v8 ? I:android.graphics.drawable.ClipDrawable) = 
      (r10v0 'shapeDrawable' android.graphics.drawable.ShapeDrawable A[D('shapeDrawable' android.graphics.drawable.ShapeDrawable)])
      (3 int)
      (1 int)
     call: android.graphics.drawable.ClipDrawable.<init>(android.graphics.drawable.Drawable, int, int):void type: CONSTRUCTOR */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.drawable.Drawable tileify(android.graphics.drawable.Drawable r16, boolean r17) {
        /*
            r15 = this;
            r0 = r16
            boolean r12 = r0 instanceof android.support.v4.graphics.drawable.WrappedDrawable
            if (r12 == 0) goto L_0x0020
            r12 = r16
            android.support.v4.graphics.drawable.WrappedDrawable r12 = (android.support.v4.graphics.drawable.WrappedDrawable) r12
            android.graphics.drawable.Drawable r7 = r12.getWrappedDrawable()
            if (r7 == 0) goto L_0x001d
            r0 = r17
            android.graphics.drawable.Drawable r7 = r15.tileify(r7, r0)
            r12 = r16
            android.support.v4.graphics.drawable.WrappedDrawable r12 = (android.support.v4.graphics.drawable.WrappedDrawable) r12
            r12.setWrappedDrawable(r7)
        L_0x001d:
            r8 = r16
        L_0x001f:
            return r8
        L_0x0020:
            r0 = r16
            boolean r12 = r0 instanceof android.graphics.drawable.LayerDrawable
            if (r12 == 0) goto L_0x0063
            r2 = r16
            android.graphics.drawable.LayerDrawable r2 = (android.graphics.drawable.LayerDrawable) r2
            int r1 = r2.getNumberOfLayers()
            android.graphics.drawable.Drawable[] r9 = new android.graphics.drawable.Drawable[r1]
            r5 = 0
        L_0x0031:
            if (r5 >= r1) goto L_0x0051
            int r6 = r2.getId(r5)
            android.graphics.drawable.Drawable r13 = r2.getDrawable(r5)
            r12 = 16908301(0x102000d, float:2.3877265E-38)
            if (r6 == r12) goto L_0x0045
            r12 = 16908303(0x102000f, float:2.387727E-38)
            if (r6 != r12) goto L_0x004f
        L_0x0045:
            r12 = 1
        L_0x0046:
            android.graphics.drawable.Drawable r12 = r15.tileify(r13, r12)
            r9[r5] = r12
            int r5 = r5 + 1
            goto L_0x0031
        L_0x004f:
            r12 = 0
            goto L_0x0046
        L_0x0051:
            android.graphics.drawable.LayerDrawable r8 = new android.graphics.drawable.LayerDrawable
            r8.<init>(r9)
            r5 = 0
        L_0x0057:
            if (r5 >= r1) goto L_0x001f
            int r12 = r2.getId(r5)
            r8.setId(r5, r12)
            int r5 = r5 + 1
            goto L_0x0057
        L_0x0063:
            r0 = r16
            boolean r12 = r0 instanceof android.graphics.drawable.BitmapDrawable
            if (r12 == 0) goto L_0x001d
            r3 = r16
            android.graphics.drawable.BitmapDrawable r3 = (android.graphics.drawable.BitmapDrawable) r3
            android.graphics.Bitmap r11 = r3.getBitmap()
            android.graphics.Bitmap r12 = r15.mSampleTile
            if (r12 != 0) goto L_0x0077
            r15.mSampleTile = r11
        L_0x0077:
            android.graphics.drawable.ShapeDrawable r10 = new android.graphics.drawable.ShapeDrawable
            android.graphics.drawable.shapes.Shape r12 = r15.getDrawableShape()
            r10.<init>(r12)
            android.graphics.BitmapShader r4 = new android.graphics.BitmapShader
            android.graphics.Shader$TileMode r12 = android.graphics.Shader.TileMode.REPEAT
            android.graphics.Shader$TileMode r13 = android.graphics.Shader.TileMode.CLAMP
            r4.<init>(r11, r12, r13)
            android.graphics.Paint r12 = r10.getPaint()
            r12.setShader(r4)
            android.graphics.Paint r12 = r10.getPaint()
            android.graphics.Paint r13 = r3.getPaint()
            android.graphics.ColorFilter r13 = r13.getColorFilter()
            r12.setColorFilter(r13)
            if (r17 == 0) goto L_0x00a9
            android.graphics.drawable.ClipDrawable r12 = new android.graphics.drawable.ClipDrawable
            r13 = 3
            r14 = 1
            r12.<init>(r10, r13, r14)
            r10 = r12
        L_0x00a9:
            r8 = r10
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatProgressBarHelper.tileify(android.graphics.drawable.Drawable, boolean):android.graphics.drawable.Drawable");
    }

    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (!(drawable instanceof AnimationDrawable)) {
            return drawable;
        }
        AnimationDrawable background = (AnimationDrawable) drawable;
        int N = background.getNumberOfFrames();
        AnimationDrawable newBg = new AnimationDrawable();
        newBg.setOneShot(background.isOneShot());
        for (int i = 0; i < N; i++) {
            Drawable frame = tileify(background.getFrame(i), true);
            frame.setLevel(10000);
            newBg.addFrame(frame, background.getDuration(i));
        }
        newBg.setLevel(10000);
        return newBg;
    }

    private Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null);
    }

    /* access modifiers changed from: package-private */
    public Bitmap getSampleTime() {
        return this.mSampleTile;
    }
}
