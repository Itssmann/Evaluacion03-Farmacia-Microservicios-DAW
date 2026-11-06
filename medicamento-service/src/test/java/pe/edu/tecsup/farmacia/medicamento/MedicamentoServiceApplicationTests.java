package pe.edu.tecsup.farmacia.medicamento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@EnabledIfEnvironmentVariable(named = "RUN_MYSQL_TESTS", matches = "true")
@SpringBootTest
class MedicamentoServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
