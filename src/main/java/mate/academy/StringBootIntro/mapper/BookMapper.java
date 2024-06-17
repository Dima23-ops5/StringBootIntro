package mate.academy.StringBootIntro.mapper;

import mate.academy.StringBootIntro.config.MapperConfig;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);
    Book toModel(CreateBookRequestDto requestDto);
}
