package com.aikon.fin.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class CurrentBalance {
    private Integer id;

    private String memberId;

    private BigDecimal balance;

    private BigDecimal profit;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentBalance that = (CurrentBalance) o;
        return Objects.equals(memberId, that.memberId) &&
                balance.subtract(((CurrentBalance) o).balance).compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(memberId, balance);
    }
}