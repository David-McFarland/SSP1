package Crud;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentJDBCTemplate implements StudentDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    public void create(String name, Integer age) {
        String SQL = "insert into Student (name, age) values (?, ?)";
        jdbcTemplateObject.update( SQL, name, age);
        System.out.println("Created Record Name = " + name + " Age = " + age);
        return;
    }
    public Student getStudent(Integer id) {
        String SQL = "select * from Student where id = ?";
        Student student = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id}, new StudentMapper());

        return student;
    }

    public boolean doesStudentExist(Integer id){
        String SQL = "select count(*) from Student where id = ?";
        boolean exists = false;
        int count = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id}, Integer.class);
        if(count > 0){
            exists = true;
        }
        return exists;
    }

    public List<Student> listStudents() {
        String SQL = "select * from Student";
        List <Student> students = jdbcTemplateObject.query(SQL, new StudentMapper());
        return students;
    }
    public void delete(Integer id) {
        String SQL = "delete from Student where id = ?";
        jdbcTemplateObject.update(SQL, id);
        System.out.println("Deleted Record with ID = " + id );
        return;
    }
    public void update(Integer id, Integer age){
        String SQL = "update Student set age = ? where id = ?";
        jdbcTemplateObject.update(SQL, age, id);
        System.out.println("Updated Record with ID = " + id );
        return;
    }

    public void update(Integer id, Integer age, String name){
        String SQL = "update Student set age = ?, name = ? where id = ?";
        jdbcTemplateObject.update(SQL, age, name, id);
        System.out.println("Updated Record with ID = " + id );
        return;
    }
}