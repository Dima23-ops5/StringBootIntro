package mate.academy.StringBootIntro.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List getAll() {
        return bookService.findAll();
    }

    @GetMapping
    public BookDto getBookById(Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDto createBook(CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

}
