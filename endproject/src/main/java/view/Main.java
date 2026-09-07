package view;

import controller.*;
import enums.*;
import model.*;
import repository.*;
import service.*;
import utils.IdGenerator;

import java.io.File;
import java.util.*;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // 1. Repositories
        MovieRepository movieRepo = new MovieRepository();
        CategoryRepository categoryRepo = new CategoryRepository();
        MovieCategoryRepository movieCategoryRepo = new MovieCategoryRepository();
        UserRepository userRepo = new UserRepository();
        WatchlistRepository watchlistRepo = new WatchlistRepository();
        FavoriteRepository favoriteRepo = new FavoriteRepository();
        HistoryRepository historyRepo = new HistoryRepository();
        RecentWatchRepository recentWatchRepo = new RecentWatchRepository();

        // 2. Services
        AuthService authService = new AuthService(userRepo);
        CategoryService categoryService = new CategoryService(categoryRepo, movieCategoryRepo);
        UserService userService = new UserService(userRepo);
        WatchService watchService = new WatchService(watchlistRepo, favoriteRepo, historyRepo, movieRepo, recentWatchRepo);
        ReportService reportService = new ReportService(movieRepo);
        MovieService movieService = new MovieService(movieRepo, categoryRepo, movieCategoryRepo, watchService);

        // 3. Controllers
        AuthController authController = new AuthController(authService);
        MovieController movieController = new MovieController(movieService);
        CategoryController categoryController = new CategoryController(categoryService);
        UserController userController = new UserController(userService);
        WatchController watchController = new WatchController(watchService);
        ReportController reportController = new ReportController(reportService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println("   MOVIE STREAMING MANAGEMENT SYSTEM (v3.0 FULL) ");
        System.out.println("==================================================");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- MAIN MENU ---");
                System.out.println("1. Đăng ký tài khoản");
                System.out.println("2. Đăng nhập");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                
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
                        System.out.println("Đăng nhập thất bại!");
                    }
                } else if (choice == 0) {
                    System.out.println("Tạm biệt!");
                    break;
                }
            } else {
                System.out.println("\n================ MAIN DASHBOARD ================");
                System.out.println("1. Show all movie (Xem danh sách, Tìm kiếm, Lọc thể loại, Stream)");
                System.out.println("2. Watchlist management (Quản lý danh sách chờ)");
                System.out.println("3. Favorite movie management (Quản lý phim yêu thích)");
                System.out.println("4. Xem phim gần đây (Recent Watch Stack - Tự lưu trữ)");
                if (currentUser.getRole() == Role.ADMIN) {
                    System.out.println("5. [ADMIN] Quản lý Category (Full CRUD bằng Tên)");
                    System.out.println("6. [ADMIN] Quản lý Movie (Full CRUD bằng Title)");
                    System.out.println("7. [ADMIN] Báo cáo thống kê");
                }
                System.out.println("0. Đăng xuất");
                System.out.print("Chọn chức năng: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: handleShowAllMovies(scanner,movieService, movieController, categoryController, watchController, currentUser); break;
                    case 2: handleWatchlistManagement(scanner, movieController, watchController, currentUser); break;
                    case 3: handleFavoriteManagement(scanner, movieController, watchController, currentUser); break;
                    case 4:
                        System.out.println("\n--- PHIM XEM GẦN ĐÂY ---");
                        List<String> recents = watchController.getRecentMovies(currentUser.getUserId());
                        if (recents.isEmpty()) System.out.println("Chưa có lịch sử xem.");
                        else {
                            recents.forEach(mId -> {
                                Movie m = movieController.getAllMovies().stream().filter(x -> x.getMovieId().equals(mId)).findFirst().orElse(null);
                                if (m != null) System.out.println("- " + m.getTitle() + " (Đạo diễn: " + m.getDirector() + ")");
                            });
                        }
                        break;
                    case 5: if (currentUser.getRole() == Role.ADMIN) handleCategoryCRUD(scanner, categoryController); break;
                    case 6: if (currentUser.getRole() == Role.ADMIN) handleMovieCRUD(scanner, movieController, categoryController); break;
                    case 7:
                        if (currentUser.getRole() == Role.ADMIN) {
                            System.out.println("\n--- TOP PHIM XEM NHIỀU NHẤT ---");
                            reportController.getTopViewed(3).forEach(m -> System.out.println("+ " + m.getTitle() + " - Lượt xem: " + m.getViews()));
                        }
                        break;
                    case 0: currentUser = null; System.out.println("Đã đăng xuất."); break;
                }
            }
        }
    }

    private static void handleShowAllMovies(Scanner scanner, MovieService movieService, MovieController movieController, CategoryController categoryController, WatchController watchController, User user) {
        while (true) {       
            System.out.println("\n--- SHOW ALL MOVIES & EXPLORE ---");
            System.out.println("1. Xem danh sách toàn bộ phim");
            System.out.println("2. Tìm kiếm phim (Title, Actor, Director)");
            System.out.println("3. Lọc phim theo Thể loại (Genre)");
            System.out.println("4. Xem chi tiết & Stream phim (Có bấm giờ & Pause/Exit)");
            System.out.println("0. Quay lại Dashboard");
            System.out.print("Chọn: ");
            int sub = Integer.parseInt(scanner.nextLine());

            if (sub == 1) {
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
            } else if (sub == 2) {
                System.out.print("Nhập từ khóa tìm kiếm: ");
                movieController.search(scanner.nextLine()).forEach(m -> System.out.println(" - " + m.getTitle() + " (Đạo diễn: " + m.getDirector() + ")"));
            } else if (sub == 3) {
                System.out.println("Danh sách Thể loại:");
                categoryController.getCategories().forEach(c -> System.out.println(" + " + c.getName()));
                System.out.print("Nhập TÊN thể loại muốn lọc: ");
                movieController.filterByCategory(scanner.nextLine()).forEach(m -> System.out.println(" - " + m.getTitle()));
            } else if (sub == 4) {
                System.out.print("Nhập TÊN PHIM chính xác: ");
                Movie m = movieController.getMovieByTitle(scanner.nextLine());
                if (m != null) {
                    printMovieDetails(m);
                    System.out.print("Bạn có muốn phát phim này ngay bây giờ? (y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("y")) {
                        simulateStreaming(scanner, watchController, user.getUserId(), m);
                    }
                } else System.out.println("Không tìm thấy phim!");
            } else if (sub == 0) break;
        }
    }

    private static void simulateStreaming(Scanner scanner, WatchController watchController, String userId, Movie movie) {
        watchController.startWatching(userId, movie.getMovieId());
        System.out.println("\n▶ ĐANG PHÁT PHIM: [" + movie.getTitle() + "]");
        System.out.println("-------------------------------------------------");
        System.out.println("[Hướng dẫn]: Nhấn phím ENTER để TẠM DỪNG / TIẾP TỤC.");
        System.out.println("            Nhập phím 'e' rồi ENTER để THOÁT PHIM.");
        System.out.println("-------------------------------------------------");

        long totalWatchedSeconds = 0;
        long lastResumeTime = System.currentTimeMillis();
        boolean isPlaying = true;

        while (true) {
            String input = scanner.nextLine();
            long now = System.currentTimeMillis();

            if (input.equalsIgnoreCase("e")) {
                if (isPlaying) totalWatchedSeconds += (now - lastResumeTime) / 1000;
                break;
            } else {
                if (isPlaying) {
                    totalWatchedSeconds += (now - lastResumeTime) / 1000;
                    isPlaying = false;
                    System.out.println("⏸ Phim đã TẠM DỪNG. Tổng thời gian bạn đã xem: " + totalWatchedSeconds + " giây.");
                    System.out.println(" (Nhấn ENTER để tiếp tục, nhập 'e' để thoát)");
                } else {
                    isPlaying = true;
                    lastResumeTime = System.currentTimeMillis();
                    System.out.println("▶TIẾP TỤC");
                }
            }
        }
        System.out.println("⏹ Bạn đã thoát trình phát phim. Tổng thời gian đã xem: " + totalWatchedSeconds + " giây.");
    }

    private static void handleWatchlistManagement(Scanner scanner, MovieController movieController, WatchController watchController, User user) {
    while (true) {
        System.out.println("\n--- WATCHLIST MANAGEMENT ---");
        List<Movie> list = watchController.getWatchlistMovies(user.getUserId());
        System.out.println("Danh sách chờ xem hiện tại:");
        if (list.isEmpty()) System.out.println(" (Trống)");
        else list.forEach(m -> System.out.println(" - " + m.getTitle()));

        System.out.println("\n1. Thêm phim vào Watchlist (Nhập Title)");
        System.out.println("2. Xóa phim khỏi Watchlist (Nhập Title)");
        System.out.println("0. Quay lại");
        System.out.print("Chọn: ");
        int c = Integer.parseInt(scanner.nextLine());

        if (c == 1) {
            System.out.print("Nhập tên phim: ");
            Movie m = movieController.getMovieByTitle(scanner.nextLine());
            if (m != null) {
                System.out.println(watchController.addToWatchlist(user.getUserId(), m.getMovieId()));
            } else System.out.println("LỖI: Phim không tồn tại trong hệ thống!");
        } else if (c == 2) {
            System.out.print("Nhập tên phim cần xóa: ");
            Movie m = movieController.getMovieByTitle(scanner.nextLine());
            if (m != null) {
                System.out.println(watchController.removeFromWatchlist(user.getUserId(), m.getMovieId()));
            } else System.out.println("LỖI: Phim không tồn tại trong hệ thống!");
        } else if (c == 0) break;
    }
}

    private static void handleFavoriteManagement(Scanner scanner, MovieController movieController, WatchController watchController, User user) {
    while (true) {
        System.out.println("\n--- FAVORITE MOVIE MANAGEMENT ---");
        List<Movie> favs = watchController.getFavoriteMovies(user.getUserId());
        System.out.println("▶ Danh sách phim yêu thích hiện tại:");
        if (favs.isEmpty()) System.out.println(" (Trống)");
        else favs.forEach(m -> System.out.println(" - " + m.getTitle() + " | Rating: " + m.getRating()));

        System.out.println("\n1. Đánh dấu Yêu thích (Nhập Title)");
        System.out.println("2. Bỏ Yêu thích (Nhập Title)");
        System.out.println("0. Quay lại");
        System.out.print("Chọn: ");
        int c = Integer.parseInt(scanner.nextLine());

        if (c == 1) {
            System.out.print("Nhập tên phim: ");
            Movie m = movieController.getMovieByTitle(scanner.nextLine());
            if (m != null) {
                System.out.println(watchController.addFavorite(user.getUserId(), m.getMovieId()));
            } else System.out.println("LỖI: Phim không tồn tại trong hệ thống!");
        } else if (c == 2) {
            System.out.print("Nhập tên phim: ");
            Movie m = movieController.getMovieByTitle(scanner.nextLine());
            if (m != null) {
                System.out.println(watchController.removeFavorite(user.getUserId(), m.getMovieId()));
            } else System.out.println("LỖI: Phim không tồn tại trong hệ thống!");
        } else if (c == 0) break;
    }
}

    private static void handleCategoryCRUD(Scanner scanner, CategoryController categoryController) {
        while (true) {
            System.out.println("\n--- [ADMIN] QUẢN LÝ CATEGORY ---");
            System.out.println("1. Danh sách Category");
            System.out.println("2. Thêm Category mới");
            System.out.println("3. Sửa Category (Bằng Tên)");
            System.out.println("4. Xóa Category (Có kiểm tra Validate)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int c = Integer.parseInt(scanner.nextLine());

            if (c == 1) {
                categoryController.getCategories().forEach(cat -> System.out.println(" - " + cat.getName() + ": " + cat.getDescription()));
            } else if (c == 2) {
                System.out.print("Tên Category: "); String n = scanner.nextLine();
                System.out.print("Mô tả: "); String d = scanner.nextLine();
                if (categoryController.addCategory(new Category(IdGenerator.generateId("CAT"), n, d))) System.out.println("Thêm thành công!");
                else System.out.println("Tên Category đã tồn tại!");
            } else if (c == 3) {
                System.out.print("Tên Category CŨ: "); String oldN = scanner.nextLine();
                System.out.print("Tên Category MỚI: "); String newN = scanner.nextLine();
                System.out.print("Mô tả MỚI: "); String d = scanner.nextLine();
                if (categoryController.updateCategory(oldN, newN, d)) System.out.println("Sửa thành công!");
                else System.out.println("Không tìm thấy Category!");
            } else if (c == 4) {
                System.out.print("Tên Category cần XÓA: ");
                System.out.println(categoryController.deleteCategory(scanner.nextLine()));
            } else if (c == 0) break;
        }
    }

    private static void handleMovieCRUD(Scanner scanner, MovieController movieController, CategoryController categoryController) {
        while (true) {
            System.out.println("\n--- [ADMIN] QUẢN LÝ MOVIE ---");
            System.out.println("1. Thêm Phim mới");
            System.out.println("2. Sửa Phim");
            System.out.println("3. Xóa Phi");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int c = Integer.parseInt(scanner.nextLine());

            if (c == 1) {
                System.out.print("Tên phim: "); String t = scanner.nextLine();
                System.out.print("Mô tả: "); String d = scanner.nextLine();
                System.out.print("Thời lượng (phút): "); int dur = Integer.parseInt(scanner.nextLine());
                System.out.print("Năm phát hành: "); int yr = Integer.parseInt(scanner.nextLine());
                System.out.print("Rating (0-10): "); double rt = Double.parseDouble(scanner.nextLine());
                System.out.print("Đạo diễn: "); String dir = scanner.nextLine();
                System.out.print("Diễn viên (cách nhau dấu phẩy): "); String act = scanner.nextLine();

                System.out.print("Thể loại gắn kèm (cách nhau dấu phẩy): "); String cats = scanner.nextLine();
                List<String> catNames = Arrays.asList(cats.split("\\s*,\\s*"));

                Movie m = new Movie(IdGenerator.generateId("MOV"), t, d, dur, yr, rt, dir, Arrays.asList(act.split("\\s*,\\s*")));
                movieController.addMovie(m, catNames);
                System.out.println("Thêm phim thành công!");
            } else if (c == 2) {
                System.out.print("Nhập TÊN PHIM CŨ cần sửa: "); String oldT = scanner.nextLine();
                System.out.print("Tên phim MỚI: "); String newT = scanner.nextLine();
                System.out.print("Mô tả MỚI: "); String d = scanner.nextLine();
                System.out.print("Thời lượng MỚI: "); int dur = Integer.parseInt(scanner.nextLine());
                System.out.print("Năm phát hành MỚI: "); int yr = Integer.parseInt(scanner.nextLine());
                System.out.print("Đạo diễn MỚI: "); String dir = scanner.nextLine();
                System.out.print("Diễn viên MỚI: "); String act = scanner.nextLine();

                if (movieController.updateMovie(oldT, newT, d, dur, yr, dir, Arrays.asList(act.split("\\s*,\\s*")))) {
                    System.out.println("Cập nhật phim thành công!");
                } else System.out.println("Không tìm thấy phim!");
            } else if (c == 3) {
                System.out.print("Nhập TÊN PHIM cần xóa: ");
                System.out.println(movieController.deleteMovieByTitle(scanner.nextLine()));
            } else if (c == 0) break;
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
}