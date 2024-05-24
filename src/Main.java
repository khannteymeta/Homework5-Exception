import View.Menu;
import java.util.Scanner;
import service.CourseServiceImp;
import service.CourseService;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            Menu.menu();
            System.out.print("\nInsert one option  : ");
            Integer option = new Scanner(System.in).nextInt();
            CourseServiceImp courseServiceImp = new CourseServiceImp();
            switch (option) {
                case 1: {
                    courseServiceImp.addNewCourse();
                    break;
                }
                case 2: {
                    courseServiceImp.listCourse();
                    break;
                }
                case 3: {
                    courseServiceImp.findById();
                    break;
                }
                case 4: {
                    courseServiceImp.findByTitle();
                    break;
                }
                case 5: {
                    courseServiceImp.removeCourse();
                    break;
                }
                case 0,99:
                    System.out.println("Exit program...");
                    exit = true;
                    break;
                default: {
                    System.out.println("Invalid input !");
                    break;
                }
            }

        }
    }
}