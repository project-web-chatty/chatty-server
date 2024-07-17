package com.messenger.chatty.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class InvitationCodeGenerator {
    // 보안 값이므로 나중에 로컬과 배포를 분리하여 외부에서 값 주입하기

    @Value("${variables.invitation.KEY}")
    private static String CHARACTERS ;
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public String generateInviteCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }


}
