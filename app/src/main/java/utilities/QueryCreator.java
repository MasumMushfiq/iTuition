package utilities;

import android.util.Log;

/**
 * Created by mushfiq on 1/2/18.
 */
public class QueryCreator {

    public static String createQuery(String[] subjects, String[] locations, String[] academic_levels,
                                     String[] departments, String[] institutes, String[] gender, int no_of_students, int salary) {
        String TAG = "Mushfiq_QUERY";
        Log.d(TAG, "No of students " + no_of_students + " Salary " + salary);
        int brackets = 0, flag = 0;
        StringBuilder query = new StringBuilder("SELECT DISTINCT Username FROM Users");
        if (subjects.length != 0) {
            StringBuilder tempQuery = new StringBuilder();
            for (int i = subjects.length - 1; i >= 0; i--) {
                if (i == 0 && flag == 0) {
                    tempQuery.insert(0, " WHERE Username in (\nSELECT DISTINCT Username FROM Subjects" +
                            " WHERE Subject = '" + subjects[i] + "'");
                    brackets++;
                    flag = 1;
                } else {
                    tempQuery.insert(0, " AND Username in (\nSELECT DISTINCT Username FROM Subjects" +
                            " WHERE Subject = '" + subjects[i] + "'");
                    brackets++;
                }

            }
            query.append(tempQuery);
        }

        if (locations.length != 0) {
            if (flag == 0) {
                query.append(" WHERE Username in (\n");
                flag = 1;
            } else {
                query.append(" AND Username in (\n");
            }
            brackets++;
            query.append("SELECT DISTINCT Username FROM Locations WHERE Location IN ('").append(locations[0]).append("'");
            for (int i = 1; i < locations.length; i++) {
                query.append(", '").append(locations[i]).append("'");
            }
            query.append(")");
        }

        if (academic_levels.length != 0) {
            if (flag == 0) {
                query.append(" WHERE Username in (\n");
                flag = 1;
            } else {
                query.append(" AND Username in (\n");
            }
            brackets++;
            query.append("SELECT DISTINCT Username FROM Academic_Levels WHERE Academic_Level IN ('").append(academic_levels[0]).append("'");
            for (int i = 1; i < academic_levels.length; i++) {
                query.append(", '").append(academic_levels[i]).append("'");
            }
            query.append(")");
        }


        if (departments.length != 0) {
            if (flag == 0) {
                query.append(" WHERE Username in (\n");
                flag = 1;
            } else {
                query.append(" AND Username in (\n");
            }
            brackets++;
            query.append("SELECT DISTINCT Username FROM Users WHERE Department IN ('").append(departments[0]).append("'");
            for (int i = 1; i < departments.length; i++) {
                query.append(", '").append(departments[i]).append("'");
            }
            query.append(")");
        }

        if (institutes.length != 0) {
            if (flag == 0) {
                query.append(" WHERE Username in (\n");
                flag = 1;
            } else {
                query.append(" AND Username in (\n");
            }
            brackets++;
            query.append("SELECT DISTINCT Username FROM Users WHERE Institute IN ('").append(institutes[0]).append("'");
            for (int i = 1; i < institutes.length; i++) {
                query.append(", '").append(institutes[i]).append("'");
            }
            query.append(")");
        }

        for (int i = 0; i < brackets; i++) {
            query.append(")");
        }
        if (gender.length == 1) {
            if (flag == 1) {
                query.append(" AND Gender = '").append(gender[0]).append("'");
            } else {
                query.append(" WHERE Gender = '").append(gender[0]).append("'");
                flag = 1;
            }
        }
        if (no_of_students != -1) {
            if (no_of_students > 4)
                no_of_students = 4;
            if (flag == 1) {
                query.append(" AND Salary_").append(no_of_students).append(" <= ").append(salary);
            } else {
                query.append(" WHERE Salary_").append(no_of_students).append(" <= ").append(salary);
                //flag = 1;
            }
        }
        query.append("\n ORDER BY Rating DESC;");
        return query.toString();
    }

    /*public static void main(String[] args) {
        QueryCreator queryCreator = new QueryCreator();
        String[] subjects = {"Math", "Physics"};
        String[] locations = {"Azimpur"};
        String[] academic_levels = {};
        String[] departments = {"CSE"};
        String[] institutes = {"BUET"};
        String[] gender = {"Male"};
        int no_of_students = 1, salary = 800;
        String string = queryCreator.createQuery(subjects, locations, academic_levels, departments, institutes,
                gender, no_of_students, salary);
        System.out.println(string);

    }*/
}

