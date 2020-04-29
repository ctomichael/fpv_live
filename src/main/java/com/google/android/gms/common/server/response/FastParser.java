package com.google.android.gms.common.server.response;

import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.server.response.FastJsonResponse;
import it.sauronsoftware.ftp4j.FTPCodes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import kotlin.text.Typography;

@ShowFirstParty
@KeepForSdk
public class FastParser<T extends FastJsonResponse> {
    private static final char[] zaqf = {'u', 'l', 'l'};
    private static final char[] zaqg = {'r', 'u', 'e'};
    private static final char[] zaqh = {'r', 'u', 'e', Typography.quote};
    private static final char[] zaqi = {'a', 'l', 's', 'e'};
    private static final char[] zaqj = {'a', 'l', 's', 'e', Typography.quote};
    private static final char[] zaqk = {10};
    private static final zaa<Integer> zaqm = new zaa();
    private static final zaa<Long> zaqn = new zab();
    private static final zaa<Float> zaqo = new zac();
    private static final zaa<Double> zaqp = new zad();
    private static final zaa<Boolean> zaqq = new zae();
    private static final zaa<String> zaqr = new zaf();
    private static final zaa<BigInteger> zaqs = new zag();
    private static final zaa<BigDecimal> zaqt = new zah();
    private final char[] zaqa = new char[1];
    private final char[] zaqb = new char[32];
    private final char[] zaqc = new char[1024];
    private final StringBuilder zaqd = new StringBuilder(32);
    private final StringBuilder zaqe = new StringBuilder(1024);
    private final Stack<Integer> zaql = new Stack<>();

    private interface zaa<O> {
        O zah(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException;
    }

    @ShowFirstParty
    @KeepForSdk
    public static class ParseException extends Exception {
        public ParseException(String str) {
            super(str);
        }

        public ParseException(String str, Throwable th) {
            super(str, th);
        }

        public ParseException(Throwable th) {
            super(th);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse):boolean
     arg types: [java.io.BufferedReader, T]
     candidates:
      com.google.android.gms.common.server.response.FastParser.zaa(com.google.android.gms.common.server.response.FastParser, java.io.BufferedReader):int
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, char[]):int
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse$Field<?, ?>):java.util.ArrayList<T>
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastParser$zaa):java.util.ArrayList<O>
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, boolean):boolean
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse):boolean */
    @KeepForSdk
    public void parse(InputStream inputStream, T t) throws ParseException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1024);
        try {
            this.zaql.push(0);
            char zaj = zaj(bufferedReader);
            switch (zaj) {
                case 0:
                    throw new ParseException("No data to parse");
                case '[':
                    this.zaql.push(5);
                    Map<String, FastJsonResponse.Field<?, ?>> fieldMappings = t.getFieldMappings();
                    if (fieldMappings.size() != 1) {
                        throw new ParseException("Object array response class must have a single Field");
                    }
                    FastJsonResponse.Field field = (FastJsonResponse.Field) fieldMappings.entrySet().iterator().next().getValue();
                    t.addConcreteTypeArrayInternal(field, field.zapu, zaa(bufferedReader, field));
                    zak(0);
                    try {
                        bufferedReader.close();
                        return;
                    } catch (IOException e) {
                        Log.w("FastParser", "Failed to close reader while parsing.");
                        return;
                    }
                case '{':
                    this.zaql.push(1);
                    zaa(bufferedReader, (FastJsonResponse) t);
                    zak(0);
                    bufferedReader.close();
                    return;
                default:
                    throw new ParseException(new StringBuilder(19).append("Unexpected token: ").append(zaj).toString());
            }
        } catch (IOException e2) {
            throw new ParseException(e2);
        } catch (Throwable th) {
            try {
                bufferedReader.close();
            } catch (IOException e3) {
                Log.w("FastParser", "Failed to close reader while parsing.");
            }
            throw th;
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, boolean):boolean
     arg types: [java.io.BufferedReader, int]
     candidates:
      com.google.android.gms.common.server.response.FastParser.zaa(com.google.android.gms.common.server.response.FastParser, java.io.BufferedReader):int
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, char[]):int
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse$Field<?, ?>):java.util.ArrayList<T>
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastParser$zaa):java.util.ArrayList<O>
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse):boolean
      com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, boolean):boolean */
    /* JADX WARNING: CFG modification limit reached, blocks count: 228 */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0184 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zaa(java.io.BufferedReader r12, com.google.android.gms.common.server.response.FastJsonResponse r13) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /*
            r11 = this;
            r10 = 4
            r3 = 0
            r9 = 110(0x6e, float:1.54E-43)
            r2 = 0
            r4 = 1
            java.util.Map r5 = r13.getFieldMappings()
            java.lang.String r0 = r11.zaa(r12)
            if (r0 != 0) goto L_0x0016
            r11.zak(r4)
            r0 = r3
        L_0x0014:
            return r0
        L_0x0015:
            r0 = r2
        L_0x0016:
            if (r0 == 0) goto L_0x02ac
            java.lang.Object r0 = r5.get(r0)
            com.google.android.gms.common.server.response.FastJsonResponse$Field r0 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r0
            if (r0 != 0) goto L_0x0025
            java.lang.String r0 = r11.zab(r12)
            goto L_0x0016
        L_0x0025:
            java.util.Stack<java.lang.Integer> r1 = r11.zaql
            java.lang.Integer r6 = java.lang.Integer.valueOf(r10)
            r1.push(r6)
            int r1 = r0.zapq
            switch(r1) {
                case 0: goto L_0x0051;
                case 1: goto L_0x0090;
                case 2: goto L_0x00a6;
                case 3: goto L_0x00bc;
                case 4: goto L_0x00d2;
                case 5: goto L_0x00ea;
                case 6: goto L_0x0102;
                case 7: goto L_0x011a;
                case 8: goto L_0x0132;
                case 9: goto L_0x0145;
                case 10: goto L_0x0158;
                case 11: goto L_0x0224;
                default: goto L_0x0033;
            }
        L_0x0033:
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            int r0 = r0.zapq
            r2 = 30
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Invalid field type "
            java.lang.StringBuilder r2 = r3.append(r2)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        L_0x0051:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x0088
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.Integer> r1 = com.google.android.gms.common.server.response.FastParser.zaqm
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zaa(r0, r1)
        L_0x005e:
            r11.zak(r10)
            r0 = 2
            r11.zak(r0)
            char r0 = r11.zaj(r12)
            switch(r0) {
                case 44: goto L_0x02a6;
                case 125: goto L_0x0015;
                default: goto L_0x006c;
            }
        L_0x006c:
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            r2 = 55
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Expected end of object or field separator, but found: "
            java.lang.StringBuilder r2 = r3.append(r2)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        L_0x0088:
            int r1 = r11.zad(r12)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0090:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x009e
            com.google.android.gms.common.server.response.FastParser$zaa<java.math.BigInteger> r1 = com.google.android.gms.common.server.response.FastParser.zaqs
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zab(r0, r1)
            goto L_0x005e
        L_0x009e:
            java.math.BigInteger r1 = r11.zaf(r12)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x00a6:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x00b4
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.Long> r1 = com.google.android.gms.common.server.response.FastParser.zaqn
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zac(r0, r1)
            goto L_0x005e
        L_0x00b4:
            long r6 = r11.zae(r12)
            r13.zaa(r0, r6)
            goto L_0x005e
        L_0x00bc:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x00ca
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.Float> r1 = com.google.android.gms.common.server.response.FastParser.zaqo
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zad(r0, r1)
            goto L_0x005e
        L_0x00ca:
            float r1 = r11.zag(r12)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x00d2:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x00e1
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.Double> r1 = com.google.android.gms.common.server.response.FastParser.zaqp
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zae(r0, r1)
            goto L_0x005e
        L_0x00e1:
            double r6 = r11.zah(r12)
            r13.zaa(r0, r6)
            goto L_0x005e
        L_0x00ea:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x00f9
            com.google.android.gms.common.server.response.FastParser$zaa<java.math.BigDecimal> r1 = com.google.android.gms.common.server.response.FastParser.zaqt
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zaf(r0, r1)
            goto L_0x005e
        L_0x00f9:
            java.math.BigDecimal r1 = r11.zai(r12)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0102:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x0111
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.Boolean> r1 = com.google.android.gms.common.server.response.FastParser.zaqq
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zag(r0, r1)
            goto L_0x005e
        L_0x0111:
            boolean r1 = r11.zaa(r12, r3)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x011a:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x0129
            com.google.android.gms.common.server.response.FastParser$zaa<java.lang.String> r1 = com.google.android.gms.common.server.response.FastParser.zaqr
            java.util.ArrayList r1 = r11.zaa(r12, r1)
            r13.zah(r0, r1)
            goto L_0x005e
        L_0x0129:
            java.lang.String r1 = r11.zac(r12)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0132:
            char[] r1 = r11.zaqc
            java.lang.StringBuilder r6 = r11.zaqe
            char[] r7 = com.google.android.gms.common.server.response.FastParser.zaqk
            java.lang.String r1 = r11.zaa(r12, r1, r6, r7)
            byte[] r1 = com.google.android.gms.common.util.Base64Utils.decode(r1)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0145:
            char[] r1 = r11.zaqc
            java.lang.StringBuilder r6 = r11.zaqe
            char[] r7 = com.google.android.gms.common.server.response.FastParser.zaqk
            java.lang.String r1 = r11.zaa(r12, r1, r6, r7)
            byte[] r1 = com.google.android.gms.common.util.Base64Utils.decodeUrlSafe(r1)
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0158:
            char r1 = r11.zaj(r12)
            if (r1 != r9) goto L_0x0169
            char[] r1 = com.google.android.gms.common.server.response.FastParser.zaqf
            r11.zab(r12, r1)
            r1 = r2
        L_0x0164:
            r13.zaa(r0, r1)
            goto L_0x005e
        L_0x0169:
            r6 = 123(0x7b, float:1.72E-43)
            if (r1 == r6) goto L_0x0176
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Expected start of a map object"
            r0.<init>(r1)
            throw r0
        L_0x0176:
            java.util.Stack<java.lang.Integer> r1 = r11.zaql
            java.lang.Integer r6 = java.lang.Integer.valueOf(r4)
            r1.push(r6)
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
        L_0x0184:
            char r6 = r11.zaj(r12)
            switch(r6) {
                case 0: goto L_0x018c;
                case 34: goto L_0x0195;
                case 125: goto L_0x021f;
                default: goto L_0x018b;
            }
        L_0x018b:
            goto L_0x0184
        L_0x018c:
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Unexpected EOF"
            r0.<init>(r1)
            throw r0
        L_0x0195:
            char[] r6 = r11.zaqb
            java.lang.StringBuilder r7 = r11.zaqd
            java.lang.String r6 = zab(r12, r6, r7, r2)
            char r7 = r11.zaj(r12)
            r8 = 58
            if (r7 == r8) goto L_0x01c2
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r2 = "No map value found for key "
            java.lang.String r0 = java.lang.String.valueOf(r6)
            int r3 = r0.length()
            if (r3 == 0) goto L_0x01bc
            java.lang.String r0 = r2.concat(r0)
        L_0x01b8:
            r1.<init>(r0)
            throw r1
        L_0x01bc:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r2)
            goto L_0x01b8
        L_0x01c2:
            char r7 = r11.zaj(r12)
            r8 = 34
            if (r7 == r8) goto L_0x01e7
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r2 = "Expected String value for key "
            java.lang.String r0 = java.lang.String.valueOf(r6)
            int r3 = r0.length()
            if (r3 == 0) goto L_0x01e1
            java.lang.String r0 = r2.concat(r0)
        L_0x01dd:
            r1.<init>(r0)
            throw r1
        L_0x01e1:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r2)
            goto L_0x01dd
        L_0x01e7:
            char[] r7 = r11.zaqb
            java.lang.StringBuilder r8 = r11.zaqd
            java.lang.String r7 = zab(r12, r7, r8, r2)
            r1.put(r6, r7)
            char r6 = r11.zaj(r12)
            r7 = 44
            if (r6 == r7) goto L_0x0184
            r7 = 125(0x7d, float:1.75E-43)
            if (r6 != r7) goto L_0x0203
            r11.zak(r4)
            goto L_0x0164
        L_0x0203:
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            r1 = 48
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>(r1)
            java.lang.String r1 = "Unexpected character while parsing string map: "
            java.lang.StringBuilder r1 = r2.append(r1)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x021f:
            r11.zak(r4)
            goto L_0x0164
        L_0x0224:
            boolean r1 = r0.zapr
            if (r1 == 0) goto L_0x025c
            char r1 = r11.zaj(r12)
            if (r1 != r9) goto L_0x023a
            char[] r1 = com.google.android.gms.common.server.response.FastParser.zaqf
            r11.zab(r12, r1)
            java.lang.String r1 = r0.zapu
            r13.addConcreteTypeArrayInternal(r0, r1, r2)
            goto L_0x005e
        L_0x023a:
            java.util.Stack<java.lang.Integer> r6 = r11.zaql
            r7 = 5
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r6.push(r7)
            r6 = 91
            if (r1 == r6) goto L_0x0251
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Expected array start"
            r0.<init>(r1)
            throw r0
        L_0x0251:
            java.lang.String r1 = r0.zapu
            java.util.ArrayList r6 = r11.zaa(r12, r0)
            r13.addConcreteTypeArrayInternal(r0, r1, r6)
            goto L_0x005e
        L_0x025c:
            char r1 = r11.zaj(r12)
            if (r1 != r9) goto L_0x026e
            char[] r1 = com.google.android.gms.common.server.response.FastParser.zaqf
            r11.zab(r12, r1)
            java.lang.String r1 = r0.zapu
            r13.addConcreteTypeInternal(r0, r1, r2)
            goto L_0x005e
        L_0x026e:
            java.util.Stack<java.lang.Integer> r6 = r11.zaql
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)
            r6.push(r7)
            r6 = 123(0x7b, float:1.72E-43)
            if (r1 == r6) goto L_0x0284
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Expected start of object"
            r0.<init>(r1)
            throw r0
        L_0x0284:
            com.google.android.gms.common.server.response.FastJsonResponse r1 = r0.zacp()     // Catch:{ InstantiationException -> 0x0292, IllegalAccessException -> 0x029c }
            r11.zaa(r12, r1)     // Catch:{ InstantiationException -> 0x0292, IllegalAccessException -> 0x029c }
            java.lang.String r6 = r0.zapu     // Catch:{ InstantiationException -> 0x0292, IllegalAccessException -> 0x029c }
            r13.addConcreteTypeInternal(r0, r6, r1)     // Catch:{ InstantiationException -> 0x0292, IllegalAccessException -> 0x029c }
            goto L_0x005e
        L_0x0292:
            r0 = move-exception
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r2 = "Error instantiating inner object"
            r1.<init>(r2, r0)
            throw r1
        L_0x029c:
            r0 = move-exception
            com.google.android.gms.common.server.response.FastParser$ParseException r1 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r2 = "Error instantiating inner object"
            r1.<init>(r2, r0)
            throw r1
        L_0x02a6:
            java.lang.String r0 = r11.zaa(r12)
            goto L_0x0016
        L_0x02ac:
            r11.zak(r4)
            r0 = r4
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zaa(java.io.BufferedReader, com.google.android.gms.common.server.response.FastJsonResponse):boolean");
    }

    private final String zaa(BufferedReader bufferedReader) throws ParseException, IOException {
        String str = null;
        this.zaql.push(2);
        char zaj = zaj(bufferedReader);
        switch (zaj) {
            case '\"':
                this.zaql.push(3);
                str = zab(bufferedReader, this.zaqb, this.zaqd, null);
                zak(3);
                if (zaj(bufferedReader) != ':') {
                    throw new ParseException("Expected key/value separator");
                }
                break;
            case ']':
                zak(2);
                zak(1);
                zak(5);
                break;
            case FTPCodes.DATA_CONNECTION_ALREADY_OPEN /*125*/:
                zak(2);
                break;
            default:
                throw new ParseException(new StringBuilder(19).append("Unexpected token: ").append(zaj).toString());
        }
        return str;
    }

    private final String zab(BufferedReader bufferedReader) throws ParseException, IOException {
        int i;
        bufferedReader.mark(1024);
        switch (zaj(bufferedReader)) {
            case '\"':
                if (bufferedReader.read(this.zaqa) != -1) {
                    char c = this.zaqa[0];
                    boolean z = false;
                    while (true) {
                        if (c == '\"' && !z) {
                            break;
                        } else {
                            boolean z2 = c == '\\' ? !z : false;
                            if (bufferedReader.read(this.zaqa) == -1) {
                                throw new ParseException("Unexpected EOF while parsing string");
                            }
                            c = this.zaqa[0];
                            if (Character.isISOControl(c)) {
                                throw new ParseException("Unexpected control character while reading string");
                            }
                            z = z2;
                        }
                    }
                } else {
                    throw new ParseException("Unexpected EOF while parsing string");
                }
            case ',':
                throw new ParseException("Missing value");
            case '[':
                this.zaql.push(5);
                bufferedReader.mark(32);
                if (zaj(bufferedReader) != ']') {
                    bufferedReader.reset();
                    int i2 = 1;
                    boolean z3 = false;
                    boolean z4 = false;
                    while (i2 > 0) {
                        char zaj = zaj(bufferedReader);
                        if (zaj == 0) {
                            throw new ParseException("Unexpected EOF while parsing array");
                        } else if (Character.isISOControl(zaj)) {
                            throw new ParseException("Unexpected control character while reading array");
                        } else {
                            if (zaj == '\"' && !z4) {
                                z3 = !z3;
                            }
                            if (zaj != '[' || z3) {
                                i = i2;
                            } else {
                                i = i2 + 1;
                            }
                            if (zaj != ']' || z3) {
                                i2 = i;
                            } else {
                                i2 = i - 1;
                            }
                            if (zaj != '\\' || !z3) {
                                z4 = false;
                            } else {
                                z4 = !z4;
                            }
                        }
                    }
                    zak(5);
                    break;
                } else {
                    zak(5);
                    break;
                }
            case '{':
                this.zaql.push(1);
                bufferedReader.mark(32);
                char zaj2 = zaj(bufferedReader);
                if (zaj2 == '}') {
                    zak(1);
                    break;
                } else if (zaj2 == '\"') {
                    bufferedReader.reset();
                    zaa(bufferedReader);
                    do {
                    } while (zab(bufferedReader) != null);
                    zak(1);
                    break;
                } else {
                    throw new ParseException(new StringBuilder(18).append("Unexpected token ").append(zaj2).toString());
                }
            default:
                bufferedReader.reset();
                zaa(bufferedReader, this.zaqc);
                break;
        }
        char zaj3 = zaj(bufferedReader);
        switch (zaj3) {
            case ',':
                zak(2);
                return zaa(bufferedReader);
            case FTPCodes.DATA_CONNECTION_ALREADY_OPEN /*125*/:
                zak(2);
                return null;
            default:
                throw new ParseException(new StringBuilder(18).append("Unexpected token ").append(zaj3).toString());
        }
    }

    /* access modifiers changed from: private */
    public final String zac(BufferedReader bufferedReader) throws ParseException, IOException {
        return zaa(bufferedReader, this.zaqb, this.zaqd, null);
    }

    private final <O> ArrayList<O> zaa(BufferedReader bufferedReader, zaa<O> zaa2) throws ParseException, IOException {
        char zaj = zaj(bufferedReader);
        if (zaj != 'n') {
            if (zaj == '[') {
                this.zaql.push(5);
                ArrayList<O> arrayList = new ArrayList<>();
                while (true) {
                    bufferedReader.mark(1024);
                    switch (zaj(bufferedReader)) {
                        case 0:
                            throw new ParseException("Unexpected EOF");
                        case ',':
                            break;
                        case ']':
                            zak(5);
                            return arrayList;
                        default:
                            bufferedReader.reset();
                            arrayList.add(zaa2.zah(this, bufferedReader));
                            break;
                    }
                }
            } else {
                throw new ParseException("Expected start of array");
            }
        } else {
            zab(bufferedReader, zaqf);
            return null;
        }
    }

    private final String zaa(BufferedReader bufferedReader, char[] cArr, StringBuilder sb, char[] cArr2) throws ParseException, IOException {
        switch (zaj(bufferedReader)) {
            case '\"':
                return zab(bufferedReader, cArr, sb, cArr2);
            case 'n':
                zab(bufferedReader, zaqf);
                return null;
            default:
                throw new ParseException("Expected string");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x002a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String zab(java.io.BufferedReader r9, char[] r10, java.lang.StringBuilder r11, char[] r12) throws com.google.android.gms.common.server.response.FastParser.ParseException, java.io.IOException {
        /*
            r4 = 1
            r1 = 0
            r11.setLength(r1)
            int r0 = r10.length
            r9.mark(r0)
            r0 = r1
            r2 = r1
        L_0x000b:
            int r6 = r9.read(r10)
            r3 = -1
            if (r6 == r3) goto L_0x0074
            r5 = r1
        L_0x0013:
            if (r5 >= r6) goto L_0x006c
            char r7 = r10[r5]
            boolean r3 = java.lang.Character.isISOControl(r7)
            if (r3 == 0) goto L_0x0038
            if (r12 == 0) goto L_0x0036
            r3 = r1
        L_0x0020:
            int r8 = r12.length
            if (r3 >= r8) goto L_0x0036
            char r8 = r12[r3]
            if (r8 != r7) goto L_0x0033
            r3 = r4
        L_0x0028:
            if (r3 != 0) goto L_0x0038
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Unexpected control character while reading string"
            r0.<init>(r1)
            throw r0
        L_0x0033:
            int r3 = r3 + 1
            goto L_0x0020
        L_0x0036:
            r3 = r1
            goto L_0x0028
        L_0x0038:
            r3 = 34
            if (r7 != r3) goto L_0x005a
            if (r2 != 0) goto L_0x005a
            r11.append(r10, r1, r5)
            r9.reset()
            int r1 = r5 + 1
            long r2 = (long) r1
            r9.skip(r2)
            if (r0 == 0) goto L_0x0055
            java.lang.String r0 = r11.toString()
            java.lang.String r0 = com.google.android.gms.common.util.JsonUtils.unescapeString(r0)
        L_0x0054:
            return r0
        L_0x0055:
            java.lang.String r0 = r11.toString()
            goto L_0x0054
        L_0x005a:
            r3 = 92
            if (r7 != r3) goto L_0x0069
            if (r2 != 0) goto L_0x0067
            r0 = r4
        L_0x0061:
            r3 = r4
            r2 = r0
        L_0x0063:
            int r5 = r5 + 1
            r0 = r3
            goto L_0x0013
        L_0x0067:
            r0 = r1
            goto L_0x0061
        L_0x0069:
            r3 = r0
            r2 = r1
            goto L_0x0063
        L_0x006c:
            r11.append(r10, r1, r6)
            int r3 = r10.length
            r9.mark(r3)
            goto L_0x000b
        L_0x0074:
            com.google.android.gms.common.server.response.FastParser$ParseException r0 = new com.google.android.gms.common.server.response.FastParser$ParseException
            java.lang.String r1 = "Unexpected EOF while parsing string"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.zab(java.io.BufferedReader, char[], java.lang.StringBuilder, char[]):java.lang.String");
    }

    /* access modifiers changed from: private */
    public final int zad(BufferedReader bufferedReader) throws ParseException, IOException {
        int i;
        int i2;
        boolean z;
        int i3;
        int i4 = 0;
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return 0;
        }
        char[] cArr = this.zaqc;
        if (zaa2 > 0) {
            if (cArr[0] == '-') {
                i = Integer.MIN_VALUE;
                i2 = 1;
                z = true;
            } else {
                i = -2147483647;
                i2 = 0;
                z = false;
            }
            if (i2 < zaa2) {
                i3 = i2 + 1;
                int digit = Character.digit(cArr[i2], 10);
                if (digit < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                i4 = -digit;
            } else {
                i3 = i2;
            }
            while (i3 < zaa2) {
                int i5 = i3 + 1;
                int digit2 = Character.digit(cArr[i3], 10);
                if (digit2 < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (i4 < -214748364) {
                    throw new ParseException("Number too large");
                } else {
                    int i6 = i4 * 10;
                    if (i6 < i + digit2) {
                        throw new ParseException("Number too large");
                    }
                    i4 = i6 - digit2;
                    i3 = i5;
                }
            }
            if (!z) {
                return -i4;
            }
            if (i3 > 1) {
                return i4;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    /* access modifiers changed from: private */
    public final long zae(BufferedReader bufferedReader) throws ParseException, IOException {
        long j;
        int i;
        boolean z;
        int i2;
        long j2 = 0;
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return 0;
        }
        char[] cArr = this.zaqc;
        if (zaa2 > 0) {
            if (cArr[0] == '-') {
                j = Long.MIN_VALUE;
                i = 1;
                z = true;
            } else {
                j = -9223372036854775807L;
                i = 0;
                z = false;
            }
            if (i < zaa2) {
                i2 = i + 1;
                int digit = Character.digit(cArr[i], 10);
                if (digit < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                j2 = (long) (-digit);
            } else {
                i2 = i;
            }
            while (i2 < zaa2) {
                int i3 = i2 + 1;
                int digit2 = Character.digit(cArr[i2], 10);
                if (digit2 < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (j2 < -922337203685477580L) {
                    throw new ParseException("Number too large");
                } else {
                    long j3 = j2 * 10;
                    if (j3 < ((long) digit2) + j) {
                        throw new ParseException("Number too large");
                    }
                    j2 = j3 - ((long) digit2);
                    i2 = i3;
                }
            }
            if (!z) {
                return -j2;
            }
            if (i2 > 1) {
                return j2;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    /* access modifiers changed from: private */
    public final BigInteger zaf(BufferedReader bufferedReader) throws ParseException, IOException {
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return null;
        }
        return new BigInteger(new String(this.zaqc, 0, zaa2));
    }

    /* access modifiers changed from: private */
    public final boolean zaa(BufferedReader bufferedReader, boolean z) throws ParseException, IOException {
        while (true) {
            char zaj = zaj(bufferedReader);
            switch (zaj) {
                case '\"':
                    if (z) {
                        throw new ParseException("No boolean value found in string");
                    }
                    z = true;
                case 'f':
                    zab(bufferedReader, z ? zaqj : zaqi);
                    return false;
                case 'n':
                    zab(bufferedReader, zaqf);
                    return false;
                case 't':
                    zab(bufferedReader, z ? zaqh : zaqg);
                    return true;
                default:
                    throw new ParseException(new StringBuilder(19).append("Unexpected token: ").append(zaj).toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public final float zag(BufferedReader bufferedReader) throws ParseException, IOException {
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return 0.0f;
        }
        return Float.parseFloat(new String(this.zaqc, 0, zaa2));
    }

    /* access modifiers changed from: private */
    public final double zah(BufferedReader bufferedReader) throws ParseException, IOException {
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return 0.0d;
        }
        return Double.parseDouble(new String(this.zaqc, 0, zaa2));
    }

    /* access modifiers changed from: private */
    public final BigDecimal zai(BufferedReader bufferedReader) throws ParseException, IOException {
        int zaa2 = zaa(bufferedReader, this.zaqc);
        if (zaa2 == 0) {
            return null;
        }
        return new BigDecimal(new String(this.zaqc, 0, zaa2));
    }

    private final <T extends FastJsonResponse> ArrayList<T> zaa(BufferedReader bufferedReader, FastJsonResponse.Field<?, ?> field) throws ParseException, IOException {
        ArrayList<T> arrayList = new ArrayList<>();
        char zaj = zaj(bufferedReader);
        switch (zaj) {
            case ']':
                zak(5);
                return arrayList;
            case 'n':
                zab(bufferedReader, zaqf);
                zak(5);
                return null;
            case '{':
                this.zaql.push(1);
                while (true) {
                    try {
                        FastJsonResponse zacp = field.zacp();
                        if (!zaa(bufferedReader, zacp)) {
                            return arrayList;
                        }
                        arrayList.add(zacp);
                        char zaj2 = zaj(bufferedReader);
                        switch (zaj2) {
                            case ',':
                                if (zaj(bufferedReader) != '{') {
                                    throw new ParseException("Expected start of next object in array");
                                }
                                this.zaql.push(1);
                            case ']':
                                zak(5);
                                return arrayList;
                            default:
                                throw new ParseException(new StringBuilder(19).append("Unexpected token: ").append(zaj2).toString());
                        }
                    } catch (InstantiationException e) {
                        throw new ParseException("Error instantiating inner object", e);
                    } catch (IllegalAccessException e2) {
                        throw new ParseException("Error instantiating inner object", e2);
                    }
                }
            default:
                throw new ParseException(new StringBuilder(19).append("Unexpected token: ").append(zaj).toString());
        }
    }

    private final char zaj(BufferedReader bufferedReader) throws ParseException, IOException {
        if (bufferedReader.read(this.zaqa) == -1) {
            return 0;
        }
        while (Character.isWhitespace(this.zaqa[0])) {
            if (bufferedReader.read(this.zaqa) == -1) {
                return 0;
            }
        }
        return this.zaqa[0];
    }

    private final int zaa(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i;
        char zaj = zaj(bufferedReader);
        if (zaj == 0) {
            throw new ParseException("Unexpected EOF");
        } else if (zaj == ',') {
            throw new ParseException("Missing value");
        } else if (zaj == 'n') {
            zab(bufferedReader, zaqf);
            return 0;
        } else {
            bufferedReader.mark(1024);
            if (zaj == '\"') {
                boolean z = false;
                int i2 = 0;
                while (i2 < cArr.length && bufferedReader.read(cArr, i2, 1) != -1) {
                    char c = cArr[i2];
                    if (Character.isISOControl(c)) {
                        throw new ParseException("Unexpected control character while reading string");
                    } else if (c != '\"' || z) {
                        z = c == '\\' ? !z : false;
                        i2++;
                    } else {
                        bufferedReader.reset();
                        bufferedReader.skip((long) (i2 + 1));
                        return i2;
                    }
                }
                i = i2;
            } else {
                cArr[0] = zaj;
                i = 1;
                while (i < cArr.length && bufferedReader.read(cArr, i, 1) != -1) {
                    if (cArr[i] == '}' || cArr[i] == ',' || Character.isWhitespace(cArr[i]) || cArr[i] == ']') {
                        bufferedReader.reset();
                        bufferedReader.skip((long) (i - 1));
                        cArr[i] = 0;
                        return i;
                    }
                    i++;
                }
            }
            if (i == cArr.length) {
                throw new ParseException("Absurdly long value");
            }
            throw new ParseException("Unexpected EOF");
        }
    }

    private final void zab(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i = 0;
        while (i < cArr.length) {
            int read = bufferedReader.read(this.zaqb, 0, cArr.length - i);
            if (read == -1) {
                throw new ParseException("Unexpected EOF");
            }
            for (int i2 = 0; i2 < read; i2++) {
                if (cArr[i2 + i] != this.zaqb[i2]) {
                    throw new ParseException("Unexpected character");
                }
            }
            i += read;
        }
    }

    private final void zak(int i) throws ParseException {
        if (this.zaql.isEmpty()) {
            throw new ParseException(new StringBuilder(46).append("Expected state ").append(i).append(" but had empty stack").toString());
        }
        int intValue = this.zaql.pop().intValue();
        if (intValue != i) {
            throw new ParseException(new StringBuilder(46).append("Expected state ").append(i).append(" but had ").append(intValue).toString());
        }
    }
}
