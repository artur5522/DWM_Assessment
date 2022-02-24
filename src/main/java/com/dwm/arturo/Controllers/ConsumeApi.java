package com.dwm.arturo.Controllers;

import com.dwm.arturo.Helpers.Utils;
import com.dwm.arturo.entities.Document;
import com.dwm.arturo.entities.User;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(path = "")
public class ConsumeApi {

    @Autowired
    private RestTemplate templateRest;

    private final String uriUsers = "http://localhost:8080/users/";
    private final String uriOneUser = "http://localhost:8080/users/";
    private final String uriUploadDocument = "http://localhost:8080/documents/upload/";
    private final String uriOneDocument = "http://localhost:8080/documents/getOne/";
    private final String uriDeleteDocument = "http://localhost:8080/documents/delete/";
    private final String uriModifyDocumentName = "http://localhost:8080/documents/modify/";
    
    @GetMapping("/")
    public String index(Model model) throws Exception {

        User[] users = templateRest.getForObject(uriUsers, User[].class);

        model.addAttribute("users", users);

        return "index";
    }

    @GetMapping("/checkDocuments/{id}")
    public String checkDocuments(@PathVariable Integer id, Model model) throws Exception {

        User user = templateRest.getForObject(uriOneUser.concat(id.toString()), User.class);

        if (user.getDocuments().isEmpty() || user.getDocuments() == null) {
            model.addAttribute("message", "The user ".concat(user.getName()).concat(" do not have"
                    + " any documents, if you would like to add one, select"
                    + " a file and then click in 'Upload' "));
        } else {
            model.addAttribute("user", user);
        }
        model.addAttribute("id", id);

        return "documents";
    }

    @PostMapping("/uploadDocuments/{userId}")
    public String uploadDocuments(@PathVariable Integer userId, @RequestParam("file") MultipartFile file, Model model) {
        User user = templateRest.getForObject(uriOneUser.concat("/").concat(userId.toString()), User.class);
        Document docu = null;
        //check if this path is invoked  with a file within
        if (file == null || file.isEmpty()) {
            model.addAttribute("fileMessage", "You have to select a file to upload");
            model.addAttribute("id", userId);
            model.addAttribute("user", user);
            return "documents";
        } else {
            String fileName = Utils.getDbPath(user, file);
            //check if there is a file with that name
            boolean action = Utils.CompareDocumentsNames(fileName, user.getDocuments());
            if (action) {
                //have to prepare the multipart file to send it to the rest api          
                HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
                try {
                    requestEntity = Utils.prepareTheMultiparFile(file);
                    //rellenar con un mensaje de exito y tratar de redirigir a algun lado
                } catch (IOException ex) {
                    //rellenar este catch con algo. mensaje de error
                }
                //if everything is in place, I consume the restApi
                docu = templateRest.postForObject(uriUploadDocument
                        .concat(userId.toString()), requestEntity, Document.class);
                user = templateRest.getForObject(uriOneUser.concat("/").concat(userId.toString()), User.class);
            } else {
                model.addAttribute("errorMessage", "Ups! it seems that you already have a file"
                        + " named: ".concat(file.getOriginalFilename())
                                .concat(" Try changing the file's name, or upload a different file"));
            }
        }
        model.addAttribute("id", userId);
        model.addAttribute("user", user);
        return "documents";
    }

    @PostMapping("/userDocuments/{userId}")
    public String userDocuments(Document document, @PathVariable Integer userId, Model model) {
        //find the document with old name
        Document docu = templateRest.getForObject(uriOneDocument.concat(document.getId().toString()), Document.class);
        //set the new file name 
        docu.setFileName(document.getFileName().concat(document.getExtension()));

        //persis in db the new uploaded file
        docu = templateRest.postForObject(uriModifyDocumentName
                .concat(userId.toString()), docu, Document.class);

        User user = templateRest.getForObject(uriOneUser.concat(userId.toString()), User.class);
        model.addAttribute("id", userId);
        model.addAttribute("user", user);

        return "documents";
    }

    @GetMapping("/delete/{idDoc}/{idUser}")
    public String deleteDocument(@PathVariable Integer idDoc, @PathVariable Integer idUser, Model model) {
        templateRest.delete(uriDeleteDocument.concat(idDoc.toString()));
        User user = templateRest.getForObject(uriOneUser.concat(idUser.toString()), User.class);

        if (user.getDocuments().isEmpty() || user.getDocuments() == null) {
            model.addAttribute("message", "The user ".concat(user.getName()).concat(" do not have"
                    + " any documents, if you would like to add one, select"
                    + " a file and then click in 'Upload' "));
            model.addAttribute("user", user);
        }
        model.addAttribute("id", idUser);
        return "documents";
    }

    @GetMapping("/modify/{idDoc}/{idUser}")
    public String modifyDocumentForm(@PathVariable Integer idDoc, @PathVariable Integer idUser, Model model) {
        Document document = templateRest.getForObject(uriOneDocument.concat(idDoc.toString()), Document.class);
        User user = templateRest.getForObject(uriOneUser.concat(idUser.toString()), User.class);
        
        //set the form's data
        int dotChar = document.getFileName().indexOf('.');
        //set the extension, so the user can only modify the name
        document.setExtension(document.getFileName().substring(dotChar));
        document.setFileName(document.getFileName().substring(0, dotChar));
        
        model.addAttribute("userName", user.getName());
        model.addAttribute("userId", user.getId());
        model.addAttribute("document", document);

        return "modifyDocument";
    }
}
