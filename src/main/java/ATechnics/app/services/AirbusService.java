package ATechnics.app.services;

import ATechnics.app.exceptions.ExceptionsText;
import ATechnics.app.exceptions.NotFoundException;
import ATechnics.app.models.AirbusTask;
import ATechnics.app.repositories.AirbusRepository;
import ATechnics.app.util.CellStyler;
import ATechnics.app.util.HelperClass;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AirbusService {
    private final AirbusRepository repository;

    @Autowired
    public AirbusService(AirbusRepository repository) {
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

        AirbusTask task;

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

            XSSFCell mpdCell = row.createCell(5);
            XSSFCell mpdNewCell = row.createCell(6);
            XSSFCell preparationCell = row.createCell(7);
            XSSFCell multipliedPreparationCell = row.createCell(8);

            mpdCell.setCellStyle(CellStyler.setStyle(excel));
            mpdNewCell.setCellStyle(CellStyler.setStyle(excel));
            preparationCell.setCellStyle(CellStyler.setStyle(excel));
            multipliedPreparationCell.setCellStyle(CellStyler.setStyle(excel));

            mpdCell.setCellValue(task.getMpd());
            mpdNewCell.setCellValue(task.getMultipliedMpd());

            if(task.getPreparation() == null || task.getPreparation().equals("g"))
                continue;
            
            preparationCell.setCellValue(task.getPreparation());
            multipliedPreparationCell.setCellValue(task.getMultipliedPreparation());
        }

        return excel;
    }

    public void addNewTask(AirbusTask task) {
        repository.save(task);
    }

    @Transactional
    public void editTask(String id, AirbusTask newTask) {
        AirbusTask task = repository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionsText.TASK_NOT_FOUND.toString())
        );

        task.setMpd(newTask.getMpd());
        task.setMultipliedMpd(newTask.getMultipliedMpd());
        task.setPreparation(newTask.getPreparation());
        task.setMultipliedPreparation(newTask.getMultipliedPreparation());

        repository.save(task);
    }
}