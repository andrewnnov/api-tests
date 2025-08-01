package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeTransferRequestModel extends BaseModel {
    private long senderAccountId;
    private long receiverAccountId;
    private double amount;
}


//{
//        "senderAccountId": 1,
//        "receiverAccountId": 2,
//        "amount": 254
//        }