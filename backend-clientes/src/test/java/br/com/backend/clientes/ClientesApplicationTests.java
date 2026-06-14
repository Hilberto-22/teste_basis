package br.com.backend.clientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "clients.source-url=classpath:clients-test.json")
class ClientesApplicationTests {

	@Test
	void contextLoads() {
	}

}
