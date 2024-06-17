package mate.academy.StringBootIntro;


import mate.academy.StringBootIntro.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import mate.academy.StringBootIntro.service.BookService;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class StringBootIntroApplication {
	private final BookService bookService;

	@Autowired
	public StringBootIntroApplication(BookService bookService) {
		this.bookService = bookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(StringBootIntroApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			Book book = new Book();
			book.setAuthor("Joanne Rowling");
			book.setTitle("Harry Potter");
			book.setDescription("Book about boy...");
			book.setCoverImage("image");
			book.setIsbn("745463");
			book.setPrice(BigDecimal.valueOf(399));
			//bookService.save(book);
			//System.out.println(bookService.findAll());
		};
	}

}
