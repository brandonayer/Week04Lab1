/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author 677571
 */
public class UserService {
    
    String betty = "betty";
    String adam = "adam";
    String password = "password";
    
    public boolean validate(String username, String password){
        if(username.equals(this.adam) || username.equals(this.betty)){
            if(password.equals(this.password)){
                return true;
            }
        }
        return false;
    }
}
