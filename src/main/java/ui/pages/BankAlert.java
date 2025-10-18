package ui.pages;

import lombok.Getter;

@Getter
public enum BankAlert {
    USER_CREATED_SUCCESSFULLY("✅ User created successfully!"),
    USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS("❌ Failed to create user:\n" +
            "\n" +
            "• username: Username must be between 3 and 15 characters"),
    NEW_ACCOUNT_CREATED("✅ New Account Created! Account Number: "),
    DEPOSIT_SUCCESSFUL("✅ Successfully deposited $"),
    DEPOSIT_UNSUCCESSFUL("❌ Please enter a valid amount."),
    TRANSFER_SUCCESSFUL("✅ Successfully transferred $"),
    TRANSFER_UNSUCCESSFUL("❌ Error: Invalid transfer: insufficient funds or invalid accounts"),
    USERNAME_UPDATED_SUCCESSFULLY("✅ Name updated successfully!");

    private final String message;

    BankAlert(String message) {
        this.message = message;
    }
}
