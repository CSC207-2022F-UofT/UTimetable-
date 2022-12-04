package database;
import entities.*;

import java.io.*;
//import java.text.ParseException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

//import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import com.google.gson.*;
public class Database {

    public static void main(String[] args) {
        getDatabase();
    }
    private static final ArrayList<User> db = new ArrayList<User>();

    public static void setDatabase(ArrayList<User> users){
        File directory = new File("./" + "src\\main\\java\\database\\UsersDataBase.json");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(directory.getAbsolutePath());
            writer.write(gson.toJson(users));
            writer.flush();
            writer.close();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public static ArrayList<User> getDatabase() {
        JSONParser parser = new JSONParser();
        ArrayList<User> returnUsers = new ArrayList<>();
        try (Reader reader = new FileReader("src/main/java/database/UsersDataBase.json")) {
            JSONArray listOfUsers = (JSONArray) parser.parse(reader);
            for (Object o: listOfUsers) {
                JSONObject user = (JSONObject) o;

                // string information
                String username = (String) user.get("username");
                String password = (String) user.get("password");
                String email = (String) user.get("email");

                // empty friendsList
                ArrayList<User> friends = new ArrayList<>();
                ArrayList<User> blocked = new ArrayList<>();

                // location Object
                JSONObject locationObject = (JSONObject) user.get("location");
                String locationName = (String) locationObject.get("name");
                Location location = new Location(locationName);

                // timetable Object
                ArrayList<Course> courses = new ArrayList<>();
                JSONArray timetableArray = (JSONArray) user.get("timetable");
                for (Object course : timetableArray) {
                    JSONObject courseObject = (JSONObject) course;

                    // courseCode
                    String courseCode = (String) courseObject.get("courseCode");
                    // sectionCode
                    String sectionCode = (String) courseObject.get("section");
                    // lecture
                    ArrayList<Lecture> lectures = new ArrayList<>();
                    JSONArray lectureArray = (JSONArray) courseObject.get("lectures");
                    for (Object lectureObject: lectureArray) {
                        JSONObject lecture = (JSONObject) lectureObject;

                        // location
                        String locationString = (String) lecture.get("location");
                        Location locationOfLecture = new Location(locationString);

                        // interval
                        JSONObject intervalObject = (JSONObject) lecture.get("interval");
                        Long start = (Long) intervalObject.get("start");
                        int s = start.intValue();
                        Long end = (Long) intervalObject.get("end");
                        int e = end.intValue();
                        Interval interval = new Interval(s, e);

                        Lecture newLecture = new Lecture(interval, locationOfLecture);
                        lectures.add(newLecture);
                    }
                    Course newCourse = new Course(courseCode, sectionCode, lectures);
                    courses.add(newCourse);
                }
                Timetable timetable = new Timetable(courses);
                User newUser = new User(username, password, email, friends, blocked, location, timetable);
                returnUsers.add(newUser);

                // add friends and blocked
                for (Object m: listOfUsers) {
                    JSONObject userObject = (JSONObject) m;
                    ArrayList<User> listOfFriends = new ArrayList<>();
                    ArrayList<User> listOfBlocked = new ArrayList<>();

                }
            }

            for (User u : returnUsers) {
                System.out.println("users: " + u.getUsername());
                System.out.println("password: " + u.getPassword());
                System.out.println("email: " + u.getEmail());
                System.out.println("location: " + u.getLocation());
                System.out.println("friends: " + u.getFriends());
                System.out.println("blocked: " + u.getBlocked());
                System.out.println("timetable: " + u.getTimetable());
                System.out.println("\n");

            }

            return db;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return db;
    }

//    public static void addUser(String username, String password, ArrayList<User> friends, ArrayList<User> blocked) {
//        db.add(new User(username, password, friends, blocked));
//    }

    public static void addUser(String username, String password, ArrayList<User> friends, ArrayList<User> blocked) {
        db.add(new User(username, password, friends, blocked));
    }
    public static User getUser(String username) {
        System.out.println(username);
        for (User user : db) {
            System.out.println(user);
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

    public static ArrayList<Timetable> getDummyTimetablesTemp() {
        ArrayList<Lecture> dummyLectures1 = new ArrayList<>();

        dummyLectures1.add(
                new Lecture(new Interval(2160, 2220), new Location("BA 2222"))
        );
        dummyLectures1.add(
                new Lecture(new Interval(6660, 6720), new Location("BA 2222"))
        );

        ArrayList<Lecture> dummyLectures2 = new ArrayList<>();

        dummyLectures2.add(
                new Lecture(new Interval(2160, 2220), new Location("BA 2222"))
        );
        dummyLectures2.add(
                new Lecture(new Interval(3720, 3840), new Location("BA 2222"))
        );


        ArrayList<Course> dummyCourses1 = new ArrayList<>();

        dummyCourses1.add(
                new Course("KIM101", "LEC 0101", dummyLectures1)
        );
        dummyCourses1.add(
                new Course("JJJ265", "LEC 0101", dummyLectures2)
        );

        ArrayList<Course> dummyCourses2 = new ArrayList<>();

        dummyCourses2.add(
                new Course("KIM101", "LEC 0101", dummyLectures1)
        );
        dummyCourses2.add(
                new Course("JJJ265", "LEC 0101", dummyLectures2)
        );

        Timetable dummyTimetable1 = new Timetable(dummyCourses1);
        Timetable dummyTimetable2 = new Timetable(dummyCourses2);

        ArrayList<Timetable> timetables = new ArrayList<>();
        timetables.add(dummyTimetable1);
        timetables.add(dummyTimetable2);

        return timetables;
    }

}