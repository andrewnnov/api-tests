package api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeTransferResponseModel extends BaseModel {
    private long receiverAccountId;
    private long senderAccountId;
    private String message;
    private double amount;

}


//{
//        "receiverAccountId": 7,
//        "senderAccountId": 6,
//        "message": "Transfer successful",
//        "amount": 50.0
//        }
