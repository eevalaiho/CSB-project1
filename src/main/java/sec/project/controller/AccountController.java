package sec.project.controller;

import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Signup;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String defaultMapping() {
        return "account";
    }

/*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listClientInfo(Model model) {
        model.addAttribute("clients", clientInfo);
        return "account";
    }
*/

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String iban) throws SQLException {

        String result = "";
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        // Execute query and retrieve the query results
        String sql = "SELECT * FROM accounts WHERE iban='" + iban + "'";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        List<Account> list = new ArrayList<Account>();

        // Do something with the results -- here, we print the books
        while (resultSet.next())
            list.add(new Account(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("iban"), resultSet.getDouble("balance")));

        model.addAttribute("accounts", list);

        // Close the connection
        resultSet.close();
        connection.close();

        //return "redirect:/account";
        return "account";
    }

}
