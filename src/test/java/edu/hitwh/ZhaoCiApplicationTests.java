package edu.hitwh;

import edu.hitwh.service.FriendService;
import edu.hitwh.service.MailboxService;
import edu.hitwh.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ZhaoCiApplicationTests {

    @Autowired
    FriendService friendService;
    @Autowired
    MailboxService mailboxService;
    @Autowired
    UserService userService;
    @Test
    public void textGetFriend(){
        System.out.println(friendService.getFriendOne(2023));
    }
    @Test
    public void textUpdate(){
        friendService.blackFriend(2024,2023,(short)1);
    }
    @Test
    public void textEmail(){
        mailboxService.deleteEmail((short)0,List.of(1)); }
    @Test
    public void textSelectBlack(){
        System.out.println(userService.isBlacked(2222,1111));}
}
