package com.kalyan.excel.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ExcelUploadController {

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
        // Check the file type
        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return "Only Excel files are allowed";
        }
        // Process the Excel file
        try {
            // Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // ...
            return "File uploaded successfully";
        } catch (Exception e) {
            return "Error uploading file";
        }
    }
}
