package org.zpd.somarker.functions.marker.service;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.orman.mapper.Model;
import org.orman.sql.Query;
import org.zpd.foundation.Tool;
import org.zpd.somarker.db.entity.FluorescentMarkerEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhb on 16/10/2.
 */
public class FluorescentMarkerFinderService implements FluorescentMarkerFinderInterface {


    private final String[] excelHeader = new String[]{"Strain Name", "Chromosome", "Genetic Position", "Fluorescent Marker", "Insertion Marker", "Image", "Genotype", "Fluorophore"};

    @Override
    public List<FluorescentMarkerEntity> searchBalancers(Map<String, String> params) {
        String chromosome = Tool.instance().getString(params.get("chromosome"));
        String position = params.get("position");
        if (chromosome.length() == 0 && position.length() == 0) {
            return new ArrayList<>();
        }
        String[] chromosomes = chromosome.split(",");
        if (chromosome != null) {
            Float _position = 0F;
            if (position != null && !"".equals(position)) {
                _position = Tool.instance().getFloat(position);
            }
            String sql = "select * from FluorescentMarker ";
            if (chromosomes.length > 0) {
                sql += "where Chromosome in (";
                for (String str : chromosomes) {
                    sql += "'" + str + "'";
                    if (str != chromosomes[chromosomes.length - 1]) {
                        sql += ",";
                    }
                }
                sql += ") ";
            }
            sql += " order by (GeneticPosition - " + _position + ") * (GeneticPosition - " + _position + ") asc LIMIT 20";
            return Model.fetchQuery(new Query(sql), FluorescentMarkerEntity.class);
        }
        return new ArrayList<>();
    }

    @Override
    public List<FluorescentMarkerEntity> fetchAllBalancers() {
        return Model.fetchAll(FluorescentMarkerEntity.class);
    }

    @Override
    public void deleteAllBalancers() {
        Model.execute(new Query("DELETE FROM FluorescentMarker"));
    }

    @Override
    public void bulkImportBalancers(File sourceFile) {
        deleteAllBalancers();
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
                    String strainName = (String) rowData.get("Strain Name");
                    String Chromosome = (String) rowData.get("Chromosome"); // 获取列名为 "ColumnName" 的值
                    double geneticPosition = (double) rowData.get("Genetic Position");
                    String fluorescentMarker = (String) rowData.get("Fluorescent Marker");
                    String insertionMarker = (String) rowData.get("Insertion Marker");
                    String image = (String) rowData.get("Image");
                    String genotype = (String) rowData.get("Genotype");
                    String fluorophore = (String) rowData.get("Fluorophore");
                    FluorescentMarkerEntity entity = new FluorescentMarkerEntity();
                    entity.setChromosome(Chromosome);
                    entity.setGeneticPosition(geneticPosition);
                    entity.setFluorescentMarker(fluorescentMarker);
                    entity.setFluorophore(fluorophore);
                    entity.setGenotype(genotype);
                    entity.setInsertionMarker(insertionMarker);
                    entity.setImage(image);
                    entity.setStrainName(strainName);
                    entity.insert();
                }
            }
            System.out.println("Data imported successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bulkExportBalancers(File sourceFile) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(sourceFile)) {
            Sheet sheet = workbook.createSheet("FluorescentMarker");
            AtomicInteger rowIndex = new AtomicInteger(0);
            Row headerRow = sheet.createRow(rowIndex.getAndIncrement());
            AtomicInteger colIndex = new AtomicInteger(0);
            Arrays.asList(excelHeader).forEach(title -> {
                Cell cell = headerRow.createCell(colIndex.getAndIncrement());
                cell.setCellValue(title);
            });
            List<FluorescentMarkerEntity> dataList = fetchAllBalancers();
            for (FluorescentMarkerEntity data : dataList) {
                String image = data.getImage();
                Object[] row = new Object[]{
                        data.getStrainName(),
                        data.getChromosome(),
                        data.getGeneticPosition(),
                        data.getFluorescentMarker(),
                        data.getInsertionMarker(),
                        image,
                        data.getGenotype(),
                        data.getFluorophore(),
                };
                colIndex.set(0);
                Row dataRow = sheet.createRow(rowIndex.getAndIncrement());
                Arrays.asList(row).forEach(colData -> {
                    Cell cell = dataRow.createCell(colIndex.getAndIncrement());
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
