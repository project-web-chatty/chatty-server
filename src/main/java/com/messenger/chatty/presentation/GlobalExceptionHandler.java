package com.messenger.chatty.presentation;
import com.messenger.chatty.presentation.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // 커스텀 에러는 여기서 handle
    @ExceptionHandler
    public ResponseEntity<Object> handleGeneralException(GeneralException generalException, HttpServletRequest request) {
        Reason reason = generalException.getErrorReason();
        return handleExceptionInternal(generalException, reason, null, request);
    }



    private ResponseEntity<Object> handleExceptionInternal(Exception e, Reason reason,
                                                           HttpHeaders headers, HttpServletRequest request) {
        return super.handleExceptionInternal(
                e,
                ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null),
                headers,
                reason.getHttpStatus(),
                new ServletWebRequest(request)
        );
    }


}
