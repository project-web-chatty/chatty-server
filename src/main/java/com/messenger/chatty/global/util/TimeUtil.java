package com.messenger.chatty.global.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

    public static Long convertTimeTypeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }

    public static LocalDateTime convertTimeTypeToLocalDateTime(Long regTime) {

        return Instant.ofEpochMilli(regTime)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
