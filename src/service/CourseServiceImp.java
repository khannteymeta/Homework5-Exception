package service;
import exception.CourseNotFound;
import exception.NumValidate;
import exception.StringValidate;
import model.Course;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import repository.CourseRepo;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import repository.CourseRepo;

public class CourseServiceImp implements CourseService {
    Scanner sc = new Scanner(System.in);
    private void validateString(String input) throws StringValidate {
        if (input.matches(".*\\d.*")) {
            throw new StringValidate(" Invalid input. Numbers are not allowed.");
        }
    }
    @Override
    public void addNewCourse() {
        String title = "";
        String[] instructorNames;
        String[] requirements;
        while (true) {
            try {
                System.out.print("-> Enter Course Title: ");
                title = sc.nextLine();
                validateString(title);
                break;
            } catch (StringValidate e) {
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            try {
                System.out.print("-> Enter Instructors : ");
                instructorNames = sc.nextLine().split(",");
                for (String instructorName : instructorNames) {
                    validateString(instructorName.trim());
                }
                break;
            } catch (StringValidate e) {
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            try {
                System.out.print("-> Enter Requirements : ");
                requirements = sc.nextLine().split(",");
                for (String requirement : requirements) {
                    validateString(requirement.trim());
                }
                break;
            } catch (StringValidate e) {
                System.out.println(e.getMessage());
            }
        }

        int id = CourseRepo.generateUniqueId();
        Date startDate = new Date();
        Course newCourse = new Course(id, title, instructorNames, requirements, startDate);
        CourseRepo.addNewCourse(newCourse);
        System.out.println("New course added successfully!");
    }

    @Override
    public void listCourse() {
        List<Course> courses = CourseRepo.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            System.out.println("Display All Data: ");

            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
            table.addCell("ID", cellStyle);
            table.addCell("Title", cellStyle);
            table.addCell("Instructors", cellStyle);
            table.addCell("Requirements", cellStyle);
            table.addCell("Start Date", cellStyle);
            for (Course course : courses) {
                table.addCell(course.getId().toString(), cellStyle);
                table.addCell(course.getTitle(), cellStyle);
                table.addCell(String.join(",", course.getInstructorName()), cellStyle);
                table.addCell(String.join(",", course.getRequirement()), cellStyle);
                table.addCell(course.getStartDate().toString(), cellStyle);
            }
            System.out.println(table.render());
        }
    }
    private int validateId(String input) throws NumValidate {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumValidate("Invalid input. Please enter a valid course ID.");
        }
    }
    @Override
    public void findById() throws NumValidate {
        if (CourseRepo.getAllCourses().isEmpty()) {
            System.out.println("No courses available to search.");
            return;
        }
        System.out.print("[+] Enter Course ID to find: ");
        Integer courseId = validateId(sc.nextLine());

        Optional<Course> foundCourse = CourseRepo.getCourseById(courseId);

        if (foundCourse.isPresent()) {
            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);

            table.addCell("ID", cellStyle);
            table.addCell("Title", cellStyle);
            table.addCell("Instructors", cellStyle);
            table.addCell("Requirements", cellStyle);
            table.addCell("Start Date", cellStyle);

            Course course = foundCourse.get();
            table.addCell(course.getId().toString(), cellStyle);
            table.addCell(course.getTitle(), cellStyle);
            table.addCell(String.join(",", course.getInstructorName()), cellStyle);
            table.addCell(String.join(",", course.getRequirement()), cellStyle);
            table.addCell(course.getStartDate().toString(), cellStyle);

            System.out.println("Course Found:");
            System.out.println(table.render());
        } else {
            System.out.println("Not found course with ID " + courseId);
        }
    }
    private String validateTitle(String input) throws StringValidate {
        if (!input.matches("[a-zA-Z\\s]+")) {
            System.out.println("+" + "~".repeat(117) + "+");
            throw new StringValidate("Invalid input. Numbers are not allowed.");
        }
        return input;
    }
    @Override
    public void findByTitle() {
        try {
            if (CourseRepo.getAllCourses().isEmpty()) {
                System.out.println("No courses available to search." );
                return;
            }
            System.out.print("-> Enter Course Title to find: ");
            String titleToFind = validateTitle(sc.nextLine());

            List<Course> foundCourses = CourseRepo.getCoursesByTitle(titleToFind);

            if (!foundCourses.isEmpty()) {
                System.out.println("Courses Found:");

                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
                CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);

                table.addCell("ID", cellStyle);
                table.addCell("Title", cellStyle);
                table.addCell("Instructors", cellStyle);
                table.addCell("Requirements", cellStyle);
                table.addCell("Start Date", cellStyle);

                for (Course course : foundCourses) {
                    table.addCell(course.getId().toString(), cellStyle);
                    table.addCell(course.getTitle(), cellStyle);
                    table.addCell(String.join(",", course.getInstructorName()), cellStyle);
                    table.addCell(String.join(",", course.getRequirement()), cellStyle);
                    table.addCell(course.getStartDate().toString(), cellStyle);
                }

                System.out.println(table.render());
            } else {
                System.out.println("No courses found with the title: " + titleToFind);
            }
        } catch (StringValidate e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeCourse() {
        try {
            if (CourseRepo.getAllCourses().isEmpty()) {
                System.out.println("No courses available to delete.");
                return;
            }
            System.out.print("[+] Enter Course ID to remove: ");
            Integer courseId = validateId(sc.nextLine());
            Optional<Course> courseOptional = CourseRepo.getCourseById(courseId);

            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();

                System.out.println("Course to be deleted:");
                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
                CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);

                table.addCell("ID", cellStyle);
                table.addCell("Title", cellStyle);
                table.addCell("Instructors", cellStyle);
                table.addCell("Requirements", cellStyle);
                table.addCell("Start Date", cellStyle);

                table.addCell(course.getId().toString(), cellStyle);
                table.addCell(course.getTitle(), cellStyle);
                table.addCell(String.join(", ", course.getInstructorName()), cellStyle);
                table.addCell(String.join(", ", course.getRequirement()), cellStyle);
                table.addCell(course.getStartDate().toString(), cellStyle);

                System.out.println(table.render());

                System.out.print("Are you sure you want to delete this course? (yes/no): ");
                String confirmation = sc.nextLine().trim().toLowerCase();

                if (confirmation.equals("yes") || confirmation.equals("y")) {
                    CourseRepo.removeCourseById(courseId);
                    System.out.println("Course has been removed successfully.");
                } else {
                    System.out.println("Course deletion cancelled.");
                }
            } else {
                System.out.println("⚠️ No course found with ID " + courseId );
            }
        } catch (NumValidate e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}



