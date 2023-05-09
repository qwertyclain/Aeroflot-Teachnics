package ATechnics.app.controllers;

import ATechnics.app.models.BoeingTask;
import ATechnics.app.services.BoeingService;
import ATechnics.app.util.HelperClass;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/boeing")
public class BoeingController {
    private final BoeingService service;

    @Autowired
    public BoeingController(BoeingService service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam("file") MultipartFile file) {
        XSSFWorkbook workbook = service.getResultExcelBook(file);

        return new ResponseEntity<>(HelperClass.createStream(workbook),
                HelperClass.setHeaders(workbook), HttpStatus.OK);
    }

    @PostMapping("/add_task")
    public ResponseEntity<String> addNewTask(@RequestBody BoeingTask task) {
        service.addNewTask(task);

        return new ResponseEntity<>("Новый task успешно добавлен!", HttpStatus.CREATED);
    }

    @PatchMapping("/edit_task/{id}")
    public ResponseEntity<String> editTask(@PathVariable("id") String id, @RequestBody BoeingTask task) {
        service.editTask(id, task);

        return new ResponseEntity<>("Изменения успешно внесены", HttpStatus.ACCEPTED);
    }
}