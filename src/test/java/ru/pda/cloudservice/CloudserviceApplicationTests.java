package ru.pda.cloudservice;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudserviceApplicationTests {

	@Rule
	public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();
	@Autowired
	private TestRestTemplate restTemplate;

	@Container
	private final GenericContainer<?> cloudservice = new GenericContainer<>("cloudservice:1.0")
			.withExposedPorts(8080);

	@Test
	public void querySql() throws Exception{
		String jdbcUrl = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();
		Connection conn = DriverManager
				.getConnection(jdbcUrl, username, password);
		ResultSet resultSet =
				conn.createStatement().executeQuery("SELECT 1");
		resultSet.next();
		int result = resultSet.getInt(1);

		assertEquals(1, result);
	}
	@Test
	public void contextLoads() throws Exception{
		Integer port = cloudservice.getMappedPort(8080);

		ResponseEntity<?> entity = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);

		assertEquals(String.class, entity);
	}

}