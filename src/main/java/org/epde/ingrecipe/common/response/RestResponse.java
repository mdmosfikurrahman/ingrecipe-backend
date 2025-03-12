package org.epde.ingrecipe.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.epde.ingrecipe.common.util.DateTimeUtil;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {

    @JsonFormat(pattern = "dd MMMM, yyyy hh:mm a")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private T error;

    public static <T> RestResponse<T> success(int status, String message, T data) {
        return RestResponse.<T>builder()
                .timestamp(DateTimeUtil.now())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> error(int status, String message, T error) {
        return RestResponse.<T>builder()
                .timestamp(DateTimeUtil.now())
                .status(status)
                .message(message)
                .error(error)
                .build();
    }
}
