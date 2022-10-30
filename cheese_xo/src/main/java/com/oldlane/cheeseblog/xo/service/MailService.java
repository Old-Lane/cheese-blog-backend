package com.oldlane.cheeseblog.xo.service;

/**
 * Date: 2022/10/9 21:46
 * Description:
 */
public interface MailService {
    void sendMimeMail( String email, String comment, String nickname);

    void sendMessage(String message);

    void sendValidateCode(String validateCode, String email, String theme);
}
