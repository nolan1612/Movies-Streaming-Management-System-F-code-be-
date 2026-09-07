package view;

import controller.*;
import enums.*;
import model.*;
import repository.*;
import service.*;
import utils.IdGenerator;

import java.io.File;
import java.util.*;

public class Endproject {
    private static User currentUser = null;

    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // Repositories
        MovieRepository movieRepo = new MovieRepository();
        CategoryRepository categoryRepo = new CategoryRepository();
        MovieCategoryRepository movieCategoryRepo = new MovieCategoryRepository();
        UserRepository userRepo = new UserRepository();
        WatchlistRepository watchlistRepo = new WatchlistRepository();
        FavoriteRepository favoriteRepo = new FavoriteRepository();
        HistoryRepository historyRepo = new HistoryRepository();

        // Services
        AuthService authService = new AuthService(userRepo);
        MovieService movieService = new MovieService(movieRepo, categoryRepo, movieCategoryRepo);
        CategoryService categoryService = new CategoryService(categoryRepo);
        UserService userService = new UserService(userRepo);
        WatchService watchService = new WatchService(watchlistRepo, favoriteRepo, historyRepo, movieRepo);
        ReportService reportService = new ReportService(movieRepo);

        // Controllers
        AuthController authController = new AuthController(authService);
        MovieController movieController = new MovieController(movieService);
        CategoryController categoryController = new CategoryController(categoryService);
        UserController userController = new UserController(userService);
        WatchController watchController = new WatchController(watchService);
        ReportController reportController = new ReportController(reportService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println("   MOVIE STREAMING MANAGEMENT SYSTEM (v2.0)       ");
        System.out.println("==================================================");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- MAIN MENU ---");
                System.out.println("1. Đăng ký tài khoản");
                System.out.println("2. Đăng nhập");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    System.out.print("Username: "); String u = scanner.nextLine();
                    System.out.print("Password: "); String p = scanner.nextLine();
                    System.out.print("Full Name: "); String fn = scanner.nextLine();
                    System.out.print("Email: "); String em = scanner.nextLine();
                    System.out.print("Role (1: USER, 2: ADMIN): "); int r = Integer.parseInt(scanner.nextLine());
                    Role role = (r == 2) ? Role.ADMIN : Role.USER;
                    
                    User user = authController.register(u, p, fn, em, role);
                    if (user != null) System.out.println("Đăng ký thành công!");
                } else if (choice == 2) {
                    System.out.print("Username: "); String u = scanner.nextLine();
                    System.out.print("Password: "); String p = scanner.nextLine();
                    currentUser = authController.login(u, p);
                    if (currentUser != null) {
                        System.out.println("Đăng nhập thành công! Xin chào, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
                    } else {
                        System.out.println("Đăng nhập thất bại! Sai thông tin.");
                    }
                } else if (choice == 0) {
                    System.out.println("Tạm biệt!");
                    break;
                }
            } else {
                System.out.println("\n================ MAIN DASHBOARD ================");
                System.out.println("1. Show all movie (Xem danh sách, Tìm kiếm, Sắp xếp, Chi tiết, Xem phim)");
                System.out.println("2. Watchlist management (Quản lý danh sách chờ xem)");
                System.out.println("3. Favorite movie management (Quản lý phim yêu thích)");
                System.out.println("4. Xem phim gần đây (Recent Watch Stack)");
                if (currentUser.getRole() == Role.ADMIN) {
                    System.out.println("5. [ADMIN] Quản lý Phim & Thể loại (CUD Movies & Categories)");
                    System.out.println("6. [ADMIN] Báo cáo Thống kê");
                }
                System.out.println("0. Đăng xuất");
                System.out.print("Chọn chức năng lớn: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        handleShowAllMovies(scanner, movieService, watchController, currentUser);
                        break;
                    case 2:
                        handleWatchlistManagement(scanner, movieService, watchService, currentUser);
                        break;
                    case 3:
                        handleFavoriteManagement(scanner, movieService, watchService, currentUser);
                        break;
                    case 4:
                        System.out.println("\n--- DANH SÁCH PHIM XEM GẦN ĐÂY ---");
                        List<String> recents = watchController.getRecentMovies();
                        if (recents.isEmpty()) {
                            System.out.println("Chưa có lịch sử xem gần đây.");
                        } else {
                            for (String mId : recents) {
                                Movie m = movieService.getMovieById(mId);
                                if (m != null) {
                                    System.out.println("- " + m.getTitle() + " (Năm: " + m.getReleaseYear() + ", Rating: " + m.getRating() + ")");
                                }
                            }
                        }
                        break;
                    case 5:
                        if (currentUser.getRole() == Role.ADMIN) {
                            handleAdminCRUD(scanner, movieService, categoryService, movieCategoryRepo);
                        }
                        break;
                    case 6:
                        if (currentUser.getRole() == Role.ADMIN) {
                            System.out.println("\n--- BÁO CÁO TOP PHIM ---");
                            System.out.println("Top 3 Phim Xem Nhiều Nhất:");
                            reportController.getTopViewed(3).forEach(m -> System.out.println(" + " + m.getTitle() + " - Views: " + m.getViews()));
                            System.out.println("Top 3 Phim Đánh Giá Cao Nhất:");
                            reportController.getTopRated(3).forEach(m -> System.out.println(" + " + m.getTitle() + " - Rating: " + m.getRating()));
                        }
                        break;
                    case 0:
                        currentUser = null;
                        System.out.println("Đã đăng xuất thành công.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }
        }
    }

    // 1. Show all movie & Search, Sort, Detail, Watch
    private static void handleShowAllMovies(Scanner scanner, MovieService movieService, WatchController watchController, User user) {
        while (true) {
            System.out.println("\n--- SHOW ALL MOVIES & EXPLORE ---");
            List<Movie> movies = movieService.getAllMovies();
            if (movies.isEmpty()) {
                System.out.println("Hiện chưa có phim nào trong hệ thống.");
                return;
            }
            
            System.out.printf("%-5s | %-25s | %-6s | %-10s | %-6s\n", "STT", "Tên Phim", "Năm", "Rating", "Views");
            System.out.println("----------------------------------------------------------------");
            for (int i = 0; i < movies.size(); i++) {
                Movie m = movies.get(i);
                System.out.printf("%-5d | %-25s | %-6d | %-10.1f | %-6d\n", (i + 1), m.getTitle(), m.getReleaseYear(), m.getRating(), m.getViews());
            }

            System.out.println("\nTùy chọn:");
            System.out.println("1. Xem chi tiết phim & Xem phim (Stream)");
            System.out.println("2. Tìm kiếm phim (Theo tiêu đề, đạo diễn, diễn viên, thể loại)");
            System.out.println("3. Sắp xếp danh sách phim");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn: ");
            int subChoice = Integer.parseInt(scanner.nextLine());

            if (subChoice == 1) {
                System.out.print("Nhập tên phim chính xác muốn xem chi tiết/phát: ");
                String title = scanner.nextLine();
                Movie m = movieService.getMovieByTitle(title);
                if (m != null) {
                    printMovieDetails(m);
                    System.out.print("Bạn có muốn xem (stream) phim này ngay không? (y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("y")) {
                        watchController.watchMovie(user.getUserId(), m.getMovieId());
                        System.out.println("▶ Đang phát phim: [" + m.getTitle() + "]. Thưởng thức nhé!");
                    }
                } else {
                    System.out.println("Không tìm thấy phim có tên này!");
                }
            } else if (subChoice == 2) {
                System.out.print("Nhập từ khóa tìm kiếm (Title, Actor, Director, Genre): ");
                String kw = scanner.nextLine();
                List<Movie> results = movieService.searchMovies(kw);
                if (results.isEmpty()) {
                    System.out.println("Không tìm thấy kết quả phù hợp.");
                } else {
                    System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
                    results.forEach(m -> System.out.println(" - " + m.getTitle() + " (Đạo diễn: " + m.getDirector() + ", Năm: " + m.getReleaseYear() + ")"));
                }
            } else if (subChoice == 3) {
                System.out.println("Sắp xếp theo: 1. TITLE, 2. RATING, 3. RELEASE_YEAR, 4. POPULARITY");
                int sIdx = Integer.parseInt(scanner.nextLine()) - 1;
                SortBy sortBy = SortBy.values()[Math.max(0, Math.min(sIdx, SortBy.values().length - 1))];
                
                System.out.println("Thứ tự: 1. ASC (Tăng dần), 2. DESC (Giảm dần)");
                int oIdx = Integer.parseInt(scanner.nextLine());
                OrderType orderType = (oIdx == 2) ? OrderType.DESC : OrderType.ASC;

                List<Movie> sortedList = movieService.sortMovies(sortBy, orderType);
                System.out.println("\n--- DANH SÁCH SAU KHI SẮP XẾP ---");
                sortedList.forEach(m -> System.out.println(" - " + m.getTitle() + " | Rating: " + m.getRating() + " | Năm: " + m.getReleaseYear() + " | Views: " + m.getViews()));
            } else if (subChoice == 0) {
                break;
            }
        }
    }

    private static void printMovieDetails(Movie m) {
        System.out.println("\n================ MOVIE DETAIL ================");
        System.out.println("Tiêu đề     : " + m.getTitle());
        System.out.println("Mô tả       : " + m.getDescription());
        System.out.println("Thời lượng  : " + m.getDuration() + " phút");
        System.out.println("Năm phát hành: " + m.getReleaseYear());
        System.out.println("Đánh giá    : " + m.getRating() + " / 10");
        System.out.println("Lượt xem    : " + m.getViews());
        System.out.println("Lượt thích  : " + m.getFavoriteCount());
        System.out.println("Đạo diễn    : " + m.getDirector());
        System.out.println("Diễn viên   : " + String.join(", ", m.getActors()));
        System.out.println("==============================================");
    }

    // 2. Watchlist Management (Cho phép xóa bất kỳ phim nào trong danh sách)
    private static void handleWatchlistManagement(Scanner scanner, MovieService movieService, WatchService watchService, User user) {
        while (true) {
            System.out.println("\n--- WATCHLIST MANAGEMENT ---");
            List<Movie> watchlistMovies = watchService.getWatchlistMovies(user.getUserId());
            if (watchlistMovies.isEmpty()) {
                System.out.println("Watchlist của bạn đang trống.");
            } else {
                System.out.println("Danh sách phim trong Watchlist:");
                for (int i = 0; i < watchlistMovies.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + watchlistMovies.get(i).getTitle());
                }
            }

            System.out.println("\n1. Thêm phim vào Watchlist");
            System.out.println("2. Xóa phim khỏi Watchlist");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.print("Nhập tên phim muốn thêm vào Watchlist: ");
                String title = scanner.nextLine();
                Movie m = movieService.getMovieByTitle(title);
                if (m != null) {
                    watchService.addToWatchlist(user.getUserId(), m.getMovieId());
                    System.out.println("Đã thêm vào Watchlist thành công!");
                } else {
                    System.out.println("Không tìm thấy phim này.");
                }
            } else if (choice == 2) {
                System.out.print("Nhập tên phim muốn xóa khỏi Watchlist: ");
                String title = scanner.nextLine();
                Movie m = movieService.getMovieByTitle(title);
                if (m != null) {
                    watchService.removeFromWatchlist(user.getUserId(), m.getMovieId());
                    System.out.println("Đã xóa phim khỏi Watchlist thành công!");
                } else {
                    System.out.println("Không tìm thấy phim này trong hệ thống.");
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    // 3. Favorite Movie Management (Xem danh sách trước, chọn bằng title để thêm/xóa)
    private static void handleFavoriteManagement(Scanner scanner, MovieService movieService, WatchService watchService, User user) {
        while (true) {
            System.out.println("\n--- FAVORITE MOVIE MANAGEMENT ---");
            List<Movie> favoriteMovies = watchService.getFavoriteMovies(user.getUserId());
            
            System.out.println("▶ Danh sách phim yêu thích hiện tại của bạn:");
            if (favoriteMovies.isEmpty()) {
                System.out.println("   (Chưa có bộ phim yêu thích nào)");
            } else {
                for (int i = 0; i < favoriteMovies.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + favoriteMovies.get(i).getTitle() + " (Rating: " + favoriteMovies.get(i).getRating() + ")");
                }
            }

            System.out.println("\nTùy chọn:");
            System.out.println("1. Thêm phim vào yêu thích (Nhập tên phim)");
            System.out.println("2. Xóa phim khỏi yêu thích (Nhập tên phim)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.print("Nhập tên bộ phim bạn muốn đánh dấu Yêu thích: ");
                String title = scanner.nextLine();
                Movie m = movieService.getMovieByTitle(title);
                if (m != null) {
                    watchService.addFavorite(user.getUserId(), m.getMovieId());
                    System.out.println("Đã thêm thành công vào danh sách yêu thích!");
                } else {
                    System.out.println("Không tìm thấy bộ phim có tiêu đề này.");
                }
            } else if (choice == 2) {
                System.out.print("Nhập tên bộ phim bạn muốn bỏ yêu thích: ");
                String title = scanner.nextLine();
                Movie m = movieService.getMovieByTitle(title);
                if (m != null) {
                    watchService.removeFavorite(user.getUserId(), m.getMovieId());
                    System.out.println("Đã xóa khỏi danh sách yêu thích!");
                } else {
                    System.out.println("Không tìm thấy bộ phim có tiêu đề này.");
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    // 5. Admin CRUD (Movie & Category)
    private static void handleAdminCRUD(Scanner scanner, MovieService movieService, CategoryService categoryService, MovieCategoryRepository movieCatRepo) {
        System.out.println("\n--- ADMIN CUD MANAGEMENT ---");
        System.out.println("1. Thêm Thể loại (Category)");
        System.out.println("2. Thêm Phim mới (Movie)");
        System.out.println("3. Xóa Phim");
        System.out.print("Chọn: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            System.out.print("Tên thể loại: "); String name = scanner.nextLine();
            System.out.print("Mô tả: "); String desc = scanner.nextLine();
            Category cat = new Category(IdGenerator.generateId("CAT"), name, desc);
            categoryService.addCategory(cat);
            System.out.println("Thêm thể loại thành công!");
        } else if (choice == 2) {
            System.out.print("Tiêu đề phim: "); String title = scanner.nextLine();
            System.out.print("Mô tả: "); String desc = scanner.nextLine();
            System.out.print("Thời lượng (phút): "); int duration = Integer.parseInt(scanner.nextLine());
            System.out.print("Năm phát hành: "); int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Đánh giá (0-10): "); double rating = Double.parseDouble(scanner.nextLine());
            System.out.print("Đạo diễn: "); String director = scanner.nextLine();
            System.out.print("Diễn viên (cách nhau bởi dấu phẩy): "); 
            String actorsStr = scanner.nextLine();
            List<String> actors = Arrays.asList(actorsStr.split("\\s*,\\s*"));

            Movie movie = new Movie(IdGenerator.generateId("MOV"), title, desc, duration, year, rating, director, actors);
            
            // Chọn Category
            System.out.println("Các thể loại hiện có:");
            List<Category> cats = categoryService.getAllCategories();
            cats.forEach(c -> System.out.println("ID: " + c.getCategoryId() + " - Tên: " + c.getName()));
            System.out.print("Nhập các Category ID liên kết (cách nhau bởi dấu phẩy): ");
            String[] catIds = scanner.nextLine().split("\\s*,\\s*");
            
            movieService.addMovie(movie, Arrays.asList(catIds));
            System.out.println("Thêm phim mới thành công!");
        } else if (choice == 3) {
            System.out.print("Nhập Movie ID cần xóa: ");
            String mId = scanner.nextLine();
            if (movieService.deleteMovie(mId)) {
                System.out.println("Xóa phim thành công!");
            } else {
                System.out.println("Không tìm thấy phim cần xóa.");
            }
        }
    }
}