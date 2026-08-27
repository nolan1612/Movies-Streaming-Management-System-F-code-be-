/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.endproject;

import controller.*;
import enums.*;
import model.*;
import repository.*;
import service.*;
import utils.IdGenerator;

import java.io.File;
import java.util.*;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class Endproject {
private static User currentUser = null;

    public static void main(String[] args) {
        // Tạo thư mục data nếu chưa tồn tại
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // Khởi tạo Layer Repositories
        MovieRepository movieRepo = new MovieRepository();
        CategoryRepository categoryRepo = new CategoryRepository();
        UserRepository userRepo = new UserRepository();
        WatchlistRepository watchlistRepo = new WatchlistRepository();
        FavoriteRepository favoriteRepo = new FavoriteRepository();
        HistoryRepository historyRepo = new HistoryRepository();

        // Khởi tạo Layer Services
        AuthService authService = new AuthService(userRepo);
        MovieService movieService = new MovieService(movieRepo);
        CategoryService categoryService = new CategoryService(categoryRepo);
        UserService userService = new UserService(userRepo);
        WatchService watchService = new WatchService(watchlistRepo, favoriteRepo, historyRepo, movieRepo);
        ReportService reportService = new ReportService(movieRepo);

        // Khởi tạo Layer Controllers
        AuthController authController = new AuthController(authService);
        MovieController movieController = new MovieController(movieService);
        CategoryController categoryController = new CategoryController(categoryService);
        UserController userController = new UserController(userService);
        WatchController watchController = new WatchController(watchService);
        ReportController reportController = new ReportController(reportService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("================================================");
        System.out.println("  WELCOME TO MOVIE STREAMING MANAGEMENT SYSTEM ");
        System.out.println("================================================");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- MAIN MENU ---");
                System.out.println("1. Đăng ký");
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
                        System.out.println("Đăng nhập thành công! Xin chào, " + currentUser.getFullName());
                    } else {
                        System.out.println("Thất bại! Sai username hoặc password.");
                    }
                } else if (choice == 0) {
                    System.out.println("Tạm biệt!");
                    break;
                }
            } else {
                System.out.println("\n--- DASHBOARD (" + currentUser.getRole() + ") ---");
                System.out.println("1. Danh sách phim");
                System.out.println("2. Tìm kiếm phim theo tên");
                System.out.println("3. Sắp xếp phim");
                System.out.println("4. Xem phim (Stream)");
                System.out.println("5. Thêm/Xóa Phim yêu thích");
                System.out.println("6. Xem phim gần đây (Stack Recent Watch)");
                if (currentUser.getRole() == Role.ADMIN) {
                    System.out.println("7. [ADMIN] Thêm phim mới");
                    System.out.println("8. [ADMIN] Báo cáo Top Phim");
                }
                System.out.println("0. Đăng xuất");
                System.out.print("Chọn chức năng: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        List<Movie> list = movieController.getAllMovies();
                        if (list.isEmpty()) System.out.println("Chưa có phim nào.");
                        for (Movie m : list) {
                            System.out.println("[" + m.getMovieId() + "] " + m.getTitle() + " | Rating: " + m.getRating() + " | Views: " + m.getViews());
                        }
                        break;
                    case 2:
                        System.out.print("Nhập từ khóa tìm kiếm: ");
                        String kw = scanner.nextLine();
                        List<Movie> searchRes = movieController.search(kw);
                        searchRes.forEach(m -> System.out.println("[" + m.getMovieId() + "] " + m.getTitle()));
                        break;
                    case 3:
                        System.out.println("Sắp xếp theo: 1. TITLE, 2. RATING, 3. RELEASE_YEAR, 4. POPULARITY");
                        int sChoice = Integer.parseInt(scanner.nextLine());
                        SortBy sortBy = SortBy.values()[sChoice - 1];
                        System.out.println("Thứ tự: 1. ASC, 2. DESC");
                        int oChoice = Integer.parseInt(scanner.nextLine());
                        OrderType orderType = (oChoice == 1) ? OrderType.ASC : OrderType.DESC;
                        
                        List<Movie> sorted = movieController.sort(sortBy, orderType);
                        sorted.forEach(m -> System.out.println("[" + m.getMovieId() + "] " + m.getTitle() + " - " + m.getRating()));
                        break;
                    case 4:
                        System.out.print("Nhập Movie ID muốn xem: ");
                        String mId = scanner.nextLine();
                        watchController.watchMovie(currentUser.getUserId(), mId);
                        System.out.println("Đang phát phim " + mId + "...");
                        break;
                    case 5:
                        System.out.print("Nhập Movie ID: ");
                        String favId = scanner.nextLine();
                        watchController.toggleFavorite(currentUser.getUserId(), favId);
                        System.out.println("Thao tác thành công!");
                        break;
                    case 6:
                        System.out.println("Danh sách phim xem gần đây (Tối đa 5 phim gần nhất):");
                        List<String> recents = watchController.getRecentMovies();
                        recents.forEach(id -> System.out.println("-> Movie ID: " + id));
                        break;
                    case 7:
                        if (currentUser.getRole() == Role.ADMIN) {
                            System.out.print("Tên phim: "); String t = scanner.nextLine();
                            System.out.print("Mô tả: "); String d = scanner.nextLine();
                            System.out.print("Thời lượng (phút): "); int dur = Integer.parseInt(scanner.nextLine());
                            System.out.print("Năm phát hành: "); int yr = Integer.parseInt(scanner.nextLine());
                            System.out.print("Rating (0-10): "); double rt = Double.parseDouble(scanner.nextLine());
                            System.out.print("Đạo diễn: "); String dir = scanner.nextLine();
                            
                            Movie m = new Movie(IdGenerator.generateId("MOV"), t, d, dur, yr, rt, dir, new ArrayList<>());
                            movieController.addMovie(m);
                            System.out.println("Thêm phim thành công!");
                        }
                        break;
                    case 8:
                        if (currentUser.getRole() == Role.ADMIN) {
                            System.out.println("--- TOP 3 VIEWS ---");
                            reportController.getTopViewed(3).forEach(m -> System.out.println(m.getTitle() + " - Views: " + m.getViews()));
                        }
                        break;
                    case 0:
                        currentUser = null;
                        System.out.println("Đã đăng xuất.");
                        break;
                }
            }
        }
    }
}