package agh.studentInternshipSupportSystem.Messager.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AddMessage {
    @NotNull(message = "Topic is mandatory")
    private String topic;

    private String message;

    @NotNull(message = "Sender Id is mandatory")
    private Long senderID;

    @NotNull(message = "Recipient Id is mandatory")
    private Long recipientID;
}
