package com.dwm.arturo.services;

import com.dwm.arturo.entities.Document;
import com.dwm.arturo.entities.User;
import com.dwm.arturo.repository.DocumentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentDAO;

    @Transactional(readOnly = true)
    public List<Document> getAll() {
        return documentDAO.findAll();
    }

    @Transactional(readOnly = true)
    public User getUser(int id) {
        return documentDAO.findById(id).orElse(null).getUser();
    }

    @Transactional(readOnly = true)
    public Document getOneDocu(int id) throws Exception {
        try {
            return documentDAO.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Document saveDocument(Document document) {
        return documentDAO.save(document);
    }

    public Document modifyDocument(Document document) {
        return documentDAO.save(document);
    }

    public boolean deleteDocument(Integer id) throws Exception {
        if (getOneDocu(id) != null) {
            documentDAO.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
