package vn.backend.ksv.common.constant.key;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:26 AM
 */
public class ConfigAPI {

    //Admin
    public static final class Admin {
        private static final String prefix = "/admin";

        private static final String LOGIN = prefix + "/login";

        public static String getLogin() {
            return LOGIN;
        }


    }
}
