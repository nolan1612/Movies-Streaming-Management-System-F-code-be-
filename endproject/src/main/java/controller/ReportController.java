/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Movie;
import service.ReportService;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class ReportController {
   private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public List<Movie> getTopViewed(int limit) { return reportService.getTopViewedMovies(limit); }
    public List<Movie> getTopRated(int limit) { return reportService.getTopRatedMovies(limit); }
}