

package com.dwm.arturo.services;

import com.dwm.arturo.entities.Document;
import com.dwm.arturo.entities.User;
import com.dwm.arturo.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userDAO;
    
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userDAO.findAll();
    }
    
    @Transactional(readOnly = true)
    public User getOne(String id) throws Exception{
         Integer identificaton = Integer.valueOf(id);
         try {
            return userDAO.findById(identificaton).orElse(null);           
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
