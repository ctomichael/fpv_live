package com.dji.frame.util;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Map;

public class V_SqliteUtil {
    public static String sqliteEscape(String keyWord) {
        return keyWord.replace(IMemberProtocol.PARAM_SEPERATOR, "//").replace("'", "''").replace(IMemberProtocol.STRING_SEPERATOR_LEFT, "/[").replace(IMemberProtocol.STRING_SEPERATOR_RIGHT, "/]").replace("%", "/%").replace("&", "/&").replace("_", "/_").replace("(", "/(").replace(")", "/)");
    }

    public static String whereBuilder(Map<String, String> mDbWhere) {
        StringBuffer where = new StringBuffer();
        for (String key : mDbWhere.keySet()) {
            if (where.equals(null)) {
                where.append(" AND ");
            }
            where.append(key).append(" = '").append(mDbWhere.get(key)).append("'");
        }
        return where.toString();
    }
}
