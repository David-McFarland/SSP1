package Crud;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
import Crud.StudentJDBCTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Controller    // This means that this class is a Controller
public class MainController { //implements WebMvcConfigurer{

    @GetMapping(path = "/create") // Map ONLY GET Requests
    public String addNewUser(Model model) {
        model.addAttribute("name", "Noone");
        model.addAttribute("studentInput", new Student());

        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");


        studentJDBCTemplate.create("Victoria", 20);
        //return "Something";
        return "Form";
    }


    @GetMapping(path = "/students") // Map ONLY GET Requests
    public String getStudents(Model model) {
        //model.addAttribute("Form");

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");
        List<Student> students = studentJDBCTemplate.listStudents();

        model.addAttribute("students", students);
        model.addAttribute("studentInput", new Student());
        return "Students";
        //return "greeting";
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        //model.addAttribute("greeting");
        return "greeting";
    }


    @GetMapping(path = "/student/{id}") // Map ONLY GET Requests
    public String getStudent(Model model, @PathVariable("id") String id) {
        model.addAttribute("id", id);
        model.addAttribute("studentInput", new Student());
        System.out.println(id);

        Student student = getStudent(id);
        if(student == null){
            return "StudentNotFound";
        }
        model.addAttribute("student", student);

        return "Student";
    }

    @PostMapping(path = "/student/{id}") // Map ONLY GET Requests
    public String postStudent(Model model, @PathVariable("id") String id) {
        System.out.println("ahhhhhhhhh");
        model.addAttribute("id", id);
        Student student = getStudent(id);
        if(student == null){
            return "StudentNotFound";
        }
        model.addAttribute("student", student);

        return "Student";
    }


    @PostMapping(path = "/student/{id}", params = "go to students") // Map ONLY GET Requests
    public String postStudentGoBack(Model model, @PathVariable("id") String id) {
        model.addAttribute("studentInput", new Student());
        return "redirect:/students";
    }


    @PostMapping(path = "/students", params = "add") // Map ONLY GET Requests
    public String postStudentAdd(Model model, @ModelAttribute Student studentInput) {
        System.out.println("add");
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");



        if(studentInput.getAge() != null && studentInput.getName() != null){
            studentJDBCTemplate.create(studentInput.getName(), studentInput.getAge());
        }
        model.addAttribute("studentInput", new Student());

        List<Student> students = studentJDBCTemplate.listStudents();

        model.addAttribute("students", students);

        return "Students";
    }


    @PostMapping(path = "/student/{id}", params = "update") // Map ONLY GET Requests
    public String postStudentUpdate(Model model, @PathVariable("id") String id, @ModelAttribute Student studentInput) {
        model.addAttribute("id", id);

        Student student = getStudent(id);
        if(student == null){
            return "StudentNotFound";
        }

        Integer ID = Integer.parseInt(id);

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");


        if(studentInput.getAge() != null && studentInput.getName() != null){
            studentJDBCTemplate.update(ID, studentInput.getAge(), studentInput.getName());
        }
        student = getStudent(id);
        model.addAttribute("student", student);
        model.addAttribute("studentInput", new Student());

        return "Student";
    }


    @PostMapping(path = "/student/{id}", params = "delete") // Map ONLY GET Requests
    public String postStudentDelete(Model model, @PathVariable("id") String id) {
        model.addAttribute("id", id);
        Student student = getStudent(id);
        if(student == null){
            return "StudentNotFound";
        }

        Integer ID = Integer.parseInt(id);

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");
        studentJDBCTemplate.delete(ID);
        model.addAttribute("student", student);
        return "StudentNotFound";
    }

    public int find(String id) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");
        int ID = -1;
        try {
            ID = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (studentJDBCTemplate.doesStudentExist(ID)) {
            return ID;
        }
        return -1;
    }

    public Student getStudent(String id) {
        int ID = find(id);
        if(ID == -1){
            return null;
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        StudentJDBCTemplate studentJDBCTemplate =
                (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");

        return studentJDBCTemplate.getStudent(ID);
    }

}