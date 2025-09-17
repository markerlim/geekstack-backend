package com.geekstack.cards.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.FirebaseUser;
import com.geekstack.cards.model.UserPost;
import com.geekstack.cards.model.UserPostFinal;
import com.geekstack.cards.service.GoogleCloudStorageService;
import com.geekstack.cards.service.UserPostService;

@RestController
@RequestMapping("/api/userpost")
public class UserPostController {

    private final static Logger logger = LoggerFactory.getLogger(UserPostController.class);

    @Autowired
    private UserPostService userPostService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @GetMapping
    public ResponseEntity<List<UserPostFinal>> listalluserpost(@RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit) {
        return ResponseEntity
                .ok(userPostService.listUserPost(Integer.parseInt(page), Integer.parseInt(limit), "DEFAULT"));
    }

    @GetMapping("/type/{posttype}")
    public ResponseEntity<List<UserPostFinal>> listAllUserPostsByType(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit,
            @PathVariable String posttype,
            @RequestParam(required = false) String code) {

        String filter;
        if (code != null && !code.trim().isEmpty()) {
            filter = "CATEGORYANDCODE:" + posttype + ":" + code;
        } else {
            filter = "CATEGORY:" + posttype;
        }

        return ResponseEntity.ok(userPostService.listUserPost(
                Integer.parseInt(page),
                Integer.parseInt(limit),
                filter));
    }

    @GetMapping("/type/{posttype}/search/{term}")
    public ResponseEntity<List<UserPostFinal>> listAllUserPostsBySearchAndType(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(required = false) String code,
            @PathVariable String posttype,
            @PathVariable String term) {
        String filter;
        if (code != null && !code.trim().isEmpty()) {
            filter = "SEARCHANDTYPE:" + posttype + ":" + term + ":" + code;
        } else {
            filter = "SEARCHANDTYPE:" + posttype + ":" + term;
        }
        return ResponseEntity.ok(userPostService.listUserPost(
                Integer.parseInt(page),
                Integer.parseInt(limit),
                filter));
    }

    /**
     * Example of Json from form
     * {
     * "postType": "decklist",
     * "code": "ABC123",
     * "isTournamentDeck": true,
     * "timestamp": "2024-02-20",
     * "selectedCard": [
     * { "cardUid": "card_001", "imageSrc": "https://example.com/image1.jpg" }
     * ],
     * "listofcards": [
     * { "cardUid": "card_002", "imageSrc": "https://example.com/image2.jpg" }
     * ]
     * }
     * 
     * @param userId
     * @param userPost
     * @return
     */
    @PostMapping(path = { "/post" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Object>> userMakePost(
            @RequestBody UserPost userPost,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        userPost.setUserId(user.getUid());
        Map<String, Object> response = new HashMap<>();
        logger.info(userPost.getPostId());
        try {
            userPostService.createPost(userPost);
            response.put("message", "Post successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error creating post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search/{term}")
    public ResponseEntity<List<UserPostFinal>> listalluserpostBySearch(@RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit, @PathVariable String term) {
        return ResponseEntity
                .ok(userPostService.listUserPost(Integer.parseInt(page), Integer.parseInt(limit), "SEARCH:" + term));
    }

    @GetMapping("/findpost/{postId}")
    public ResponseEntity<?> getPostByPostId(@PathVariable String postId) {
        try {
            UserPost post = userPostService.getOnePost(postId);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Post not found", "postId", postId));
            }
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error retrieving post", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Map<String, Object>> userDeletePost(@PathVariable String postId,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            userPostService.deletePost(postId, user.getUid());
            response.put("message", "Post deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error deleting post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> commentPost(
            @RequestBody String payload, @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info(payload);
            Comment commentObj = userPostService.commentPost(payload, user.getUid());
            response.put("message", "Comment created successfully");
            response.put("commentObject", commentObj);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/comment/{postId}/delete/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable String postId,
            @PathVariable String commentId, @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String userId = user.getUid();
        try {
            userPostService.deleteCommmentFromPost(commentId, userId);
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error deleting comments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // userId refer to the user that trigger this action
    @PostMapping("/like/{postId}/postedby/{posterId}")
    public ResponseEntity<Map<String, Object>> likeAPost(
            @PathVariable String postId,
            @PathVariable String posterId,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            userPostService.handleLikeEvent(postId, posterId, user.getUid(), user.getPicture(), user.getName());
            response.put("message", "like recorded successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error liking post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/unlike/{postId}")
    public ResponseEntity<Map<String, Object>> unlikeAPost(@PathVariable String postId,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String userId = user.getUid();
        try {
            userPostService.unlikePost(postId, userId);
            response.put("message", "unlike recorded successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error unliking post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/listuserpostings")
    public ResponseEntity<List<UserPostFinal>> listPostingOfUser(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "10") String limit,
            @AuthenticationPrincipal FirebaseUser.Principal user) throws NumberFormatException, Exception {
        String userId = user.getUid();
        return ResponseEntity.ok(
                userPostService.listUserPost(Integer.parseInt(page), Integer.parseInt(limit),
                        "USERPOST:" + userId));
    }

    @GetMapping("/listoflikedpost")
    public ResponseEntity<List<UserPostFinal>> listLikedPostOfUser(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "10") String limit,
            @AuthenticationPrincipal FirebaseUser.Principal user) throws NumberFormatException, Exception {
        String userId = user.getUid();
        return ResponseEntity.ok(
                userPostService.listUserPost(Integer.parseInt(page),
                        Integer.parseInt(limit), "LIKEDPOST:" + userId));
    }

    @PostMapping("/upd/{postId}")
    public ResponseEntity<Map<String, Object>> updateImage(@PathVariable String postId,
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                if (!extension.equals(".webp") && !extension.equals(".gif")) {
                    response.put("message", "Invalid file type. Only .webp and .gif are allowed.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else {
                extension = ".webp";
            }

            String fileName = "post_" + postId + "_" + System.currentTimeMillis() + extension;
            String fileUrl = googleCloudStorageService.uploadPostImage(file.getBytes(), postId, fileName);

            response.put("message", "Successfully Uploaded");
            response.put("fileUrl", fileUrl);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("Error uploading image", e);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
