

package org.example.gestion_stock.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firebase")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @PostMapping("/block/{uid}")
    public String blockUser(@PathVariable String uid) throws Exception {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid).setDisabled(true);
        FirebaseAuth.getInstance().updateUser(request);
        return "Utilisateur bloqué.";
    }

    @PostMapping("/unblock/{uid}")
    public String unblockUser(@PathVariable String uid) throws Exception {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid).setDisabled(false);
        FirebaseAuth.getInstance().updateUser(request);
        return "Utilisateur débloqué.";
    }
}
