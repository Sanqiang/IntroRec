/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.introrec1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanqiangzhao
 */
public class Matrix {

    public static final String MoviesFile = "recsys-data-movie-titles.csv", UsersFile = "recsys-data-users.csv", RattingFile = "recsys-data-ratings.csv", csvspliter = ",";
    public HashMap<Long, String> Movies = new HashMap<Long, String>(), Users = new HashMap<Long, String>();
    //public HashMap<String, Double> Ratting = new HashMap<String, Double>();
    public HashMap<Long, HashSet<Long>> MovieByUser = new HashMap<Long, HashSet<Long>>();

    public Matrix() {
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(MoviesFile));

            while ((line = br.readLine()) != null) {
                String[] items = line.split(csvspliter);
                Movies.put(Long.parseLong(items[0]), items[1]);
            }
            br = new BufferedReader(new FileReader(UsersFile));
            while ((line = br.readLine()) != null) {
                String[] items = line.split(csvspliter);
                Users.put(Long.parseLong(items[0]), items[1]);
            }
            br = new BufferedReader(new FileReader(RattingFile));
            while ((line = br.readLine()) != null) {
                String[] items = line.split(csvspliter);
                //Ratting.put(items[0] + " " + items[1], Double.parseDouble(items[2]));
                long userNum = Long.parseLong(items[0]), movieId = Long.parseLong(items[1]);
                if (!MovieByUser.containsKey(userNum)) {
                    MovieByUser.put(userNum, new HashSet<Long>());
                }
                MovieByUser.get(userNum).add(movieId);
            }
        } catch (Exception ex) {
            Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws Exception {
        String line = "";
        HashMap<Long, Integer> report = new HashMap<Long, Integer>();
        BufferedReader br = new BufferedReader(new FileReader(RattingFile));
        while ((line = br.readLine()) != null) {
            String[] items = line.split(csvspliter);
            long userNum = Long.parseLong(items[0]), movieId = Long.parseLong(items[1]);
            if (!report.containsKey(userNum)) {
                report.put(userNum, 0);
            }
            if (movieId == 120 || movieId == 121) {
                report.put(userNum, report.get(userNum) + 1);
            }

        }
        for (long uid : report.keySet()) {
            if (report.get(uid) >= 2) {
                System.out.println(uid);
                return;
            }
        }
    }
}
