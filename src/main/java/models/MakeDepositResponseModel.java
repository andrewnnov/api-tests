package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeDepositResponseModel extends BaseModel {
    private long id;
    private String accountNumber;
    private double balance;
    private List<TransactionResponseModel> transactions;
}



//{
//        "id": 1,
//        "accountNumber": "ACC1",
//        "balance": 0.0,
//        "transactions": []
//        }

//{
//        "id": 1,
//        "accountNumber": "ACC1",
//        "balance": 100.0,
//        "transactions": [
//        {
//        "id": 1,
//        "amount": 100.0,
//        "type": "DEPOSIT",
//        "timestamp": "Thu Jul 31 15:49:00 UTC 2025",
//        "relatedAccountId": 1
//        }
//        ]
//        }