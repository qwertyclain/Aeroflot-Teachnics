package ATechnics.app.services;

import ATechnics.app.exceptions.ExceptionsText;
import ATechnics.app.exceptions.NotFoundException;
import ATechnics.app.models.BoeingTask;
import ATechnics.app.repositories.BoeingRepository;
import ATechnics.app.util.CellStyler;
import ATechnics.app.util.HelperClass;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class BoeingService {
    private final BoeingRepository repository;

    @Autowired
    public BoeingService(BoeingRepository repository) {
        this.repository = repository;
    }

    public XSSFWorkbook getResultExcelBook(MultipartFile file) {
        File convertedFile = HelperClass.convertMultipartFileToFile(file);
        try {
            return getExcelFile(convertedFile);
        } catch (IOException | InvalidFormatException e) {
            throw new NotFoundException(ExceptionsText.FILE_NOT_FOUND.toString());
        }
    }

    private XSSFWorkbook getExcelFile(File convertedFile) throws IOException, InvalidFormatException {
        XSSFWorkbook excel = new XSSFWorkbook(convertedFile);
        XSSFSheet table = excel.getSheet("WP");

        BoeingTask task;

        for(int i = 0; i < table.getLastRowNum(); i++) {
            XSSFRow row = table.getRow(i);
            XSSFCell cell = row.getCell(1);

            if(cell == null)
                break;

            if(cell.getCellType() == CellType.NUMERIC)
                task = repository.findById(String.valueOf(cell.getNumericCellValue())).orElse(null);
            else
                task = repository.findById(cell.getStringCellValue()).orElse(null);

            if(task == null)
                continue;

            XSSFCell mpdCell = row.createCell(6);
            XSSFCell mpdNewCell = row.createCell(7);

            mpdCell.setCellStyle(CellStyler.setStyle(excel));
            mpdNewCell.setCellStyle(CellStyler.setStyle(excel));

            mpdCell.setCellValue(task.getMpd());
            mpdNewCell.setCellValue(task.getMultipliedMpd());
        }

        return excel;
    }

    public void addNewTask(BoeingTask task) {
        repository.save(task);
    }

    @Transactional
    public void editTask(String id, BoeingTask newTask) {
        BoeingTask task = repository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionsText.TASK_NOT_FOUND.toString())
        );

        task.setMpd(newTask.getMpd());
        task.setMultipliedMpd(newTask.getMultipliedMpd());
    }
}