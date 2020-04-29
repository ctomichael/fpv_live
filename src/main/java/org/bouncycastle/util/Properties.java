package org.bouncycastle.util;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Properties {
    public static Set<String> asKeySet(String str) {
        HashSet hashSet = new HashSet();
        String property = System.getProperty(str);
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
            while (stringTokenizer.hasMoreElements()) {
                hashSet.add(Strings.toLowerCase(stringTokenizer.nextToken()).trim());
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    public static boolean isOverrideSet(final String str) {
        try {
            return "true".equals(AccessController.doPrivileged(new PrivilegedAction() {
                /* class org.bouncycastle.util.Properties.AnonymousClass1 */

                public Object run() {
                    String property = System.getProperty(str);
                    if (property == null) {
                        return null;
                    }
                    return Strings.toLowerCase(property);
                }
            }));
        } catch (AccessControlException e) {
            return false;
        }
    }
}
