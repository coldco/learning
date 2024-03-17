package edu.hitwh.controller;

import edu.hitwh.response.ResultResponse;
import edu.hitwh.response.EmailPageResponse;
import edu.hitwh.request.EmailQueryRequest;
import edu.hitwh.response.EmailDetailResponse;
import edu.hitwh.service.MailboxService;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "信箱操作控制层(路径参数box为0表示收件箱，为1表示发件箱)")
@RequiredArgsConstructor
@RequestMapping("/mailbox/{box}")
public class MailboxController {
   private final MailboxService mailboxService;
   private final UserService userService;
   @Operation(summary = "查看信箱(模糊查询)")
   @GetMapping("/box")
   public ResultResponse<EmailPageResponse> getMailbox(@PathVariable Short box,
                                                       @Parameter @RequestParam EmailQueryRequest query) {
      EmailPageResponse pageBean = mailboxService.getEmail(JWTUtils.currentJwt, box, query);
      return ResultResponse.success(pageBean);
   }

   @Operation(summary = "查看信件详细信息")
   @GetMapping("/read")
   public ResultResponse<EmailDetailResponse> readEmail(@Parameter(description = "信件id") Integer id,
                                   @PathVariable Short box) {
      EmailDetailResponse email = mailboxService.readEmail(id,box);
      return ResultResponse.success(email);
   }

   @Operation(summary = "（批量）删除信件")
   @DeleteMapping("/{ids}")
   public ResultResponse<Object> deleteEmail(@PathVariable Short box,
                                     @PathVariable List<Integer> ids) {
      mailboxService.deleteEmail(box, ids);
      return ResultResponse.success();
   }

   @Operation(summary = "新建分组")
   @PutMapping("/group")
   public ResultResponse<Object> newMailGroup(@PathVariable Short box,
                                              @Parameter(description = "新建组名") @RequestParam String group) {
      userService.newMailGroup(box, group);
      return ResultResponse.success();
   }

   @Operation(summary = "删除分组")
   @DeleteMapping("/group")
   @Transactional
   public ResultResponse<Object> deleteMailGroup(@PathVariable Short box,
                                         @Parameter(description = "删除组名")@RequestParam String group) {
      //避免重复套用，分开编写方法
      mailboxService.deleteGroup(box, group);
      userService.deleteMailGroup(box, group);
      return ResultResponse.success();
   }

   @Operation(summary = "得到分组信息/将信件添加到组时用于分组信息显示")
   @GetMapping("/group")
   public ResultResponse<List<String>> getMailGroup(@PathVariable Short box) {
      return ResultResponse.success(userService.getMailGroup(box));
   }

   @Operation(summary = "置顶信件", description = "用于查看分组包含的信件时将指定信件置顶")
   @PutMapping("/group/top")
   public ResultResponse<Object> setMailTop(@Parameter(description = "信件id") Integer id,@PathVariable Short box) {
      mailboxService.topEmail(id, (short) 1,box);
      return ResultResponse.success();
   }

   @Operation(summary = "取消置顶")
   @PutMapping("/group/untop")
   public ResultResponse<Object> setMailUnTop(@Parameter(description = "信件id") Integer id,@PathVariable Short box) {
      mailboxService.topEmail(id, (short) 0,box);
      return ResultResponse.success();
   }

   @Operation(summary = "查看指定分组的全部信件")
   @GetMapping("/group/getEmail")
   public ResultResponse<EmailPageResponse> getGroupMail(@PathVariable Short box,
                                                         @Parameter(description = "组名") String group){
      //新建查询类将组名传入
      EmailQueryRequest query = new EmailQueryRequest();
      query.setGroup(group);
      return ResultResponse.success(mailboxService.getEmail(JWTUtils.currentJwt,box,query));
   }

}
