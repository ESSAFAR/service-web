package com.example.demowithibtissam.service;

import org.springframework.stereotype.Service;

@Service

public class EmailService {
    public void notifyProject(String project) {
        System.out.println("Email sent");
    }
}
