package dev.ojas.cryptoCurrency.exception;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private String message;
    private int status;
    private Long errorCode;
    private String transactionUuid;

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
