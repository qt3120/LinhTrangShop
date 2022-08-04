package com.util;

import javax.servlet.http.HttpSession;

import com.constant.SESSION_ATTR;

public class NewsUtil {
    public static boolean isLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUserLogin(HttpSession session) {
        if (session.getAttribute(SESSION_ATTR.USER_SESSION) != null) {
            return true;
        } else {
            return false;
        }
    }
}
