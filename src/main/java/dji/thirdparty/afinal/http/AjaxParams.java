package dji.thirdparty.afinal.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class AjaxParams {
    private static String ENCODING = "UTF-8";
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    protected ConcurrentHashMap<String, String> urlParams;

    public AjaxParams() {
        init();
    }

    public AjaxParams(Map<String, String> source) {
        init();
        for (Map.Entry<String, String> entry : source.entrySet()) {
            put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public AjaxParams(String key, String value) {
        init();
        put(key, value);
    }

    public AjaxParams(Object... keysAndValues) {
        init();
        int len = keysAndValues.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Supplied arguments must be even");
        }
        for (int i = 0; i < len; i += 2) {
            put(String.valueOf(keysAndValues[i]), String.valueOf(keysAndValues[i + 1]));
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException {
        put(key, new FileInputStream(file), file.getName());
    }

    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    public void put(String key, InputStream stream, String fileName) {
        put(key, stream, fileName, null);
    }

    public void put(String key, InputStream stream, String fileName, String contentType) {
        if (key != null && stream != null) {
            this.fileParams.put(key, new FileWrapper(stream, fileName, contentType));
        }
    }

    public void remove(String key) {
        this.urlParams.remove(key);
        this.fileParams.remove(key);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : this.urlParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry.getKey());
            result.append("=");
            result.append((String) entry.getValue());
        }
        for (Map.Entry<String, FileWrapper> entry2 : this.fileParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry2.getKey());
            result.append("=");
            result.append("FILE");
        }
        return result.toString();
    }

    public HttpEntity getEntity() {
        if (!this.fileParams.isEmpty()) {
            HttpEntity multipartEntity = new MultipartEntity();
            for (Map.Entry<String, String> entry : this.urlParams.entrySet()) {
                multipartEntity.addPart((String) entry.getKey(), (String) entry.getValue());
            }
            int currentIndex = 0;
            int lastIndex = this.fileParams.entrySet().size() - 1;
            for (Map.Entry<String, FileWrapper> entry2 : this.fileParams.entrySet()) {
                FileWrapper file = (FileWrapper) entry2.getValue();
                if (file.inputStream != null) {
                    boolean isLast = currentIndex == lastIndex;
                    if (file.contentType != null) {
                        multipartEntity.addPart((String) entry2.getKey(), file.getFileName(), file.inputStream, file.contentType, isLast);
                    } else {
                        multipartEntity.addPart((String) entry2.getKey(), file.getFileName(), file.inputStream, isLast);
                    }
                }
                currentIndex++;
            }
            return multipartEntity;
        }
        try {
            return new UrlEncodedFormEntity(getParamsList(), ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpEntity getFormDataEntity() {
        MultipartEntity multipartEntity = new MultipartEntity();
        if (!this.urlParams.isEmpty()) {
            int lastUrlParam = this.urlParams.entrySet().size() - 1;
            int currentUrlParamIndex = 0;
            for (Map.Entry<String, String> entry : this.urlParams.entrySet()) {
                multipartEntity.addPart((String) entry.getKey(), (String) entry.getValue(), currentUrlParamIndex == lastUrlParam && this.fileParams.isEmpty());
                currentUrlParamIndex++;
            }
        }
        if (!this.fileParams.isEmpty()) {
            int currentIndex = 0;
            int lastIndex = this.fileParams.entrySet().size() - 1;
            for (Map.Entry<String, FileWrapper> entry2 : this.fileParams.entrySet()) {
                FileWrapper file = (FileWrapper) entry2.getValue();
                if (file.inputStream != null) {
                    boolean isLast = currentIndex == lastIndex;
                    if (file.contentType != null) {
                        multipartEntity.addPart((String) entry2.getKey(), file.getFileName(), file.inputStream, file.contentType, isLast);
                    } else {
                        multipartEntity.addPart((String) entry2.getKey(), file.getFileName(), file.inputStream, isLast);
                    }
                }
                currentIndex++;
            }
        }
        return multipartEntity;
    }

    private void init() {
        this.urlParams = new ConcurrentHashMap<>();
        this.fileParams = new ConcurrentHashMap<>();
    }

    /* access modifiers changed from: protected */
    public List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<>();
        for (Map.Entry<String, String> entry : this.urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        return lparams;
    }

    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

    private static class FileWrapper {
        public String contentType;
        public String fileName;
        public InputStream inputStream;

        public FileWrapper(InputStream inputStream2, String fileName2, String contentType2) {
            this.inputStream = inputStream2;
            this.fileName = fileName2;
            this.contentType = contentType2;
        }

        public String getFileName() {
            if (this.fileName != null) {
                return this.fileName;
            }
            return "nofilename";
        }
    }
}
