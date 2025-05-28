package com.team22.webcraft.Service;

import com.team22.webcraft.DTO.MapData.*;
import com.team22.webcraft.Domain.AccessType;
import com.team22.webcraft.Domain.MapData;
import com.team22.webcraft.Exception.MapData.DuplicatedMapNameException;
import com.team22.webcraft.Exception.MapData.FileOwnerException;
import com.team22.webcraft.Exception.MapData.TooManyFilesException;
import com.team22.webcraft.Repository.MapDataRepository;
import com.team22.webcraft.Service.File.FileOperator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapDataService {
    private final MapDataRepository mapDataRepository;
    private final FileOperator fileOperator;

    public void mapSave(Long userDataId, MultipartFile file, MapSaveDTO mapSaveDTO) throws Exception {
        // if map name already exists
        if (mapDataRepository.checkUserDataId_MapNameExists(userDataId, mapSaveDTO.getMapName()))
            throw new DuplicatedMapNameException("A map with the same name already exists");

        // user can have 30 files as maximum
        if (mapDataRepository.countUserOwnedFiles(userDataId) >= 30)
            throw new TooManyFilesException("Users can save up to 30 maps");

        MapData mapData = new MapData(mapSaveDTO.getMapName(), mapSaveDTO.getAccessType());
        mapDataRepository.saveMapDataWithFK(userDataId, mapData);

        fileOperator.saveFile(userDataId, file, mapSaveDTO);
    }

    public List<MapDataDTO> mapSearch(Long userDataId, MapSearchDTO mapSearchDTO) {
        List<MapData> mapDataList = null;
        List<MapDataDTO> dtoList = new ArrayList<>();
        MapDataDTO dto = null;

        if (mapSearchDTO.getAccessType() == AccessType.PUBLIC)
            mapDataList = mapDataRepository.getPublicMapList();
        else
            mapDataList = mapDataRepository.getOwnedMapList(userDataId);

        for (MapData md : mapDataList) {
            dto = new MapDataDTO(md.getId(), md.getUserData().getUserId(), md.getMapName(), md.getCreateTime(), md.getLastUpdateTime(), md.getAccessType());
            dtoList.add(dto);
        }

        // if map count == 0, return empty arraylist
        return dtoList;
    }

    public void mapUpdate(Long userDataId, MultipartFile file, MapUpdateDTO mapUpdateDTO) throws Exception {
        if (!mapDataRepository.checkThisUserOwnedMap(userDataId, mapUpdateDTO.getMapDataId()))
            throw new FileOwnerException("Only file owner can update file");

        if (mapDataRepository.checkUserDataId_MapNameExists(userDataId, mapUpdateDTO.getNewMapName()))
            throw new DuplicatedMapNameException("A map with the same name already exists");

        long mapDataId = mapUpdateDTO.getMapDataId();
        String prevMapName = mapDataRepository.getMapNameById(mapUpdateDTO.getMapDataId());
        String newMapName = mapUpdateDTO.getNewMapName();

        // change: map name
        if (mapUpdateDTO.getNewMapName() != null)
            mapDataRepository.updateMapName(mapDataId, newMapName);

        // change: access type
        if (mapUpdateDTO.getNewAccessType() != null)
            mapDataRepository.updateAccessType(mapDataId, mapUpdateDTO.getNewAccessType());

        fileOperator.updateFile(userDataId, file, mapUpdateDTO, prevMapName);
    }

    public void mapRemove(Long userDataId, MapRemoveDTO mapRemoveDTO) throws Exception {
        if (!mapDataRepository.checkThisUserOwnedMap(userDataId, mapRemoveDTO.getMapDataId()))
            throw new FileOwnerException("Only file owner can remove file");

        mapDataRepository.removeMapData(mapRemoveDTO.getMapDataId());

        fileOperator.removeFile(userDataId, mapRemoveDTO);
    }

    public ResponseEntity<Resource> mapProvide(Long userDataId, MapProvideDTO mapProvideDTO) throws Exception {
        AccessType accessType = mapDataRepository.getAccessType(mapProvideDTO.getMapDataId());
        Long mapOwnerId = userDataId;

        // request other user's private map is not allowed
        if (accessType == AccessType.PRIVATE) {
            if (!mapDataRepository.checkThisUserOwnedMap(userDataId, mapProvideDTO.getMapDataId()))
                throw new FileOwnerException("Only file owner can request PRIVATE file provide");
        } else {// accessType == AccessType.PUBLIC
            mapOwnerId = mapDataRepository.getMapOwner(mapProvideDTO.getMapDataId());
        }

        return fileOperator.provideMap(mapOwnerId, mapProvideDTO);
    }
}
