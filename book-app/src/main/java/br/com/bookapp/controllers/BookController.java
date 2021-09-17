package br.com.bookapp.controllers;

import br.com.bookapp.feign.CambioClient;
import br.com.bookapp.model.Book;
import br.com.bookapp.repository.BookRepository;
import br.com.bookapp.response.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    @Autowired
    private CambioClient cambioClient;

    @GetMapping(value = "/{bookId}/{currency}")
    public Book findBook(
        @PathVariable("bookId") Long bookId,
        @PathVariable("currency") String currency
    ) {

        Book book = repository.getById(bookId);
        if (book == null) throw new RuntimeException("Book not Found");

        Cambio cambio = cambioClient.getCambio(book.getPrice(), "USD", currency);

        if (cambio == null) throw new RuntimeException("Not found cambio");

        book.setPrice(cambio.getConvertedValue());
        book.setEnvironment(environment.getProperty("local.server.port"));

        return book;
    }
}
