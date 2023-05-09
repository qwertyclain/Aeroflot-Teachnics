package ATechnics.app.controllers;

import ATechnics.app.models.AirbusTask;
import ATechnics.app.services.AirbusService;
import ATechnics.app.util.HelperClass;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/airbus")
public class AirbusController {
    private final AirbusService service;

    @Autowired
    public AirbusController(AirbusService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam("file") MultipartFile file) {
        XSSFWorkbook workbook = service.getResultExcelBook(file);

        return new ResponseEntity<>(HelperClass.createStream(workbook),
                HelperClass.setHeaders(workbook), HttpStatus.OK);
    }

    @PostMapping("/add_task")
    public ResponseEntity<String> addNewTask(@RequestBody AirbusTask task) {
        service.addNewTask(task);

        return new ResponseEntity<>("Новый task успешно добавлен!", HttpStatus.CREATED);
    }

    @PatchMapping("/edit_task/{id}")
    public ResponseEntity<String> editTask(@PathVariable("id") String id, @RequestBody AirbusTask task) {
        service.editTask(id, task);

        return new ResponseEntity<>("Изменения успешно внесены!", HttpStatus.ACCEPTED);
    }
}