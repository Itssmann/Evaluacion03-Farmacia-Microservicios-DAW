package pe.edu.tecsup.farmacia.venta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@EnabledIfEnvironmentVariable(named = "RUN_MYSQL_TESTS", matches = "true")
@SpringBootTest
class VentaServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
