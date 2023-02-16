package com.kalyan.excel.service;

import com.kalyan.excel.helper.ExcelHelper;
import com.kalyan.excel.model.Tutorial;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {


    public ByteArrayInputStream load() {
        List<Tutorial> tutorials = new ArrayList<>();

        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(tutorials);
        return in;
    }

}
