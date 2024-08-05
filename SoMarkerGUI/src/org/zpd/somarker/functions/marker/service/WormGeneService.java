package org.zpd.somarker.functions.marker.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.orman.mapper.Model;
import org.orman.sql.Query;
import org.zpd.somarker.db.entity.WormGeneEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhb on 16/10/3.
 */
public class WormGeneService implements WormGeneInterface {

    private final String[] excelHeader = new String[]{"Gene", "Chromosome", "Genetic Position", "PPFrom", "PPTo"};
    public static List<WormGeneEntity> allWormGenes;

    static {
        allWormGenes = Model.fetchAll(WormGeneEntity.class);
    }

    @Override
    public List<WormGeneEntity> fetchAllWormGene() {
        return Model.fetchAll(WormGeneEntity.class);
    }

    @Override
    public void deleteAllWormGenes() {
        Model.execute(new Query("DELETE FROM WormGene"));
    }

    @Override
    public void bulkImportWormGenes(File sourceFile) {
        deleteAllWormGenes();
        try (FileInputStream fis = new FileInputStream(sourceFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Iterator<Cell> headerCells = headerRow.iterator();
            List<String> headers = new ArrayList<>();
            while (headerCells.hasNext()) {
                Cell headerCell = headerCells.next();
                String headerValue = headerCell.getStringCellValue();
                headers.add(headerValue);
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Map<String, Object> rowData = new HashMap<>();
                    Iterator<Cell> cells = row.iterator();
                    int cellIndex = 0;
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        String headerKey = headers.get(cellIndex++);
                        Object cellValue = null;
                        // 根据单元格类型获取值
                        if (Objects.requireNonNull(cell.getCellType()) == CellType.NUMERIC) {// 注意：这里可能需要根据单元格的格式来判断是数字、日期还是其他
                            cellValue = cell.getNumericCellValue();
                        } else {
                            cellValue = cell.getStringCellValue();
                        }
                        rowData.put(headerKey, cellValue);
                    }
                    String gene = (String) rowData.get("Gene");
                    String Chromosome = (String) rowData.get("Chromosome"); // 获取列名为 "ColumnName" 的值
                    double geneticPosition = (double) rowData.get("Genetic Position");
                    double ppFrom = (double) rowData.get("PPFrom");
                    double ppTo = (double) rowData.get("PPTo");
                    WormGeneEntity entity = new WormGeneEntity();
                    entity.setGene(gene);
                    entity.setChromosome(Chromosome);
                    entity.setGeneticPosition(geneticPosition);
                    entity.setPpFrom(ppFrom);
                    entity.setPpTo(ppTo);
                    entity.insert();
                }
            }
            WormGeneService.allWormGenes = fetchAllWormGene();
            System.out.println("Data imported successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bulkExportWormGenes(File sourceFile) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(sourceFile)) {
            Sheet sheet = workbook.createSheet("WormGene");
            AtomicInteger rowIndex = new AtomicInteger(0);
            Row headerRow = sheet.createRow(rowIndex.getAndIncrement());
            AtomicInteger colIndex = new AtomicInteger(0);
            Arrays.asList(excelHeader).forEach(title -> {
                Cell cell = headerRow.createCell(colIndex.getAndIncrement());
                cell.setCellValue(title);
            });
            List<WormGeneEntity> dataList = fetchAllWormGene();
            for (WormGeneEntity data : dataList) {
                Object[] row = new Object[]{
                        data.getGene(),
                        data.getChromosome(),
                        data.getGeneticPosition(),
                        data.getPpFrom(),
                        data.getPpTo(),
                };
                colIndex.set(0);
                Row dataRow = sheet.createRow(rowIndex.getAndIncrement());
                Arrays.asList(row).forEach(colData -> {
                    int index = colIndex.getAndIncrement();
                    Cell cell = dataRow.createCell(index);
                    if (colData instanceof String) {
                        cell.setCellValue((String) colData);
                    } else if (colData instanceof Double) {
                        cell.setCellValue((Double) colData);
                    }
                });
            }
            workbook.write(outputStream);
            System.out.println("Data exported successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
