package study.toy.everythingshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import study.toy.everythingshop.entity.UserMEntity;
import study.toy.everythingshop.repository.UserDAO;
import study.toy.everythingshop.service.MyPageService;

/**
 * fileName : MypageController
 * author   : pilming
 * date     : 2023-03-29
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/myPage")
public class MyPageController {

    private final UserDAO userDAO;
    private final MyPageService myPageService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String myPage() {
        return "myPage";
    }

    @RequestMapping(value = "/myInfo", method = RequestMethod.GET)
    public String myInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        //AuthenticationPrincipal어노테이션으로 현재 로그인한 사용자의 정보를 나타내는 객체인 Principal객체를 주입받을수있다
        //이렇게 주입받은 객체는 UserDetails타입으로 캐스팅해서 사용 가능

        //사용자 정보 조회
        UserMEntity userMEntity = userDAO.findByUserId(userDetails.getUsername());

        //디버깅
        log.info(">>>>>>>>>>>>userMEntity : {}",userMEntity);

        //모델 추가
        model.addAttribute("userInfo", userMEntity);
        return "myInfo";
    }

    @RequestMapping(value = "/editMyInfo", method = RequestMethod.GET)
    public String editMyInfoView(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        //AuthenticationPrincipal어노테이션으로 현재 로그인한 사용자의 정보를 나타내는 객체인 Principal객체를 주입받을수있다
        //이렇게 주입받은 객체는 UserDetails타입으로 캐스팅해서 사용 가능

        //사용자 정보 조회
        UserMEntity userMEntity = userDAO.findByUserId(userDetails.getUsername());

        //디버깅
        log.info(">>>>>>>>>>>>userMEntity : {}",userMEntity);

        //모델 추가
        model.addAttribute("userInfo", userMEntity);
        return "editMyInfo";
    }

    @RequestMapping(value = "/editMyInfo/{userId}", method = RequestMethod.POST)
    public String editMyInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String userId,
                             @Validated @ModelAttribute("userInfo") UserMEntity userMEntity, BindingResult bindingResult) {
        log.info("userMEntity={}", userMEntity);
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "editMyInfo";
        }

        //로그인 한 아이디와 수정 요청 한 아이디가 동일한지 체크
        if (!userId.equals(userDetails.getUsername()) || !userMEntity.getUserId().equals(userDetails.getUsername())) {
            //todo 예외 페이지로 처리할지 아니면 수정화면에서 bindingResult로 처리할지 결정 필요
            throw new AccessDeniedException("Cannot update other users' information");
        }

        myPageService.updateUserInfo(userMEntity);

        //디버깅
        log.info(">>>>>>>>>>>>userMEntity : {}",userMEntity);
        log.info(">>>>>>>>>>>>userId : {}",userId);

        return "redirect:/myPage/myInfo";
    }
}
