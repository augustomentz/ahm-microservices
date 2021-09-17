package br.com.bookapp.feign;

import br.com.bookapp.response.Cambio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "cambio-app", url = "localhost:8000")
public interface CambioClient {
    @GetMapping(value = "/cambio/{amount}/{from}/{to}")
    public Cambio getCambio(
        @PathVariable("amount") Double amount,
        @PathVariable("from") String from,
        @PathVariable("to") String to
    );
}
