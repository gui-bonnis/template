package com.soul.fin.accounting.vo;

// finalAmount = subTotal - discountApplied + taxAmount
public record PriceSummary(double subTotal,
                           double discountApplied,
                           double taxAmount,
                           double finalAmount) {
}
