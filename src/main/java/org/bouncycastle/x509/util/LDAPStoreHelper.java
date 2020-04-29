package org.bouncycastle.x509.util;

import com.mapzen.android.lost.internal.FusionEngine;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.jce.provider.X509AttrCertParser;
import org.bouncycastle.jce.provider.X509CRLParser;
import org.bouncycastle.jce.provider.X509CertPairParser;
import org.bouncycastle.jce.provider.X509CertParser;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.X509CertPairStoreSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509CertificatePair;

public class LDAPStoreHelper {
    private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
    private static String REFERRALS_IGNORE = "ignore";
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private static int cacheSize = 32;
    private static long lifeTime = FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
    private Map cacheMap = new HashMap(cacheSize);
    private X509LDAPCertStoreParameters params;

    public LDAPStoreHelper(X509LDAPCertStoreParameters x509LDAPCertStoreParameters) {
        this.params = x509LDAPCertStoreParameters;
    }

    private synchronized void addToCache(String str, List list) {
        Object obj;
        Date date = new Date(System.currentTimeMillis());
        ArrayList arrayList = new ArrayList();
        arrayList.add(date);
        arrayList.add(list);
        if (this.cacheMap.containsKey(str)) {
            this.cacheMap.put(str, arrayList);
        } else {
            if (this.cacheMap.size() >= cacheSize) {
                long time = date.getTime();
                Object obj2 = null;
                for (Map.Entry entry : this.cacheMap.entrySet()) {
                    long time2 = ((Date) ((List) entry.getValue()).get(0)).getTime();
                    if (time2 < time) {
                        obj = entry.getKey();
                    } else {
                        obj = obj2;
                        time2 = time;
                    }
                    obj2 = obj;
                    time = time2;
                }
                this.cacheMap.remove(obj2);
            }
            this.cacheMap.put(str, arrayList);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00e0 A[LOOP:1: B:32:0x00da->B:34:0x00e0, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List attrCertSubjectSerialSearch(org.bouncycastle.x509.X509AttributeCertStoreSelector r8, java.lang.String[] r9, java.lang.String[] r10, java.lang.String[] r11) throws org.bouncycastle.util.StoreException {
        /*
            r7 = this;
            r1 = 0
            r2 = 0
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.HashSet r4 = new java.util.HashSet
            r4.<init>()
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r8.getHolder()
            if (r0 == 0) goto L_0x010b
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r8.getHolder()
            java.math.BigInteger r0 = r0.getSerialNumber()
            if (r0 == 0) goto L_0x002b
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r8.getHolder()
            java.math.BigInteger r0 = r0.getSerialNumber()
            java.lang.String r0 = r0.toString()
            r4.add(r0)
        L_0x002b:
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r8.getHolder()
            java.security.Principal[] r0 = r0.getEntityNames()
            if (r0 == 0) goto L_0x010b
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r8.getHolder()
            java.security.Principal[] r0 = r0.getEntityNames()
        L_0x003d:
            org.bouncycastle.x509.X509AttributeCertificate r5 = r8.getAttributeCert()
            if (r5 == 0) goto L_0x006c
            org.bouncycastle.x509.X509AttributeCertificate r5 = r8.getAttributeCert()
            org.bouncycastle.x509.AttributeCertificateHolder r5 = r5.getHolder()
            java.security.Principal[] r5 = r5.getEntityNames()
            if (r5 == 0) goto L_0x005d
            org.bouncycastle.x509.X509AttributeCertificate r0 = r8.getAttributeCert()
            org.bouncycastle.x509.AttributeCertificateHolder r0 = r0.getHolder()
            java.security.Principal[] r0 = r0.getEntityNames()
        L_0x005d:
            org.bouncycastle.x509.X509AttributeCertificate r5 = r8.getAttributeCert()
            java.math.BigInteger r5 = r5.getSerialNumber()
            java.lang.String r5 = r5.toString()
            r4.add(r5)
        L_0x006c:
            if (r0 == 0) goto L_0x007f
            r1 = r0[r2]
            boolean r1 = r1 instanceof javax.security.auth.x500.X500Principal
            if (r1 == 0) goto L_0x00c1
            r0 = r0[r2]
            javax.security.auth.x500.X500Principal r0 = (javax.security.auth.x500.X500Principal) r0
            java.lang.String r1 = "RFC1779"
            java.lang.String r1 = r0.getName(r1)
        L_0x007f:
            java.math.BigInteger r0 = r8.getSerialNumber()
            if (r0 == 0) goto L_0x0090
            java.math.BigInteger r0 = r8.getSerialNumber()
            java.lang.String r0 = r0.toString()
            r4.add(r0)
        L_0x0090:
            if (r1 == 0) goto L_0x00c8
            r0 = r2
        L_0x0093:
            int r2 = r11.length
            if (r0 >= r2) goto L_0x00c8
            r2 = r11[r0]
            java.lang.String r2 = r7.parseDN(r1, r2)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "*"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r2 = r5.append(r2)
            java.lang.String r5 = "*"
            java.lang.StringBuilder r2 = r2.append(r5)
            java.lang.String r2 = r2.toString()
            java.util.List r2 = r7.search(r10, r2, r9)
            r3.addAll(r2)
            int r0 = r0 + 1
            goto L_0x0093
        L_0x00c1:
            r0 = r0[r2]
            java.lang.String r1 = r0.getName()
            goto L_0x007f
        L_0x00c8:
            int r0 = r4.size()
            if (r0 <= 0) goto L_0x00f8
            org.bouncycastle.jce.X509LDAPCertStoreParameters r0 = r7.params
            java.lang.String r0 = r0.getSearchForSerialNumberIn()
            if (r0 == 0) goto L_0x00f8
            java.util.Iterator r2 = r4.iterator()
        L_0x00da:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x00f8
            java.lang.Object r0 = r2.next()
            java.lang.String r0 = (java.lang.String) r0
            org.bouncycastle.jce.X509LDAPCertStoreParameters r5 = r7.params
            java.lang.String r5 = r5.getSearchForSerialNumberIn()
            java.lang.String[] r5 = r7.splitString(r5)
            java.util.List r0 = r7.search(r5, r0, r9)
            r3.addAll(r0)
            goto L_0x00da
        L_0x00f8:
            int r0 = r4.size()
            if (r0 != 0) goto L_0x010a
            if (r1 != 0) goto L_0x010a
            java.lang.String r0 = "*"
            java.util.List r0 = r7.search(r10, r0, r9)
            r3.addAll(r0)
        L_0x010a:
            return r3
        L_0x010b:
            r0 = r1
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.x509.util.LDAPStoreHelper.attrCertSubjectSerialSearch(org.bouncycastle.x509.X509AttributeCertStoreSelector, java.lang.String[], java.lang.String[], java.lang.String[]):java.util.List");
    }

    private List cRLIssuerSearch(X509CRLStoreSelector x509CRLStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) throws StoreException {
        String str;
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        HashSet hashSet = new HashSet();
        if (x509CRLStoreSelector.getIssuers() != null) {
            hashSet.addAll(x509CRLStoreSelector.getIssuers());
        }
        if (x509CRLStoreSelector.getCertificateChecking() != null) {
            hashSet.add(getCertificateIssuer(x509CRLStoreSelector.getCertificateChecking()));
        }
        if (x509CRLStoreSelector.getAttrCertificateChecking() != null) {
            Principal[] principals = x509CRLStoreSelector.getAttrCertificateChecking().getIssuer().getPrincipals();
            for (int i = 0; i < principals.length; i++) {
                if (principals[i] instanceof X500Principal) {
                    hashSet.add(principals[i]);
                }
            }
        }
        Iterator it2 = hashSet.iterator();
        while (true) {
            str = str2;
            if (!it2.hasNext()) {
                break;
            }
            str2 = ((X500Principal) it2.next()).getName("RFC1779");
            for (int i2 = 0; i2 < strArr3.length; i2++) {
                arrayList.addAll(search(strArr2, "*" + parseDN(str2, strArr3[i2]) + "*", strArr));
            }
        }
        if (str == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
    }

    private List certSubjectSerialSearch(X509CertStoreSelector x509CertStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) throws StoreException {
        String str;
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        String subjectAsString = getSubjectAsString(x509CertStoreSelector);
        if (x509CertStoreSelector.getSerialNumber() != null) {
            str2 = x509CertStoreSelector.getSerialNumber().toString();
        }
        if (x509CertStoreSelector.getCertificate() != null) {
            subjectAsString = x509CertStoreSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
            str = x509CertStoreSelector.getCertificate().getSerialNumber().toString();
        } else {
            str = str2;
        }
        if (subjectAsString != null) {
            for (int i = 0; i < strArr3.length; i++) {
                arrayList.addAll(search(strArr2, "*" + parseDN(subjectAsString, strArr3[i]) + "*", strArr));
            }
        }
        if (!(str == null || this.params.getSearchForSerialNumberIn() == null)) {
            arrayList.addAll(search(splitString(this.params.getSearchForSerialNumberIn()), str, strArr));
        }
        if (str == null && subjectAsString == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
    }

    private DirContext connectLDAP() throws NamingException {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", LDAP_PROVIDER);
        properties.setProperty("java.naming.batchsize", "0");
        properties.setProperty("java.naming.provider.url", this.params.getLdapURL());
        properties.setProperty("java.naming.factory.url.pkgs", URL_CONTEXT_PREFIX);
        properties.setProperty("java.naming.referral", REFERRALS_IGNORE);
        properties.setProperty("java.naming.security.authentication", "none");
        return new InitialDirContext(properties);
    }

    private Set createAttributeCertificates(List list, X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        HashSet hashSet = new HashSet();
        Iterator it2 = list.iterator();
        X509AttrCertParser x509AttrCertParser = new X509AttrCertParser();
        while (it2.hasNext()) {
            try {
                x509AttrCertParser.engineInit(new ByteArrayInputStream((byte[]) it2.next()));
                X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate) x509AttrCertParser.engineRead();
                if (x509AttributeCertStoreSelector.match(x509AttributeCertificate)) {
                    hashSet.add(x509AttributeCertificate);
                }
            } catch (StreamParsingException e) {
            }
        }
        return hashSet;
    }

    private Set createCRLs(List list, X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        HashSet hashSet = new HashSet();
        X509CRLParser x509CRLParser = new X509CRLParser();
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            try {
                x509CRLParser.engineInit(new ByteArrayInputStream((byte[]) it2.next()));
                X509CRL x509crl = (X509CRL) x509CRLParser.engineRead();
                if (x509CRLStoreSelector.match((Object) x509crl)) {
                    hashSet.add(x509crl);
                }
            } catch (StreamParsingException e) {
            }
        }
        return hashSet;
    }

    private Set createCerts(List list, X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        HashSet hashSet = new HashSet();
        Iterator it2 = list.iterator();
        X509CertParser x509CertParser = new X509CertParser();
        while (it2.hasNext()) {
            try {
                x509CertParser.engineInit(new ByteArrayInputStream((byte[]) it2.next()));
                X509Certificate x509Certificate = (X509Certificate) x509CertParser.engineRead();
                if (x509CertStoreSelector.match((Object) x509Certificate)) {
                    hashSet.add(x509Certificate);
                }
            } catch (Exception e) {
            }
        }
        return hashSet;
    }

    private Set createCrossCertificatePairs(List list, X509CertPairStoreSelector x509CertPairStoreSelector) throws StoreException {
        int i;
        int i2;
        X509CertificatePair x509CertificatePair;
        HashSet hashSet = new HashSet();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= list.size()) {
                return hashSet;
            }
            try {
                X509CertPairParser x509CertPairParser = new X509CertPairParser();
                x509CertPairParser.engineInit(new ByteArrayInputStream((byte[]) list.get(i4)));
                x509CertificatePair = (X509CertificatePair) x509CertPairParser.engineRead();
                i2 = i4;
            } catch (StreamParsingException e) {
                try {
                    i2 = i4 + 1;
                    x509CertificatePair = new X509CertificatePair(new CertificatePair(Certificate.getInstance(new ASN1InputStream((byte[]) list.get(i4)).readObject()), Certificate.getInstance(new ASN1InputStream((byte[]) list.get(i4 + 1)).readObject())));
                } catch (CertificateParsingException e2) {
                    i = i4;
                } catch (IOException e3) {
                    i = i4;
                }
            }
            try {
                if (x509CertPairStoreSelector.match(x509CertificatePair)) {
                    hashSet.add(x509CertificatePair);
                }
                i = i2;
            } catch (CertificateParsingException e4) {
                i = i2;
            } catch (IOException e5) {
                i = i2;
            }
            i3 = i + 1;
        }
    }

    private List crossCertificatePairSubjectSearch(X509CertPairStoreSelector x509CertPairStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) throws StoreException {
        ArrayList arrayList = new ArrayList();
        String str = null;
        if (x509CertPairStoreSelector.getForwardSelector() != null) {
            str = getSubjectAsString(x509CertPairStoreSelector.getForwardSelector());
        }
        String name = (x509CertPairStoreSelector.getCertPair() == null || x509CertPairStoreSelector.getCertPair().getForward() == null) ? str : x509CertPairStoreSelector.getCertPair().getForward().getSubjectX500Principal().getName("RFC1779");
        if (name != null) {
            for (int i = 0; i < strArr3.length; i++) {
                arrayList.addAll(search(strArr2, "*" + parseDN(name, strArr3[i]) + "*", strArr));
            }
        }
        if (name == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
    }

    private X500Principal getCertificateIssuer(X509Certificate x509Certificate) {
        return x509Certificate.getIssuerX500Principal();
    }

    private List getFromCache(String str) {
        List list = (List) this.cacheMap.get(str);
        long currentTimeMillis = System.currentTimeMillis();
        if (list == null) {
            return null;
        }
        if (((Date) list.get(0)).getTime() < currentTimeMillis - lifeTime) {
            return null;
        }
        return (List) list.get(1);
    }

    private String getSubjectAsString(X509CertStoreSelector x509CertStoreSelector) {
        try {
            byte[] subjectAsBytes = x509CertStoreSelector.getSubjectAsBytes();
            if (subjectAsBytes != null) {
                return new X500Principal(subjectAsBytes).getName("RFC1779");
            }
            return null;
        } catch (IOException e) {
            throw new StoreException("exception processing name: " + e.getMessage(), e);
        }
    }

    private String parseDN(String str, String str2) {
        int i;
        int indexOf = str.toLowerCase().indexOf(str2.toLowerCase() + "=");
        if (indexOf == -1) {
            return "";
        }
        String substring = str.substring(indexOf + str2.length());
        int indexOf2 = substring.indexOf(44);
        if (indexOf2 == -1) {
            indexOf2 = substring.length();
        }
        while (substring.charAt(i - 1) == '\\') {
            i = substring.indexOf(44, i + 1);
            if (i == -1) {
                i = substring.length();
            }
        }
        String substring2 = substring.substring(0, i);
        String substring3 = substring2.substring(substring2.indexOf(61) + 1);
        if (substring3.charAt(0) == ' ') {
            substring3 = substring3.substring(1);
        }
        if (substring3.startsWith("\"")) {
            substring3 = substring3.substring(1);
        }
        return substring3.endsWith("\"") ? substring3.substring(0, substring3.length() - 1) : substring3;
    }

    private List search(String[] strArr, String str, String[] strArr2) throws StoreException {
        String str2;
        DirContext dirContext = null;
        if (strArr == null) {
            str2 = null;
        } else {
            String str3 = "";
            if (str.equals("**")) {
                str = "*";
            }
            for (int i = 0; i < strArr.length; i++) {
                str3 = str3 + "(" + strArr[i] + "=" + str + ")";
            }
            str2 = "(|" + str3 + ")";
        }
        String str4 = "";
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            str4 = str4 + "(" + strArr2[i2] + "=*)";
        }
        String str5 = "(|" + str4 + ")";
        String str6 = "(&" + str2 + "" + str5 + ")";
        if (str2 != null) {
            str5 = str6;
        }
        List fromCache = getFromCache(str5);
        if (fromCache != null) {
            return fromCache;
        }
        ArrayList arrayList = new ArrayList();
        try {
            dirContext = connectLDAP();
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(2);
            searchControls.setCountLimit(0);
            searchControls.setReturningAttributes(strArr2);
            NamingEnumeration search = dirContext.search(this.params.getBaseDN(), str5, searchControls);
            while (search.hasMoreElements()) {
                NamingEnumeration all = ((Attribute) ((SearchResult) search.next()).getAttributes().getAll().next()).getAll();
                while (all.hasMore()) {
                    arrayList.add(all.next());
                }
            }
            addToCache(str5, arrayList);
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e) {
                }
            }
        } catch (NamingException e2) {
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
        return arrayList;
    }

    private String[] splitString(String str) {
        return str.split("\\s+");
    }

    public Collection getAACertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAACertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapAACertificateAttributeName());
        String[] splitString3 = splitString(this.params.getAACertificateSubjectAttributeName());
        Set createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAttributeAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAttributeAuthorityRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeAuthorityRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeAuthorityRevocationListIssuerAttributeName());
        Set createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getAttributeCertificateAttributes(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAttributeCertificateAttributeAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeCertificateAttributeAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeCertificateAttributeSubjectAttributeName());
        Set createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAttributeCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAttributeCertificateRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeCertificateRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeCertificateRevocationListIssuerAttributeName());
        Set createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getAttributeDescriptorCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAttributeDescriptorCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeDescriptorCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeDescriptorCertificateSubjectAttributeName());
        Set createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getAuthorityRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAuthorityRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAuthorityRevocationListIssuerAttributeName());
        Set createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getCACertificates(X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getCACertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapCACertificateAttributeName());
        String[] splitString3 = splitString(this.params.getCACertificateSubjectAttributeName());
        Set createCerts = createCerts(certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (createCerts.size() == 0) {
            createCerts.addAll(createCerts(certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return createCerts;
    }

    public Collection getCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getCertificateRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapCertificateRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getCertificateRevocationListIssuerAttributeName());
        Set createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getCrossCertificatePairs(X509CertPairStoreSelector x509CertPairStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getCrossCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapCrossCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getCrossCertificateSubjectAttributeName());
        Set createCrossCertificatePairs = createCrossCertificatePairs(crossCertificatePairSubjectSearch(x509CertPairStoreSelector, splitString, splitString2, splitString3), x509CertPairStoreSelector);
        if (createCrossCertificatePairs.size() == 0) {
            X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            X509CertPairStoreSelector x509CertPairStoreSelector2 = new X509CertPairStoreSelector();
            x509CertPairStoreSelector2.setForwardSelector(x509CertStoreSelector);
            x509CertPairStoreSelector2.setReverseSelector(x509CertStoreSelector);
            createCrossCertificatePairs.addAll(createCrossCertificatePairs(crossCertificatePairSubjectSearch(x509CertPairStoreSelector2, splitString, splitString2, splitString3), x509CertPairStoreSelector));
        }
        return createCrossCertificatePairs;
    }

    public Collection getDeltaCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getDeltaRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapDeltaRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getDeltaRevocationListIssuerAttributeName());
        Set createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getUserCertificates(X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        String[] splitString = splitString(this.params.getUserCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapUserCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getUserCertificateSubjectAttributeName());
        Set createCerts = createCerts(certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (createCerts.size() == 0) {
            createCerts.addAll(createCerts(certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return createCerts;
    }
}
