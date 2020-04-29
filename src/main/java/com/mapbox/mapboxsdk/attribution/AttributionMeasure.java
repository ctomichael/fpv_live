package com.mapbox.mapboxsdk.attribution;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

public class AttributionMeasure {
    /* access modifiers changed from: private */
    public Bitmap logo;
    /* access modifiers changed from: private */
    public Bitmap logoSmall;
    /* access modifiers changed from: private */
    public float margin;
    private boolean shorterText;
    /* access modifiers changed from: private */
    public Bitmap snapshot;
    /* access modifiers changed from: private */
    public TextView textView;
    /* access modifiers changed from: private */
    public TextView textViewShort;

    public interface Command {
        @Nullable
        AttributionLayout execute(AttributionMeasure attributionMeasure);
    }

    AttributionMeasure(Bitmap snapshot2, Bitmap logo2, Bitmap logoSmall2, TextView tv, TextView tvShort, float margin2) {
        this.snapshot = snapshot2;
        this.logo = logo2;
        this.logoSmall = logoSmall2;
        this.textView = tv;
        this.textViewShort = tvShort;
        this.margin = margin2;
    }

    @Nullable
    public AttributionLayout measure() {
        AttributionLayout attributionLayout = new Chain(new FullLogoLongTextCommand(), new FullLogoShortTextCommand(), new SmallLogoLongTextCommand(), new SmallLogoShortTextCommand(), new LongTextCommand(), new ShortTextCommand(), new NoTextCommand()).start(this);
        this.shorterText = attributionLayout.isShortText();
        return attributionLayout;
    }

    private static class FullLogoLongTextCommand implements Command {
        private FullLogoLongTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            boolean fitBounds;
            if (measure.getLogoContainerWidth() + measure.getTextViewContainerWidth() <= measure.getMaxSize()) {
                fitBounds = true;
            } else {
                fitBounds = false;
            }
            if (!fitBounds) {
                return null;
            }
            return new AttributionLayout(measure.logo, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textView, measure.margin), false);
        }
    }

    private static class FullLogoShortTextCommand implements Command {
        private FullLogoShortTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            if (!(measure.getLogoContainerWidth() + measure.getTextViewShortContainerWidth() <= measure.getMaxSizeShort())) {
                return null;
            }
            return new AttributionLayout(measure.logo, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textViewShort, measure.margin), true);
        }
    }

    private static class SmallLogoLongTextCommand implements Command {
        private SmallLogoLongTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            boolean fitBounds;
            if (measure.getLogoSmallContainerWidth() + measure.getTextViewContainerWidth() <= measure.getMaxSize()) {
                fitBounds = true;
            } else {
                fitBounds = false;
            }
            if (!fitBounds) {
                return null;
            }
            return new AttributionLayout(measure.logoSmall, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textView, measure.margin), false);
        }
    }

    private static class SmallLogoShortTextCommand implements Command {
        private SmallLogoShortTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            if (!(measure.getLogoContainerWidth() + measure.getTextViewShortContainerWidth() <= measure.getMaxSizeShort())) {
                return null;
            }
            return new AttributionLayout(measure.logoSmall, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textViewShort, measure.margin), true);
        }
    }

    private static class LongTextCommand implements Command {
        private LongTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            boolean fitBounds;
            if (measure.getTextViewContainerWidth() + measure.margin <= measure.getMaxSize()) {
                fitBounds = true;
            } else {
                fitBounds = false;
            }
            if (fitBounds) {
                return new AttributionLayout(null, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textView, measure.margin), false);
            }
            return null;
        }
    }

    private static class ShortTextCommand implements Command {
        private ShortTextCommand() {
        }

        @Nullable
        public AttributionLayout execute(@NonNull AttributionMeasure measure) {
            if (measure.getTextViewShortContainerWidth() + measure.margin <= measure.getMaxSizeShort()) {
                return new AttributionLayout(null, AttributionMeasure.calculateAnchor(measure.snapshot, measure.textViewShort, measure.margin), true);
            }
            return null;
        }
    }

    private static class NoTextCommand implements Command {
        private NoTextCommand() {
        }

        @NonNull
        public AttributionLayout execute(AttributionMeasure measure) {
            return new AttributionLayout(null, null, false);
        }
    }

    /* access modifiers changed from: private */
    public static PointF calculateAnchor(Bitmap snapshot2, TextView textView2, float margin2) {
        return new PointF(((float) (snapshot2.getWidth() - textView2.getMeasuredWidth())) - margin2, (((float) snapshot2.getHeight()) - margin2) - ((float) textView2.getMeasuredHeight()));
    }

    public TextView getTextView() {
        return this.shorterText ? this.textViewShort : this.textView;
    }

    private class Chain {
        public List<Command> commands;

        Chain(Command... commands2) {
            this.commands = Arrays.asList(commands2);
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x000d  */
        @android.support.annotation.Nullable
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.mapbox.mapboxsdk.attribution.AttributionLayout start(com.mapbox.mapboxsdk.attribution.AttributionMeasure r5) {
            /*
                r4 = this;
                r0 = 0
                java.util.List<com.mapbox.mapboxsdk.attribution.AttributionMeasure$Command> r2 = r4.commands
                java.util.Iterator r2 = r2.iterator()
            L_0x0007:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x0019
                java.lang.Object r1 = r2.next()
                com.mapbox.mapboxsdk.attribution.AttributionMeasure$Command r1 = (com.mapbox.mapboxsdk.attribution.AttributionMeasure.Command) r1
                com.mapbox.mapboxsdk.attribution.AttributionLayout r0 = r1.execute(r5)
                if (r0 == 0) goto L_0x0007
            L_0x0019:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mapbox.mapboxsdk.attribution.AttributionMeasure.Chain.start(com.mapbox.mapboxsdk.attribution.AttributionMeasure):com.mapbox.mapboxsdk.attribution.AttributionLayout");
        }
    }

    /* access modifiers changed from: private */
    public float getTextViewContainerWidth() {
        return ((float) this.textView.getMeasuredWidth()) + this.margin;
    }

    /* access modifiers changed from: private */
    public float getLogoContainerWidth() {
        return ((float) this.logo.getWidth()) + (2.0f * this.margin);
    }

    /* access modifiers changed from: private */
    public float getTextViewShortContainerWidth() {
        return ((float) this.textViewShort.getMeasuredWidth()) + this.margin;
    }

    /* access modifiers changed from: private */
    public float getLogoSmallContainerWidth() {
        return ((float) this.logoSmall.getWidth()) + (2.0f * this.margin);
    }

    /* access modifiers changed from: private */
    public float getMaxSize() {
        return (float) ((this.snapshot.getWidth() * 8) / 10);
    }

    /* access modifiers changed from: private */
    public float getMaxSizeShort() {
        return (float) this.snapshot.getWidth();
    }

    public static class Builder {
        private Bitmap logo;
        private Bitmap logoSmall;
        private float marginPadding;
        private Bitmap snapshot;
        private TextView textView;
        private TextView textViewShort;

        @NonNull
        public Builder setSnapshot(Bitmap snapshot2) {
            this.snapshot = snapshot2;
            return this;
        }

        @NonNull
        public Builder setLogo(Bitmap logo2) {
            this.logo = logo2;
            return this;
        }

        @NonNull
        public Builder setLogoSmall(Bitmap logoSmall2) {
            this.logoSmall = logoSmall2;
            return this;
        }

        @NonNull
        public Builder setTextView(TextView textView2) {
            this.textView = textView2;
            return this;
        }

        @NonNull
        public Builder setTextViewShort(TextView textViewShort2) {
            this.textViewShort = textViewShort2;
            return this;
        }

        @NonNull
        public Builder setMarginPadding(float marginPadding2) {
            this.marginPadding = marginPadding2;
            return this;
        }

        @NonNull
        public AttributionMeasure build() {
            return new AttributionMeasure(this.snapshot, this.logo, this.logoSmall, this.textView, this.textViewShort, this.marginPadding);
        }
    }
}
