package br.com.cambioapp.contollers;

import br.com.cambioapp.model.Cambio;
import br.com.cambioapp.repository.CambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@RestController
@RequestMapping("cambio")
public class CambioController {
    @Autowired
    private Environment environment;

    @Autowired
    private CambioRepository repository;

    @GetMapping(value = "/{amount}/{from}/{to}")
    public Cambio getCambio(
        @PathVariable("amount") BigDecimal amount,
        @PathVariable("from") String from,
        @PathVariable("to") String to
    ) {
         Cambio cambio = repository.findByFromAndTo(from, to);

        if (cambio == null) throw new RuntimeException("Currency Unsupported");

        cambio.setEnvironment(environment.getProperty("local.server.port"));
        cambio.setConvertedValue(
                cambio.getConversionFactor()
                        .multiply(amount)
                        .setScale(2, RoundingMode.CEILING)
        );

        return cambio;
    }
}
