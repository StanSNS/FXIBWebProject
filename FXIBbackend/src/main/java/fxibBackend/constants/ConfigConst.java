package fxibBackend.constants;

public class ConfigConst {
    public final static String CUSTOM_DATE_FORMAT = "dd MMM yyyy HH:mm";
    public final static String EMAIL_HOST = "smtp.gmail.com";
    public final static int EMAIL_PORT = 587;
    public final static String EMAIL_ORIGIN = "forexindicesbulgaria@gmail.com";
    public final static String EMAIL_PASSWORD = "wykd wldo rsaq gsau";
    public final static String EMAIL_PROPS_KEY_ONE = "mail.smtp.starttls.enable";
    public final static String EMAIL_PROPS_VALUE_ONE = "true";
    public final static String EMAIL_PROPS_KEY_TWO = "mail.smtp.auth";
    public final static String EMAIL_PROPS_VALUE_TWO = "true";
    public final static String EMAIL_PROPS_KEY_THREE = "mail.smtp.ssl.trust";
    public final static String EMAIL_PROPS_VALUE_THREE = "smtp.gmail.com";
    public final static String EMAIL_FORGOT_PASSWORD_SUBJECT = "FXIB Reset Password";
    public static final String EMAIL_FORGOT_PASSWORD_HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Password Reset</title>
            </head>
            <body>
                <h1>Hello %s,</h1>
                <p>We received a request to reset your password. To reset your password, please click the link below:</p>
                <p><a href="http://localhost:3000/reset-password-update?token=%s">Reset My Password</a></p>
                <p>If you did not request a password reset, you can ignore this email.</p>
                <p>Thank you,</p>
                <p>FXIB</p>
            </body>
            </html>""";
    public final static String TWO_FACTOR_AUTH_SUBJECT = "FXIB - Two Authentication 6-digit Code";
    public static final String TWO_FACTOR_AUTH_HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Authentication Code</title>
            </head>
            <body>
                <h1>Hello %s,</h1>
                <p>Your authentication code is: <strong>%s</strong></p>
                <p>If you did not tried to login recently, please change your password.</p>
                <p>Thank you,</p>
                <p>FXIB</p>
            </body>
            </html>""";
    public final static String DIFFERENT_LOCATION_AUTH_SUBJECT = "FXIB - Login from a different location!";
    public static final String DIFFERENT_LOCATION_HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Login from a different location!</title>
            </head>
            <body>
                <h1>Hello %s,</h1>
                <p>If you have knowledge of someone else logging into your account from a different location, please take action immediately. Here are the details of the unauthorized login:
                <ul>
                    <li>Continent: %s</li>
                    <li><img src="%s" alt="Country Flag" width=16 height=10> Country: %s</li>
                    <li>City: %s</li>
                    <li>IP Address: %s</li>
                </ul>
                </p>
                       <p>Your registration was made at this location:
                <ul>
                    <li>Continent: %s</li>
                    <li><img src="%s" alt="Country Flag" width=16 height=10> Country: %s</li>
                    <li>City: %s</li>
                    <li>IP Address: %s</li>
                </ul>
                </p>
                
                <p>If you are aware of this login, you can ignore this message.</p>
                <p>You can change your location from the settings.</p>
                <p>Thank you,</p>
                <p>FXIB</p>
            </body>
            </html>""";
    public final static String EMAIL_ENCODING = "UTF-8";
    public final static String STRIPE_API_KEY = "sk_test_51IlHWpGlSGATKmQPCHQ7IkuW2JX6oYZbQaxYtclDmIFVcM2mQ6aoAWYucKJk6TV2NffBiXH6UUmZTlorAoCgAYab00THACivIs";
    public final static String MYFXBOOK_SESSION_ID = "5RUboY2LjLlnTl6937uo1984674";
    public final static String MYFXBOOK_GET_ALL_ACCOUNTS_URL = "https://www.myfxbook.com/api/get-my-accounts.json?session=";
    public final static String MYFXBOOK_GET_ALL_ACCOUNT_TRADES_URL = "https://www.myfxbook.com/api/get-history.json?session=%s&id=%s";
    public static final Integer MAX_LOGINS = 5;
    public static final String GEOLOCATION_API_KEY = "3570f9e4a0c34984a0e9bbcccd756548";
}
