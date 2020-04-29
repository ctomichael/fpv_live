package com.loc;

import android.util.Xml;
import com.loc.dy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* compiled from: TransactionXMLFile */
public final class ea {
    /* access modifiers changed from: private */
    public static final Object c = new Object();
    private final Object a = new Object();
    private File b;
    private HashMap<File, a> d = new HashMap<>();

    /* compiled from: TransactionXMLFile */
    private static final class a implements dy {
        private static final Object f = new Object();
        private final File a;
        private final File b;
        private final int c;
        /* access modifiers changed from: private */
        public Map d;
        private boolean e = false;
        /* access modifiers changed from: private */
        public WeakHashMap<Object, Object> g;

        /* renamed from: com.loc.ea$a$a  reason: collision with other inner class name */
        /* compiled from: TransactionXMLFile */
        public final class C0015a implements dy.a {
            private final Map<String, Object> b = new HashMap();
            private boolean c = false;

            public C0015a() {
            }

            public final dy.a a() {
                synchronized (this) {
                    this.c = true;
                }
                return this;
            }

            public final dy.a a(String str, float f) {
                synchronized (this) {
                    this.b.put(str, Float.valueOf(f));
                }
                return this;
            }

            public final dy.a a(String str, int i) {
                synchronized (this) {
                    this.b.put(str, Integer.valueOf(i));
                }
                return this;
            }

            public final dy.a a(String str, long j) {
                synchronized (this) {
                    this.b.put(str, Long.valueOf(j));
                }
                return this;
            }

            public final dy.a a(String str, String str2) {
                synchronized (this) {
                    this.b.put(str, str2);
                }
                return this;
            }

            public final dy.a a(String str, boolean z) {
                synchronized (this) {
                    this.b.put(str, Boolean.valueOf(z));
                }
                return this;
            }

            public final boolean b() {
                boolean z;
                HashSet hashSet;
                ArrayList arrayList;
                boolean c2;
                synchronized (ea.c) {
                    z = a.this.g.size() > 0;
                    if (z) {
                        ArrayList arrayList2 = new ArrayList();
                        hashSet = new HashSet(a.this.g.keySet());
                        arrayList = arrayList2;
                    } else {
                        hashSet = null;
                        arrayList = null;
                    }
                    synchronized (this) {
                        if (this.c) {
                            a.this.d.clear();
                            this.c = false;
                        }
                        for (Map.Entry entry : this.b.entrySet()) {
                            String str = (String) entry.getKey();
                            Object value = entry.getValue();
                            if (value == this) {
                                a.this.d.remove(str);
                            } else {
                                a.this.d.put(str, value);
                            }
                            if (z) {
                                arrayList.add(str);
                            }
                        }
                        this.b.clear();
                    }
                    c2 = a.this.f();
                    if (c2) {
                        a.this.d();
                    }
                }
                if (z) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        arrayList.get(size);
                        Iterator it2 = hashSet.iterator();
                        while (it2.hasNext()) {
                            it2.next();
                        }
                    }
                }
                return c2;
            }
        }

        a(File file, Map map) {
            this.a = file;
            this.b = ea.b(file);
            this.c = 0;
            this.d = map == null ? new HashMap() : map;
            this.g = new WeakHashMap<>();
        }

        private static FileOutputStream a(File file) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e2) {
                if (!file.getParentFile().mkdir()) {
                    return null;
                }
                try {
                    return new FileOutputStream(file);
                } catch (FileNotFoundException e3) {
                    return null;
                }
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.loc.cw.a(java.util.Map, java.lang.String, org.xmlpull.v1.XmlSerializer):void
         arg types: [java.util.Map, ?[OBJECT, ARRAY], com.loc.dx]
         candidates:
          com.loc.cw.a(org.xmlpull.v1.XmlPullParser, java.lang.String, java.lang.String[]):java.util.HashMap
          com.loc.cw.a(java.lang.Object, java.lang.String, org.xmlpull.v1.XmlSerializer):void
          com.loc.cw.a(java.util.List, java.lang.String, org.xmlpull.v1.XmlSerializer):void
          com.loc.cw.a(int[], java.lang.String, org.xmlpull.v1.XmlSerializer):void
          com.loc.cw.a(java.util.Map, java.lang.String, org.xmlpull.v1.XmlSerializer):void */
        /* access modifiers changed from: private */
        public boolean f() {
            if (this.a.exists()) {
                if (this.b.exists()) {
                    this.a.delete();
                } else if (!this.a.renameTo(this.b)) {
                    return false;
                }
            }
            try {
                FileOutputStream a2 = a(this.a);
                if (a2 == null) {
                    return false;
                }
                Map map = this.d;
                dx dxVar = new dx();
                dxVar.setOutput(a2, "utf-8");
                dxVar.startDocument(null, true);
                dxVar.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                cw.a(map, (String) null, (XmlSerializer) dxVar);
                dxVar.endDocument();
                a2.close();
                this.b.delete();
                return true;
            } catch (IOException | XmlPullParserException e2) {
                if (!this.a.exists()) {
                    return false;
                }
                this.a.delete();
                return false;
            }
        }

        public final long a(String str) {
            long longValue;
            synchronized (this) {
                Long l = (Long) this.d.get(str);
                longValue = l != null ? l.longValue() : 0;
            }
            return longValue;
        }

        public final String a(String str, String str2) {
            String str3;
            synchronized (this) {
                str3 = (String) this.d.get(str);
                if (str3 == null) {
                    str3 = str2;
                }
            }
            return str3;
        }

        public final void a(Map map) {
            if (map != null) {
                synchronized (this) {
                    this.d = map;
                }
            }
        }

        public final boolean a() {
            return this.a != null && new File(this.a.getAbsolutePath()).exists();
        }

        public final Map<String, ?> b() {
            HashMap hashMap;
            synchronized (this) {
                hashMap = new HashMap(this.d);
            }
            return hashMap;
        }

        public final dy.a c() {
            return new C0015a();
        }

        public final void d() {
            synchronized (this) {
                this.e = true;
            }
        }

        public final boolean e() {
            boolean z;
            synchronized (this) {
                z = this.e;
            }
            return z;
        }
    }

    public ea(String str) {
        if (str == null || str.length() <= 0) {
            throw new RuntimeException("Directory can not be empty");
        }
        this.b = new File(str);
    }

    private File b() {
        File file;
        synchronized (this.a) {
            file = this.b;
        }
        return file;
    }

    /* access modifiers changed from: private */
    public static File b(File file) {
        return new File(file.getPath() + ".bak");
    }

    public final dy a(String str) {
        a aVar;
        HashMap hashMap;
        File b2 = b();
        String str2 = str + ".xml";
        if (str2.indexOf(File.separatorChar) < 0) {
            File file = new File(b2, str2);
            synchronized (c) {
                aVar = this.d.get(file);
                if (aVar == null || aVar.e()) {
                    File b3 = b(file);
                    if (b3.exists()) {
                        file.delete();
                        b3.renameTo(file);
                    }
                    if (file.exists()) {
                        file.canRead();
                    }
                    if (!file.exists() || !file.canRead()) {
                        hashMap = null;
                    } else {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            XmlPullParser newPullParser = Xml.newPullParser();
                            newPullParser.setInput(fileInputStream, null);
                            hashMap = (HashMap) cw.a(newPullParser, new String[1]);
                            try {
                                fileInputStream.close();
                            } catch (XmlPullParserException e) {
                                try {
                                    FileInputStream fileInputStream2 = new FileInputStream(file);
                                    byte[] bArr = new byte[fileInputStream2.available()];
                                    fileInputStream2.read(bArr);
                                    new String(bArr, 0, bArr.length, "UTF-8");
                                } catch (FileNotFoundException e2) {
                                    e2.printStackTrace();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            } catch (FileNotFoundException | IOException e4) {
                            }
                        } catch (XmlPullParserException e5) {
                            hashMap = null;
                            FileInputStream fileInputStream22 = new FileInputStream(file);
                            byte[] bArr2 = new byte[fileInputStream22.available()];
                            fileInputStream22.read(bArr2);
                            new String(bArr2, 0, bArr2.length, "UTF-8");
                        } catch (FileNotFoundException e6) {
                            hashMap = null;
                        } catch (IOException e7) {
                            hashMap = null;
                        }
                    }
                    synchronized (c) {
                        if (aVar != null) {
                            aVar.a(hashMap);
                        } else {
                            aVar = this.d.get(file);
                            if (aVar == null) {
                                aVar = new a(file, hashMap);
                                this.d.put(file, aVar);
                            }
                        }
                    }
                }
            }
            return aVar;
        }
        throw new IllegalArgumentException("File " + str2 + " contains a path separator");
    }
}
