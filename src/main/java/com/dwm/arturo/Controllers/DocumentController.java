package com.dwm.arturo.Controllers;

import com.dwm.arturo.Helpers.Utils;
import com.dwm.arturo.entities.Document;
import com.dwm.arturo.entities.User;
import com.dwm.arturo.model.Response;
import com.dwm.arturo.services.DocumentService;
import com.dwm.arturo.services.UserService;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

//    Response response;
//    response  = new Response("Server Error: Try again later");
//    return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    @GetMapping("/")
    public ResponseEntity<?> getAllDocuments() {
        Response response;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(documentService.getAll());
        } catch (Exception e) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<?> getOneDocu(@PathVariable Integer id) {
        Response response;
        try {
            Document docu = documentService.getOneDocu(id);
            if (docu == null) {
                response = new Response("There is no document with id: ".concat(id.toString()));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(documentService.getOneDocu(id));
        } catch (Exception e) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> saveDocument(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        Response response;
        try {
            if (file == null || file.isEmpty()) {
                response = new Response("Do not forguet to attach a file. Use key as 'file'");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (file.getSize() > 2097152) {
                response = new Response("File too large. Max capacity 2MB");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User user = userService.getOne(id.toString());
            if (user == null) {
                response = new Response("There is no user with id: ".concat(id.toString()).concat(". "
                        + "Make sure you are uploading a file to the right user"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            //create the path that will be storaged in the server. this path is absolute in the server file sistem
            String serverPath = Utils.getPathForStorageInServer(user, file.getOriginalFilename());

            //write the file in the server location
            byte[] fileBytes = file.getBytes();
            Path path = Paths.get(serverPath);
            Files.write(path, fileBytes);

            //create the path that will be storaged in the db. this path is relative to the statics folder
            String dbPath = Utils.getPathForStorageInDataBase(user, file.getOriginalFilename());

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Document doc = new Document(user, fileName, file.getContentType(), dbPath);

            return ResponseEntity.status(HttpStatus.CREATED).body(documentService.saveDocument(doc));
        } catch (IOException ex) {
            response = new Response("Server storage error, ask the developer for more details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception ex) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @DeleteMapping("/delete/{docId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Integer docId) {

        Response response;
        try {
            Document document = documentService.getOneDocu(docId);
            if (document == null) {
                response = new Response("There is no document with id: ".concat(docId.toString()));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Path path = Utils.getPathForDeletingInServer(document);

            Files.delete(path);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(documentService.deleteDocument(docId));
        } catch (IOException e) {
            response = new Response("Server storage error, ask the developer for more details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping("/modify/{userId}")
    public ResponseEntity<?> modifyDocument(@PathVariable Integer userId, @RequestBody Document document) {
        Response response;
        try {
            User user = userService.getOne(userId.toString());
            if (user == null) {
                response = new Response("There is no user with id: ".concat(userId.toString()).concat(". "
                        + "Make sure you are deleting a file from the right user"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            if (document.getFileName().equals("") || document.getFileName().isEmpty()
                    || document.getPath().isEmpty() || document.getPath().equals("")) {
                response = new Response("oops! Looks the request body does not match the"
                        + "specifications. Make sure you read the documentation about how to "
                        + "send the body request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            //get old file path and set new one
            File file1 = Utils.getFilesPathToReWrite(document, user)[0];
            File file2 = Utils.getFilesPathToReWrite(document, user)[1];

            //dummy path
            File file3 = Utils.getFilesPathToReWrite(document, user)[2];

            file1.renameTo(file2);

            //set properties for the document
            document.setPath(file3.getPath());
            document.setUser(user);
            file3 = null;

            return ResponseEntity.status(HttpStatus.OK).body(documentService.modifyDocument(document));
        } catch (Exception e) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
