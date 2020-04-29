package dji.thirdparty.sanselan.common;

import dji.thirdparty.sanselan.common.IImageMetadata;
import java.util.ArrayList;

public class ImageMetadata implements IImageMetadata {
    protected static final String newline = System.getProperty("line.separator");
    private final ArrayList items = new ArrayList();

    public void add(String keyword, String text) {
        add(new Item(keyword, text));
    }

    public void add(IImageMetadata.IImageMetadataItem item) {
        this.items.add(item);
    }

    public ArrayList getItems() {
        return new ArrayList(this.items);
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < this.items.size(); i++) {
            if (i > 0) {
                result.append(newline);
            }
            result.append(((IImageMetadata.IImageMetadataItem) this.items.get(i)).toString(prefix + "\t"));
        }
        return result.toString();
    }

    public static class Item implements IImageMetadata.IImageMetadataItem {
        private final String keyword;
        private final String text;

        public Item(String keyword2, String text2) {
            this.keyword = keyword2;
            this.text = text2;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public String getText() {
            return this.text;
        }

        public String toString() {
            return toString(null);
        }

        public String toString(String prefix) {
            String result = this.keyword + ": " + this.text;
            if (prefix != null) {
                return prefix + result;
            }
            return result;
        }
    }
}
