package dji.thirdparty.sanselan.common;

import java.util.ArrayList;

public interface IImageMetadata {

    public interface IImageMetadataItem {
        String toString();

        String toString(String str);
    }

    ArrayList getItems();

    String toString(String str);
}
