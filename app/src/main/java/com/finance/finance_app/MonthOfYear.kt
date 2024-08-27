package com.finance.finance_app

enum class MonthOfYear (val num: Int){
    ENE(1),
    FEB(2),
    MAR(3),
    ABR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AGO(8),
    SEP(9),
    OCT(10),
    NOV(11),
    DIC(12);

    companion object {
        fun fromValue(value: Int): MonthOfYear? {
            return entries.firstOrNull { it.num == value }
        }
    }
}