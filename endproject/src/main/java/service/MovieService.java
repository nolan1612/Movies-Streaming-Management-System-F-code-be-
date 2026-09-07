package service;

import enums.OrderType;
import enums.SortBy;
import model.Category;
import model.Movie;
import model.MovieCategory;
import repository.CategoryRepository;
import repository.MovieCategoryRepository;
import repository.MovieRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    private MovieRepository movieRepository;
    private CategoryRepository categoryRepository;
    private MovieCategoryRepository movieCategoryRepository;
    private WatchService watchService;

    public MovieService(MovieRepository movieRepository, CategoryRepository categoryRepository, 
                        MovieCategoryRepository movieCategoryRepository, WatchService watchService) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.watchService = watchService;
    }

    public List<Movie> getAllMovies() { return movieRepository.findAll(); }

    public Movie getMovieById(String id) { return movieRepository.findById(id); }

    public Movie getMovieByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    public void addMovie(Movie movie, List<String> categoryNames) {
        movieRepository.add(movie);
        if (categoryNames != null) {
            for (String catName : categoryNames) {
                Category cat = categoryRepository.findByName(catName);
                if (cat != null) {
                    movieCategoryRepository.add(new MovieCategory(movie.getMovieId(), cat.getCategoryId()));
                }
            }
        }
    }

    public boolean updateMovie(String oldTitle, String newTitle, String desc, int duration, int year, String director, List<String> actors) {
        Movie movie = getMovieByTitle(oldTitle);
        if (movie == null) return false;

        movie.setTitle(newTitle);
        movie.setDescription(desc);
        movie.setDuration(duration);
        movie.setReleaseYear(year);
        movie.setDirector(director);
        movie.setActors(actors);

        movieRepository.update(movie);
        return true;
    }

    public String deleteMovieByTitle(String title) {
        Movie m = getMovieByTitle(title);
        if (m == null) {
            return "LỖI: Không tìm thấy phim có tiêu đề '" + title + "' trong hệ thống!";
        }

        // 1. Dọn dẹp phim khỏi tất cả Favorite & Watchlist của người dùng
        if (watchService != null) {
            watchService.removeMovieFromAllLists(m.getMovieId());
        }

        // 2. Xóa liên kết thể loại
        movieCategoryRepository.deleteByMovieId(m.getMovieId());

        // 3. Xóa phim khỏi cơ sở dữ liệu
        movieRepository.delete(m.getMovieId());

        return "Thành công: Đã xóa hoàn toàn phim '" + m.getTitle() + "' khỏi hệ thống và các danh sách liên quan!";
    }

    public List<Movie> searchMovies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return new ArrayList<>();
        String kw = keyword.toLowerCase().trim();

        return movieRepository.findAll().stream().filter(m -> {
            boolean matchTitle = m.getTitle().toLowerCase().contains(kw);
            boolean matchDirector = m.getDirector() != null && m.getDirector().toLowerCase().contains(kw);
            boolean matchActor = m.getActors() != null && m.getActors().stream().anyMatch(a -> a.toLowerCase().contains(kw));
            return matchTitle || matchDirector || matchActor;
        }).collect(Collectors.toList());
    }

    // Bổ sung: Tìm kiếm chính xác theo Category Name
    public List<Movie> getMoviesByCategoryName(String categoryName) {
        Category cat = categoryRepository.findByName(categoryName);
        if (cat == null) return new ArrayList<>();
        
        List<String> movieIds = movieCategoryRepository.getMovieIdsByCategory(cat.getCategoryId());
        return movieIds.stream()
                .map(id -> movieRepository.findById(id))
                .filter(m -> m != null)
                .collect(Collectors.toList());
    }

    public List<Movie> sortMovies(SortBy sortBy, OrderType orderType) {
        List<Movie> list = new ArrayList<>(movieRepository.findAll());
        Comparator<Movie> comparator = null;

        switch (sortBy) {
            case TITLE: comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER); break;
            case RATING: comparator = Comparator.comparingDouble(Movie::getRating); break;
            case RELEASE_YEAR: comparator = Comparator.comparingInt(Movie::getReleaseYear); break;
            case POPULARITY: comparator = Comparator.comparingInt(Movie::getViews); break;
        }

        if (comparator != null) {
            if (orderType == OrderType.DESC) comparator = comparator.reversed();
            list.sort(comparator);
        }
        return list;
    }
}
