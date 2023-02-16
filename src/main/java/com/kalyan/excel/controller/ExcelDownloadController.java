package com.kalyan.excel.controller;

import org.apache.avro.Schema;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RestController
public class ExcelDownloadController {

    @GetMapping("/download")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // Create the Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("insert");
        Row headerRow = sheet.createRow(0);
        try {
            Schema schema = new Schema.Parser()
                    .parse(new File("src/main/resources/twitter.avsc"));

            for (int index = 0; index < schema.getFields().size(); index++) {
                headerRow.createCell(index).setCellValue(schema.getFields().get(index).name());
//             Row row = sheet.createRow(rowNum++);
//             row.createCell(0).setCellValue(person.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Write the workbook to a ByteArrayOutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // Set the response headers
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");
        response.setContentLength(out.size());

        // Write the workbook to the response output stream
        response.getOutputStream().write(out.toByteArray());
        response.flushBuffer();
    }

}
