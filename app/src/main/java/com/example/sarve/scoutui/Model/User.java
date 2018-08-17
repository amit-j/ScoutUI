package com.example.sarve.scoutui.Model;
/*the global file containing the default constructors and parameterized constructors dealing with username and password */
/*useful for retrieving and storing purposes*/
public class User {
    private String name;
    private String password;

    public User()
    {

    }
    public User(String Pname, String Ppassword)
    {
        name = Pname;
        password = Ppassword;
    }

    public String setname(String Pname) {
        name = Pname;
        return name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
