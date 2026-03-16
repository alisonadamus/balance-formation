package com.balanceformation;

import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.balance1.assets.*;
import com.balanceformation.entity.balance1.liabilities.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;

@Service
public class ExcelExporterBalance1 {

    public void export(Balance1 balance, InputStream templateStream, String output) throws Exception {

        try (Workbook workbook = new XSSFWorkbook(templateStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            NonCurrentAssets nca = balance.getAssets().getNonCurrentAssets();
            CurrentAssets ca = balance.getAssets().getCurrentAssets();
            Assets assets = balance.getAssets();

            Equity equity = balance.getLiabilities().getEquity();
            LongTermLiabilities ltl = balance.getLiabilities().getLongTermLiabilities();
            CurrentLiabilities cl = balance.getLiabilities().getCurrentLiabilities();

            // ---------- NON CURRENT ASSETS ----------
            set(sheet,"AY9", nca.getIntangibleInitialCost());
            set(sheet,"AY11", nca.getIntangibleAmortization());
            set(sheet,"AY12", nca.getUnfinishedInvestments());
            set(sheet,"AY13", nca.getFixedAssetsInitialCost());
            set(sheet,"AY14", nca.getFixedAssetsDepreciation());
            set(sheet,"AY15", nca.getInvestmentProperty());
            set(sheet,"AY16", nca.getLongTermBiologicalAssets());
            set(sheet,"AY17", nca.getLongTermFinancialInvestmentsEquity());
            set(sheet,"AY18", nca.getOtherFinancialInvestments());
            set(sheet,"AY19", nca.getLongTermReceivables());
            set(sheet,"AY21", nca.getDeferredTaxAssets());
            set(sheet,"AY22", nca.getOtherNonCurrentAssets());
            set(sheet,"AY23", nca.getIntangibleAssets());
            set(sheet,"AY24", nca.getFixedAssets());
            set(sheet,"AY25", nca.getTotal());

            // ---------- CURRENT ASSETS ----------
            set(sheet,"AY26", ca.getInventories());
            set(sheet,"AY28", ca.getBiologicalAssetsCurrent());
            set(sheet,"AY29", ca.getReceivablesProducts());
            set(sheet,"AY30", ca.getAdvancesIssued());
            set(sheet,"AY32", ca.getReceivablesBudget());
            set(sheet,"AY33", ca.getIncomeTaxReceivable());
            set(sheet,"AY34", ca.getOtherReceivables());
            set(sheet,"AY35", ca.getCurrentFinancialInvestments());
            set(sheet,"AY36", ca.getCash());
            set(sheet,"AY37", ca.getPrepaidExpenses());
            set(sheet,"AY38", ca.getOtherCurrentAssets());
            set(sheet,"AY39", ca.getTotal());

            // ---------- ASSETS ----------
            set(sheet,"AY40", assets.getNonCurrentAssetsForSale());
            set(sheet,"AY41", assets.getTotal());

            // ---------- EQUITY ----------
            set(sheet,"AY46", equity.getRegisteredCapital());
            set(sheet,"AY48", equity.getRevaluationCapital());
            set(sheet,"AY49", equity.getAdditionalCapital());
            set(sheet,"AY50", equity.getReserveCapital());
            set(sheet,"AY51", equity.getRetainedEarnings());
            set(sheet,"AY52", equity.getUnpaidCapital());
            set(sheet,"AY53", equity.getWithdrawnCapital());
            set(sheet,"AY54", equity.getTotal());

            // ---------- LONG TERM LIABILITIES ----------
            set(sheet,"AY55", ltl.getDeferredTaxLiabilities());
            set(sheet,"AY57", ltl.getLongTermBankLoans());
            set(sheet,"AY58", ltl.getOtherLongTermLiabilities());
            set(sheet,"AY59", ltl.getLongTermProvisions());
            set(sheet,"AY60", ltl.getTargetedFinancing());
            set(sheet,"AY61", ltl.getTotal());

            // ---------- CURRENT LIABILITIES ----------
            set(sheet,"AY62", cl.getShortTermLoans());
            set(sheet,"AY65", cl.getPayableLongTerm());
            set(sheet,"AY66", cl.getPayableSuppliers());
            set(sheet,"AY67", cl.getPayableBudget());
            set(sheet,"AY68", cl.getIncomeTaxPayable());
            set(sheet,"AY69", cl.getInsurancePayable());
            set(sheet,"AY70", cl.getSalaryPayable());
            set(sheet,"AY71", cl.getCurrentProvisions());
            set(sheet,"AY72", cl.getDeferredIncome());
            set(sheet,"AY73", cl.getOtherCurrentLiabilities());
            set(sheet,"AY74", cl.getTotal());

            // ---------- TOTAL LIABILITIES ----------
            set(sheet,"AY77", balance.getLiabilities().getTotal());

            try (FileOutputStream fos = new FileOutputStream(output)) {
                workbook.write(fos);
            }
        }
    }

    private void set(Sheet sheet,String address, BigDecimal value){

        CellReference ref = new CellReference(address);

        Row row = sheet.getRow(ref.getRow());
        if(row == null) row = sheet.createRow(ref.getRow());

        Cell cell = row.getCell(ref.getCol());
        if(cell == null) cell = row.createCell(ref.getCol());

        if(value != null)
            cell.setCellValue(value.doubleValue());
    }
}