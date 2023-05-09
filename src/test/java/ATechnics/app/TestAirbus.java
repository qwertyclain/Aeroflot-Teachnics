package ATechnics.app;

import ATechnics.app.exceptions.NotFoundException;
import ATechnics.app.models.AirbusTask;
import ATechnics.app.repositories.AirbusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestAirbus {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private AirbusRepository repository;

	@BeforeEach
	public void addTestTasks() {
		AirbusTask task = new AirbusTask();

		task.setTaskCard("1111-11-1");
		task.setMpd("2");
		task.setMultipliedMpd("4,6");
		task.setPreparation("1");
		task.setMultipliedPreparation("2,3");

		repository.save(task);
	}

	@AfterEach
	public void cleaningDataBase() {
		repository.deleteById("1111-11-1");
		repository.deleteById("Test");
	}

	@Test
	@DisplayName("Внесение в БД нового task card")
	public void addNewTaskCard() throws Exception {
		mockMvc.perform(post("/airbus/add_task")
				.content(mapper.writeValueAsString(getAirbusTask()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Редактирование существующего task card")
	public void editTaskCard() throws Exception {
		mockMvc.perform(patch("/airbus/edit_task/{id}", "1111-11-1")
				.content(mapper.writeValueAsString(getAirbusTask()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}

	@Test
	@DisplayName("Получаем ошибку из метода сервиса, т. к. выполнен поиск несуществующего task card")
	public void getExceptionTaskNotFound() throws Exception {
		mockMvc.perform(patch("/airbus/edit_task/{id}", "1"))
				.andExpect(result ->
						result.getResolvedException().getClass().equals(NotFoundException.class));
	}

	@Test
	@DisplayName("Получаем ошибку, т. к. в метод не направлен excel-файл")
	public void getExceptionFileNotFound() throws Exception {
		mockMvc.perform(post("/airbus"))
				.andExpect(result ->
						result.getResolvedException().getClass().equals(NotFoundException.class));
	}

	private AirbusTask getAirbusTask() {
		AirbusTask task = new AirbusTask();

		task.setTaskCard("Test");
		task.setMpd("1");
		task.setMultipliedMpd("2,3");
		task.setPreparation("2");
		task.setMultipliedPreparation("4,6");

		return task;
	}
}