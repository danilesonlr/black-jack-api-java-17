package br.com.alt.bank.blackjack;

import br.com.alt.bank.blackjack.step.BlackJackControllerStepTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:Feature")
@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest
public class BlackJackApplicationTests {
}
