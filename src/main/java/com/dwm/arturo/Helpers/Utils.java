package com.dwm.arturo.Helpers;

import com.dwm.arturo.entities.Document;
import com.dwm.arturo.entities.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static HttpEntity<MultiValueMap<String, Object>> prepareTheMultiparFile(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(file.getOriginalFilename())
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(file.getBytes(), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return requestEntity;
    }

    public static boolean CompareDocumentsNames(String value, List<Document> documents) {
        for (Document document : documents) {
            if (document.getPath().equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }

    public static String getProyectPath() {
        //get the proyect's root path  in server's location
        Path currentRelativePath = Paths.get("");
        String proyectPath = currentRelativePath.toAbsolutePath().toString();
        return proyectPath;
    }

    public static String getPathForStorageInServer(User user, String fileName) {
        StringBuilder builder = new StringBuilder();

        String proyectPath = getProyectPath();

        builder.append(proyectPath);
        builder.append(File.separator);

        builder.append("src");
        builder.append(File.separator);
        builder.append("main");
        builder.append(File.separator);
        builder.append("resources");
        builder.append(File.separator);
        builder.append("static");
        builder.append(File.separator);
        builder.append("Users_Files");
        builder.append(File.separator);
        builder.append(user.getName());
        File folder = new File(builder.toString());
        if (!folder.exists()) {
            //buil the user folder if not exists
            folder.mkdirs();
        }

        builder.append(File.separator);
        builder.append(fileName);

        return builder.toString();
    }

    public static Path getPathForDeletingInServer(Document document) {

        String proyectPath = getProyectPath();

        //get the path file in server 
        StringBuilder builer = new StringBuilder();
        builer.append(proyectPath);
        builer.append(File.separator);
        builer.append("src");
        builer.append(File.separator);
        builer.append("main");
        builer.append(File.separator);
        builer.append("resources");
        builer.append(File.separator);
        builer.append("static");
        builer.append(File.separator);
        builer.append(document.getPath());

        Path path = Paths.get(builer.toString());

        return path;
    }

    public static String getPathForStorageInDataBase(User user, String fileName) {
        StringBuilder builder = new StringBuilder();

        //get the path. this path is relative to the statics folder proyect
        builder.append("/Users_Files");
        builder.append(File.separator);
        builder.append(user.getName());
        builder.append(File.separator);
        builder.append(fileName);

        return builder.toString();
    }

    public static File[] getFilesPathToReWrite(Document document, User user) {
        String proyectPath = getProyectPath();

        StringBuilder builer = new StringBuilder();
        builer.append(proyectPath);
        builer.append(File.separator);

        builer.append("src");
        builer.append(File.separator);
        builer.append("main");
        builer.append(File.separator);
        builer.append("resources");
        builer.append(File.separator);
        builer.append("static");

        //create both paths for the file to be renamed
        String dummyPath = builer.toString().concat(File.separator);

        builer.append(document.getPath());
        File file1 = new File(builer.toString());

        String finalPath = dummyPath.concat("Users_Files/".concat(user.getName()).concat("/").concat(document.getFileName()));
        File file2 = new File(finalPath);
        File dummyFile3 = new File("/Users_Files/".concat(user.getName()).concat("/").concat(document.getFileName()));

        File[] arrayFiles = {file1, file2, dummyFile3};
        return arrayFiles;
    }

    public static String getDbPath(User user, MultipartFile file) {
        //recreate the file name as it is in the database
        StringBuilder builder = new StringBuilder("/Users_Files\\");
        builder.append(user.getName());
        builder.append(File.separator);
        builder.append(file.getOriginalFilename());

        return builder.toString();
    }

}
