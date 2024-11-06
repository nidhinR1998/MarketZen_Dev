package com.nidhin.marketzen.utils;

public class Constants {
    public static final String EMAIL_TEMPLATE_RESET_PASSWORD =
            "<html>" +
                    "<head><style>" +
                    "body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333; }" +
                    ".container { width: 80%; margin: auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); position: relative; }" +
                    ".container::before { content: ''; position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: url('https://www.transparenttextures.com/patterns/scream.png'); opacity: 0.2; z-index: -1; }" +
                    "h1 { color: #4CAF50; font-size: 24px; }" +
                    ".reminder-tag { background-color: #ff5722; color: #fff; font-size: 14px; padding: 5px 10px; border-radius: 3px; display: inline-block; margin-bottom: 20px; font-weight: bold; }" +
                    "p { font-size: 16px; line-height: 1.5; margin: 10px 0; font-weight: bold; }" +
                    "a { color: #ff9800; background-color: #ff9800; padding: 10px 20px; text-decoration: none; font-weight: bold; border-radius: 5px; display: inline-block; font-size: 18px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); transition: box-shadow 0.3s ease-in-out; }" +
                    "a:hover { background-color: #ffb74d; box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3); }" +
                    ".highlight { background-color: #eaf3fc; padding: 10px; border-radius: 5px; }" +
                    ".info-box { border: 1px solid #eaf3fc; padding: 10px; border-radius: 5px; background-color: #f9f9f9; }" +
                    ".info-box p { margin: 5px 0; }" +
                    ".info-box strong { color: #4CAF50; }" +
                    ".highlight p { font-style: italic; color: #ff9800; }" +
                    "footer { font-size: 14px; color: #777; margin-top: 20px; font-weight: bold; text-align: left; }" +
                    "</style></head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h1>Password Reset Request</h1>" +
                    "<p>Hi <strong>{fullname}</strong>,</p>" +
                    "<p>We received a request to reset your password. Use the OTP below to reset your password:</p>" +
                    "<p class='reminder-tag'>Your OTP: <strong>{otp}</strong></p>" +
                    "<div class='info-box'>" +
                    "<p>If you did not request a password reset, please ignore this email.</p>" +
                    "<p class='highlight'><em><strong>Note:</strong> This is an automated message. Please do not reply to this email.</em></p>" +
                    "</div>" +
                    "<footer>" +
                    "<p>Thanks and regards,</p>" +
                    "<p>Your Food Check List Team</p>" +
                    "<p>Have a nice day!</p>" +
                    "</footer>" +
                    "</div>" +
                    "</body></html>";


    //Email Values
    public static final String VALIED_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
}
