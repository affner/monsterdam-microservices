package com.fanflip.finance.domain.enumeration;

/**
 * The WalletTransactionType enumeration.
 */
public enum WalletTransactionType {
    TOP_UP("viewer.wallet.transaction.type.deposit"),
    PURCHASE("viewer.wallet.transaction.type.purchase"),
    REFUND("viewer.wallet.transaction.type.refund"),
    WITHDRAWAL("viewer.wallet.transaction.type.withdrawal");

    private final String value;

    WalletTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
