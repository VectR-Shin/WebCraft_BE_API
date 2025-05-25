package com.team22.webcraft.Repository;

import com.team22.webcraft.Domain.AccessType;
import com.team22.webcraft.Domain.MapData;
import com.team22.webcraft.Domain.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class MapDataRepositoryTest {
    @Autowired
    private MapDataRepository mapDataRepository;
    @Autowired
    private UserDataRepository userDataRepository;
    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void init() {
        // initialize before test
        UserData userData1 = new UserData("initUser1", "pw1");
        UserData userData2 = new UserData("initUser2", "pw2");

        MapData mapData1 = new MapData("initMap1", AccessType.PRIVATE);
        MapData mapData2 = new MapData("initMap2", AccessType.PUBLIC);
        MapData mapData3 = new MapData("initMap3", AccessType.PRIVATE);

        // set relation
        userData1.addMapData_bothSide(mapData1);
        mapData2.setUserData_bothSide(userData1);

        userData2.addMapData_bothSide(mapData3);

        // save(use cascade)
        userDataRepository.saveUserData(userData1);
        userDataRepository.saveUserData(userData2);

        em.flush();
        em.clear();

        System.out.println("실행되어따");
    }

    @Test
    @DisplayName("MapDataRepository test - saveMapData")
    void testSaveMapData() {
        MapData mapData1 = new MapData("map1", AccessType.PRIVATE);
        MapData mapData2 = new MapData("map2", AccessType.PUBLIC);

        mapDataRepository.saveMapData(mapData1);
        mapDataRepository.saveMapData(mapData2);
    }

    @Test
    @DisplayName("MapDataRepository test - checkUserDataId_MapNameExists")
    void testCheckUserDataId_MapNameExists() {
        Assertions.assertTrue(mapDataRepository.checkUserDataId_MapNameExists(1l, "initMap1"));
        Assertions.assertTrue(mapDataRepository.checkUserDataId_MapNameExists(1l, "initMap2"));
        Assertions.assertTrue(mapDataRepository.checkUserDataId_MapNameExists(2l, "initMap3"));

        Assertions.assertFalse(mapDataRepository.checkUserDataId_MapNameExists(1l, "newMap"));
    }

    @Test
    @DisplayName("MapDataRepository test - checkThisUserOwnedMap")
    void testCheckThisUserOwnedMap() {
        Assertions.assertTrue(mapDataRepository.checkThisUserOwnedMap(1l, 1l));
        Assertions.assertTrue(mapDataRepository.checkThisUserOwnedMap(1l, 2l));
        Assertions.assertTrue(mapDataRepository.checkThisUserOwnedMap(2l, 3l));

        Assertions.assertFalse(mapDataRepository.checkThisUserOwnedMap(1l, 3l));
    }

    @Test
    @DisplayName("MapDataRepository test - countUserOwnedFiles")
    void testCountUserOwnedFiles() {
        Assertions.assertEquals(mapDataRepository.countUserOwnedFiles(1l), 2);
        Assertions.assertEquals(mapDataRepository.countUserOwnedFiles(2l), 1);
    }

    @Test
    @DisplayName("MapDataRepository test - getAccessType")
    void testGetAccessType() {
        Assertions.assertEquals(mapDataRepository.getAccessType(1l), AccessType.PRIVATE);
        Assertions.assertEquals(mapDataRepository.getAccessType(2l), AccessType.PUBLIC);
        Assertions.assertEquals(mapDataRepository.getAccessType(3l), AccessType.PRIVATE);
    }

    @Test
    @DisplayName("MapDataRepository test - getOwnedMapList")
    void testGetOwnedMapList() {
        List<MapData> user1Map = mapDataRepository.getOwnedMapList(1l);
        List<MapData> user2Map = mapDataRepository.getOwnedMapList(2l);

        Assertions.assertEquals(user1Map.get(0).getMapName(), "initMap1");
        Assertions.assertEquals(user1Map.get(1).getMapName(), "initMap2");
        Assertions.assertEquals(user2Map.get(0).getMapName(), "initMap3");
    }

    @Test
    @DisplayName("MapDataRepository test - getPublicMapList")
    void testGetPublicMapList() {
        List<MapData> list = mapDataRepository.getPublicMapList();

        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getMapName(), "initMap2");
    }

    @Test
    @DisplayName("MapDataRepository test - updateMapName")
    void testUpdateMapName() {
        mapDataRepository.updateMapName(1l, "changeName1");
        mapDataRepository.updateMapName(2l, "changeName2");
        mapDataRepository.updateMapName(3l, "changeName3");

        List<MapData> user1Map = mapDataRepository.getOwnedMapList(1l);
        List<MapData> user2Map = mapDataRepository.getOwnedMapList(2l);

        Assertions.assertEquals(user1Map.get(0).getMapName(), "changeName1");
        Assertions.assertEquals(user1Map.get(1).getMapName(), "changeName2");
        Assertions.assertEquals(user2Map.get(0).getMapName(), "changeName3");
    }

    @Test
    @DisplayName("MapDataRepository test - removeMapData")
    void testRemoveMapData() {
        mapDataRepository.removeMapData(1l);
        mapDataRepository.removeMapData(2l);
        mapDataRepository.removeMapData(3l);

        em.flush();
        em.clear();

        List<MapData> user1Map = mapDataRepository.getOwnedMapList(1l);
        List<MapData> user2Map = mapDataRepository.getOwnedMapList(2l);

        Assertions.assertEquals(user1Map.size(), 0);
        Assertions.assertEquals(user2Map.size(), 0);
    }
}
