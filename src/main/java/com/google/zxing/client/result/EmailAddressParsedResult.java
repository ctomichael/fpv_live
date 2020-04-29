package com.google.zxing.client.result;

public final class EmailAddressParsedResult extends ParsedResult {
    private final String[] bccs;
    private final String body;
    private final String[] ccs;
    private final String subject;
    private final String[] tos;

    EmailAddressParsedResult(String to) {
        this(new String[]{to}, null, null, null, null);
    }

    EmailAddressParsedResult(String[] tos2, String[] ccs2, String[] bccs2, String subject2, String body2) {
        super(ParsedResultType.EMAIL_ADDRESS);
        this.tos = tos2;
        this.ccs = ccs2;
        this.bccs = bccs2;
        this.subject = subject2;
        this.body = body2;
    }

    @Deprecated
    public String getEmailAddress() {
        if (this.tos == null || this.tos.length == 0) {
            return null;
        }
        return this.tos[0];
    }

    public String[] getTos() {
        return this.tos;
    }

    public String[] getCCs() {
        return this.ccs;
    }

    public String[] getBCCs() {
        return this.bccs;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }

    @Deprecated
    public String getMailtoURI() {
        return "mailto:";
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(30);
        maybeAppend(this.tos, result);
        maybeAppend(this.ccs, result);
        maybeAppend(this.bccs, result);
        maybeAppend(this.subject, result);
        maybeAppend(this.body, result);
        return result.toString();
    }
}
