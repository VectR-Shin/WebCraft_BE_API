package com.team22.webcraft.Repository;

import com.team22.webcraft.Domain.AccessType;
import com.team22.webcraft.Domain.MapData;
import com.team22.webcraft.Domain.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MapDataRepository {
    private static final Logger log = LoggerFactory.getLogger(MapDataRepository.class);
    private final EntityManager em;

    public void saveMapData(MapData mapData) {em.persist(mapData);}

    public void saveMapDataWithFK(Long userDataId, MapData mapData) {
        UserData ud = em.find(UserData.class, userDataId);
        em.persist(mapData);
        mapData.setUserData_bothSide(ud);
    }

    public boolean checkUserDataId_MapNameExists(Long userDataId, String mapName) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM MapData AS MD WHERE MD.userData.id = :userData AND MD.mapName = :mapName", Long.class);
        query.setParameter("userData", userDataId);
        query.setParameter("mapName", mapName);

        Long count = query.getSingleResult();
        if (count == null)
            count = 0l;

        // if exists return true, not exists return false;
        return (count >= 1);
    }

    public boolean checkThisUserOwnedMap(Long userDataId, Long mapDataId) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM MapData AS MD WHERE MD.id = :mapDataId AND MD.userData.id = :userDataId", Long.class);
        query.setParameter("mapDataId", mapDataId);
        query.setParameter("userDataId", userDataId);

        Long count = query.getSingleResult();
        if (count == null)
            count = 0l;

        // if exists return true, not exists return false;
        return (count >= 1);
    }

    public long countUserOwnedFiles(Long userDataId) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM MapData AS MD WHERE MD.userData.id = :userDataId", Long.class);
        query.setParameter("userDataId", userDataId);

        Long count = query.getSingleResult();
        if (count == null)
            count = 0l;

        return count;
    }

    public String getMapNameById(Long mapDataId) {
        TypedQuery<String> query = em.createQuery("SELECT MD.mapName FROM MapData AS MD WHERE MD.id = :mapDataId", String.class);
        query.setParameter("mapDataId", mapDataId);
        return query.getSingleResult();
    }

    public AccessType getAccessType(Long mapDataId) {
        TypedQuery<AccessType> query = em.createQuery("SELECT MD.accessType FROM MapData AS MD WHERE MD.id = :mapDataId", AccessType.class);
        query.setParameter("mapDataId", mapDataId);

        return query.getSingleResult();
    }

    public List<MapData> getOwnedMapList(Long userDataId) {
        TypedQuery<MapData> query = em.createQuery("SELECT MD FROM MapData AS MD JOIN FETCH MD.userData WHERE MD.userData.id = :userDataId ORDER BY MD.mapName", MapData.class);
        query.setParameter("userDataId", userDataId);

        return query.getResultList();
    }

    public List<MapData> getPublicMapList() {
        TypedQuery<MapData> query = em.createQuery("SELECT MD FROM MapData AS MD JOIN FETCH MD.userData WHERE MD.accessType = :accessType ORDER BY MD.mapName", MapData.class);
        query.setParameter("accessType", AccessType.PUBLIC);

        return query.getResultList();
    }

    public void updateMapName(Long mapDataId, String mapName) {
        MapData mapData = em.find(MapData.class, mapDataId);
        mapData.setMapName(mapName);
    }

    public void updateAccessType(Long mapDataId, AccessType accessType) {
        MapData mapData = em.find(MapData.class, mapDataId);
        mapData.setAccessType(accessType);
    }

    @Transactional
    public void removeMapData(Long mapDataId) {
        em.createQuery("DELETE FROM MapData AS MD WHERE MD.id = :mapDataId")
                .setParameter("mapDataId", mapDataId)
                .executeUpdate();
    }
}