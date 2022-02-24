package com.dwm.arturo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_type")
    private String fileType;
    
   
    private String path;
    
    //for the program functionality only
    @Transient
    private String extension;
    
    
    public Document(User user, String fileName, String fileType, String path) {
        this.user = user;
        this.fileName = fileName;
        this.fileType = fileType;
        this.path = path;
    }
    
     public Document() {
    }
}
