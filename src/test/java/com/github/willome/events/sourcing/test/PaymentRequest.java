package com.github.willome.events.sourcing.test;

public class PaymentRequest {

    private Long accountId = 1L;
    private Double ammount = 1D;

    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Double getAmmount() {
        return ammount;
    }
    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

}
