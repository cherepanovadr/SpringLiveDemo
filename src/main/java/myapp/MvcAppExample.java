package myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication

public class MvcAppExample {

    @Controller
    public static class MainController {
        static class Student {
            String name;
            int grade;

            public Student(String name, int grade) {
                this.name = name;
                this.grade = grade;
            }

            public String getName() {
                return name;
            }

            public int getGrade() {
                return grade;
            }
        }

        List<Student> students = new ArrayList<>(
                Arrays.asList(
                        new Student("Joro", 2),
                        new Student("Ivan", 6),
                        new Student("Ani", 6)
                ));

        @GetMapping("/home")
        public ModelAndView getHome(ModelAndView modelAndView) {
            modelAndView.setViewName("home.html");

            return modelAndView;
        }

        @GetMapping("/hello")
        public ModelAndView getHello(ModelAndView modelAndView) {
            modelAndView.setViewName("hello.html");
            modelAndView.addObject("name", "Pesho");
            List<Student> topStudents = students.stream().
                    filter(s -> s.getGrade() == 6).collect(Collectors.toList());
            modelAndView.addObject("topStudents", topStudents);
            return modelAndView;
        }


        //listStudents?grade=6
        @GetMapping("/listStudents")
        public ModelAndView listStudents(@RequestParam("grade") String grade) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView((map, httpServletRequest, httpServletResponse) -> {
                httpServletResponse.setContentType("text/utf-8");
                int gradeValue = Integer.parseInt(grade);
                students.stream().filter(s -> s.getGrade() == gradeValue)
                        .forEach(s -> {
                            try {
                                httpServletResponse.getWriter().write(s.getName() + " ");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            });


            return modelAndView;
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(MvcAppExample.class, args);
    }


}
