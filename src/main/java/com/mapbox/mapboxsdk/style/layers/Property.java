package com.mapbox.mapboxsdk.style.layers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Property {
    public static final String ANCHOR_MAP = "map";
    public static final String ANCHOR_VIEWPORT = "viewport";
    public static final String CIRCLE_PITCH_ALIGNMENT_MAP = "map";
    public static final String CIRCLE_PITCH_ALIGNMENT_VIEWPORT = "viewport";
    public static final String CIRCLE_PITCH_SCALE_MAP = "map";
    public static final String CIRCLE_PITCH_SCALE_VIEWPORT = "viewport";
    public static final String CIRCLE_TRANSLATE_ANCHOR_MAP = "map";
    public static final String CIRCLE_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String FILL_EXTRUSION_TRANSLATE_ANCHOR_MAP = "map";
    public static final String FILL_EXTRUSION_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String FILL_TRANSLATE_ANCHOR_MAP = "map";
    public static final String FILL_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String HILLSHADE_ILLUMINATION_ANCHOR_MAP = "map";
    public static final String HILLSHADE_ILLUMINATION_ANCHOR_VIEWPORT = "viewport";
    public static final String ICON_ANCHOR_BOTTOM = "bottom";
    public static final String ICON_ANCHOR_BOTTOM_LEFT = "bottom-left";
    public static final String ICON_ANCHOR_BOTTOM_RIGHT = "bottom-right";
    public static final String ICON_ANCHOR_CENTER = "center";
    public static final String ICON_ANCHOR_LEFT = "left";
    public static final String ICON_ANCHOR_RIGHT = "right";
    public static final String ICON_ANCHOR_TOP = "top";
    public static final String ICON_ANCHOR_TOP_LEFT = "top-left";
    public static final String ICON_ANCHOR_TOP_RIGHT = "top-right";
    public static final String ICON_PITCH_ALIGNMENT_AUTO = "auto";
    public static final String ICON_PITCH_ALIGNMENT_MAP = "map";
    public static final String ICON_PITCH_ALIGNMENT_VIEWPORT = "viewport";
    public static final String ICON_ROTATION_ALIGNMENT_AUTO = "auto";
    public static final String ICON_ROTATION_ALIGNMENT_MAP = "map";
    public static final String ICON_ROTATION_ALIGNMENT_VIEWPORT = "viewport";
    public static final String ICON_TEXT_FIT_BOTH = "both";
    public static final String ICON_TEXT_FIT_HEIGHT = "height";
    public static final String ICON_TEXT_FIT_NONE = "none";
    public static final String ICON_TEXT_FIT_WIDTH = "width";
    public static final String ICON_TRANSLATE_ANCHOR_MAP = "map";
    public static final String ICON_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String LINE_CAP_BUTT = "butt";
    public static final String LINE_CAP_ROUND = "round";
    public static final String LINE_CAP_SQUARE = "square";
    public static final String LINE_JOIN_BEVEL = "bevel";
    public static final String LINE_JOIN_MITER = "miter";
    public static final String LINE_JOIN_ROUND = "round";
    public static final String LINE_TRANSLATE_ANCHOR_MAP = "map";
    public static final String LINE_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String NONE = "none";
    public static final String RASTER_RESAMPLING_LINEAR = "linear";
    public static final String RASTER_RESAMPLING_NEAREST = "nearest";
    public static final String SYMBOL_PLACEMENT_LINE = "line";
    public static final String SYMBOL_PLACEMENT_LINE_CENTER = "line-center";
    public static final String SYMBOL_PLACEMENT_POINT = "point";
    public static final String SYMBOL_Z_ORDER_AUTO = "auto";
    public static final String SYMBOL_Z_ORDER_SOURCE = "source";
    public static final String SYMBOL_Z_ORDER_VIEWPORT_Y = "viewport-y";
    public static final String TEXT_ANCHOR_BOTTOM = "bottom";
    public static final String TEXT_ANCHOR_BOTTOM_LEFT = "bottom-left";
    public static final String TEXT_ANCHOR_BOTTOM_RIGHT = "bottom-right";
    public static final String TEXT_ANCHOR_CENTER = "center";
    public static final String TEXT_ANCHOR_LEFT = "left";
    public static final String TEXT_ANCHOR_RIGHT = "right";
    public static final String TEXT_ANCHOR_TOP = "top";
    public static final String TEXT_ANCHOR_TOP_LEFT = "top-left";
    public static final String TEXT_ANCHOR_TOP_RIGHT = "top-right";
    public static final String TEXT_JUSTIFY_AUTO = "auto";
    public static final String TEXT_JUSTIFY_CENTER = "center";
    public static final String TEXT_JUSTIFY_LEFT = "left";
    public static final String TEXT_JUSTIFY_RIGHT = "right";
    public static final String TEXT_PITCH_ALIGNMENT_AUTO = "auto";
    public static final String TEXT_PITCH_ALIGNMENT_MAP = "map";
    public static final String TEXT_PITCH_ALIGNMENT_VIEWPORT = "viewport";
    public static final String TEXT_ROTATION_ALIGNMENT_AUTO = "auto";
    public static final String TEXT_ROTATION_ALIGNMENT_MAP = "map";
    public static final String TEXT_ROTATION_ALIGNMENT_VIEWPORT = "viewport";
    public static final String TEXT_TRANSFORM_LOWERCASE = "lowercase";
    public static final String TEXT_TRANSFORM_NONE = "none";
    public static final String TEXT_TRANSFORM_UPPERCASE = "uppercase";
    public static final String TEXT_TRANSLATE_ANCHOR_MAP = "map";
    public static final String TEXT_TRANSLATE_ANCHOR_VIEWPORT = "viewport";
    public static final String TEXT_WRITING_MODE_HORIZONTAL = "horizontal";
    public static final String TEXT_WRITING_MODE_VERTICAL = "vertical";
    public static final String VISIBLE = "visible";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CIRCLE_PITCH_ALIGNMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CIRCLE_PITCH_SCALE {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CIRCLE_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FILL_EXTRUSION_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FILL_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HILLSHADE_ILLUMINATION_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ICON_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ICON_PITCH_ALIGNMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ICON_ROTATION_ALIGNMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ICON_TEXT_FIT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ICON_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LINE_CAP {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LINE_JOIN {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LINE_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RASTER_RESAMPLING {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SYMBOL_PLACEMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SYMBOL_Z_ORDER {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_JUSTIFY {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_PITCH_ALIGNMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_ROTATION_ALIGNMENT {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_TRANSFORM {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_TRANSLATE_ANCHOR {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TEXT_WRITING_MODE {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VISIBILITY {
    }

    private Property() {
    }
}
