package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Credential;
import sec.project.domain.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

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
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM messages WHERE type='STO'");

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
        String insertQuery = "INSERT INTO messages (type, title, content) VALUES('STO', ?, ?)";
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

    @RequestMapping(value = "/phishing", method = RequestMethod.GET)
    public String phishing(Model model, String content) throws SQLException {

        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Get all messages
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM messages WHERE type='PHI'");
        List<Message> messages = new ArrayList<Message>();
        while (rs.next())
            messages .add(new Message(rs.getString("content")));
        model.addAttribute("messages", messages );
        rs.close();

        // Get all credentials
        rs = connection.createStatement().executeQuery("SELECT * FROM credentials");
        List<Credential> list = new ArrayList<Credential>();
        while (rs.next())
            list.add(new Credential(rs.getString("username"), rs.getString("password")));
        model.addAttribute("credentials", list);
        rs.close();

        // Close the connection
        connection.close();

        return "phishing";
    }

    @RequestMapping(value = "/phishing", method = RequestMethod.POST)
    public String submitPhishingForm(Model model, @RequestParam String content) throws SQLException {

        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Insert new message
        String insertQuery = "INSERT INTO messages (type, content) VALUES('PHI', ?)";
        connection.setAutoCommit(false);
        PreparedStatement insert = connection.prepareStatement(insertQuery);
        insert.setString(1, content);
        insert.execute();
        connection.commit();

        // Close the connection
        connection.close();

        return "redirect:/phishing";
    }

    @RequestMapping(value = "/catcher", method = RequestMethod.GET)
    public String phishingCatcher(String username, String password) throws SQLException {

        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Insert new message
        String insertQuery = "INSERT INTO credentials (username, password) VALUES(?, ?)";
        connection.setAutoCommit(false);
        PreparedStatement insert = connection.prepareStatement(insertQuery);
        insert.setString(1, username);
        insert.setString(2, password);
        insert.execute();
        connection.commit();

        // Close the connection
        connection.close();

        return "phishing";
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {
        return "redirect";
    }


    @RequestMapping(value = "/redirectToUrl", method = RequestMethod.GET)
    public String redirectToUrl(HttpServletResponse response, @RequestParam String url) throws IOException {
        response.sendRedirect(url);
        return "redirect";
    }

}
