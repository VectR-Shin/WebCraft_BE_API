package com.team22.webcraft.Controller;

import com.team22.webcraft.Controller.ReturnValue.SuccessReturn;
import com.team22.webcraft.DTO.MapData.*;
import com.team22.webcraft.Domain.UserData;
import com.team22.webcraft.Interceptor.SessionConst;
import com.team22.webcraft.Service.MapDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MapDataController {
    private final MapDataService mapService;

    @PostMapping(value = "/map/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessReturn mapSave(@RequestPart("file")MultipartFile file,
                                 @RequestPart("dto")MapSaveDTO dto,
                                 HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserData ud = (UserData)session.getAttribute(SessionConst.LOGIN_MEMBER);

        mapService.mapSave(ud.getId(), file, dto);

        return new SuccessReturn("Map save completed");
    }

    @PostMapping(value = "/map/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessReturn mapUpdate(@RequestPart("file")MultipartFile file,
                                   @RequestPart("dto") MapUpdateDTO dto,
                                   HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserData ud = (UserData)session.getAttribute(SessionConst.LOGIN_MEMBER);

        mapService.mapUpdate(ud.getId(), file, dto);

        return new SuccessReturn("Map update completed");
    }

    @PostMapping(value = "/map/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MapDataDTO>> mapSearch(@RequestBody MapSearchDTO dto,
                                          HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserData ud = (UserData)session.getAttribute(SessionConst.LOGIN_MEMBER);

        List<MapDataDTO> list = mapService.mapSearch(ud.getId(), dto);

        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/map/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SuccessReturn mapRemove(@RequestBody MapRemoveDTO dto,
                                   HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserData ud = (UserData)session.getAttribute(SessionConst.LOGIN_MEMBER);

        mapService.mapRemove(ud.getId(), dto);

        return new SuccessReturn("Map remove completed");
    }

    @PostMapping(value = "/map/provide", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> mapProvide(@RequestBody MapProvideDTO dto,
                                               HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserData ud = (UserData)session.getAttribute(SessionConst.LOGIN_MEMBER);

        return mapService.mapProvide(ud.getId(), dto);
    }
}
