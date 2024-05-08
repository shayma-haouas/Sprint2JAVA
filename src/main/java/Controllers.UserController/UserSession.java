package Controllers.UserController;

public class UserSession {

        private static String authenticatedEmail;

        public static String getAuthenticatedEmail() {
            return authenticatedEmail;
        }

        public static void setAuthenticatedEmail(String email) {
            authenticatedEmail = email;
        }


}
