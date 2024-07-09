package mate.academy.springbootintro.dto.bookdto;

public record BookSearchParametersDto(String[] authors, String[] titles, String[] isbns) {
}
