package com.balanceformation.entity.solvencygroups;

public enum PastYearsPerformance {

    NO_DATA_OR_TWO_YEARS_LOSS(1, "збиткова діяльність за два минулі роки або звіт за попередній\n" +
            "звітний рік не наданий"),
    LAST_YEAR_LOSS(2, "збиткова діяльність за минулий рік"),
    NO_PROFIT_NO_LOSS(3, " діяльність за відсутності прибутків та збитків, або відсутності\n" +
            "діяльності"),
    LAST_YEAR_PROFIT(4, "прибуткова за минулий рік"),
    TWO_YEARS_PROFIT(5, " прибуткова за два минулих роки");

    private final int value;
    private final String description;

    PastYearsPerformance(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
