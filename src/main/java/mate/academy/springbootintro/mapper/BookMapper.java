package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.bookdto.BookDto;
import mate.academy.springbootintro.dto.bookdto.CreateBookRequestDto;
import mate.academy.springbootintro.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    void updateBookFromDto(CreateBookRequestDto createBookRequestDto, @MappingTarget Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
