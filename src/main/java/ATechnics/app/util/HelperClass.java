package ATechnics.app.util;

import ATechnics.app.exceptions.ExceptionClosingStream;
import ATechnics.app.exceptions.ExceptionsText;
import ATechnics.app.exceptions.NotFoundException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class HelperClass {
    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File result;
        try {
            result = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(result);
        } catch (IOException e) {
            throw new NotFoundException("Ошибка в имени файла или файл не существует!");
        }
        return result;
    }

    public static ByteArrayResource createStream(XSSFWorkbook workbook) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        setHeaders(workbook);

        try {
            workbook.write(stream);
            workbook.close();
        } catch (IOException e) {
            throw new ExceptionClosingStream(ExceptionsText.ERROR_CLOSING_STREAM.toString());
        }

        return new ByteArrayResource(stream.toByteArray());
    }

    public static HttpHeaders setHeaders(XSSFWorkbook workbook) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("workbook", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx");

        return header;
    }
}