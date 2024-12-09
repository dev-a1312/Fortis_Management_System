package com.resume_project;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewPatients")
public class PatientServlet extends HttpServlet {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "tommy";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM patients";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a list to store patients
            List<PatientData> patients = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                patients.add(new PatientData(id, name, age, gender));
            }

            // Convert the list to JSON using Gson
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(patients);

            // Send the JSON response
            out.print(jsonResponse);
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Data transfer object to represent patient data
    private static class PatientData {
        private int id;
        private String name;
        private int age;
        private String gender;

        public PatientData(int id, String name, int age, String gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
    }
}
