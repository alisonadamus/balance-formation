package com.balanceformation;

import com.balanceformation.entity.balance2.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.math.BigDecimal;

import java.io.InputStream;

@Service
public class ExcelExporterBalance2 {

    public void export(Balance2 balance,
                       InputStream templateStream,
                       String output) throws Exception {

        try (Workbook workbook = new HSSFWorkbook(templateStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            FinancialResults fr = balance.getFinancialResults();
            ComprehensiveIncome ci = balance.getComprehensiveIncome();
            OperatingExpenses oe = balance.getOperatingExpenses();
            ShareProfitabilityIndicators sp = balance.getShareProfitabilityIndicators();

            /* ---------------- FINANCIAL RESULTS ---------------- */

            set(sheet,"BG16", fr.getNetRevenue());
            set(sheet,"BI17", fr.getCostOfGoodsSold());

            set(sheet,"BG20", fr.getOtherOperatingIncome());

            set(sheet,"BI21", fr.getAdministrativeExpenses());
            set(sheet,"BI22", fr.getSellingExpenses());
            set(sheet,"BI23", fr.getOtherExpenses());

            set(sheet,"BG26", fr.getIncomeFromEquityParticipation());
            set(sheet,"BG27", fr.getOtherFinancialIncome());
            set(sheet,"BG28", fr.getOtherIncome());

            set(sheet,"BI29", fr.getFinancialExpenses());
            set(sheet,"BI30", fr.getLossesFromEquityParticipation());
            set(sheet,"BI31", fr.getOtherExpenses());

            set(sheet,"BG34", fr.getIncomeTaxExpenses());
            set(sheet,"BG35", fr.getProfitFromDiscontinuedOperations());

            set(sheet,"BG18", fr.getGrossProfit());
            set(sheet,"BI19", fr.getGrossLoss());

            set(sheet,"BG24", fr.getOperatingProfit());
            set(sheet,"BI25", fr.getOperatingLoss());

            set(sheet,"BG36", fr.getNetFinancialResult());

            /* ---------------- COMPREHENSIVE INCOME ---------------- */

            set(sheet,"BG43", ci.getRevaluationNonCurrentAssets());
            set(sheet,"BG44", ci.getRevaluationFinancialInstruments());
            set(sheet,"BG45", ci.getAccumulatedCurrencyDifferences());
            set(sheet,"BG46", ci.getShareOfOtherComprehensiveIncomeAssoc());
            set(sheet,"BG47", ci.getOtherComprehensiveIncome());

            set(sheet,"BG48", ci.getOtherComprehensiveIncomeBeforeTax());
            set(sheet,"BG49", ci.getTaxOnOtherComprehensiveIncome());

            set(sheet,"BG50", ci.getOtherComprehensiveIncomeAfterTax());
            set(sheet,"BG51", ci.getTotalComprehensiveIncome(fr.getNetFinancialResult()));

            /* ---------------- OPERATING EXPENSES ---------------- */

            set(sheet,"BG56", oe.getMaterialCosts());
            set(sheet,"BG57", oe.getPayrollExpenses());
            set(sheet,"BG58", oe.getSocialSecurityContributions());
            set(sheet,"BG59", oe.getDepreciation());
            set(sheet,"BG60", oe.getOtherOperatingExpenses());

            set(sheet,"BG61", oe.getTotal());

            /* ---------------- SHARES ---------------- */

            set(sheet,"BG66", sp.getAverageNumberOfShares());
            set(sheet,"BG67", sp.getAdjustedAverageNumberOfShares());
            set(sheet,"BG68", sp.getNetProfitPerShare());
            set(sheet,"BG69", sp.getAdjustedNetProfitPerShare());
            set(sheet,"BG70", sp.getDividendsPerShare());

            try (FileOutputStream fos = new FileOutputStream(output)) {
                workbook.write(fos);
            }
        }
    }

    private void set(Sheet sheet, String address, BigDecimal value){

        CellReference ref = new CellReference(address);
        Row row = sheet.getRow(ref.getRow());

        if(row == null){
            row = sheet.createRow(ref.getRow());
        }

        Cell cell = row.getCell(ref.getCol());

        if(cell == null){
            cell = row.createCell(ref.getCol());
        }

        if(value != null){
            cell.setCellValue(value.doubleValue());
        }
    }
}