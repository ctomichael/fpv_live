package org.bouncycastle.voms;

import dji.component.accountcenter.IMemberProtocol;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.IetfAttrSyntax;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

public class VOMSAttribute {
    public static final String VOMS_ATTR_OID = "1.3.6.1.4.1.8005.100.100.4";
    private X509AttributeCertificateHolder myAC;
    private List myFQANs = new ArrayList();
    private String myHostPort;
    private List myStringList = new ArrayList();
    private String myVo;

    public class FQAN {
        String capability;
        String fqan;
        String group;
        String role;

        public FQAN(String str) {
            this.fqan = str;
        }

        public FQAN(String str, String str2, String str3) {
            this.group = str;
            this.role = str2;
            this.capability = str3;
        }

        public String getCapability() {
            if (this.group == null && this.fqan != null) {
                split();
            }
            return this.capability;
        }

        public String getFQAN() {
            if (this.fqan != null) {
                return this.fqan;
            }
            this.fqan = this.group + "/Role=" + (this.role != null ? this.role : "") + (this.capability != null ? "/Capability=" + this.capability : "");
            return this.fqan;
        }

        public String getGroup() {
            if (this.group == null && this.fqan != null) {
                split();
            }
            return this.group;
        }

        public String getRole() {
            if (this.group == null && this.fqan != null) {
                split();
            }
            return this.role;
        }

        /* access modifiers changed from: protected */
        public void split() {
            String str = null;
            this.fqan.length();
            int indexOf = this.fqan.indexOf("/Role=");
            if (indexOf >= 0) {
                this.group = this.fqan.substring(0, indexOf);
                int indexOf2 = this.fqan.indexOf("/Capability=", indexOf + 6);
                String substring = indexOf2 < 0 ? this.fqan.substring(indexOf + 6) : this.fqan.substring(indexOf + 6, indexOf2);
                if (substring.length() == 0) {
                    substring = null;
                }
                this.role = substring;
                String substring2 = indexOf2 < 0 ? null : this.fqan.substring(indexOf2 + 12);
                if (!(substring2 == null || substring2.length() == 0)) {
                    str = substring2;
                }
                this.capability = str;
            }
        }

        public String toString() {
            return getFQAN();
        }
    }

    public VOMSAttribute(X509AttributeCertificateHolder x509AttributeCertificateHolder) {
        if (x509AttributeCertificateHolder == null) {
            throw new IllegalArgumentException("VOMSAttribute: AttributeCertificate is NULL");
        }
        this.myAC = x509AttributeCertificateHolder;
        Attribute[] attributes = x509AttributeCertificateHolder.getAttributes(new ASN1ObjectIdentifier(VOMS_ATTR_OID));
        if (attributes != null) {
            int i = 0;
            while (i != attributes.length) {
                try {
                    IetfAttrSyntax instance = IetfAttrSyntax.getInstance(attributes[i].getAttributeValues()[0]);
                    String string = ((DERIA5String) instance.getPolicyAuthority().getNames()[0].getName()).getString();
                    int indexOf = string.indexOf("://");
                    if (indexOf < 0 || indexOf == string.length() - 1) {
                        throw new IllegalArgumentException("Bad encoding of VOMS policyAuthority : [" + string + IMemberProtocol.STRING_SEPERATOR_RIGHT);
                    }
                    this.myVo = string.substring(0, indexOf);
                    this.myHostPort = string.substring(indexOf + 3);
                    if (instance.getValueType() != 1) {
                        throw new IllegalArgumentException("VOMS attribute values are not encoded as octet strings, policyAuthority = " + string);
                    }
                    ASN1OctetString[] aSN1OctetStringArr = (ASN1OctetString[]) instance.getValues();
                    for (int i2 = 0; i2 != aSN1OctetStringArr.length; i2++) {
                        String str = new String(aSN1OctetStringArr[i2].getOctets());
                        FQAN fqan = new FQAN(str);
                        if (!this.myStringList.contains(str) && str.startsWith(IMemberProtocol.PARAM_SEPERATOR + this.myVo + IMemberProtocol.PARAM_SEPERATOR)) {
                            this.myStringList.add(str);
                            this.myFQANs.add(fqan);
                        }
                    }
                    i++;
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new IllegalArgumentException("Badly encoded VOMS extension in AC issued by " + x509AttributeCertificateHolder.getIssuer());
                }
            }
        }
    }

    public X509AttributeCertificateHolder getAC() {
        return this.myAC;
    }

    public List getFullyQualifiedAttributes() {
        return this.myStringList;
    }

    public String getHostPort() {
        return this.myHostPort;
    }

    public List getListOfFQAN() {
        return this.myFQANs;
    }

    public String getVO() {
        return this.myVo;
    }

    public String toString() {
        return "VO      :" + this.myVo + "\n" + "HostPort:" + this.myHostPort + "\n" + "FQANs   :" + this.myFQANs;
    }
}
