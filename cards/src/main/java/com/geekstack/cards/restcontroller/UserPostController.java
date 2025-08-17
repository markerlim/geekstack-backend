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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.FirebaseUser;
import com.geekstack.cards.model.UserPost;
import com.geekstack.cards.service.UserPostService;

@RestController
@RequestMapping("/api/userpost")
public class UserPostController {

    private final static Logger logger = LoggerFactory.getLogger(UserPostController.class);

    @Autowired
    private UserPostService userPostService;

    @GetMapping
    public ResponseEntity<List<UserPost>> listalluserpost(@RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit) {
        return ResponseEntity.ok(userPostService.listUserPost(Integer.parseInt(page), Integer.parseInt(limit)));
    }

    @GetMapping("/type/{posttype}")
    public ResponseEntity<List<UserPost>> listalluserpostByType(@RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit, @PathVariable String posttype) {
        return ResponseEntity
                .ok(userPostService.listUserPostByType(Integer.parseInt(page), Integer.parseInt(limit), posttype));
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
     * ],
     * "listoflikes": [],
     * "listofcomments": [
     * 
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
            userPostService.deleteCommmentFromPost(postId, commentId, userId);
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
    public ResponseEntity<List<UserPost>> listPostingOfUser(@RequestHeader("Authorization") String authorization,
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit) throws NumberFormatException, Exception {
        return ResponseEntity.ok(
                userPostService.listUserPostByUserId(authorization, Integer.parseInt(page), Integer.parseInt(limit)));
    }

    @GetMapping("/listoflikedpost")
    public ResponseEntity<List<UserPost>> listLikedPostOfUser(@RequestHeader("Authorization") String authorization,
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String limit) throws NumberFormatException, Exception {
        return ResponseEntity.ok(
                userPostService.listUserPostLikedByUserId(authorization, Integer.parseInt(page),
                        Integer.parseInt(limit)));
    }

}
