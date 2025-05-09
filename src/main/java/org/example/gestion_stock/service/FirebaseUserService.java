package org.example.gestion_stock.service;


import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseUserService {

    public List<UserRecord> getAllUsers() throws Exception {
        List<UserRecord> users = new ArrayList<>();

        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        for (ExportedUserRecord user : page.iterateAll()) {
            users.add(user);
        }

        return users;
    }
}
