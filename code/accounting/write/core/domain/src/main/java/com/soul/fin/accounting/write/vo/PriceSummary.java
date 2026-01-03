package com.soul.fin.accounting.write.vo;

// finalAmount = subTotal - discountApplied + taxAmount
public record PriceSummary(double subTotal,
                           double discountApplied,
                           double taxAmount,
                           double finalAmount) {
}
