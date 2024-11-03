package net.enjoy.springboot.fooddelivery.controller;

import net.enjoy.springboot.fooddelivery.repository.RoleRepository;
import net.enjoy.springboot.fooddelivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.enjoy.springboot.fooddelivery.entity.Role;
import net.enjoy.springboot.fooddelivery.entity.User;
import net.enjoy.springboot.fooddelivery.repository.UserRepository;
import net.enjoy.springboot.fooddelivery.service.FileStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;


@Controller
public class ProfileController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @PostMapping("/uploadProfilePicture")
    public String handleFileUpload(@RequestParam("profilePicture") MultipartFile file, @RequestParam(value = "userId") Long userId, RedirectAttributes redirectAttributes) {
        try {
           String savedFile = fileStorageService.saveFile(file);
           userRepository.findById(userId).ifPresent(user -> {
               user.setProfilePicture(savedFile);
               userRepository.save(user);
              });
            redirectAttributes.addFlashAttribute("messageSuccess", "Profile picture updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", "Failed to upload profile picture!");
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/download-profile")
    public ResponseEntity<Resource> downloadFileFaster(@RequestParam("fileName") String filename) {
        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //form cedrick
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard/users")
    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
        List<User> users = userService.findAllUsers();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("ID,Username,Email");

        for (User user : users) {
            writer.printf("%d,%s,%s%n", user.getId(), user.getFirstName(), user.getEmail());
        }
        writer.flush();
        writer.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }


}
