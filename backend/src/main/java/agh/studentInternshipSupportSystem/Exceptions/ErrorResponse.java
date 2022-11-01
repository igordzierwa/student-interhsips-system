package agh.studentInternshipSupportSystem.Exceptions;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class ErrorResponse
{
    private int status;

    private Date timestamp;

    private String message;

    private List<String> details;

    public ErrorResponse(int statusCode, Date timestamp, String message, List <String> details) {
        this.status = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(int status, Date timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }
}