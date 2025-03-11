package org.epde.ingrecipe.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> error(int status, String message, T error) {
        return RestResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .error(error)
                .build();
    }

    public String toJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy hh:mm a");
        return "{" +
                "\"timestamp\":\"" + (timestamp != null ? timestamp.format(formatter) : null) + "\"," +
                "\"status\":" + status + "," +
                "\"message\":\"" + message + "\"," +
                "\"data\":" + (data != null ? "\"" + data + "\"" : "null") + "," +
                "\"error\":" + (error != null ? "\"" + error + "\"" : "null") +
                "}";
    }
}
