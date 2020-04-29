package com.loc;

/* compiled from: UTUtdidHelper2 */
public final class db {
    private String a;

    public db() {
        this.a = "XwYp8WL8bm6S4wu6yEYmLGy4RRRdJDIhxCBdk3CiNZTwGoj1bScVZEeVp9vBiiIsgwDtqZHP8QLoFM6o6MRYjW8QqyrZBI654mqoUk5SOLDyzordzOU5QhYguEJh54q3K1KqMEXpdEQJJjs1Urqjm2s4jgPfCZ4hMuIjAMRrEQluA7FeoqWMJOwghcLcPVleQ8PLzAcaKidybmwhvNAxIyKRpbZlcDjNCcUvsJYvyzEA9VUIaHkIAJ62lpA3EE3H";
        this.a = dt.a(this.a.getBytes(), 0);
    }

    public final String a(String str) {
        return ds.b(this.a, str);
    }

    public final String b(String str) {
        String b = ds.b(this.a, str);
        if (dw.a(b)) {
            return null;
        }
        try {
            return new String(dt.a(b));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
