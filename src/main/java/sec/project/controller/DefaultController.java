package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.context.SpringWebContext;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "index";
    }

    @RequestMapping(value = "/sqlinjection", method = RequestMethod.GET)
    public String sqlInjection() {
        return "sqlinjection";
    }

    @RequestMapping(value = "/sqlinjection", method = RequestMethod.POST)
    public String submitSqlInjectionForm(Model model, @RequestParam String iban) throws SQLException {

        String result = "";
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Execute query and retrieve the query results
        String sql = "SELECT * FROM accounts WHERE iban='" + iban + "'";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        List<Account> list = new ArrayList<Account>();

        while (resultSet.next())
            list.add(new Account(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("iban"), resultSet.getDouble("balance")));

        model.addAttribute("accounts", list);

        // Close the connection
        resultSet.close();
        connection.close();

        return "sqlinjection";
    }

    @RequestMapping(value = "/brokenauthentication", method = RequestMethod.GET)
    public String brokenAuthentication() {
        return "brokenauthentication";
    }

    @RequestMapping(value = "/brokenauthentication", method = RequestMethod.POST)
    public String submitSqlInjectionForm(HttpServletRequest request, ModelMap model) {//, @RequestParam String username, @RequestParam String password) {
        String username = "";
        String password = "";
        try {
            username = request.getParameter("username").toString();
            password = request.getParameter("password").toString();

            if (!(username.equals("larry") && password.equals("istheking"))) {
                model.addAttribute("error", "Invalid credentials!");
                return "brokenauthentication";
            }
        } catch (Exception ex) {
            String foo = "";
        }

        return "done";
    }

    @RequestMapping(value = "/crossitescripting", method = RequestMethod.GET)
    public String xss(Model model) throws SQLException {

        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Get all messages
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM messages");

        List<Message> list = new ArrayList<Message>();
        while (resultSet.next())
            list.add(new Message(resultSet.getString("title"), resultSet.getString("content")));
        model.addAttribute("messages", list);

        // Close the connection
        resultSet.close();
        connection.close();

        return "crossitescripting";
    }

    @RequestMapping(value = "/crossitescripting", method = RequestMethod.POST)
    public String submitXssForm(Model model, @RequestParam String title, String content) throws SQLException {

        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Insert new message
        String insertQuery = "INSERT INTO messages (title, content) VALUES(?, ?)";
        connection.setAutoCommit(false);
        PreparedStatement insert = connection.prepareStatement(insertQuery);
        insert.setString(1, title);
        insert.setString(2, content);
        insert.execute();
        connection.commit();

        // Close the connection
        connection.close();

        return "redirect:/crossitescripting";
    }
}
